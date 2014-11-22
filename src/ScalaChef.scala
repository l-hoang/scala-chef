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


/* Refer to the Baysick implementation,
 * LOLCODE and Shakessembly impelementations from last year's class
 * for details on how to do this */

/* Chef syntax stuff (simplified):
 * 
 * Mixing bowl = stack
 * Baking dish = return stack
 * Liquid = interpret as character
 * Dry = interpret as #
 *
 * Take (ingredient) : assign STDIN into ingredient
 *
 * Put : puts an ingredient value into a stack
 * 
 * Fold : essentially a pop into an ingredient
 *
 * It's unclear if these ops push a new value onto the stack or just modify the
 * value on the stack in place (need to check)
 *
 * Add : take a value and add it to the value on top of the stack
 * Remove : subtract
 * Combine : multiply
 * Divide : divide
 *
 * Add dry ingredients : add all dry ingredients, push result
 *
 * Liquefy : make an var interpreted as unicode
 * Liquefy contents : entire stack interpreted as unicode
 *
 * Stir for (number) minutes :  ???
 * Stir ingredient : ???
 *
 * Mix : randomize a stack
 *
 * Clean : remove all elements from a stack
 *
 * Pour : move a stack into a return stack
 *
 * Verb (ingredient) : start loop; as long as ingredient is non-zero, loop
 * Verb [ingredient] until verbed : end loop; verbed must match verb in
 * beginning of loop; specify ingredient = gets decremeneted at end of every
 * loop
 *
 * Set aside : break from loop
 *
 * Serve with recipe : function call
 *
 * Refrigerate : return statement (can specify how many baking dishes to return)
 *
 *
 *
 *
 */

import scala.language.postfixOps
import scala.collection.mutable
import java.util.ArrayDeque

class ScalaChef {
    abstract sealed class ChefLine
    case class PrintStack() extends ChefLine
    case class PushStack(fn: () => Unit) extends ChefLine
    case class PopStack(fn: () => Unit) extends ChefLine
    case class AddStack(fn: () => Unit) extends ChefLine
    case class SubtractStack() extends ChefLine
    case class MultiplyStack(fn: () => Unit) extends ChefLine
    case class DivideStack(fn: () => Unit) extends ChefLine
    case class AddDry() extends ChefLine
    case class ToUnicode(fn: () => Unit) extends ChefLine
    case class StackToUnicode() extends ChefLine
    case class MixStack() extends ChefLine
    case class EmptyStack(fn: () => Unit) extends ChefLine
    case class ArrangeStack() extends ChefLine
    case class StackToReturnStack() extends ChefLine
    /* missing loop case classes; do we need them? */
    case class Break() extends ChefLine
    case class CallFunction() extends ChefLine
    case class Return() extends ChefLine

    /* ways to intrepret an ingredient */
    val I_DRY = 0
    val I_LIQUID = 1
    val I_EITHER = 2
    /* Holds an Ingredient value and how it is to be interpreted */
    class Ingredient(value: Int, interpretation: Int) {
        var number = value

        if (interpretation != I_DRY && interpretation != I_LIQUID &&
                interpretation != I_EITHER) {
            throw new RuntimeException("bad ingredient designation")
        }
        var state = interpretation
        
        /* change the interpretation of this ingredient */
        def changeInterpretation(newInterpretation: Int) = {
            if (newInterpretation != I_DRY && newInterpretation != I_LIQUID &&
                    newInterpretation != I_EITHER) {
                throw new RuntimeException("bad ingredient designation in change" +
                                           "interpretation")
            }
            state = newInterpretation
        }

       
        /* returns this ingredient's value as a number */
        def asNumber(): Int = {
            number       
        }

        /* returns this ingredient's value as a Unicode char */
        def asChar(): Char = {
            number.toChar
        }
    }



    /* operation "enum" types */
    var currentOpType = -1
    val O_NOTHING = -1
    val O_TAKE = 0
    val O_PUT = 1
    val O_FOLD = 2
    val O_ADD = 3
    val O_REMOVE = 4
    val O_COMBINE = 5
    val O_DIVIDE = 6
    val O_ADDDRY = 7
    val O_LIQUEFY = 8
    val O_LIQUEFY2 = 9
    val O_STIR = 10
    val O_STIR2 = 11
    val O_MIX = 12
    val O_CLEAN = 13
    val O_POUR = 14
    val O_VERB = 15
    val O_VERBEND = 16
    val O_SET = 17
    val O_SERVE = 18
    val O_REFRIDGE = 19
    val O_SERVES = 20


