scala-chef
==========

Implementation of the esoteric programming language Chef as an internal Scala DSL.
For more details, check the webpage: http://www.dangermouse.net/esoteric/chef.html

## Disclaimer
The information in this readme could potentially be inaccurate. Apologies if it is.

## Changes from Chef/Important Implementation Details
* The END keyword is used in most places instead of a period.
* The ingredient parsing begins and ends with START/END_INGREDIENTS; it is also
mandatory
* There are only 5 mixing bowls and 5 baking dishes
* Symbol notation is required for ingredients, and spaces aren't allowed in
ingredient names 
* Symbols should be in parens.
* Cooking time and oven temperature are not supported
* measure-type in ingredients not supported
* Everything is in CAPS
* mixing bowl and baking dish are combined into MIXING_BOWL and BAKING_DISH
* negative ingredients aren't possible
* A loop verb's end counter part is created by adding a d or ed (even if it's
gramatically incorrect
* Some things that could be optional in the original language are not optional
here
* Start a program by having RUN at the end of the program
* The arithmetic operations (add, subtract, multiply, divide) will modify
the value on the stack in place (i.e. it won't push another value onto the
stack).
* Some instructions may require parens around them (most notably CLEAN, which
requires the second keyword to be in parens).
* PUT creates an unspecified ingredient (i.e. not dry or liquid).
* Singular measures (e.g. PINCH, CUP) will only work with 1

## How to Run

First, compile ScalaChef.
```
scalac ScalaChef.scala
```

Create your Chef program in this fashion.

```
object TITLEHERE extends ScalaChef {
    def main(args: Array[String]): Unit = {
        /* write your Chef here */
    }
}
```
Compile...
```
scalac TITLEHERE.scala
```
Then run.
```
scala TITLEHERE
```

## How to run tests

Run these 2 commands in the src directory. Make sure ScalaChef.scala
has already been compiled.

```
scalac -cp scalatest_2.11-2.2.1.jar:. Tests.scala
scala -cp scalatest_2.11-2.2.1.jar org.scalatest.run Tests

```

For more details, check here:
http://www.scalatest.org/quick_start

## Impelmentation Details


### Syntax

The internal DSL takes this general form:

```
TITLE ("title here") END

START_INGREDIENTS

ingredients here (must be ended with an END)

END_INGREDIENTS

program lines

SERVES (a # from 1 to 5) END

RUN (this should start runtime evaluation)
```

**NOTE THAT IT IS HIGHLY RECOMMENDED TO FOLLOW ALL CHEF LINES WITH A BLANK LINE.**
This is because otherwise certain commands will break.

### How state is stored/represented

Chef has mixing bowls and baking dishes. These are esentially stacks. They
are represented in the program as ArrayDeques. To access a particular 1 (e.g.
the first one), HashMaps exist that take FIRST, SECOND, THIRD, FOURTH, or
FIFTH, and it'll grab the corrosponding bowl/dish.

Statements will have a case class that is associated with it. These
classes are stored in a HashMap where the key to get them is the line # 
of the program. This representation allows easy runtime evaluation: a program
would start by grabbing the case class for a particular line number, and
depending on the case class, different things will be done to alter program
state.

Ingredients are represented by an Ingredient class that holds its value and
its interpretation (as a number, character, or either).

Bindings for ingredients are represented as a HashMap that maps a Symbol
(the ingredient name) to an Ingredient object.

There exists a HashMap that maps recipe titles to line numbers. This is so
a function call can just jump to a particular line number and start from there.
It will also store the end line of a recipe to know when a recipe is finished.

There exists a HashMap that maps titles to default variable bindings. 
This makes it so calling recipes will load a copy of its default variable 
bindings.

Each recipe has a set of loop bindings that hold the loop info for some verb
(which marks the start/end of a loop). In order to keep track of loops, a stack
is used to track the order of loops entered.

### How it is parsed

The code has a concept of "modes". There are 3 modes: title parsing, ingredient
parsing, and program parsing. Depending on which of the 3 modes the program is
currently in, the END statement at the end of most lines will function
differently. Additionally, the code will also use the mode its currently in to
make sure that you are following correct program order.

For example, a program will start in title parsing mode: if it doesn't find
a title, the END evaluator will throw a runtime exception since a title hasn't
been parsed yet.

