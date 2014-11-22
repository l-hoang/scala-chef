scala-chef
==========

Implementation of the esoteric programming language Chef as an internal Scala DSL.
For more details, check the webpage: http://www.dangermouse.net/esoteric/chef.html

# Changes from Chef
* The END keyword is used in most places instead of a period.
* The ingredient parsing begins and ends with START/END_INGREDIENTS; it is also
mandatory
* There are only 5 mixing bowls and 5 baking dishes
* Symbol notation is required for ingredients, and spaces aren't allowed in
ingredient names (I think)
* Cooking time and oven temperature are not supported
* measure-type in ingredients not supported
* Everything is in CAPS

# To Do:
- [ ] implement runtime evaluator
- [ ] implement loops
- [ ] implement the rest of the instructions
- [ ] decide how to do function calls
- [ ] write a bunch of tests