    /* ingredient in use; tracks the ingredient that is being used in some line */  
    var currentIngredient: Symbol = null

    /* this argument is used to hold integer arguments for a line */
    var intArg: Int = -1

    /* the number of the stack we want to use; unlike Chef, this code only has
     * a limited # of stacks (at the moment) */
    val NONE = "none"
    val FIRST = "FIRST"
    val SECOND = "SECOND"
    val THIRD = "THIRD"
    val FOURTH = "FOURTH"
    val FIFTH = "FIFTH"
    var currentStack = NONE

    /* stack type we are referring to: mixing bowl or baking dish */
    var stackType = -1
    val T_NOTHING = -1
    val T_BOWL = 0
    val T_DISH = 1

    var currentLine = 1
    /* This structure holds the program lines */
    val lines = new mutable.HashMap[Int, ChefLine]
    /* This structure holds ingredient bindings */
    val variableBindings = new mutable.HashMap[Symbol, Ingredient]

    /* This structure holds mixing bowl stacks */
    val mixingStacks = new mutable.HashMap[String, ArrayDeque[Ingredient]]

    /* set up mixingStacks (only 5 set up for now) */
    mixingStacks.put(FIRST, new ArrayDeque)
    mixingStacks.put(SECOND, new ArrayDeque)
    mixingStacks.put(THIRD, new ArrayDeque)
    mixingStacks.put(FOURTH, new ArrayDeque)
    mixingStacks.put(FIFTH, new ArrayDeque)

    /* This structure holds baking dish stacks */
    val bakingStacks = new mutable.HashMap[String, ArrayDeque[Ingredient]]

    /* set up bakingStacks (again only 5 for now) */
    bakingStacks.put(FIRST, new ArrayDeque)
    bakingStacks.put(SECOND, new ArrayDeque)
    bakingStacks.put(THIRD, new ArrayDeque)
    bakingStacks.put(FOURTH, new ArrayDeque)
    bakingStacks.put(FIFTH, new ArrayDeque)


    /* mode the program parsing is in; starts by parsing a title */
    val M_TITLE = 0
    val M_INGREDIENT = 1
    val M_PROGRAM =  2
    var currentMode = M_TITLE

    /* tells you if you can start parsing ingredients */
    var canParseIngredients = 0
    var ingredientType = -1

    /*********************************/
    /* Here begins keywords for Chef */
    /*********************************/

    /* Start evaluating a line that starts with TITLE */
    object TITLE {
        def apply(title: String):Ender = {
            println(title)
            new Ender(END)
        }
    }

    /* Start ingredient parsing */
    def START_INGREDIENTS {
        if (currentMode != M_INGREDIENT) {
            throw new RuntimeException("not in ingredient mode")
        }
        if (canParseIngredients == 1) {
            throw new RuntimeException("you've already called Start_Ingre once")
        }

        canParseIngredients = 1
    }

    case class IngredientGetter(num: Int) {
        if (canParseIngredients == 0) {
            throw new RuntimeException("you can't parse ingredients now")
        }

        def G(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_DRY
            intArg = num
            new Ender(END)
        }

        def KG(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_DRY
            intArg = num
            new Ender(END)
        }

        def PINCHES(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_DRY
            intArg = num
            new Ender(END)
        }

        def ML(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_LIQUID
            intArg = num
            new Ender(END)
        }

        def L(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_LIQUID
            intArg = num
            new Ender(END)
        }

        def DASHES(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_LIQUID
            intArg = num
            new Ender(END)
        }

        def CUPS(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }

        def TEASPOONS(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }

        def TABLESPOONS(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }

        /* this method is if they don't specify a measure */
        def apply(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }

    }


    implicit def int2IngredientGetter(i: Int) = IngredientGetter(i)

    /* This def serves to end ingredient mode */
    def END_INGREDIENTS {
        if (currentMode != M_INGREDIENT) {
            throw new RuntimeException("not in ingredient mode")
        }
        if (canParseIngredients == 0) {
            throw new RuntimeException("START_INGREDIENT not called")
        }

        canParseIngredients = 0;
        currentMode = M_PROGRAM;
    }

    /* Start evaluating a line that starts with TAKE */
    object TAKE {
        // MISSING
    }

    /* Start evaluating a line that starts with PUT */
    object PUT {
        def apply(ingredient: Symbol) = {
            currentOpType = O_PUT
            currentIngredient = ingredient
            new Into
        }
    }
    
    /* Start evaluating a line that starts with FOLD */
    object FOLD {
        def apply(ingredient: Symbol) = {
            currentOpType = O_FOLD
            currentIngredient = ingredient
            new Into
        }
    }

