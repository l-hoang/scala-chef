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

        GeneralTest2.run()
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

        GeneralTest3.run()
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

        GeneralTest4.run()
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

        GeneralTest5.run()
    }

    // test to make sure you can't start a program with Chef statements
    "General test 6" should "not start without necessary Chef statements" in {
        object GeneralTest6 extends ScalaChef {
            def run(): Unit = { 
                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        GeneralTest6.run()
    }


    // test to make sure declaring an ingredient with the same name more
    // than once will take the latest declaration
    "General test 7" should "make sure latest declaration of variable is used" in {
        object GeneralTest7 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("General test 7") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('potatoes) END

                END_INGREDIENTS


                assert(variableBindings('potatoes).asNumber == 2)
            }
        }

        GeneralTest7.run()
    }



    // TAKE
    // TODO

    // test to make sure PUT puts stuff in a stack
    "Put test 1" should "do a simple PUT into first mixing bowl" in {
        object PutTest1 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 1)
            }
        }

        PutTest1.run()
    }

    // test to make sure you can put to all 5 mixing bowls
    "Put test 2" should "PUT into all mixing bowls" in {
        object PutTest2 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 2") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                4 ('bananas) END

                5 ('apples) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO SECOND MIXING_BOWL END

                PUT ('strawberries) INTO THIRD MIXING_BOWL END

                PUT ('bananas) INTO FOURTH MIXING_BOWL END

                PUT ('apples) INTO FIFTH MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 1)
                assert(mixingStacks(SECOND).peek.asNumber == 2)
                assert(mixingStacks(THIRD).peek.asNumber == 3)
                assert(mixingStacks(FOURTH).peek.asNumber == 4)
                assert(mixingStacks(FIFTH).peek.asNumber == 5)
            }
        }

        PutTest2.run()
    }

    // test to make sure PUT doesn't take a non-existent ingredient
    // TODO
    // "Put test 3" should "make sure PUT doesn't take a non-existent ingredient" in {
    //     object PutTest3 extends ScalaChef {
    //         def run(): Unit = { 
    //             TITLE ("Put test 3") END


    //             START_INGREDIENTS

    //             1 ('potatoes) END

    //             END_INGREDIENTS


    //             intercept[RuntimeException] {
    //                 PUT ('cakes) INTO FIRST MIXING_BOWL END
    //             }
    //         }
    //     }

    //     PutTest3.run()
    // }

    // test to make sure you can't PUT into a baking dish
    "Put test 4" should "make sureyou can't PUT into a baking dish" in {
        object PutTest4 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 4") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                intercept[RuntimeException] {
                    PUT ('potatoes) INTO FIRST BAKING_DISH END
                }
            }
        }

        PutTest4.run()
    }

    // test to make sure FOLD actually removes the value from a mixing bowl
    "Fold test 1" should "make sure FOLD actually removes the value from a mixing bowl" in {
        object FoldTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                FOLD ('cakes) INTO FIRST MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).isEmpty)
            }
        }

        FoldTest1.run()
    }

    // test to make sure FOLD assigns the value it removed from a mixing bowl
    // to the ingredient you specified
    "Fold test 2" should "make sure FOLD assigns removed value to specified ingredient" in {
        object FoldTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 2") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                3 ('fruit) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                FOLD ('fruit) INTO FIRST MIXING_BOWL END


                RUN


                assert(variableBindings('fruit).asNumber == 2)
            }
        }

        FoldTest2.run()
    }

    // test to make sure FOLD doesn't take a non-existent ingredient
    "Fold test 3" should "make sure FOLD doesnt take a non-existent ingredient" in {
        object FoldTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 3") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                3 ('fruit) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                intercept[RuntimeException] {
                    FOLD ('vegetable) INTO FIRST MIXING_BOWL END
                }
            }
        }

        FoldTest3.run()
    }


    // test to make sure FOLD fails if the mixing bowl specified is empty
    "Fold test 4" should "make sure FOLD fails if the specified mixing bowl is empty" in {
        object FoldTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 4") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                intercept[RuntimeException] {
                    FOLD ('potatoes) INTO FIRST MIXING_BOWL END
                }
            }
        }

        FoldTest4.run()
    }

    // test to make sure FOLD works on all 5 mixing bowls
    "Fold test 5" should "make sure FOLD works on all 5 mixing bowls" in {
        object FoldTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 5") END


                START_INGREDIENTS

                1 ('flour) END

                2 ('blueberries) END

                3 ('strawberries) END

                4 ('bananas) END

                5 ('apples) END

                6 ('pears) END

                7 ('pie) END

                END_INGREDIENTS


                PUT ('flour) INTO FIRST MIXING_BOWL END

                PUT ('flour) INTO SECOND MIXING_BOWL END

                PUT ('flour) INTO THIRD MIXING_BOWL END

                PUT ('flour) INTO FOURTH MIXING_BOWL END

                PUT ('flour) INTO FIFTH MIXING_BOWL END

                FOLD ('blueberries) INTO FIRST MIXING_BOWL END

                FOLD ('strawberries) INTO SECOND MIXING_BOWL END

                FOLD ('bananas) INTO THIRD MIXING_BOWL END

                FOLD ('apples) INTO FOURTH MIXING_BOWL END

                FOLD ('pears) INTO FIFTH MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).isEmpty)
                assert(mixingStacks(SECOND).isEmpty)
                assert(mixingStacks(THIRD).isEmpty)
                assert(mixingStacks(FOURTH).isEmpty)
                assert(mixingStacks(FIFTH).isEmpty)
                assert(variableBindings('blueberries).asNumber == 1)
                assert(variableBindings('strawberries).asNumber == 1)
                assert(variableBindings('bananas).asNumber == 1)
                assert(variableBindings('apples).asNumber == 1)
                assert(variableBindings('pears).asNumber == 1)
            }
        }

        FoldTest5.run()
    }

    // test to make sure you can't FOLD on a baking dish
    "Fold test 6" should "make sure you can't FOLD into a baking dish" in {
        object FoldTest6 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Fold test 6") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                intercept[RuntimeException] {
                    FOLD ('potatoes) INTO FIRST BAKING_DISH END
                }
            }
        }

        FoldTest6.run()
    }

    // test to make sure FOLD DOESN'T change the state of the ingredient that
    // you specify to the state of the ingredient that you pop on the stack
    // (i.e. you fold and get a liquid ingredient, but the ingredient you assign
    // to is a dry ingredient; according to the spec, only the value should be
    // copied)
    // TODO


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
                // assert(mixingStacks(FIRST).peek.asNumber == 2)
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
                // assert(mixingStacks(FIRST).peek.asNumber == 1)
                assert(mixingStacks(SECOND).pop.asNumber == 4)
                // assert(mixingStacks(SECOND).peek.asNumber == 1)
                assert(mixingStacks(THIRD).pop.asNumber == 5)
                // assert(mixingStacks(THIRD).peek.asNumber == 1)
                assert(mixingStacks(FOURTH).pop.asNumber == 6)
                // assert(mixingStacks(FOURTH).peek.asNumber == 1)
                assert(mixingStacks(FIFTH).pop.asNumber == 7)
                // assert(mixingStacks(FIFTH).peek.asNumber == 1)
            }
        }

        AddTest2.run()
    }

    // test to make sure ADD doesn't take a non-existent ingredient
    // TODO
    // "Add test 3" should "make sure ADD doesn't take a non-existent ingredient" in {
    //     object AddTest3 extends ScalaChef {
    //         def run(): Unit = {       
    //             TITLE ("Add test 3") END
        

    //             START_INGREDIENTS
        
    //             2 ('potatoes) END
        
    //             END_INGREDIENTS


    //             PUT ('potatoes) INTO FIRST MIXING_BOWL END
        
    //             intercept[RuntimeException] {    
    //                 ADD ('cakes) TO FIRST MIXING_BOWL END
    //             }
    //         }
    //     }
        
    //     AddTest3.run()
    // }

    // test to make sure ADD fails if nothing is in the specified stack
    "Add test 4" should "make sure ADD fails if nothing is in the specified stack" in {
        object AddTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    ADD ('potatoes) TO FIRST MIXING_BOWL END
                }
            }
        }

        AddTest4.run()
    }

    // test to make sure you can't ADD on a baking dish
    "Add test 5" should "make sure you can't ADD to a baking dish" in {
        object AddTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 5") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS

                
                intercept[RuntimeException] {
                    ADD ('potatoes) TO FIRST BAKING_DISH END
                }
            }
        }

        AddTest5.run()
    }


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
                // assert(mixingStacks(FIRST).peek.asNumber == 2)
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
                // assert(mixingStacks(FIRST).peek.asNumber == 6)
                assert(mixingStacks(SECOND).pop.asNumber == 2)
                // assert(mixingStacks(SECOND).peek.asNumber == 6)
                assert(mixingStacks(THIRD).pop.asNumber == 3)
                // assert(mixingStacks(THIRD).peek.asNumber == 6)
                assert(mixingStacks(FOURTH).pop.asNumber == 4)
                // assert(mixingStacks(FOURTH).peek.asNumber == 6)
                assert(mixingStacks(FIFTH).pop.asNumber == 5)
                // assert(mixingStacks(FIFTH).peek.asNumber == 6)
            }
        }

        RemoveTest2.run()
    }

    // test to make sure REMOVE doesn't take a non-existent ingredient
    // TODO

    // test to make sure REMOVE fails if nothing is in the specified stack
    "Remove test 4" should "make sure REMOVE fails if nothing is in the specified stack" in {
        object RemoveTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    REMOVE ('potatoes) FROM FIRST MIXING_BOWL END
                }
            }
        }

        RemoveTest4.run()
    }

    // test to make sure you can't REMOVE on a baking dish
    "Remove test 5" should "make sure you can't REMOVE from a baking dish" in {
        object RemoveTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 5") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    REMOVE ('potatoes) FROM FIRST BAKING_DISH END
                }
            }
        }

        RemoveTest5.run()
    }

    // test to make sure you can't do a remove that will result in an ingredient
    // with a negative value
    // TODO 

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


    // STIR NTH MIXING BOWL

    // test general functionality (moves top ingredient down some # )

    // test to make sure it moves top ingredient to bottom if you stir
    // a # greater than the # of things in the stack

    // test to make sure you can't stir a negative # of minutes 

    // test to make sure you can't stir 0 minutes

    // test to make sure it works on all 5 mixing bowls
    
    // test to make sure can't work on baking dishes




    // STIR INGREDIENT



    // MIX

    // general functionality

    // works on all 5 bowls

    // fails on a dish



    // CLEAN 

    // test general funcitonality

    // test to make sure cleaning a mixing bowl will NOT affect the ingredients
    // not in the mixing bowl

    // test to see if it works on all 5 bowls




    // POUR

    // general functionality

    // test to make sure you can pour to the same dish twice with
    // expected behavior

    // make sure order of mixing bowl that is poured is preserved
    // (i.e. the top of the mixing bowl should be the top of the 
    // baking dish after pour)

    // make sure editing the mixing bowl after a POUR doesn't alter
    // the same ingredient in the baking dish





    // REFRIGERATE

    // general functionality

    // test to make sure it passes back the called function's first
    // mixing bowl if it's called in a sub-recipe

    // test to make sure the default version works (REFRIGERATE END)

    // test to make sure REFRIGERATE FOR 0-5 HOURS works for all of the
    // numbers specified here (even 0)

    // test to make sure it prints baking stacks even if it's not the
    // main program
    


    // SERVES

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




    // TAKE

    // note you'll have to provide input to these tests when you run them

    // general functionality (input int, it saves it to an ingredient)

    // TAKE on an existing ingredient does not alter it's state (i.e. if you
    // take on a potato which is I_DRY, it won't change to I_EITHER, which is
    // the default for new ingredients

    // can't TAKE negative values

    // can't TAKE non numbers

    // can't TAKE floats




    // LOOPS: test cases to consider
  
    // Loop with initial ingredient already 0
    "Loop test 1" should "jump to end of loop" in {
        object LoopTest1 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 1") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                0 ('beans) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                "COOK" THE ('beans) UNTIL "COOKED" END

                RUN
                

                assert(mixingStacks(FIRST).isEmpty())
            }
        }
        
        LoopTest1.run()
    }
    
    // verbs ending with ^e work
    "Loop test 2" should "loop properly" in {
        object LoopTest2 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 2") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                3 ('beans) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                "COOK" THE ('beans) UNTIL "COOKED" END

                RUN
                

                assert(mixingStacks(FIRST).size() == 3)
            }
        }
        LoopTest2.run()
    }
    
    // verbs ending with e work
    "Loop test 3" should "loop properly" in {
        object LoopTest3 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 3") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                3 ('beans) END

                END_INGREDIENTS

                "BAKE" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                "BAKE" THE ('beans) UNTIL "BAKED" END

                RUN
                

                assert(mixingStacks(FIRST).size() == 3)
            }
        }
        LoopTest3.run()
    }
    
    // Loop endings without giving an ingredient work
    "Loop test 4" should "loop properly" in {
        object LoopTest4 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 4") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                3 ('beans) END
                
                0 ('onions) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END
                
                PUT ('onions) INTO FIRST MIXING_BOWL END
                
                FOLD ('beans) INTO FIRST MIXING_BOWL END

                "COOK" UNTIL "COOKED" END

                RUN
                

                assert(mixingStacks(FIRST).size() == 1)
                assert(variableBindings('potatoes).number == 1)
            }
        }
        
        LoopTest4.run()
    }
    
    // Loop endings with different ingredient work
    "Loop test 5" should "loop properly" in {
        object LoopTest5 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 5") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                3 ('beans) END
                
                0 ('onions) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END
                
                PUT ('onions) INTO FIRST MIXING_BOWL END
                
                FOLD ('beans) INTO FIRST MIXING_BOWL END

                "COOK" THE ('potatoes) UNTIL "COOKED" END

                RUN
                
                assert(mixingStacks(FIRST).size() == 1)
                assert(variableBindings('potatoes).number == 0)
            }
        }
        
        LoopTest5.run()
    }
    
    // loops without ending phrase throw an exception
    "Loop test 6" should "throw an exception" in {
        object LoopTest6 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 6") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END

                END_INGREDIENTS
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END
                
                intercept[RuntimeException] {
                    "COOK" THE ('beans) UNTIL "COOKED" END
                }
            }
        }     
        LoopTest6.run()
    }
    
    
    // loops without starting phrase throw an exception
    "Loop test 7" should "throw an exception" in {
        object LoopTest7 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 7") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL END
                
                intercept[RuntimeException] {
                    SERVES (1) END
                }
            }
        }
        LoopTest7.run()
    }
    
    // loops with messed up scopes throw an exception 
    // cook 1 ... bake 2 ... do until cooked ... do until baked
    "Loop test 8" should "throw an exception" in {
        object LoopTest8 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 8") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END
                
                END_INGREDIENTS

                "BAKE" THE ('beans) END
                
                "COOK" THE ('potatoes) END 

                intercept[RuntimeException] {
                     "BAKE" THE ('beans) UNTIL "BAKED" END
                }
            }
        }
        LoopTest8.run()
    }
    
    // loops with disagreeing verbs throw an exception 
    //(cook potatoes ... cook potatoes until baked)
    "Loop test 9" should "throw an exception" in {
        object LoopTest9 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 9") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) END 

                intercept[RuntimeException] {
                     "COOK" THE ('beans) UNTIL "BAKED" END
                }
            }
        }
        LoopTest9.run()
    }
    
    //(cook potatoes ... cook potatoes until cook)
    "Loop test 10" should "throw an exception" in {
        object LoopTest10 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 10") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) END 

                intercept[RuntimeException] {
                     "COOK" THE ('beans) UNTIL "COOK" END
                }
            }
        }
        LoopTest10.run()
    }
    
    // can't define loops with same verb
    "Loop test 11" should "throw an exception" in {
        object LoopTest11 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 11") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) END 
                
                "COOK" THE ('potatoes) UNTIL "COOKED" END

                intercept[RuntimeException] {
                     "COOK" THE ('beans) END
                }
            }
        }
        LoopTest11.run()
    }
    
    // nested loops work
    "Loop test 12" should "loop properly" in {
        object LoopTest12 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 12") END


                START_INGREDIENTS

                5 ('potatoes) END
                
                2 ('beans) END
                
                5 ('onions) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                "BAKE" THE ('potatoes) END
                
                PUT ('beans) INTO FIRST MIXING_BOWL END
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" END
                
                PUT ('onions) INTO FIRST MIXING_BOWL END
                
                FOLD ('potatoes) INTO FIRST MIXING_BOWL END

                "COOK" THE ('beans) UNTIL "COOKED" END

                RUN
                

                assert(mixingStacks(FIRST).size() == 10)
            }
        }
        LoopTest12.run()
    }
    
    // breaking works
    "Loop test 13" should "break out of the loop" in {
        object LoopTest13 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 13") END


                START_INGREDIENTS

                5 ('potatoes) END
                
                2 ('beans) END
                
                END_INGREDIENTS
                
                "BAKE" THE ('potatoes) END
                
                PUT ('beans) INTO FIRST MIXING_BOWL END
                
                SET ASIDE END
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" END


                RUN
                

                assert(mixingStacks(FIRST).size() == 1)
                assert(mixingStacks(FIRST).peek().number == 2)
            }
        }
        LoopTest13.run()
    }
    
    // breaking outside a loop throws an exception
    "Loop test 14" should "throw an exception" in {
        object LoopTest14 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 14") END


                START_INGREDIENTS

                1 ('potatoes) END
                
                1 ('beans) END
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) END 
                
                "COOK" THE ('potatoes) UNTIL "COOKED" END
                
                SET ASIDE END

                intercept[RuntimeException] {
                    RUN
                }
            }
        }
        LoopTest14.run()
    }
    
    
    // breaking in an inner loop does not break the outer loop
    "Loop test 15" should "not break outer loop" in {
        object LoopTest15 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 15") END


                START_INGREDIENTS

                5 ('potatoes) END
                
                3 ('beans) END

                END_INGREDIENTS

                "COOK" THE ('beans) END
                
                "BAKE" THE ('potatoes) END
                
                PUT ('beans) INTO FIRST MIXING_BOWL END
                
                SET ASIDE END
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" END

                "COOK" THE ('beans) UNTIL "COOKED" END

                RUN
                

                assert(mixingStacks(FIRST).size() == 3)
            }
        }
        LoopTest15.run()
    }
}