As for the parsing itself, the keyword at the beginning of each Chef line is
either an object or a function call. The object has a method that will grab the
next word in the line. It will then usually create a class to return, and that class
will be responsible for grabbing the next word in the line (otherwise it does
some other thing that will let it continue parsing).
Depending on the keywords/arguments that are parsed in each line, the program will set variables
that will be used at the end of line parsing. At the end of line parsing,
depending on how the variables were set, the program will alter the parsing
mode, change the state of things like variable bindings, or create a case class
that holds relevant values that the program needs to do that function when it
gets reached by the line evaluator.

For a more concrete example, the object PUT has an apply method that will grab 
the ingredient name as a symbol. It saves the fact that it's parsing PUT and
it saves the ingredient name. It creates an Into class, which will grab the
INTO keyword as the Into class defines INTO as a function. The function grabs
the stack that the line specified, then a BowlOrDish class is created to grab
MIXING_BOWL. The END keyword is finally parsed, and it eventually calls the
finish routine. The finish routine will see that it's currently parsing a
PUT line with some specified ingredient and stack, so it will create a case
class that holds the ingredient that needs to be pushed and the stack that
needs to be pushed to for the runtime evaluator to do it.

### How loops work

Loops in Chef consist of these 2 statements:

*Verb the ingredient.* (loop declaration)
*Verb [the ingredient] until verbed.* (loop end)

The first checks to see if the value of the ingredient is non-zero. If it is,
it does everything until the *Verb [the ingredient] until verbed.*. *verbed* must
match the *Verb* in the loop declaration. Also, the loop end statement will
decrement the value of the ingredient specified by 1 every time it hits it.
Specifying an ingredient in the loop end is optional.

When the parsing hits a loop start statement, END.finish will do these things:

* Add "D" or "ED" to the end of the verb for indexing
* Create a LoopInfo entry for the verb being used in the loopBindings hashmap.
* Assign the Ingredient and Starting line to the LoopInfo entry
* Create a LoopStart class and save it to the hash map of lines and their
functions.

The runtime evaluator will need to grab the values from the hashmaps itself
when it sees that a line is a LoopStart line.It also needs to check if the 
condition (non-zero value) holds: if it doesn't, it will get the LoopEnd value
of this verb and jump there. Otherwise, it'll "enter the loop" by going into
the next line and pushing the current verb onto a loop stack to keep track of
the current loop frame.

When parsing hits the loop end/until statement, these things need to be done:

* Update the end line and ingredient value to be decremented in the hashtable
* Create a LoopEnd class and save it to the hashmap of lines and their
functions for the runtime evaluator to use later.

The runtime evaluator will need to grab the values from the hashmaps itself
when it sees that a line is a LoopEnd line. From there, the runtime 
evaluator checks the initial loop condition and jumps back to the beginning 
of the loop if the condition is met. Otherwise, it exits the loop by popping
the verb from the stack and continuing evaluation on the next line.

### How function calls work

When a recipe is "served" (i.e. function call), the current state is saved
by pushing info onto a bunch of stacks.

* The line to return to after the function ends
* The line that the function being called ends at
* The currently running recipe's loop stack (that tracks loops)
* The currently running recipe's mixing bowls
* The currently running recipe's baking dishes
* The currently running recipe's ingredient bindings
* The currently running recipe's loop bindings (loop info)

A function's start/end line are gotten and saved during the initial parsing 
stage of running the program. They're saved into a class that holds the 2
values, and this class is stored in a HashMap.

Before jumping to the start of the new recipe, the program must make a deep
copy of the caller's mixing bowls and baking dishes. It must also load a copy
of its default ingredient bindings (saved during the initial parse) and its 
loop bindings. Its loop stack will start completely empty (since it hasn't
even begun to run).

Before evaluating a line, the program checks to see if the current line matches
the end line on the stack of function end lines (i.e. if the function its in has
ended). If it matches, then the function ends by popping all of the values stored
on the stacks above and dumping the called function's first mixing bowl into the
original caller's first mixing bowl. A return command (REFRIGERATE) will do the
same thing (pop all of the values on the stacks, dump mixing bowl) except that
it can be done in the middle of a recipe.

The stacks allow both recursion and calling functions within other functions (but
you can't call the main recipe).
It may be limited by memory, however.