    /* Start evaluating a line that starts with ADD */
    object ADD {
        def apply(ingredient: Symbol) = {
            currentOpType = O_ADD
            currentIngredient = ingredient
            new To
        }

        def DRY(ingredients: String) {
            // MISSING
            currentOpType = O_ADDDRY
            new To
        }
    }

    /* Start evaluating a line that starts with REMOVE */
    object REMOVE {
        // MISSING
    }
    
    /* Start evaluating a line that starts with COMBINE */
    object COMBINE {
        def apply(ingredient: Symbol) = {
            currentOpType = O_COMBINE
            currentIngredient = ingredient
            new Into
        }
    }
    
    /* Start evaluating a line that starts with DIVIDE */
    object DIVIDE {
        def apply(ingredient: Symbol) = {
            currentOpType = O_DIVIDE
            currentIngredient = ingredient
            new Into
        }
    }
    

    /* Start evaluating a line that starts with LIQUEFY */
    object LIQUEFY {
        def apply(ingredient: Symbol):Ender = {
            currentOpType = O_LIQUEFY
            currentIngredient = ingredient
            new Ender(END)
        }
        def CONTENTS(of: String) {
            // MISSING
            currentOpType = O_LIQUEFY2
            new The
        }
    }

    /* Start evaluating a line that starts with STIR */
    object STIR {
        /* Stir the nth mixing.... */
        def THE(stack: String) {
            // MISSING
        }

        /* Stir ingredient ... */
        def apply(ingredient: Symbol) = {
            // MISSING
        }

    }

    /* Start evaluating a line that starts with MIX */
    object MIX {
        def THE(stack: String) {
            // MISSING
        }
    }

    /* Start evaluating a line that starts with CLEAN */
    object CLEAN {
        def apply(stack: String) = {
            currentOpType = O_CLEAN
            currentStack = stack
            new BowlOrDish
        }
    }

    /* Start evaluating a line that starts with POUR */
    object POUR {
        // MISSING

    }
   
    /* Start evaluating a line that starts with SERVES (the last line) */
    object SERVES {
        def apply(numberOfDiners: Int) = {
            currentOpType = O_SERVES
            if (numberOfDiners <= 0 || numberOfDiners > 5) {
                throw new RuntimeException("You can only serve 5 people and " +
                                            "you need to serve at least 1")
            }   
            intArg = numberOfDiners
            new Ender(END)
        }

    }




    /* This class reads the keyword INTO in a line */
    class Into {
        def INTO(stack: String) = {
            currentStack = stack
            new BowlOrDish
        }
    }
     
    /* This class reads the keyword TO in a line */
    class To {
        def TO(stack: String) = {
            currentStack = stack
            new BowlOrDish
        }
    }

    /* This class reads the keyword THE in a line */
    class The {
        def THE(stack: String) = {
            currentStack = stack
            new BowlOrDish
        }
    }

    /* This class will evaluate MIXING_BOWL or BAKING_DISH in a line.
     * It sets the line stack type, then calls END's finish method */
    class BowlOrDish {
        def MIXING_BOWL(e: End) = {
            stackType = T_BOWL
            e.finish
        }

        def BAKING_DISH(e: End) = {
            if (currentOpType != O_POUR) {
                throw new RuntimeException("You can only do POUR on a baking dish")
            }
            stackType = T_DISH
            e.finish
        }
    }

    /* This class is created when the next keyword to parse is END; it absorbs
     * the END keyword and calls the object END finish method to finish the
     * line eval. */
    class Ender(e: End) {
        def END = {
            e.finish
        }
    }


    /* This class's purpose is solely to let the object END extend it so that
     * END can be used as an argument/pass type checking. */
    abstract sealed class End {
        def finish
    }

