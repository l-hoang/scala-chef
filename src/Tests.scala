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




    // NEW: make sure ingredients can't be negative (0 is fine even though
    // it's a bit strange)



    // TAKE

    // note you'll have to provide input to these tests when you run them

    // general functionality (input int, it saves it to an ingredient)

    // TAKE on an existing ingredient does not alter it's state (i.e. if you
    // take on a potato which is I_DRY, it won't change to I_EITHER, which is
    // the default for new ingredients

    // can't TAKE negative values

    // can't TAKE non numbers

    // can't TAKE floats


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
    "Put test 3" should "make sure PUT doesn't take a non-existent ingredient" in {
        object PutTest3 extends ScalaChef {
            def run(): Unit = { 
                TITLE ("Put test 3") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                PUT ('cakes) INTO FIRST MIXING_BOWL END


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

                FOLD ('vegetable) INTO FIRST MIXING_BOWL END


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
                TITLE ("Fold test 4") END


                START_INGREDIENTS

                1 ('potatoes) END

                END_INGREDIENTS


                FOLD ('potatoes) INTO FIRST MIXING_BOWL END


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


    // test to make sure ADD adds to something already on a stack 
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
                TITLE ("Add test 3") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                ADD ('cakes) TO FIRST MIXING_BOWL END
        

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
                TITLE ("Add test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                ADD ('potatoes) TO FIRST MIXING_BOWL END


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


    // test to make sure REMOVE subtracts from something already on a stack 
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
    "Remove test 3" should "make sure REMOVE doesn't take a non-existent ingredient" in {
        object RemoveTest3 extends ScalaChef {
            def run(): Unit = {       
                TITLE ("Remove test 3") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                REMOVE ('cakes) FROM FIRST MIXING_BOWL END
        

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
                TITLE ("Remove test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                REMOVE ('potatoes) FROM FIRST MIXING_BOWL END


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

    // test to make sure COMBINE multiplies to something already on a stack
    "Combine test 1" should "do a simple COMBINE in first mixing bowl" in {
        object CombineTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Combine test 1") END


                START_INGREDIENTS

                2 ('chocolate) END

                3 ('milk) END

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL END

                COMBINE ('chocolate) INTO FIRST MIXING_BOWL END


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
                TITLE ("Combine test 2") END


                START_INGREDIENTS

                5 ('doughnuts) END

                4 ('glaze) END

                3 ('chocolate) END

                2 ('creme) END

                1 ('maple) END

                0 ('sugar) END

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL END

                PUT ('doughnuts) INTO SECOND MIXING_BOWL END

                PUT ('doughnuts) INTO THIRD MIXING_BOWL END

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL END

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL END

                COMBINE ('glaze) INTO FIRST MIXING_BOWL END

                COMBINE ('chocolate) INTO SECOND MIXING_BOWL END

                COMBINE ('creme) INTO THIRD MIXING_BOWL END

                COMBINE ('maple) INTO FOURTH MIXING_BOWL END

                COMBINE ('sugar) INTO FIFTH MIXING_BOWL END


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
                TITLE ("Combine test 3") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                COMBINE ('cakes) INTO FIRST MIXING_BOWL END
        

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
                TITLE ("Combine test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                COMBINE ('potatoes) INTO FIRST MIXING_BOWL END


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
                TITLE ("Combine test 5") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    COMBINE ('potatoes) INTO FIRST BAKING_DISH END
                }
            }
        }

        CombineTest5.run()
    }


    // test to make sure add DIVIDE divides something already on a stack
    "Divide test 1" should "do a simple DIVIDE in first mixing bowl" in {
        object DivideTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Divide test 1") END


                START_INGREDIENTS

                2 ('chocolate) END

                6 ('milk) END

                END_INGREDIENTS


                PUT ('milk) INTO FIRST MIXING_BOWL END

                DIVIDE ('chocolate) INTO FIRST MIXING_BOWL END


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
                TITLE ("Divide test 2") END


                START_INGREDIENTS

                20 ('doughnuts) END

                10 ('glaze) END

                5 ('chocolate) END

                4 ('creme) END

                2 ('maple) END

                1 ('sugar) END

                END_INGREDIENTS


                PUT ('doughnuts) INTO FIRST MIXING_BOWL END

                PUT ('doughnuts) INTO SECOND MIXING_BOWL END

                PUT ('doughnuts) INTO THIRD MIXING_BOWL END

                PUT ('doughnuts) INTO FOURTH MIXING_BOWL END

                PUT ('doughnuts) INTO FIFTH MIXING_BOWL END

                DIVIDE ('glaze) INTO FIRST MIXING_BOWL END

                DIVIDE ('chocolate) INTO SECOND MIXING_BOWL END

                DIVIDE ('creme) INTO THIRD MIXING_BOWL END

                DIVIDE ('maple) INTO FOURTH MIXING_BOWL END

                DIVIDE ('sugar) INTO FIFTH MIXING_BOWL END


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
                TITLE ("Divide test 3") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                DIVIDE ('cakes) INTO FIRST MIXING_BOWL END
        

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
                TITLE ("Divide test 4") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                DIVIDE ('potatoes) INTO FIRST MIXING_BOWL END


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
                TITLE ("Divide test 5") END
        

                START_INGREDIENTS
        
                2 ('potatoes) END
        
                END_INGREDIENTS


                intercept[RuntimeException] {
                    DIVIDE ('potatoes) INTO FIRST BAKING_DISH END
                }
            }
        }

        DivideTest5.run()
    }


    // test to make sure ADD DRY INGREDIENTS adds all of the dry ingredients
    // together (i.e. does it work basically)
    // "Add dry ingredients test 1" should "make sure it works" in {
    //     object AddDryTest1 extends ScalaChef {
    //         def run(): Unit = {
    //             TITLE ("Add dry ingredients test 1") END


    //             START_INGREDIENTS

    //             2 G ('potatoes) END

    //             3 KG ('turkey) END

    //             4 G ('stuffing) END

    //             0 G ('corn) END

    //             END_INGREDIENTS


    //             ADD DRY INGREDIENTS TO FIRST MIXING_BOWL END


    //             RUN


    //             assert(mixingStacks(FIRST).peek.asNumber == 9)
    //         }
    //     }

    //     AddDryTest1.run()
    // }

    // test to make sure ADD DRY INGREDIENTS ignores liquid/either ingredients

    // test to make sure ADD DRY INGREDIENTS works on all 5 mixing bowls

    // test to make sure ADD DRY INGREDIENTS adds 0 to a mixing bowl if there are no
    // dry ingredients

    // test to make sure ADD DRY INGREDIENTS adds a DRY ingredient when it does
    // the pushing onto a mixing bowl

    // test to make sure you can't ADD DRY INGREDIENTS on a baking dish




    // test to make sure LIQUEFY ingredient changes a dry or non-specified
    // ingredient to a liqued (i.e. when it outputs it outputs a character)
    "Liquefy test 1" should "make sure it works" in {
        object LiquefyTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Liquefy test 1") END


                START_INGREDIENTS

                1 G ('salt) END

                END_INGREDIENTS


                LIQUEFY ('salt) END


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
                TITLE ("Liquefy test 2") END


                START_INGREDIENTS

                1 G ('salt) END

                END_INGREDIENTS


                LIQUEFY ('pepper) END


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
                TITLE ("Liquefy test 3") END


                START_INGREDIENTS

                1 ML ('juice) END

                END_INGREDIENTS


                LIQUEFY ('juice) END


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
                TITLE ("Liquefy test 4") END


                START_INGREDIENTS

                1 G ('apples) END

                5 G ('bananas) END

                6 KG ('pears) END

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL END

                PUT ('bananas) INTO FIRST MIXING_BOWL END

                PUT ('pears) INTO FIRST MIXING_BOWL END

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END


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
                TITLE ("Liquefy test 5") END


                START_INGREDIENTS

                1 G ('apples) END

                5 G ('bananas) END

                6 KG ('pears) END

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL END

                PUT ('bananas) INTO FIRST MIXING_BOWL END

                PUT ('pears) INTO FIRST MIXING_BOWL END

                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END


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
                TITLE ("Liquefy test 6") END


                START_INGREDIENTS

                1 G ('apples) END

                5 G ('bananas) END

                END_INGREDIENTS


                PUT ('apples) INTO FIRST MIXING_BOWL END

                PUT ('bananas) INTO FIRST MIXING_BOWL END

                PUT ('apples) INTO SECOND MIXING_BOWL END

                PUT ('bananas) INTO SECOND MIXING_BOWL END

                PUT ('apples) INTO THIRD MIXING_BOWL END

                PUT ('bananas) INTO THIRD MIXING_BOWL END

                PUT ('apples) INTO FOURTH MIXING_BOWL END

                PUT ('bananas) INTO FOURTH MIXING_BOWL END

                PUT ('apples) INTO FIFTH MIXING_BOWL END

                PUT ('bananas) INTO FIFTH MIXING_BOWL END


                LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END

                LIQUEFY CONTENTS OF THE SECOND MIXING_BOWL END

                LIQUEFY CONTENTS OF THE THIRD MIXING_BOWL END

                LIQUEFY CONTENTS OF THE FOURTH MIXING_BOWL END

                LIQUEFY CONTENTS OF THE FIFTH MIXING_BOWL END


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
                TITLE ("Liquefy test 7") END


                START_INGREDIENTS

                1 G ('potatoes) END

                END_INGREDIENTS


                intercept[RuntimeException] {
                    LIQUEFY CONTENTS OF THE FIRST BAKING_DISH END
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
                TITLE ("Stir test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                4 ('bananas) END

                5 ('apples) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END

                PUT ('bananas) INTO FIRST MIXING_BOWL END

                PUT ('apples) INTO FIRST MIXING_BOWL END    

                STIR THE FIRST MIXING_BOWL FOR (3) MINUTES END  


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

    // test to make sure you can't stir a negative # of minutes
    "Stir test 3" should "make sure you can't STIR for a negative number of minutes" in {
        object StirTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 3") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                intercept[RuntimeException] {
                    STIR THE FIRST MIXING_BOWL FOR (-1) MINUTES END 
                }   
            }
        }

        StirTest3.run()
    } 

    // test to make sure you can't stir 0 minutes
    "Stir test 4" should "make sure you can't STIR for 0 minutes" in {
        object StirTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 4") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                intercept[RuntimeException] {
                    STIR THE FIRST MIXING_BOWL FOR (0) MINUTES END 
                }   
            }
        }

        StirTest4.run()
    } 

    // test to make sure it works on all 5 mixing bowls
    "Stir test 5" should "make sure STIR works on all 5 mixing bowls" in {
        object StirTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir test 5") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                PUT ('potatoes) INTO SECOND MIXING_BOWL END

                PUT ('blueberries) INTO SECOND MIXING_BOWL END

                PUT ('strawberries) INTO SECOND MIXING_BOWL END 

                PUT ('potatoes) INTO THIRD MIXING_BOWL END

                PUT ('blueberries) INTO THIRD MIXING_BOWL END

                PUT ('strawberries) INTO THIRD MIXING_BOWL END 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL END

                PUT ('blueberries) INTO FOURTH MIXING_BOWL END

                PUT ('strawberries) INTO FOURTH MIXING_BOWL END 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL END

                PUT ('blueberries) INTO FIFTH MIXING_BOWL END

                PUT ('strawberries) INTO FIFTH MIXING_BOWL END

                STIR THE FIRST MIXING_BOWL FOR (2) MINUTES END

                STIR THE SECOND MIXING_BOWL FOR (2) MINUTES END

                STIR THE THIRD MIXING_BOWL FOR (2) MINUTES END

                STIR THE FOURTH MIXING_BOWL FOR (2) MINUTES END

                STIR THE FIFTH MIXING_BOWL FOR (2) MINUTES END 

                
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
    //             TITLE ("Stir test 6") END


    //             START_INGREDIENTS

    //             1 ('potatoes) END

    //             END_INGREDIENTS

    //             intercept[RuntimeException] {
    //                 STIR THE FIRST BAKING_DISH FOR (1) MINUTES END 
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
                TITLE ("Stir ingredient test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                4 ('bananas) END

                5 ('apples) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END

                PUT ('bananas) INTO FIRST MIXING_BOWL END

                PUT ('apples) INTO FIRST MIXING_BOWL END    

                STIR ('strawberries) INTO THE FIRST MIXING_BOWL END  


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
    // TODO

    // test to make sure you can't stir a negative # of minutes
    "Stir ingredient test 3" should "make sure you can't STIR for a negative number of minutes" in {
        object StirIngredientTest3 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 3") END


                START_INGREDIENTS

                -1 ('cookies) END

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                STIR ('cookies) INTO THE FIRST MIXING_BOWL END 


                intercept[RuntimeException] {
                    RUN
                }
            }
        }

        StirIngredientTest3.run()
    }  

    // test to make sure you can't stir 0 minutes
    "Stir ingredient test 4" should "make sure you can't STIR for 0 minutes" in {
        object StirIngredientTest4 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 4") END


                START_INGREDIENTS

                0 ('cookies) END

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                STIR ('cookies) INTO THE FIRST MIXING_BOWL END 


                intercept[RuntimeException] {
                    RUN
                }   
            }
        }

        StirIngredientTest4.run()
    } 

    // test to make sure it works on all 5 mixing bowls
    "Stir ingredient test 5" should "make sure STIR works on all 5 mixing bowls" in {
        object StirIngredientTest5 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Stir ingredient test 5") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('blueberries) END

                3 ('strawberries) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('blueberries) INTO FIRST MIXING_BOWL END

                PUT ('strawberries) INTO FIRST MIXING_BOWL END 

                PUT ('potatoes) INTO SECOND MIXING_BOWL END

                PUT ('blueberries) INTO SECOND MIXING_BOWL END

                PUT ('strawberries) INTO SECOND MIXING_BOWL END 

                PUT ('potatoes) INTO THIRD MIXING_BOWL END

                PUT ('blueberries) INTO THIRD MIXING_BOWL END

                PUT ('strawberries) INTO THIRD MIXING_BOWL END 

                PUT ('potatoes) INTO FOURTH MIXING_BOWL END

                PUT ('blueberries) INTO FOURTH MIXING_BOWL END

                PUT ('strawberries) INTO FOURTH MIXING_BOWL END 

                PUT ('potatoes) INTO FIFTH MIXING_BOWL END

                PUT ('blueberries) INTO FIFTH MIXING_BOWL END

                PUT ('strawberries) INTO FIFTH MIXING_BOWL END

                STIR ('blueberries) INTO THE FIRST MIXING_BOWL END

                STIR ('blueberries) INTO THE SECOND MIXING_BOWL END

                STIR ('blueberries) INTO THE THIRD MIXING_BOWL END

                STIR ('blueberries) INTO THE FOURTH MIXING_BOWL END

                STIR ('blueberries) INTO THE FIFTH MIXING_BOWL END 

                
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

        StirIngredientTest5.run()
    } 
    
    // test to make sure can't work on baking dishes
    // "Stir ingredient test 6" should "make sure you can't STIR a baking dish" in {
    //     object StirIngredientTest6 extends ScalaChef {
    //         def run(): Unit = {
    //             TITLE ("Stir ingredient test 6") END


    //             START_INGREDIENTS

    //             1 ('potatoes) END

    //             END_INGREDIENTS

    //             intercept[RuntimeException] {
    //                 STIR ('potatoes) INTO THE FIRST BAKING_DISH END 
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
                TITLE ("Mix test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                MIX THE FIRST MIXING_BOWL WELL END


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
                TITLE ("Mix test 2") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                PUT ('potatoes) INTO SECOND MIXING_BOWL END

                PUT ('cakes) INTO SECOND MIXING_BOWL END

                PUT ('potatoes) INTO THIRD MIXING_BOWL END

                PUT ('cakes) INTO THIRD MIXING_BOWL END

                PUT ('potatoes) INTO FOURTH MIXING_BOWL END

                PUT ('cakes) INTO FOURTH MIXING_BOWL END

                PUT ('potatoes) INTO FIFTH MIXING_BOWL END

                PUT ('cakes) INTO FIFTH MIXING_BOWL END

                MIX THE FIRST MIXING_BOWL WELL END

                MIX THE SECOND MIXING_BOWL WELL END

                MIX THE THIRD MIXING_BOWL WELL END

                MIX THE FOURTH MIXING_BOWL WELL END

                MIX THE FIFTH MIXING_BOWL WELL END


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
    //             TITLE ("Mix test 3") END


    //             START_INGREDIENTS

    //             1 ('potatoes) END

    //             END_INGREDIENTS


    //             intercept[RuntimeException] {
    //                 MIX THE FIRST BAKING_DISH WELL END
    //             }
    //         }
    //     }

    //     MixTest3.run()
    // }


    // CLEAN 

    // test general funcitonality
    // TODO
    "Clean test 1" should "make sure it works" in {
        object CleanTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Clean test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                CLEAN (FIRST) MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).isEmpty)
            }
        }

        CleanTest1.run()
    }

    // test to make sure cleaning a mixing bowl will NOT affect the ingredients
    // not in the mixing bowl

    // test to see if it works on all 5 bowls




    // POUR

    // general functionality
    "Pour test 1" should "make sure it works" in {
        object PourTest1 extends ScalaChef {
            def run(): Unit = {
                TITLE ("Pour test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END


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
                TITLE ("Pour test 2") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                3 ('tacos) END

                4 ('hamburgers) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                PUT ('tacos) INTO SECOND MIXING_BOWL END

                PUT ('hamburgers) INTO SECOND MIXING_BOWL END

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END

                POUR CONTENTS OF THE SECOND MIXING_BOWL INTO THE FIRST BAKING_DISH END


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
                TITLE ("Pour test 3") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END


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
                TITLE ("Pour test 4") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                PUT ('cakes) INTO FIRST MIXING_BOWL END

                POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END

                PUT ('potatoes) INTO FIRST MIXING_BOWL END


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
                TITLE ("Refrigerate test 1") END


                START_INGREDIENTS

                1 ('potatoes) END

                2 ('cakes) END

                END_INGREDIENTS


                PUT ('potatoes) INTO FIRST MIXING_BOWL END

                REFRIGERATE END

                PUT ('cakes) INTO FIRST MIXING_BOWL END


                RUN


                assert(mixingStacks(FIRST).peek.asNumber == 1)
            }
        }

        RefrigerateTest1.run()
    }

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
