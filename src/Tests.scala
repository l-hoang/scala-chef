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
                TITLE ("tester") END
        
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

    // test to make sure you can't declare a title twice in a row

    // test to make sure a program must have ingredients after the
    // title declaration

    // test to make sure you can't have 2 START_INGREDIENTS

    // test to make sure you can't have 2 END_INGREDIENTS

    // test to make sure you can't start a program with just ingredients

    // test to make sure you can't start a program with Chef statements


    // test to make sure PUT puts stuff in a stack

    // test to make sure you can put to all 5 mixing bowls


    // test to make sure ADD adds to something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can ADD to all 5 bowls

    // test to make sure ADD fails if nothing is in the specified stack


    // test to make sure REMOVE subtracts from something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can REMOVE from all 5 bowls

    // test to make sure REMOVE fails if nothing is in the specified stack


    // test to make sure COMBINE multiplies to something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can COMBINE to all 5 bowls

    // test to make sure COMBINE fails if nothing is in the specified stack


    // test to make sure add DIVIDE divides something already on a stack and
    // pushes that new value to the stack (while leaving the other one
    // intact)

    // test to make sure you can DIVIDE to all 5 bowls

    // test to make sure DIVIDE fails if nothing is in the specified stack


    // test to make sure SERVES prints nothing if the baking dishes (but
    // not the mixing bowls) are empty

    // test to make sure SERVES actually prints stuff from all 5 dishes
}
