scala-chef
==========

Implementation of the esoteric programming language Chef as an internal Scala DSL.
For more details, check the webpage: http://www.dangermouse.net/esoteric/chef.html

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

**NOTE THAT ALL LINES SHOULD BE FOLLOWED BY A BLANK LINE**
This is because otherwise certain commands will break.

### How state is stored/represented

Chef has mixing bowls and baking dishes. These are esentially stacks. They
are represented in the program as ArrayDeques. To access a particular 1 (e.g.
the first one), HashMaps exists that takes FIRST, SECOND, THIRD, FOURTH, or
FIFTH, and it'll grab the corrosponding bowl/dish.

Statements will have a case class that is associated with it. These
classes are stored in a HashMap where the key to get them is the line # 
of the program. This representation allows easy runtime evaluation: a program
would start by grabbing the case class for a particular line number, and
depending on the case class, different things will be done to alter program
state. Most case classes will have a function that the runtime evaluator can
call to do the work that's required. Others will need to have some logic
in the runtime evaluator itself (e.g. loops).

Ingredients are represented by an Ingredient class that holds its value and
its interpretation (as a number, character, or either).

Bindings for ingredients are represented as a HashMap that maps a Symbol
(the ingredient name) to an Ingredient object.

**What follows is stuff that still needs to be implemented, but it's the idea
I've come up with for it**

There will exist 2 HashMaps for looping purposes: LoopStart and LoopEnd. 
LoopStart takes a Verb and returns the line where the loop starts.
LoopEnd takes a Verb and returns the line where the program would execute given
that the loop condition fails (i.e. after the until statement)

There will exist a HashMap that maps recipe titles to line numbers. This is so
a function call can just jump to a particular line number and start from there.

There will also exist a HashMap that maps titles to variable bindings. This is
so every function/recipe can have its own set of variable bindings.

Likewise, there will be a HashMap that maps titles to mixing bowls and 
baking dishes. Again, this is so every recipe has its own set.

### How it is parsed

The code has a concept of "modes". There are 3 modes: title parsing, ingredient
parsing, and program parsing. Depending on which of the 3 modes the program is
currently in, the END statement at the end of most lines will function
differently. Additionally, the code will also use the mode its currently in to
make sure that you are following correct program order.

For example, every program will start in title parsing mode: if it doesn't find
a title, the END evaluator will throw a runtime exception since a title hasn't
been parsed yet.

As for the parsing itself, the keyword at the beginning of each Chef line is
either an object or a function call. The object has a method that will grab the
next word in the line. It will then create a class to return, and that class
will be responsible for grabbing the next word in the line. Depending on the 
keywords/arguments that are parsed in each line, the program will set variables
that will be used at the end of line parsing. At the end of line parsing,
depending on how the variables were set, the program will alter the parsing
mode, change the state of things like variable bindings, or create a case class
holding a function that will be saved into a hash table that tracks line 
numbers. In the latter case, the runtime evaluator will use the line number
to get this case class and call this function during evaluation. The function
will do what the parsed line needed it to do.

For a more concrete example, the object PUT has an apply method that will grab 
the ingredient name as a symbol. It saves the fact that it's parsing PUT and
it saves the ingredient name. It creates an Into class, which will grab the
INTO keyword as the Into class defines INTO as a function. The function grabs
the stack that the line specified, then a BowlOrDish class is created to grab
MIXING_BOWL. The END keyword is finally parsed, and it eventually calls the
finish routine. The finish routine will see that it's currently parsing a
PUT line with some specified ingredient and stack, so it will create a function
that pushes that ingredient value onto the specified stack and save that
function in the HashMap of lines for runtime evaluation.

### How loops work

**NEEDS TO BE IMPLEMENTED**

Loops in Chef consist of these 2 statements:

*Verb the ingredient.* (loop declaration)
*Verb the ingredient until verbed.* (loop end)

The first checks to see if the value of the ingredient is non-zero. If it is,
it does everything until the *Verb the ingredient until verbed.*. *verbed* must
match the *Verb* in the loop declaration. Also, the loop end statement will
decrement the value of the ingredient specified by 1 every time it hits it.

When the parsing hits a loop start statement, END.finish will need to do these
things:

* Create an entry for the verb being used in the LoopStart hashmap, and assign
it to the current line number.
* Create an entry for the verb being used in the LoopEnd hashmap. It will be
assigned by the "until" statement.
* Create a LoopStart class and save it to the hash map of lines and their
functions.

The runtime evaluator will need to grab the values from the hashmaps itself
when it sees that a line is a LoopStart line. (i.e. this doesn't create a
function to use) It also needs to check if the condition (non-zero value)
holds: if it doesn't, it will get the LoopEnd value of this verb and jump
there. Otherwise, it'll "enter the loop" by going into the next line.

When parsing hits the loop end/until statement, these things need to be done:

* Create a function that will decrement the ingredient value that was provided.
* Take 'verbed' and take the -d off the end. Use that to index into the LoopEnd
hashmap, and assign that entry the line number of the line that this statement
is on PLUS 1 (so it can jump to the point after this statement).
* Create a LoopEnd class and save it to the hashmap of lines and their
functions for the runtime evaluator to use later. Also be sure to pass in the
function that decrements the ingredient value.

The runtime evaluator will need to grab the values from the hashmaps itself
when it sees that a line is a LoopStart line. From there, the runtime 
evaluator needs to jump back to the beginning of the loop where the condition
will be evaluated once more.

### How function calls work

**NEEDS TO BE IMPLEMENTED**



## Changes from Chef
* The END keyword is used in most places instead of a period.
* The ingredient parsing begins and ends with START/END_INGREDIENTS; it is also
mandatory
* There are only 5 mixing bowls and 5 baking dishes
* Symbol notation is required for ingredients, and spaces aren't allowed in
ingredient names (I think)
* Cooking time and oven temperature are not supported
* measure-type in ingredients not supported
* Everything is in CAPS
* mixing bowl and baking dish are combined into MIXING_BOWL and BAKING_DISH

## To Do:
- [ ] implement runtime evaluator
- [ ] implement loops
- [ ] implement the rest of the instructions
- [ ] decide how to do function calls
- [ ] write a bunch of tests

