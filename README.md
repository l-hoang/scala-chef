scala-chef
==========

Implementation of the esoteric programming language Chef as an internal Scala DSL.
For more details, check the webpage: http://www.dangermouse.net/esoteric/chef.html

## Impelmentation Details

### How state is stored



**What follows is stuff that still needs to be implemented, but it's the idea
I've come up with for it**

There will exist a HashMap that maps recipe titles to line numbers. This is so
a function call can just jump to a particular line number and start from there.

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

### How function calls work



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

