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

/* we try testing for most things, but some tests may be left with a description
 * but no implementation. The description and the test may not match up as well:
 * please look at the actual test to see what is being tested */

import org.scalatest.FlatSpec
import scala.language.postfixOps

class Tests extends FlatSpec {
    "Sanity check" should "not run into any errors" in {
        object DoesItCompile extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Sanity check") 

        
                START_INGREDIENTS
        
                111 ('potatoes) 
        
                END_INGREDIENTS

        
                PUT ('potatoes) INTO FIRST MIXING_BOWL 
        
                FOLD ('potatoes) INTO FIRST MIXING_BOWL 
        
                ADD ('potatoes) TO FIRST MIXING_BOWL 
        
                COMBINE ('potatoes) INTO FIRST MIXING_BOWL 
        
                DIVIDE ('potatoes) INTO FIRST MIXING_BOWL 
        
                LIQUEFY ('potatoes) 
        
                CLEAN FIRST MIXING_BOWL 
        
                SERVES (1) 
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

                    1 ('potatoes) 

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
                TITLE ("General test 2") 

                intercept[RuntimeException] {
                    TITLE ("This is bad") 
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
                TITLE ("General test 3") 

                intercept[RuntimeException] {
                    CLEAN FIRST MIXING_BOWL 
                }
            }
        }

        GeneralTest3.run()
    }

    // test to make sure you can't have 2 START_INGREDIENTS
    "General test 4" should "not allow two START_INGREDIENTS" in {
        object GeneralTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("General test 4") 


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
                TITLE ("General test 5") 


                START_INGREDIENTS

                1 ('potatoes) 

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
                    PUT ('potatoes) INTO FIRST MIXING_BOWL 

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
                TITLE ("General test 7") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('potatoes) 

                END_INGREDIENTS


                assert(variableBindings('potatoes).asNumber == 2)
            }
        }

        GeneralTest7.run()
    }


    // NEW: make sure ingredients can't be negative (0 is fine even though
    // it's a bit strange)
    "Ingredients test 1" should "make sure ingredients can't be negative" in {
        object IngredientsTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 1") 


                START_INGREDIENTS

                0 ('potatoes) 

                intercept[RuntimeException] {
                    -1 ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest1.run()
    }

    // make sure PINCH can only have value 1 (i.e. 4 PINCH fails)
    "Ingredients test 2" should "make sure you can only have a pinch of size 1" in {
        object IngredientsTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 2") 

                START_INGREDIENTS

                intercept[RuntimeException] {
                    2 PINCH ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest2.run()
    }

    // make sure that the above (only value 1) applies to 
    // DASH, CUP, TEASPOON, TABLESPOON (4 different tests here) 
    "Ingredients test 3" should "make sure you can only have a dash of size 1" in {
        object IngredientsTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 3") 


                START_INGREDIENTS

                0 ('potatoes) 

                intercept[RuntimeException] {
                    2 DASH ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest3.run()
    }

    "Ingredients test 4" should "make sure you can only have a cup of size 1" in {
        object IngredientsTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 4") 


                START_INGREDIENTS

                0 ('potatoes) 

                intercept[RuntimeException] {
                    2 CUP ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest4.run()
    }

    "Ingredients test 5" should "make sure you can only have a teaspoon of size 1" in {
        object IngredientsTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 5") 


                START_INGREDIENTS

                0 ('potatoes) 

                intercept[RuntimeException] {
                    2 TEASPOON ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest5.run()
    }

    "Ingredients test 6" should "make sure you can only have a tablespoon of size 1" in {
        object IngredientsTest6 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Ingredients test 6") 


                START_INGREDIENTS

                0 ('potatoes) 

                intercept[RuntimeException] {
                    2 TABLESPOON ('cakes) 
                }

                END_INGREDIENTS
            }
        }

        IngredientsTest6.run()
    }

    // TAKE

    // note you'll have to provide input to these tests when you run them

    // general functionality (input int, it saves it to an ingredient)
    "Take test 1" should "make sure it works" in {
        object TakeTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Take test 1") 


                START_INGREDIENTS

                0 ('potatoes) 

                END_INGREDIENTS


                TAKE ('potatoes) FROM REFRIGERATOR 


                RUN


                assert(variableBindings('potatoes).asNumber != 0)
            }
        }

        TakeTest1.run()
    }

    // TAKE on an existing ingredient does not alter it's state (i.e. if you
    // take on a potato which is I_DRY, it won't change to I_EITHER, which is
    // the default for new ingredients
    "Take test 2" should "make sure state stays the same" in {
        object TakeTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Take test 2") 


                START_INGREDIENTS

                0 G ('potatoes) 

                END_INGREDIENTS


                TAKE ('potatoes) FROM REFRIGERATOR 


                RUN


                assert(variableBindings('potatoes).state == I_DRY)
            }
        }

        TakeTest2.run()
    }

    // can't TAKE negative values
    "Take test 3" should "make sure you can't TAKE negative" in {
        object TakeTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Take test 3") 


                START_INGREDIENTS

                0 ('potatoes) 

                END_INGREDIENTS


                TAKE ('potatoes) FROM REFRIGERATOR 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        TakeTest3.run()
    }

    // can't TAKE non numbers
    "Take test 4" should "make sure you can't TAKE non numbers" in {
        object TakeTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Take test 4") 


                START_INGREDIENTS

                0 ('potatoes) 

                END_INGREDIENTS


                TAKE ('potatoes) FROM REFRIGERATOR 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        TakeTest4.run()
    }

    // can't TAKE floats
    "Take test 5" should "make sure you can't TAKE floats" in {
        object TakeTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Take test 5") 


                START_INGREDIENTS

                0 ('potatoes) 

                END_INGREDIENTS


                TAKE ('potatoes) FROM REFRIGERATOR 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        TakeTest5.run()
    }


    // test to make sure PUT puts stuff in a stack
    "Put test 1" should "do a simple PUT into first mixing bowl" in {
        object PutTest1 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 


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
                TITLE ("Put test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                4 ('bananas) 

                5 ('apples) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO SECOND MIXING_BOWL 

                PUT ('strawberries) INTO THIRD MIXING_BOWL 

                PUT ('bananas) INTO FOURTH MIXING_BOWL 

                PUT ('apples) INTO FIFTH MIXING_BOWL 


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
    "Put test 3" should "make sure PUT doesn't take a non-existent ingredient" in {
        object PutTest3 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                PUT ('cakes) INTO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        PutTest3.run()
    }

    // test to make sure you can't PUT into a baking dish
    "Put test 4" should "make sureyou can't PUT into a baking dish" in {
        object PutTest4 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                intercept[RuntimeException] {
                    PUT ('potatoes) INTO FIRST BAKING_DISH 
                }
            }
        }

        PutTest4.run()
    }

    // test to make sure FOLD actually removes the value from a mixing bowl
    "Fold test 1" should "make sure FOLD actually removes the value from a mixing bowl" in {
        object FoldTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                FOLD ('cakes) INTO FIRST MIXING_BOWL 


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
                TITLE ("Fold test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                3 ('fruit) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                FOLD ('fruit) INTO FIRST MIXING_BOWL 


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
                TITLE ("Fold test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                3 ('fruit) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                FOLD ('vegetable) INTO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        FoldTest3.run()
    }


    // test to make sure FOLD fails if the mixing bowl specified is empty
    "Fold test 4" should "make sure FOLD fails if the specified mixing bowl is empty" in {
        object FoldTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                FOLD ('potatoes) INTO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        FoldTest4.run()
    }

    // test to make sure FOLD works on all 5 mixing bowls
    "Fold test 5" should "make sure FOLD works on all 5 mixing bowls" in {
        object FoldTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 5") 


                START_INGREDIENTS

                1 ('flour) 

                2 ('blueberries) 

                3 ('strawberries) 

                4 ('bananas) 

                5 ('apples) 

                6 ('pears) 

                7 ('pie) 

                END_INGREDIENTS


                PUT ('flour) INTO FIRST MIXING_BOWL 

                PUT ('flour) INTO SECOND MIXING_BOWL 

                PUT ('flour) INTO THIRD MIXING_BOWL 

                PUT ('flour) INTO FOURTH MIXING_BOWL 

                PUT ('flour) INTO FIFTH MIXING_BOWL 

                FOLD ('blueberries) INTO FIRST MIXING_BOWL 

                FOLD ('strawberries) INTO SECOND MIXING_BOWL 

                FOLD ('bananas) INTO THIRD MIXING_BOWL 

                FOLD ('apples) INTO FOURTH MIXING_BOWL 

                FOLD ('pears) INTO FIFTH MIXING_BOWL 


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
                TITLE ("Fold test 6") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                intercept[RuntimeException] {
                    FOLD ('potatoes) INTO FIRST BAKING_DISH 
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
    "Fold Test 7" should "make sure FOLD doesn't change the state of an ingredient" in {
        object FoldTest7 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Fold test 7") 


                START_INGREDIENTS

                1 G ('potatoes) 

                2 ML ('oil) 

                END_INGREDIENTS


                PUT ('oil) INTO FIRST MIXING_BOWL 

                FOLD ('potatoes) INTO FIRST MIXING_BOWL 


                RUN


                assert(variableBindings('potatoes).asNumber == 2)
                assert(variableBindings('potatoes).state == I_DRY)
            }
        }
    }


    // test to make sure ADD adds to something already on a stack 
    "Add test 1" should "do a simple ADD in first mixing bowl" in {
        object AddTest1 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 1") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 

                3 ('cakes) 
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 
        
                ADD ('cakes) TO FIRST MIXING_BOWL 
        

                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 5)
            }
        }

        AddTest1.run()
    }

    // test to make sure you can ADD to all 5 bowls
    "Add test 2" should "ADD to all mixing bowls" in {
        object AddTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add test 2") 


                START_INGREDIENTS

                1 ('flour) 

                2 ('blueberries) 

                3 ('strawberries) 

                4 ('bananas) 

                5 ('apples) 

                6 ('pears) 

                END_INGREDIENTS


                PUT ('flour) INTO FIRST MIXING_BOWL 

                PUT ('flour) INTO SECOND MIXING_BOWL 

                PUT ('flour) INTO THIRD MIXING_BOWL 

                PUT ('flour) INTO FOURTH MIXING_BOWL 

                PUT ('flour) INTO FIFTH MIXING_BOWL 

                ADD ('blueberries) TO FIRST MIXING_BOWL 

                ADD ('strawberries) TO SECOND MIXING_BOWL 

                ADD ('bananas) TO THIRD MIXING_BOWL 

                ADD ('apples) TO FOURTH MIXING_BOWL 

                ADD ('pears) TO FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(SECOND).pop.asNumber == 4)
                assert(mixingStacks(THIRD).pop.asNumber == 5)
                assert(mixingStacks(FOURTH).pop.asNumber == 6)
                assert(mixingStacks(FIFTH).pop.asNumber == 7)
            }
        }

        AddTest2.run()
    }

    // test to make sure ADD doesn't take a non-existent ingredient
    "Add test 3" should "make sure ADD doesn't take a non-existent ingredient" in {
        object AddTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 3") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                ADD ('cakes) TO FIRST MIXING_BOWL 
        

                intercept[RuntimeException] {
                    RUN   
                }
            }
        }
        
        AddTest3.run()
    }

    // test to make sure ADD fails if nothing is in the specified stack
    "Add test 4" should "make sure ADD fails if nothing is in the specified stack" in {
        object AddTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 4") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                ADD ('potatoes) TO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        AddTest4.run()
    }

    // test to make sure you can't ADD on a baking dish
    "Add test 5" should "make sure you can't ADD into a baking dish" in {
        object AddTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Add test 5") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS

                
                intercept[RuntimeException] {
                    ADD ('potatoes) TO FIRST BAKING_DISH 
                }
            }
        }

        AddTest5.run()
    }


    // test to make sure REMOVE subtracts from something already on a stack 
    "Remove test 1" should "do a simple REMOVE in first mixing bowl" in {
        object RemoveTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Remove test 1") 


                START_INGREDIENTS

                1 ('water) 

                2 ('milk) 

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL 

                REMOVE ('water) FROM FIRST MIXING_BOWL 


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
                TITLE ("Remove test 2") 


                START_INGREDIENTS

                6 ('doughnuts) 

                5 ('glaze) 

                4 ('chocolate) 

                3 ('creme) 

                2 ('maple) 

                1 ('sugar) 

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL 

                PUT ('doughnuts) INTO SECOND MIXING_BOWL 

                PUT ('doughnuts) INTO THIRD MIXING_BOWL 

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL 

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL 

                REMOVE ('glaze) FROM FIRST MIXING_BOWL 

                REMOVE ('chocolate) FROM SECOND MIXING_BOWL 

                REMOVE ('creme) FROM THIRD MIXING_BOWL 

                REMOVE ('maple) FROM FOURTH MIXING_BOWL 

                REMOVE ('sugar) FROM FIFTH MIXING_BOWL 


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
    "Remove test 3" should "make sure REMOVE doesn't take a non-existent ingredient" in {
        object RemoveTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 3") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                REMOVE ('cakes) FROM FIRST MIXING_BOWL 
        

                intercept[RuntimeException] {
                    RUN   
                }
            }
        }
        
        RemoveTest3.run()
    }

    // test to make sure REMOVE fails if nothing is in the specified stack
    "Remove test 4" should "make sure REMOVE fails if nothing is in the specified stack" in {
        object RemoveTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 4") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                REMOVE ('potatoes) FROM FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        RemoveTest4.run()
    }

    // test to make sure you can't REMOVE on a baking dish
    "Remove test 5" should "make sure you can't REMOVE from a baking dish" in {
        object RemoveTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 5") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    REMOVE ('potatoes) FROM FIRST BAKING_DISH 
                }
            }
        }

        RemoveTest5.run()
    }

    // test to make sure you can't do a remove that will result in an ingredient
    // with a negative value
    "Remove test 6" should "make sure you can't do a REMOVE that will result in negative" in {
        object RemoveTest6 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Remove test 6") 


                START_INGREDIENTS

                2 ('potatoes) 

                3 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                REMOVE ('cakes) FROM FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        RemoveTest6.run()
    }

    // test to make sure COMBINE multiplies to something already on a stack
    "Combine test 1" should "do a simple COMBINE in first mixing bowl" in {
        object CombineTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Combine test 1") 


                START_INGREDIENTS

                2 ('chocolate) 

                3 ('milk) 

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL 

                COMBINE ('chocolate) INTO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 6)
                // assert(mixingStacks(FIRST).peek.asNumber == 3)
            }
        }

        CombineTest1.run()
    } 

    // test to make sure you can COMBINE to all 5 bowls
    "Combine test 2" should "COMBINE into all mixing bowls" in {
        object CombineTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Combine test 2") 


                START_INGREDIENTS

                5 ('doughnuts) 

                4 ('glaze) 

                3 ('chocolate) 

                2 ('creme) 

                1 ('maple) 

                0 ('sugar) 

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL 

                PUT ('doughnuts) INTO SECOND MIXING_BOWL 

                PUT ('doughnuts) INTO THIRD MIXING_BOWL 

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL 

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL 

                COMBINE ('glaze) INTO FIRST MIXING_BOWL 

                COMBINE ('chocolate) INTO SECOND MIXING_BOWL 

                COMBINE ('creme) INTO THIRD MIXING_BOWL 

                COMBINE ('maple) INTO FOURTH MIXING_BOWL 

                COMBINE ('sugar) INTO FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 20)
                // assert(mixingStacks(FIRST).peek.asNumber == 5)
                assert(mixingStacks(SECOND).pop.asNumber == 15)
                // assert(mixingStacks(SECOND).peek.asNumber == 5)
                assert(mixingStacks(THIRD).pop.asNumber == 10)
                // assert(mixingStacks(THIRD).peek.asNumber == 5)
                assert(mixingStacks(FOURTH).pop.asNumber == 5)
                // assert(mixingStacks(FOURTH).peek.asNumber == 5)
                assert(mixingStacks(FIFTH).pop.asNumber == 0)
                // assert(mixingStacks(FIFTH).peek.asNumber == 5)
            }
        }

        CombineTest2.run()
    }

    // test to make sure COMBINE doesn't take a non-existent ingredient
    "Combine test 3" should "make sure COMBINE doesn't take a non-existent ingredient" in {
        object CombineTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Combine test 3") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                COMBINE ('cakes) INTO FIRST MIXING_BOWL 
        

                intercept[RuntimeException] {
                    RUN   
                }
            }
        }
        
        CombineTest3.run()
    }

    // test to make sure COMBINE fails if nothing is in the specified stack
    "Combine test 4" should "make sure COMBINE fails if nothing is in the specified stack" in {
        object CombineTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Combine test 4") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                COMBINE ('potatoes) INTO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        CombineTest4.run()
    }

    // test to make sure you can't COMBINE on a baking dish
    "Combine test 5" should "make sure you can't COMBINE into a baking dish" in {
        object CombineTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Combine test 5") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    COMBINE ('potatoes) INTO FIRST BAKING_DISH 
                }
            }
        }

        CombineTest5.run()
    }


    // test to make sure add DIVIDE divides something already on a stack
    "Divide test 1" should "do a simple DIVIDE in first mixing bowl" in {
        object DivideTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Divide test 1") 


                START_INGREDIENTS

                2 ('chocolate) 

                6 ('milk) 

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL 

                DIVIDE ('chocolate) INTO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 3)
                // assert(mixingStacks(FIRST).peek.asNumber == 3)
            }
        }

        DivideTest1.run()
    } 

    // test to make sure you can DIVIDE to all 5 bowls
    "Divide test 2" should "DIVIDE into all mixing bowls" in {
        object DivideTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Divide test 2") 


                START_INGREDIENTS

                20 ('doughnuts) 

                10 ('glaze) 

                5 ('chocolate) 

                4 ('creme) 

                2 ('maple) 

                1 ('sugar) 

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL 

                PUT ('doughnuts) INTO SECOND MIXING_BOWL 

                PUT ('doughnuts) INTO THIRD MIXING_BOWL 

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL 

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL 

                DIVIDE ('glaze) INTO FIRST MIXING_BOWL 

                DIVIDE ('chocolate) INTO SECOND MIXING_BOWL 

                DIVIDE ('creme) INTO THIRD MIXING_BOWL 

                DIVIDE ('maple) INTO FOURTH MIXING_BOWL 

                DIVIDE ('sugar) INTO FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 2)
                // assert(mixingStacks(FIRST).peek.asNumber == 5)
                assert(mixingStacks(SECOND).pop.asNumber == 4)
                // assert(mixingStacks(SECOND).peek.asNumber == 5)
                assert(mixingStacks(THIRD).pop.asNumber == 5)
                // assert(mixingStacks(THIRD).peek.asNumber == 5)
                assert(mixingStacks(FOURTH).pop.asNumber == 10)
                // assert(mixingStacks(FOURTH).peek.asNumber == 5)
                assert(mixingStacks(FIFTH).pop.asNumber == 20)
                // assert(mixingStacks(FIFTH).peek.asNumber == 5)
            }
        }

        DivideTest2.run()
    }

    // test to make sure DIVIDE doesn't take a non-existent ingredient
    "Divide test 3" should "make sure DIVIDE doesn't take a non-existent ingredient" in {
        object DivideTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Divide test 3") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                DIVIDE ('cakes) INTO FIRST MIXING_BOWL 
        

                intercept[RuntimeException] {
                    RUN   
                }
            }
        }
        
        DivideTest3.run()
    }

    // test to make sure DIVIDE fails if nothing is in the specified stack
    "Divide test 4" should "make sure DIVIDE fails if nothing is in the specified stack" in {
        object DivideTest4 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Divide test 4") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                DIVIDE ('potatoes) INTO FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        DivideTest4.run()
    }

    // test to make sure you can't DIVIDE on a baking dish
    "Divide test 5" should "make sure you can't DIVIDE into a baking dish" in {
        object DivideTest5 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Divide test 5") 
        

                START_INGREDIENTS
        
                2 ('potatoes) 
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    DIVIDE ('potatoes) INTO FIRST BAKING_DISH 
                }
            }
        }

        DivideTest5.run()
    }


    // test to make sure ADD DRY INGREDIENTS adds all of the dry ingredients
    // together (i.e. does it work basically)
    "Add dry ingredients test 1" should "make sure it works" in {
        object AddDryTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add dry ingredients test 1") 


                START_INGREDIENTS

                2 G ('potatoes) 

                3 KG ('turkey) 

                4 G ('stuffing) 

                0 G ('corn) 

                END_INGREDIENTS


                ADD DRY INGREDIENTS TO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 9)
            }
        }

        AddDryTest1.run()
    }

    // test to make sure ADD DRY INGREDIENTS ignores liquid/either ingredients
    "Add dry ingredients test 2" should "make sure it ignores liquids/eithers" in {
        object AddDryTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add dry ingredients test 2") 


                START_INGREDIENTS

                2 G ('potatoes) 

                3 KG ('turkey) 

                4 G ('stuffing) 

                0 G ('corn) 

                10 ML ('oil) 

                100 ('fries) 

                END_INGREDIENTS


                ADD DRY INGREDIENTS TO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 9)
            }
        }

        AddDryTest2.run()
    }

    // test to make sure ADD DRY INGREDIENTS works on all 5 mixing bowls
    "Add dry ingredients test 3" should "make sure it works on all 5 mixing bowls" in {
        object AddDryTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add dry ingredients test 3") 


                START_INGREDIENTS

                2 G ('potatoes) 

                3 KG ('turkey) 

                4 G ('stuffing) 

                0 G ('corn) 

                END_INGREDIENTS


                ADD DRY INGREDIENTS TO FIRST MIXING_BOWL 

                ADD DRY INGREDIENTS TO SECOND MIXING_BOWL 

                ADD DRY INGREDIENTS TO THIRD MIXING_BOWL 

                ADD DRY INGREDIENTS TO FOURTH MIXING_BOWL 

                ADD DRY INGREDIENTS TO FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 9)
                assert(mixingStacks(SECOND).peek.asNumber == 9)
                assert(mixingStacks(THIRD).peek.asNumber == 9)
                assert(mixingStacks(FOURTH).peek.asNumber == 9)
                assert(mixingStacks(FIFTH).peek.asNumber == 9)
            }
        }

        AddDryTest3.run()
    }

    // test to make sure ADD DRY INGREDIENTS adds 0 to a mixing bowl if there are no
    // dry ingredients
    "Add dry ingredients test 4" should "make sure it adds a 0 if no ingredients" in {
        object AddDryTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add dry ingredients test 4") 


                START_INGREDIENTS

                END_INGREDIENTS


                ADD DRY INGREDIENTS TO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 0)
            }
        }

        AddDryTest4.run()
    }

    // test to make sure ADD DRY INGREDIENTS adds a DRY ingredient when it does
    // the pushing onto a mixing bowl
    "Add dry ingredients test 5" should "make sure it adds a dry ingredient to the mixing bowl" in {
        object AddDryTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Add dry ingredients test 5") 


                START_INGREDIENTS

                2 G ('potatoes) 

                3 KG ('turkey) 

                4 G ('stuffing) 

                0 G ('corn) 

                END_INGREDIENTS


                ADD DRY INGREDIENTS TO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.state == I_DRY)
            }
        }

        AddDryTest5.run()
    }

    // test to make sure you can't ADD DRY INGREDIENTS on a baking dish


    // test to make sure LIQUEFY ingredient changes a dry or non-specified
    // ingredient to a liqued (i.e. when it outputs it outputs a character)
    "Liquefy test 1" should "make sure it works" in {
        object LiquefyTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 1") 


                START_INGREDIENTS

                1 G ('salt) 

                END_INGREDIENTS


                LIQUEFY ('salt) 


                RUN


                assert(variableBindings('salt).state == I_LIQUID)
            }
        }

        LiquefyTest1.run()
    }

    // test to make sure LIQUEFY can't work on unspecified ingredients
    "Liquefy test 2" should "make sure LIQUEFY doesn't work on unspecified ingredients" in {
        object LiquefyTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 2") 


                START_INGREDIENTS

                1 G ('salt) 

                END_INGREDIENTS


                LIQUEFY ('pepper) 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        LiquefyTest2.run()
    }

    // test to make sure LIQUEFY on an already liquid ingredient doesn't do
    // anything (i.e. succeed, but it shouldn't change the value or anything)
    "Liquefy test 3" should "make sure LIQUEFY doesn't do anything to liquid ingredient" in {
        object LiquefyTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 3") 


                START_INGREDIENTS

                1 ML ('juice) 

                END_INGREDIENTS


                LIQUEFY ('juice) 


                RUN


                assert(variableBindings('juice).state == I_LIQUID)
            }
        }

        LiquefyTest3.run()
    }

    // test to make sure LIQUEFY CONTENTS makes everything in the bowl a liquid
    "Liquefy test 4" should "make sure LIQUEFY CONTENTS makes everything in the bowl a liquid" in {
        object LiquefyTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 4") 


                START_INGREDIENTS

                1 G ('apples) 

                5 G ('bananas) 

                6 KG ('pears) 

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL 

                PUT ('bananas) INTO FIRST MIXING_BOWL 

                PUT ('pears) INTO FIRST MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.state == I_LIQUID)
                assert(mixingStacks(FIRST).pop.state == I_LIQUID)
                assert(mixingStacks(FIRST).pop.state == I_LIQUID)
            }
        }

        LiquefyTest4.run()
    }
    
    // test to make sure LIQUIFY CONTENTS DOESN'T affect the original ingredients
    // but only the ingredients in the bowl (i.e. if 'potatoes are in a bowl,
    // and you liquify the bowl, the original potatoes don't get liquified
    "Liquefy test 5" should "make sure LIQUEFY CONTENTS doesn't affect original ingredients" in {
        object LiquefyTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 5") 


                START_INGREDIENTS

                1 G ('apples) 

                5 G ('bananas) 

                6 KG ('pears) 

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL 

                PUT ('bananas) INTO FIRST MIXING_BOWL 

                PUT ('pears) INTO FIRST MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 


                RUN


                assert(variableBindings('apples).state == I_DRY)
                assert(variableBindings('bananas).state == I_DRY)
                assert(variableBindings('pears).state == I_DRY)
            }
        }

        LiquefyTest5.run()
    }

    // test LIQUEFY CONTENTS on all 5 bowls
    "Liquefy test 6" should "make sure LIQUEFY CONTENTS doesn't affect original ingredients" in {
        object LiquefyTest6 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 6") 


                START_INGREDIENTS

                1 G ('apples) 

                5 G ('bananas) 

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL 

                PUT ('bananas) INTO FIRST MIXING_BOWL 

                PUT ('apples) INTO SECOND MIXING_BOWL 

                PUT ('bananas) INTO SECOND MIXING_BOWL 

                PUT ('apples) INTO THIRD MIXING_BOWL 

                PUT ('bananas) INTO THIRD MIXING_BOWL 

                PUT ('apples) INTO FOURTH MIXING_BOWL 

                PUT ('bananas) INTO FOURTH MIXING_BOWL 

                PUT ('apples) INTO FIFTH MIXING_BOWL 

                PUT ('bananas) INTO FIFTH MIXING_BOWL 


                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 

                LIQUEFY CONTENTS OF THE SECOND MIXING_BOWL 

                LIQUEFY CONTENTS OF THE THIRD MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FOURTH MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.state == I_LIQUID)
                assert(mixingStacks(FIRST).pop.state == I_LIQUID)
                assert(mixingStacks(SECOND).pop.state == I_LIQUID)
                assert(mixingStacks(SECOND).pop.state == I_LIQUID)
                assert(mixingStacks(THIRD).pop.state == I_LIQUID)
                assert(mixingStacks(THIRD).pop.state == I_LIQUID)
                assert(mixingStacks(FOURTH).pop.state == I_LIQUID)
                assert(mixingStacks(FOURTH).pop.state == I_LIQUID)
                assert(mixingStacks(FIFTH).pop.state == I_LIQUID)
                assert(mixingStacks(FIFTH).pop.state == I_LIQUID)
            }
        }

        LiquefyTest6.run()
    }
    
    // test to make sure LIQUEFY CONTENTS fails on a baking dish
    "Liquefy test 7" should "make sure LIQUEFY CONTENTS fails on a baking dish" in {
        object LiquefyTest7 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 7") 


                START_INGREDIENTS

                1 G ('potatoes) 

                END_INGREDIENTS


                intercept[RuntimeException] {
                    LIQUEFY CONTENTS OF THE FIRST BAKING_DISH 
                }
            }
        }

        LiquefyTest7.run()
    }


    // STIR NTH MIXING BOWL

    // test general functionality (moves top ingredient down some #)
    "Stir test 1" should "make sure it works" in {
        object StirTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                4 ('bananas) 

                5 ('apples) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                PUT ('bananas) INTO FIRST MIXING_BOWL 

                PUT ('apples) INTO FIRST MIXING_BOWL 

                STIR THE FIRST MIXING_BOWL FOR (3) MINUTES 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 4)
                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 5)
                assert(mixingStacks(FIRST).pop.asNumber == 1)          
            }
        }

        StirTest1.run()
    }

    // test to make sure it moves top ingredient to bottom if you stir
    // a # greater than the # of things in the stack
    "Stir test 2" should "make sure top ingredient is moved to bottom if # is greater than size of stack" in {
        object StirTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                STIR THE FIRST MIXING_BOWL FOR (5) MINUTES 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).peek.asNumber == 3)
            }
        }

        StirTest2.run()
    }

    // test to make sure you can't stir a negative # of minutes
    "Stir test 3" should "make sure you can't STIR for a negative number of minutes" in {
        object StirTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                intercept[RuntimeException] {
                    STIR THE FIRST MIXING_BOWL FOR (-1) MINUTES 
                }   
            }
        }

        StirTest3.run()
    } 

    // test to make sure you can't stir 0 minutes
    "Stir test 4" should "make sure you can't STIR for 0 minutes" in {
        object StirTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                intercept[RuntimeException] {
                    STIR THE FIRST MIXING_BOWL FOR (0) MINUTES 
                }   
            }
        }

        StirTest4.run()
    } 

    // test to make sure it works on all 5 mixing bowls
    "Stir test 5" should "make sure STIR works on all 5 mixing bowls" in {
        object StirTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 5") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                PUT ('potatoes) INTO SECOND MIXING_BOWL 

                PUT ('blueberries) INTO SECOND MIXING_BOWL 

                PUT ('strawberries) INTO SECOND MIXING_BOWL 

                PUT ('potatoes) INTO THIRD MIXING_BOWL 

                PUT ('blueberries) INTO THIRD MIXING_BOWL 

                PUT ('strawberries) INTO THIRD MIXING_BOWL 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL 

                PUT ('blueberries) INTO FOURTH MIXING_BOWL 

                PUT ('strawberries) INTO FOURTH MIXING_BOWL 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL 

                PUT ('blueberries) INTO FIFTH MIXING_BOWL 

                PUT ('strawberries) INTO FIFTH MIXING_BOWL 

                STIR THE FIRST MIXING_BOWL FOR (2) MINUTES 

                STIR THE SECOND MIXING_BOWL FOR (2) MINUTES 

                STIR THE THIRD MIXING_BOWL FOR (2) MINUTES 

                STIR THE FOURTH MIXING_BOWL FOR (2) MINUTES 

                STIR THE FIFTH MIXING_BOWL FOR (2) MINUTES 

                
                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(SECOND).pop.asNumber == 2)
                assert(mixingStacks(SECOND).pop.asNumber == 1)
                assert(mixingStacks(SECOND).pop.asNumber == 3)
                assert(mixingStacks(THIRD).pop.asNumber == 2)
                assert(mixingStacks(THIRD).pop.asNumber == 1)
                assert(mixingStacks(THIRD).pop.asNumber == 3)
                assert(mixingStacks(FOURTH).pop.asNumber == 2)
                assert(mixingStacks(FOURTH).pop.asNumber == 1)
                assert(mixingStacks(FOURTH).pop.asNumber == 3)
                assert(mixingStacks(FIFTH).pop.asNumber == 2)
                assert(mixingStacks(FIFTH).pop.asNumber == 1)
                assert(mixingStacks(FIFTH).pop.asNumber == 3)
            }
        }

        StirTest5.run()
    } 
    
    // test to make sure can't work on baking dishes
    // TODO
    // "Stir test 6" should "make sure you can't STIR a baking dish" in {
    //     object StirTest6 extends ScalaChef {
    //         def run(): Unit = {
    //             TITLE ("Stir test 6") 


    //             START_INGREDIENTS

    //             1 ('potatoes) 

    //             END_INGREDIENTS

    //             intercept[RuntimeException] {
    //                 STIR THE FIRST BAKING_DISH FOR (1) MINUTES 
    //             }   
    //         }
    //     }

    //     StirTest6.run()
    // } 


    // STIR INGREDIENT

    // note these tests are exactly as above, except now the # comes from
    // the ingredient

    // test general functionality (moves top ingredient down some # )
    "Stir ingredient test 1" should "make sure it works" in {
        object StirIngredientTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                4 ('bananas) 

                5 ('apples) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                PUT ('bananas) INTO FIRST MIXING_BOWL 

                PUT ('apples) INTO FIRST MIXING_BOWL 

                STIR ('strawberries) INTO THE FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 4)
                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 5)
                assert(mixingStacks(FIRST).pop.asNumber == 1)          
            }
        }

        StirIngredientTest1.run()
    }

    // test to make sure it moves top ingredient to bottom if you stir
    // a # greater than the # of things in the stack
    "Stir ingredient test 2" should "make sure top ingredient is moved to bottom if # is greater than size of stack" in {
        object StirIngredientTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                5 ML ('vinegar) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                STIR ('vinegar) INTO THE FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).peek.asNumber == 3)
            }
        }

        StirIngredientTest2.run()
    }

    // test to make sure you can't stir 0 minutes
    "Stir ingredient test 3" should "make sure you can't STIR for 0 minutes" in {
        object StirIngredientTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 3") 


                START_INGREDIENTS

                0 ('cookies) 

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                STIR ('cookies) INTO THE FIRST MIXING_BOWL 


                intercept[RuntimeException] {
                    RUN
                }   
            }
        }

        StirIngredientTest3.run()
    } 

    // test to make sure it works on all 5 mixing bowls
    "Stir ingredient test 4" should "make sure STIR works on all 5 mixing bowls" in {
        object StirIngredientTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('blueberries) 

                3 ('strawberries) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('blueberries) INTO FIRST MIXING_BOWL 

                PUT ('strawberries) INTO FIRST MIXING_BOWL 

                PUT ('potatoes) INTO SECOND MIXING_BOWL 

                PUT ('blueberries) INTO SECOND MIXING_BOWL 

                PUT ('strawberries) INTO SECOND MIXING_BOWL 

                PUT ('potatoes) INTO THIRD MIXING_BOWL 

                PUT ('blueberries) INTO THIRD MIXING_BOWL 

                PUT ('strawberries) INTO THIRD MIXING_BOWL 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL 

                PUT ('blueberries) INTO FOURTH MIXING_BOWL 

                PUT ('strawberries) INTO FOURTH MIXING_BOWL 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL 

                PUT ('blueberries) INTO FIFTH MIXING_BOWL 

                PUT ('strawberries) INTO FIFTH MIXING_BOWL 

                STIR ('blueberries) INTO THE FIRST MIXING_BOWL 

                STIR ('blueberries) INTO THE SECOND MIXING_BOWL 

                STIR ('blueberries) INTO THE THIRD MIXING_BOWL 

                STIR ('blueberries) INTO THE FOURTH MIXING_BOWL 

                STIR ('blueberries) INTO THE FIFTH MIXING_BOWL 

                
                RUN


                assert(mixingStacks(FIRST).pop.asNumber == 2)
                assert(mixingStacks(FIRST).pop.asNumber == 1)
                assert(mixingStacks(FIRST).pop.asNumber == 3)
                assert(mixingStacks(SECOND).pop.asNumber == 2)
                assert(mixingStacks(SECOND).pop.asNumber == 1)
                assert(mixingStacks(SECOND).pop.asNumber == 3)
                assert(mixingStacks(THIRD).pop.asNumber == 2)
                assert(mixingStacks(THIRD).pop.asNumber == 1)
                assert(mixingStacks(THIRD).pop.asNumber == 3)
                assert(mixingStacks(FOURTH).pop.asNumber == 2)
                assert(mixingStacks(FOURTH).pop.asNumber == 1)
                assert(mixingStacks(FOURTH).pop.asNumber == 3)
                assert(mixingStacks(FIFTH).pop.asNumber == 2)
                assert(mixingStacks(FIFTH).pop.asNumber == 1)
                assert(mixingStacks(FIFTH).pop.asNumber == 3)
            }
        }

        StirIngredientTest4.run()
    } 
    
    // test to make sure can't work on baking dishes
    // "Stir ingredient test 6" should "make sure you can't STIR a baking dish" in {
    //     object StirIngredientTest6 extends ScalaChef {
    //         def run(): Unit = {
    //             TITLE ("Stir ingredient test 6") 


    //             START_INGREDIENTS

    //             1 ('potatoes) 

    //             END_INGREDIENTS

    //             intercept[RuntimeException] {
    //                 STIR ('potatoes) INTO THE FIRST BAKING_DISH 
    //             }   
    //         }
    //     }

    //     StirIngredientTest6.run()
    // } 

    // DIFFERENT: make sure the ingredient actually exists
    // TODO


    // MIX

    // general functionality
    "Mix test 1" should "make sure it works" in {
        object MixTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Mix test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                MIX THE FIRST MIXING_BOWL WELL 


                RUN


                if(mixingStacks(FIRST).pop.asNumber == 1) {
                    assert(mixingStacks(FIRST).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(FIRST).peek.asNumber == 1)
                }
            }
        }

        MixTest1.run()
    }

    // works on all 5 bowls
    "Mix test 2" should "make sure you can MIX all 5 bowls" in {
        object MixTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Mix test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                PUT ('potatoes) INTO SECOND MIXING_BOWL 

                PUT ('cakes) INTO SECOND MIXING_BOWL 

                PUT ('potatoes) INTO THIRD MIXING_BOWL 

                PUT ('cakes) INTO THIRD MIXING_BOWL 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL 

                PUT ('cakes) INTO FOURTH MIXING_BOWL 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL 

                PUT ('cakes) INTO FIFTH MIXING_BOWL 

                MIX THE FIRST MIXING_BOWL WELL 

                MIX THE SECOND MIXING_BOWL WELL 

                MIX THE THIRD MIXING_BOWL WELL 

                MIX THE FOURTH MIXING_BOWL WELL 

                MIX THE FIFTH MIXING_BOWL WELL 


                RUN


                if(mixingStacks(FIRST).pop.asNumber == 1) {
                    assert(mixingStacks(FIRST).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(FIRST).peek.asNumber == 1)
                }
                if(mixingStacks(SECOND).pop.asNumber == 1) {
                    assert(mixingStacks(SECOND).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(SECOND).peek.asNumber == 1)
                }
                if(mixingStacks(THIRD).pop.asNumber == 1) {
                    assert(mixingStacks(THIRD).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(THIRD).peek.asNumber == 1)
                }
                if(mixingStacks(FOURTH).pop.asNumber == 1) {
                    assert(mixingStacks(FOURTH).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(FOURTH).peek.asNumber == 1)
                }
                if(mixingStacks(FIFTH).pop.asNumber == 1) {
                    assert(mixingStacks(FIFTH).peek.asNumber == 2)
                }
                else {
                    assert(mixingStacks(FIFTH).peek.asNumber == 1)
                }
            }
        }

        MixTest2.run()
    }

    // fails on a dish
    // TODO
    // "Mix test 3" should "make sure MIX doesn't work on a baking dish" in {
    //     object MixTest3 extends ScalaChef {
    //         def run(): Unit = {
    //             TITLE ("Mix test 3") 


    //             START_INGREDIENTS

    //             1 ('potatoes) 

    //             END_INGREDIENTS


    //             intercept[RuntimeException] {
    //                 MIX THE FIRST BAKING_DISH WELL 
    //             }
    //         }
    //     }

    //     MixTest3.run()
    // }


    // CLEAN 

    // test general funcitonality
    "Clean test 1" should "make sure it works" in {
        object CleanTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Clean test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                CLEAN FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).isEmpty)
            }
        }

        CleanTest1.run()
    }

    // test to make sure cleaning a mixing bowl will NOT affect the ingredients
    // not in the mixing bowl
    "Clean test 2" should "make sure CLEAN doesn't affect ingredients not in bowl" in {
        object CleanTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Clean test 2") 


                START_INGREDIENTS

                1 G ('potatoes) 

                2 ML ('vinegar) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('vinegar) INTO FIRST MIXING_BOWL 

                CLEAN FIRST MIXING_BOWL 


                RUN


                assert(variableBindings('potatoes).asNumber == 1)
                assert(variableBindings('potatoes).state == I_DRY)
                assert(variableBindings('vinegar).asNumber == 2)
                assert(variableBindings('vinegar).state == I_LIQUID)
            }
        }

        CleanTest2.run()
    }

    // test to see if it works on all 5 bowls
    "Clean test 3" should "make sure CLEAN works on all 5 mixing bowls" in {
        object CleanTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Clean test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                PUT ('potatoes) INTO SECOND MIXING_BOWL 

                PUT ('cakes) INTO SECOND MIXING_BOWL 

                PUT ('potatoes) INTO THIRD MIXING_BOWL 

                PUT ('cakes) INTO THIRD MIXING_BOWL 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL 

                PUT ('cakes) INTO FOURTH MIXING_BOWL 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL 

                PUT ('cakes) INTO FIFTH MIXING_BOWL 

                CLEAN FIRST MIXING_BOWL 

                CLEAN SECOND MIXING_BOWL 

                CLEAN THIRD MIXING_BOWL 

                CLEAN FOURTH MIXING_BOWL 

                CLEAN FIFTH MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).isEmpty)
                assert(mixingStacks(SECOND).isEmpty)
                assert(mixingStacks(THIRD).isEmpty)
                assert(mixingStacks(FOURTH).isEmpty)
                assert(mixingStacks(FIFTH).isEmpty)
            }
        }

        CleanTest3.run()
    }



    // POUR

    // general functionality
    "Pour test 1" should "make sure it works" in {
        object PourTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Pour test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 


                RUN


                assert(bakingStacks(FIRST).pop.asNumber == 2)
                assert(bakingStacks(FIRST).peek.asNumber == 1)
            }
        }

        PourTest1.run()
    }

    // test to make sure you can pour to the same dish twice with
    // expected behavior
    "Pour test 2" should "make sure you can pour to the same dish twice" in {
        object PourTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Pour test 2") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                3 ('tacos) 

                4 ('hamburgers) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                PUT ('tacos) INTO SECOND MIXING_BOWL 

                PUT ('hamburgers) INTO SECOND MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

                POUR CONTENTS OF THE SECOND MIXING_BOWL INTO THE FIRST BAKING_DISH 


                RUN


                assert(bakingStacks(FIRST).pop.asNumber == 4)
                assert(bakingStacks(FIRST).pop.asNumber == 3)
                assert(bakingStacks(FIRST).pop.asNumber == 2)
                assert(bakingStacks(FIRST).pop.asNumber == 1)
            }
        }

        PourTest2.run()
    }

    // make sure order of mixing bowl that is poured is preserved
    // (i.e. the top of the mixing bowl should be the top of the 
    // baking dish after pour)
    "Pour test 3" should "make sure mixing bowl is maintained" in {
        object PourTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Pour test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 


                RUN


                assert(bakingStacks(FIRST).peek.asNumber == mixingStacks(FIRST).peek.asNumber)
            }
        }

        PourTest3.run()
    }

    // make sure editing the mixing bowl after a POUR doesn't alter
    // the same ingredient in the baking dish
    "Pour test 4" should "make sure altering mixing bowl doesn't alter baking dish" in {
        object PourTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Pour test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                PUT ('cakes) INTO FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

                PUT ('potatoes) INTO FIRST MIXING_BOWL 


                RUN


                assert(bakingStacks(FIRST).peek.asNumber == 2)
            }
        }

        PourTest4.run()
    }



    // REFRIGERATE

    // general functionality
    "Refrigerate test 1" should "make sure it works" in {
        object RefrigerateTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Refrigerate test 1") 


                START_INGREDIENTS

                1 ('potatoes) 

                2 ('cakes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                REFRIGERATE NOW

                PUT ('cakes) INTO FIRST MIXING_BOWL 


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 1)
            }
        }

        RefrigerateTest1.run()
    }

    // test to make sure it passes back the called function's first
    // mixing bowl if it's called in a sub-recipe
    // TODO

    // test to make sure the default version works (REFRIGERATE 

    // test to make sure REFRIGERATE FOR 0-5 HOURS works for all of the
    // numbers specified here (even 0)

    // test to make sure it prints baking stacks even if it's not the
    // main program
    


    // SERVES

    // test to make sure SERVES prints nothing if the baking dishes (but
    // not the mixing bowls) are empty
    "Serves test 1" should "print nothing" in {
        object ServesTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Serves test 1") 


                START_INGREDIENTS

                70 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                SERVES (1) 


                RUN
            }
        }

        ServesTest1.run()
    }

    // test to make sure SERVES actually prints stuff from all 5 dishes
    "Serves test 2" should "print stuff from all 5 dishes" in {
        object ServesTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Serves test 2") 


                START_INGREDIENTS

                70 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE SECOND BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE THIRD BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FOURTH BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIFTH BAKING_DISH 

                SERVES (5) 


                RUN
            }
        }

        ServesTest2.run()
    }

    // test to make sure SERVES only prints the specified # of baking dishes
    // (e.g. SERVES (2) only prints the first 2 and ignores latter 3
    "Serves test 3" should "print stuff from first 2 dishes" in {
        object ServesTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Serves test 3") 


                START_INGREDIENTS

                70 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE SECOND BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE THIRD BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FOURTH BAKING_DISH 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIFTH BAKING_DISH 

                SERVES (2) 


                RUN
            }
        }

        ServesTest3.run()
    }

    // test to make sure only 1 SERVES can exist in a program
    "Serves test 4" should "make sure only 1 SERVES can exist in a program" in {
        object ServesTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Serves test 4") 


                START_INGREDIENTS

                70 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                SERVES (1) 

                intercept[RuntimeException] {
                    SERVES (1) 
                }
            }
        }

        ServesTest4.run()
    }

    // test to make sure SERVES only works in the main recipe
    "Serves test 5" should "make sure SERVES only works in the main recipe" in {
        object ServesTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Serves test 5") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                SERVE WITH "Serves auxiliary" 


                TITLE ("Serves auxiliary") 


                START_INGREDIENTS

                2 ('cakes)

                END_INGREDIENTS

                intercept[RuntimeException] {
                    SERVES (1) 
                }
            }
        }

        ServesTest5.run()
    }

    // function related tests

    // can't declare a new function (i.e. start a new TITLE declaration)
    // while parsing ingredients
    "Function test 1" should "make sure you can't declare a new function while parsing ingredients" in {
        object FunctionTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Function test 1") 


                intercept[RuntimeException] {
                    START_INGREDIENTS

                    TITLE ("This is bad") 

                    1 ('potatoes) 

                    END_INGREDIENTS
                }
            }
        }

        FunctionTest1.run()
    }

    // can't declare a new function until ingredients have been declared (even
    // if the ingredients are empty)

    "Function test 2" should "make sure you can't declare a new function until ingredients declared" in {
        object FunctionTest2 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Function test 2") 

                intercept[RuntimeException] {
                    TITLE ("This is bad") 
                }
            }
        }

        FunctionTest2.run()
    }



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
    "Function test 3" should "make sure you can't call the main recipe" in {
        object FunctionTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Function test 3") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                SERVE WITH "Function test 1" 
                

                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        FunctionTest3.run()
    }


    // make sure that if main recipe doesn't have a SERVES statement that
    // it DOESN'T run to the sub recipes
    "Function test 4" should "make sure a serves in the main recipe stops execution" in {
        object FunctionTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Function test 4") 


                START_INGREDIENTS

                1 ('potatoes) 

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

                SERVES (1) 
                

                TITLE ("Serves auxiliary") 


                START_INGREDIENTS

                2 ('cakes)

                END_INGREDIENTS


                PUT ('cakes) INTO FIRST MIXING_BOWL 

                
                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 1)
            }
        }

        FunctionTest4.run()
    }




    // LOOPS: test cases to consider
  
    // Loop with initial ingredient already 0
    "Loop test 1" should "jump to end of loop" in {
        object LoopTest1 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 1") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                0 ('beans) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                "COOK" THE ('beans) UNTIL "COOKED" 

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
                TITLE ("Loop test 2") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                3 ('beans) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                "COOK" THE ('beans) UNTIL "COOKED" 

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
                TITLE ("Loop test 3") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                3 ('beans) 

                END_INGREDIENTS

                "BAKE" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 

                "BAKE" THE ('beans) UNTIL "BAKED" 

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
                TITLE ("Loop test 4") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                3 ('beans) 
                
                0 ('onions) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 
                
                PUT ('onions) INTO FIRST MIXING_BOWL 
                
                FOLD ('beans) INTO FIRST MIXING_BOWL 

                "COOK" UNTIL "COOKED" 

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
                TITLE ("Loop test 5") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                3 ('beans) 
                
                0 ('onions) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 
                
                PUT ('onions) INTO FIRST MIXING_BOWL 
                
                FOLD ('beans) INTO FIRST MIXING_BOWL 

                "COOK" THE ('potatoes) UNTIL "COOKED" 

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
                TITLE ("Loop test 6") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 

                END_INGREDIENTS
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 
                
                intercept[RuntimeException] {
                    "COOK" THE ('beans) UNTIL "COOKED" 
                }
            }
        }     
        LoopTest6.run()
    }
    
    
    // loops without starting phrase throw an exception
    "Loop test 7" should "throw an exception" in {
        object LoopTest7 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 7") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                PUT ('potatoes) INTO FIRST MIXING_BOWL 
                
                intercept[RuntimeException] {
                    SERVES (1) 
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
                TITLE ("Loop test 8") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 
                
                END_INGREDIENTS

                "BAKE" THE ('beans) NOW
                
                "COOK" THE ('potatoes) NOW

                intercept[RuntimeException] {
                     "BAKE" THE ('beans) UNTIL "BAKED" 
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
                TITLE ("Loop test 9") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) NOW

                intercept[RuntimeException] {
                     "COOK" THE ('beans) UNTIL "BAKED" 
                }
            }
        }
        LoopTest9.run()
    }
    
    //(cook potatoes ... cook potatoes until cook)
    "Loop test 10" should "throw an exception" in {
        object LoopTest10 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 10") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) NOW

                intercept[RuntimeException] {
                     "COOK" THE ('beans) UNTIL "COOK" 
                }
            }
        }
        LoopTest10.run()
    }
    
    // can't define loops with same verb
    "Loop test 11" should "throw an exception" in {
        object LoopTest11 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 11") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) NOW
                
                "COOK" THE ('potatoes) UNTIL "COOKED" 

                intercept[RuntimeException] {
                     "COOK" THE ('beans) NOW
                }
            }
        }
        LoopTest11.run()
    }
    
    // nested loops work
    "Loop test 12" should "loop properly" in {
        object LoopTest12 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Loop test 12") 


                START_INGREDIENTS

                5 ('potatoes) 
                
                2 ('beans) 
                
                5 ('onions) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                "BAKE" THE ('potatoes) NOW
                
                PUT ('beans) INTO FIRST MIXING_BOWL 
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" 
                
                PUT ('onions) INTO FIRST MIXING_BOWL 
                
                FOLD ('potatoes) INTO FIRST MIXING_BOWL 

                "COOK" THE ('beans) UNTIL "COOKED" 

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
                TITLE ("Loop test 13") 


                START_INGREDIENTS

                5 ('potatoes) 
                
                2 ('beans) 
                
                END_INGREDIENTS
                
                "BAKE" THE ('potatoes) NOW
                
                PUT ('beans) INTO FIRST MIXING_BOWL 
                
                SET ASIDE 
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" 


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
                TITLE ("Loop test 14") 


                START_INGREDIENTS

                1 ('potatoes) 
                
                1 ('beans) 
                
                END_INGREDIENTS

                "COOK" THE ('potatoes) NOW
                
                "COOK" THE ('potatoes) UNTIL "COOKED" 
                
                SET ASIDE 

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
                TITLE ("Loop test 15") 


                START_INGREDIENTS

                5 ('potatoes) 
                
                3 ('beans) 

                END_INGREDIENTS

                "COOK" THE ('beans) NOW
                
                "BAKE" THE ('potatoes) NOW
                
                PUT ('beans) INTO FIRST MIXING_BOWL 
                
                SET ASIDE 
                
                "BAKE" THE ('potatoes) UNTIL "BAKED" 

                "COOK" THE ('beans) UNTIL "COOKED" 

                RUN
                

                assert(mixingStacks(FIRST).size() == 3)
            }
        }
        LoopTest15.run()
    }
}
