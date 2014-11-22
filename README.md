scala-chef
==========

Implementation of the esoteric programming language Chef as an internal Scala DSL.
For more details, check the webpage: http://www.dangermouse.net/esoteric/chef.html

## How it works

The code has a concept of "modes". There are 3 modes: title parsing, ingredient
parsing, and program parsing. Depending on which of the 3 modes the program is
currently in, the END statement at the end of most lines will function
differently. Additionally, the code will also use the mode its currently in to
make sure that you are following correct program order.

For example, every program will start in title parsing mode: if it doesn't find
a title, the END evaluator will throw a runtime exception.

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

## To Do:
- [ ] implement runtime evaluator
- [ ] implement loops
- [ ] implement the rest of the instructions
- [ ] decide how to do function calls
- [ ] write a bunch of tests

