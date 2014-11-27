/*
The MIT License (MIT)

Copyright (c) 2014 Zane Urbanski, Eric Yu, Loc Hoang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import org.scalatest.FlatSpec

class Tests extends FlatSpec {
    "Sanity check" should "not run into any errors" in {
        object DoesItCompile extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Sanity check") END

        
                START_INGREDIENTS
        
                111 ('potatoes) END
        
                END_INGREDIENTS

        
                PUT ('potatoes) INTO FIRST MIXING_BOWL END
        
                FOLD ('potatoes) INTO FIRST MIXING_BOWL END
        
                ADD ('potatoes) TO FIRST MIXING_BOWL END
        
                COMBINE ('potatoes) INTO FIRST MIXING_BOWL END
        
                DIVIDE ('potatoes) INTO FIRST MIXING_BOWL END
        
                LIQUEFY ('potatoes) END
        
                CLEAN (FIRST) MIXING_BOWL END
        
                SERVES (1) END
            }
        }

        DoesItCompile.run()
    }


    // tests to make sure a program must start with a title
    "General test 1" should "start with a title" in {
        object GeneralTest1 extends ScalaChef {
            def run(): Unit = {
                intercept[RuntimeException] {
                    START_INGREDIENTS

                    1 ('potatoes) END

                    END_INGREDIENTS
                }
            }
        }

        GeneralTest1.run()
    }

    // test to make sure you can't declare a title twice in a row
    "General test 2" should "not allow declaration of a title twice in a row" in {
        object GeneralTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("General test 2") END

                intercept[RuntimeException] {
                    TITLE ("This is bad") END
                }
            }
        }

        GeneralTest2.run();
    }

    // test to make sure a program must have ingredients after the
    // title declaration
    "General test 3" should "require ingredients immediately after title" in {
        object GeneralTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("General test 3") END

                intercept[RuntimeException] {
                    CLEAN (FIRST) MIXING_BOWL END
                }
            }
        }

        GeneralTest3.run();
    }

    // test to make sure you can't have 2 START_INGREDIENTS
    "General test 4" should "not allow two START_INGREDIENTS" in {
        object GeneralTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("General test 4") END

                START_INGREDIENTS

                intercept[RuntimeException] {
                    START_INGREDIENTS
                }
            }
        }

        GeneralTest4.run();
    }

    // test to make sure you can't have 2 END_INGREDIENTS
    "General test 5" should "not allow two END_INGREDIENTS" in {
        object GeneralTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("General test 5") END

                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS

                intercept[RuntimeException] {
                    END_INGREDIENTS
                }
            }
        }

        GeneralTest5.run();
    }

    // test to make sure you can't start a program with just ingredients

    // test to make sure you can't start a program with Chef statements

    // test to make sure declaring an ingredient with the same name more
    // than once will take the latest declaration


    // test to make sure PUT puts stuff in a stack

    // test to make sure PUT doesn't take a non-existent ingredient

    // test to make sure you can put to all 5 mixing bowls

    // test to make sure you can't PUT into a baking dish


    // test to make sure FOLD actually removes the value from a mixing bowl

    // test to make sure FOLD assigns the value it removed from a mixing bowl
    // to the ingredient you specified

    // test to make sure FOLD doesn't take a non-existent ingredient

    // test to make sure FOLD fails if the mixing bowl specified is empty

    // test to make sure FOLD works on all 5 mixing bowls

    // test to make sure you can't FOLD on a baking dish


    // test to make sure ADD adds to something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)
    "Add test 1" should "do a simple ADD in first mixing bowl" in {
        object AddTest1 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 1") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END

                3 ('cakes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END
        
                ADD ('cakes) TO FIRST MIXING_BOWL END
        

                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 5)
                assert(mixingStacks(FIRST).peek.asNumber == 2)
            }
        }

        AddTest1.run()
    }

    // test to make sure you can ADD to all 5 bowls
    "Add test 2" should "ADD to all mixing bowls" in {
        object AddTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add test 2") END


                START_INGREDIENTS

                1 ('flour) END

                2 ('blueberries) END

                3 ('strawberries) END

                4 ('bananas) END

                5 ('apples) END

                6 ('pears) END

                END_INGREDIENTS


                PUT ('flour) INTO FIRST MIXING_BOWL END

                PUT ('flour) INTO SECOND MIXING_BOWL END

                PUT ('flour) INTO THIRD MIXING_BOWL END

                PUT ('flour) INTO FOURTH MIXING_BOWL END

                PUT ('flour) INTO FIFTH MIXING_BOWL END

                ADD ('blueberries) TO FIRST MIXING_BOWL END

                ADD ('strawberries) TO SECOND MIXING_BOWL END

                ADD ('bananas) TO THIRD MIXING_BOWL END

                ADD ('apples) TO FOURTH MIXING_BOWL END

                ADD ('pears) TO FIFTH MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(FIRST).peek.asNumber == 1)
                assert(mixingStacks(SECOND).pop.asNumber == 4)
                assert(mixingStacks(SECOND).peek.asNumber == 1)
                assert(mixingStacks(THIRD).pop.asNumber == 5)
                assert(mixingStacks(THIRD).peek.asNumber == 1)
                assert(mixingStacks(FOURTH).pop.asNumber == 6)
                assert(mixingStacks(FOURTH).peek.asNumber == 1)
                assert(mixingStacks(FIFTH).pop.asNumber == 7)
                assert(mixingStacks(FIFTH).peek.asNumber == 1)
            }
        }

        AddTest2.run()
    }

    // test to make sure ADD doesn't take a non-existent ingredient
    "Add test 3" should "make sure ADD doesn't take a non-existent ingredient" in {
        object AddTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 3") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END
        
                intercept[RuntimeException] {    
                    ADD ('cakes) TO FIRST MIXING_BOWL END
                }
            }
        }

        AddTest3.run()
    }

    // test to make sure ADD fails if nothing is in the specified stack

    // test to make sure you can't ADD on a baking dish


    // test to make sure REMOVE subtracts from something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)
    "Remove test 1" should "do a simple REMOVE in first mixing bowl" in {
        object RemoveTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Remove test 1") END


                START_INGREDIENTS

                1 ('water) END

                2 ('milk) END

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL END

                REMOVE ('water) FROM FIRST MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).peek.asNumber == 2)
            }
        }

        RemoveTest1.run()
    }

    // test to make sure you can REMOVE from all 5 bowls
    "Remove test 2" should "REMOVE from all mixing bowls" in {
        object RemoveTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Remove test 2") END


                START_INGREDIENTS

                6 ('doughnuts) END

                5 ('glaze) END

                4 ('chocolate) END

                3 ('creme) END

                2 ('maple) END

                1 ('sugar) END

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL END

                PUT ('doughnuts) INTO SECOND MIXING_BOWL END

                PUT ('doughnuts) INTO THIRD MIXING_BOWL END

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL END

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL END

                REMOVE ('glaze) FROM FIRST MIXING_BOWL END

                REMOVE ('chocolate) FROM SECOND MIXING_BOWL END

                REMOVE ('creme) FROM THIRD MIXING_BOWL END

                REMOVE ('maple) FROM FOURTH MIXING_BOWL END

                REMOVE ('sugar) FROM FIFTH MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).peek.asNumber == 6)
                assert(mixingStacks(SECOND).pop.asNumber == 2)
                assert(mixingStacks(SECOND).peek.asNumber == 6)
                assert(mixingStacks(THIRD).pop.asNumber == 3)
                assert(mixingStacks(THIRD).peek.asNumber == 6)
                assert(mixingStacks(FOURTH).pop.asNumber == 4)
                assert(mixingStacks(FOURTH).peek.asNumber == 6)
                assert(mixingStacks(FIFTH).pop.asNumber == 5)
                assert(mixingStacks(FIFTH).peek.asNumber == 6)
            }
        }

        RemoveTest2.run()
    }

    // test to make sure REMOVE doesn't take a non-existent ingredient

    // test to make sure REMOVE fails if nothing is in the specified stack

    // test to make sure you can't REMOVE on a baking dish


    // test to make sure COMBINE multiplies to something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can COMBINE to all 5 bowls

    // test to make sure COMBINE doesn't take a non-existent ingredient

    // test to make sure COMBINE fails if nothing is in the specified stack

    // test to make sure you can't COMBINE on a baking dish


    // test to make sure add DIVIDE divides something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can DIVIDE to all 5 bowls

    // test to make sure DIVIDE doesn't take a non-existent ingredient

    // test to make sure DIVIDE fails if nothing is in the specified stack

    // test to make sure you can't DIVIDE on a baking dish


    // test to make sure ADD DRY INGREDIENTS adds all of the dry ingredients
    // together (i.e. does it work basically)

    // test to make sure ADD DRY INGREDIENTS ignores liquid/either ingredients

    // test to make sure ADD DRY INGREDIENTS works on all 5 mixing bowls

    // test to make sure ADD DRY INGREDIENTS adds 0 to a mixing bowl if there are no
    // dry ingredients

    // test to make sure ADD DRY INGREDIENTS adds a DRY ingredient when it does
    // the pushing onto a mixing bowl

    // test to make sure you can't ADD DRY INGREDIENTS on a baking dish


    // test to make sure LIQUEFY ingredient changes a dry or non-specified
    // ingredient to a liqued (i.e. when it outputs it outputs a character)

    // test to make sure LIQUEFY can't work on unspecified ingredients

    // test to make sure LIQUEFY on an already liquid ingredient doesn't do
    // anything (i.e. succeed, but it shouldn't change the value or anything)


    // test to make sure LIQUEFY CONTENTS makes everything in the bowl a liquid
    
    // test to make sure LIQUIFY CONTENTS DOESN'T affect the original ingredients
    // but only the ingredients in the bowl (i.e. if 'potatoes are in a bowl,
    // and you liquify the bowl, the original potatoes don't get liquified

    // test LIQUEFY CONTENTS on all 5 bowls
    
    // test to make sure LIQUEFY CONTENTS fails on a baking dish


    // STIR



    // MIX



    // CLEAN 



    // POUR



    // REFRIGERATE

    

    // test to make sure SERVES prints nothing if the baking dishes (but
    // not the mixing bowls) are empty

    // test to make sure SERVES actually prints stuff from all 5 dishes
    
    // test to make sure SERVES does not print from mixing bowls

    // test to make sure SERVES only prints the specified # of baking dishes
    // (e.g. SERVES (2) END only prints the first 2 and ignores latter 3

    // test to make sure only 1 SERVES can exist in a program

    // test to make sure SERVES only works in the main recipe


    // function related tests

    // can't declare a new function (i.e. start a new TITLE declaration)
    // while parsing ingredients

    // can't declare a new function until ingredients have been declared (even
    // if the ingredients are empty)

    // SERVE/function calls

    // I'm only listing some corner casey things you might need to consider:
    // please test basic functionality and other things (basic functionality
    // = copy first mixing bowl to calling function, make sure it returns to
    // the right spot, make sure it gets a copy of caller's bowls/dishes
    // BUT NOT ingredients, can't call non-existing things, etc.)

    // make sure a function call always loads its default ingredients and
    // not the ingredients of a function call to the same recipe that have
    // already altered it 
    // for example, if recipe "a" has ingredient 11 potatoes, and a call to
    // it changes it to 13 potatoes IN THAT CALL, then a new call to it 
    // should load 11 potatoes


    // make sure nested function calls work


    // make sure you can't call the main recipe (i.e. the first one)

    
    // LOOPS: test cases to consider
  
    // Loop with initial ingredient already 0
    
    // verbs ending with e vs verbs not ending with e
    
    // Loop endings with and without giving an ingredient
    
    // loops without ending phrase throw an exception
    
    // loops without starting phrase throw an exception
    
    // loops with messed up scopes throw an exception 
    // cook 1 ... bake 2 ... do until cooked ... do until baked
    
    // loops with disagreeing verbs throw an exception 
    //(cook potatoes ... cook potatoes until burned)
    //(cook potatoes ... cook potatoes until cook)
    
    // can't define loops with same verb
    
    // breaking works
    
    // breaking outside a loop throws an exception
    
    // breaking in an inner loop does not break the outer loop
    
    // empty loops work
}