    /* The keyword END must be at the end of every line. The finish method of
     * this object will finish reading the line and add it to the list of
     * lines for evaluation later 
     * 
     * It extends the abstract class End so that other keywords can take END
     * as an "argument". */
    object END extends End {
        def finish = {
            /* this mode = program parsing */
            if (currentMode == M_TITLE) {
                /* go to ingredient parsing mode */
                println("here")
                currentMode = M_INGREDIENT
            } else if (currentMode == M_INGREDIENT) {
                /* save an ingredient var into the bindings */
                val ingredientToAdd = new Ingredient(intArg, ingredientType)
                variableBindings(currentIngredient) = ingredientToAdd

                currentIngredient = null
                ingredientType = -1
                intArg = -1
            } else if (currentMode == M_PROGRAM) {
                /* do different things depending on what the operation of the line
                 * is */
                currentOpType match {
                    case O_PUT => {
                        val ingredient = variableBindings(currentIngredient)

                        /* should make getter/setters later */
                        val ingredientValue = ingredient.number
                        val ingredientState = ingredient.state
                        
                        /* make a copy of the ingredient since you don't want any
                         * changes to the original ingredient to affect what is on
                         * the stack */
                        val ingredientToPush = new Ingredient(ingredientValue,
                                                              ingredientState)
                        
                        /* make a function that will push the ingredient copy onto
                         * the stack */
                        val fn = {() => {
                                   mixingStacks(currentStack).push(ingredientToPush)
                                 }}
                        /* assign this function to the current line */
                        lines(currentLine) = PushStack(fn)
                    }
                    case O_FOLD => {
                        /* make a function that will peek from the stack and
                         * write the result to ingredient */
                        val fn = {() => {
                                   variableBindings(currentIngredient) = mixingStacks(currentStack).pop()
                                 }}
                        
                        /* assign this function to the current line */
                        lines(currentLine) = PopStack(fn)
                    }
                    case O_ADD => {
                        val ingredient = variableBindings(currentIngredient)

                        /* should make getter/setters later */
                        val ingredientValue = ingredient.number 
                        val ingredientState = ingredient.state
                        
                        /* make a function that will push the new ingredient onto
                         * the stack */
                        val fn = {() => {
                                   /* make a copy of the ingredient since you don't want any
                                    * changes to the original ingredient to affect what is on
                                    * the stack */
                                   val ingredientToPush = new Ingredient((ingredientValue + 
                                                    mixingStacks(currentStack).peek.asNumber),
                                                    ingredientState)

                                   mixingStacks(currentStack).push(ingredientToPush)
                                 }}
                        
                        /* assign this function to the current line */
                        lines(currentLine) = AddStack(fn)
                    }
                    case O_COMBINE => {
                        val ingredient = variableBindings(currentIngredient)

                        /* should make getter/setters later */
                        val ingredientValue = ingredient.number 
                        val ingredientState = ingredient.state
                        
                        
                        /* make a function that will push the new ingredient onto
                         * the stack */
                        val fn = {() => {
                                  /* make a copy of the ingredient since you don't want any
                                   * changes to the original ingredient to affect what is on
                                   * the stack */
                                  val ingredientToPush = new Ingredient(ingredientValue
                                                           * mixingStacks(currentStack).peek.asNumber,
                                                           ingredientState)

                                   mixingStacks(currentStack).push(ingredientToPush)
                                 }}
                        
                        /* assign this function to the current line */
                        lines(currentLine) = MultiplyStack(fn)
                    }
                    case O_DIVIDE => {
                        val ingredient = variableBindings(currentIngredient)

                        /* should make getter/setters later */
                        val ingredientValue = ingredient.number 
                        val ingredientState = ingredient.state
                        
                        
                        /* make a function that will push the new ingredient onto
                         * the stack */
                        val fn = {() => {
                                   /* make a copy of the ingredient since you don't want any
                                    * changes to the original ingredient to affect what is on
                                    * the stack */
                                   val ingredientToPush = new Ingredient(ingredientValue
                                                            / mixingStacks(currentStack).peek.asNumber,
                                                            ingredientState)

                                   mixingStacks(currentStack).push(ingredientToPush)
                                 }}
                        
                        /* assign this function to the current line */
                        lines(currentLine) = DivideStack(fn)
                    }
                    case O_LIQUEFY => {
                        val ingredient = variableBindings(currentIngredient)

                        val fn = {() => {
                                    ingredient.changeInterpretation(I_LIQUID)
                                 }}

                        /* assign function to current line */
                        lines(currentLine) = ToUnicode(fn)

                    }
                    case O_CLEAN => { 
                        val fn = {() => {
                                    mixingStacks(currentStack).clear()
                                 }}
                        /* assign function to current line */
                        lines(currentLine) = EmptyStack(fn)
                    }
                    case O_SERVES => {
                        val fn = {() => {
                            val stackNumbers = Array(FIRST, SECOND, THIRD, FOURTH,
                                                     FIFTH)
                            // MISSING
                        }}

                    }
                    case _ => {
                        println("currentOpType invalid")
                    }
                }


                /* get ready to read next line */
                currentOpType = O_NOTHING
                currentIngredient = null
                currentStack = NONE
                stackType = T_NOTHING
                intArg = -1
                currentLine += 1
            } else {
                /* no right mode specified; die */
                throw new RuntimeException("incorrect mode")
            }
        }
    }
}
