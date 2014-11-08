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

import scala.collection.mutable

class ScalaChef {
    abstract sealed class ChefLine
    case class PrintStack() extends ChefLine
    case class PushStack(fn: () => mutable.Stack[Ingredient]) extends ChefLine
    case class PopStack() extends ChefLine
    case class Add() extends ChefLine
    case class Subtract() extends ChefLine
    case class Multiply() extends ChefLine
    case class Divide() extends ChefLine
    case class AddDry() extends ChefLine
    case class ToUnicode() extends ChefLine
    case class StackToUnicode() extends ChefLine
    case class MixStack() extends ChefLine
    case class EmptyStack() extends ChefLine
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

        if (interpretation != I_DRY || interpretation != I_LIQUID ||
                interpretation != I_EITHER) {
            throw new RuntimeException("bad ingredient designation")
        }
        var state = interpretation
        
        /* returns this ingredient's value as a number */
        def asNumber(): Int = {
            number       
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

    /* ingredient in use; tracks the ingredient that is being used in some line */  
    var currentIngredient: Symbol = null

    /* the number of the stack we want to use; unlike Chef, this code only has
     * a limited # of stacks (at the moment) */
    var currentStack = "none"
    val NONE = "none"
    val FIRST = "FIRST"
    val SECOND = "SECOND"
    val THIRD = "THIRD"
    val FOURTH = "FOURTH"
    val FIFTH = "FIFTH"
    val SIXTH = "SIXTH"
    val SEVENTH = "SEVENTH"

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
    val mixingStacks = new mutable.HashMap[String, mutable.Stack[Ingredient]]

    /* set up mixingStacks (only 5 set up for now) */
    mixingStacks.put(FIRST, new mutable.Stack)
    mixingStacks.put(SECOND, new mutable.Stack)
    mixingStacks.put(THIRD, new mutable.Stack)
    mixingStacks.put(FOURTH, new mutable.Stack)
    mixingStacks.put(FIFTH, new mutable.Stack)

    /* This structure holds baking dish stacks */
    val bakingStacks = new mutable.HashMap[String, mutable.Stack[Ingredient]]

    /* set up bakingStacks (again only 5 for now) */
    bakingStacks.put(FIRST, new mutable.Stack)
    bakingStacks.put(SECOND, new mutable.Stack)
    bakingStacks.put(THIRD, new mutable.Stack)
    bakingStacks.put(FOURTH, new mutable.Stack)
    bakingStacks.put(FIFTH, new mutable.Stack)



    /*********************************/
    /* Here begins keywords for Chef */
    /*********************************/

    /* Start evaluating a line that starts with PUT */
    object PUT {
        def apply(ingredient: Symbol) = {
            currentOpType = O_PUT
            currentIngredient = ingredient
            new Into
        }
    }

    /* This class reads the keyword INTO in a line */
    class Into {
        def INTO(stack: String) = {
            currentStack = stack
            new BowlOrDish
        }
    }

    /* This class will evaluate MIXING_BOWL or BAKING_DISH in a line.
     * It sets the line stack type, then calls END's finish method */
    class BowlOrDish() {
        def MIXING_BOWL(e: End) = {
            stackType = T_BOWL
            e.finish
        }

        def BAKING_DISH(e: End) = {
            stackType = T_DISH
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
                case _ => {
                    println("currentOpType invalid")
                }

            }

            /* get ready to read next line */
            currentOpType = O_NOTHING
            currentIngredient = null
            currentStack = NONE
            stackType = T_NOTHING
            currentLine += 1
        }
    }
}
