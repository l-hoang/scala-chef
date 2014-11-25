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

import scala.language.implicitConversions
import scala.language.postfixOps
import scala.collection.mutable
import java.util.ArrayDeque
import java.util.ArrayList
import java.util.Collections
import java.util.Scanner

class ScalaChef {
    abstract sealed class ChefLine
    case class Read(ingredient:Symbol) extends ChefLine
    case class PushStack(stack: String, ingredient:Symbol) extends ChefLine
    case class PopStack(stack: String, ingredient:Symbol) extends ChefLine
    case class AddStack(stack: String, ingredient:Symbol) extends ChefLine
    case class SubtractStack(stack: String, ingredient:Symbol) extends ChefLine
    case class MultiplyStack(stack: String, ingredient:Symbol) extends ChefLine
    case class DivideStack(stack: String, ingredient:Symbol) extends ChefLine
    case class AddDry() extends ChefLine
    case class ToUnicode(ingredient:Symbol) extends ChefLine
    case class StackToUnicode(stack: String) extends ChefLine
    case class MixStack(stack: String) extends ChefLine
    case class EmptyStack(stack: String) extends ChefLine
    case class ArrangeStack() extends ChefLine
    case class StackToReturnStack(stack: String, dish: String) extends ChefLine
    case class LoopStart(verb: String) extends ChefLine
    case class LoopEnd(verb: String) extends ChefLine
    case class Break() extends ChefLine
    case class CallFunction() extends ChefLine
    case class Return() extends ChefLine
    case class PrintStacks(num : Int) extends ChefLine

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
    var currentDish = NONE

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


    /* This class stores loop information */
    class LoopInfo(ingredient : Symbol, s : Int){
        var start = s
        var end = -1
        var loopIngredient = ingredient
        var decIngredient: Symbol = null
        
        def setEnd(ingredient: Symbol, e : Int) = {
            end = e
            decIngredient = ingredient
        }
    }
    
    /* This structure stores the program's loop stack*/
    val loopStack = new ArrayDeque[String]
    
    /* This structure stores the program's loop info*/
    val loopBindings = new mutable.HashMap[String, LoopInfo]
    
    /* The current verb*/
    var currentVerb = ""
    
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

    /* This builds loops*/
    case class LoopBuilder(verb:String){
        if(verb.capitalize.last == 'E'){
            currentVerb = (verb + "D").capitalize
        } else {
            currentVerb = (verb + "ED").capitalize
        }
        
        def THE(ingredient:Symbol) = {
            currentIngredient = ingredient
            new LoopFollow
        }
        
        def UNTIL(verbed:String) = {
            currentOpType = O_VERBEND
            new Ender(END)
        }
    }
    
    class LoopFollow(){
        def END() = {
            currentOpType = O_VERB
            ScalaChef.this.END.finish
        }
        
        def UNTIL(verbed: String) = {
            currentVerb = (verbed).capitalize
            currentOpType = O_VERBEND
            new Ender(ScalaChef.this.END)
        }
    }
    
    implicit def string2LoopBuilder(verb: String) = LoopBuilder(verb);
    
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
        def apply(ingredient: Symbol) = {
            currentOpType = O_TAKE
            currentIngredient = ingredient
            new fromRefr;
        }
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
        def CONTENTS(of: Of) = {
            currentOpType = O_LIQUEFY2
            new The
        }
    }    
    abstract sealed class Of{
    }
    object OF extends Of{
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
        def THE(stack: String) = {
            currentOpType = O_MIX
            currentStack = stack
            new mixWell
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
        def CONTENTS(of: Of) = {
            currentOpType = O_POUR
            new PourThe
        }
    }
    class PourThe {
        def THE(stack: String) = {
            currentStack = stack
            new PourBowl
        }
    }
    class PourBowl {
        def MIXING_BOWL(into:Into) = {
            stackType = T_BOWL
            new PourThe2
        }
    }
    object INTO extends Into{
    }
    class PourThe2 {
        def THE(stack: String) = {
            currentDish = stack
            new PourDish
        }
    }
    class PourDish {
        def BAKING_DISH(e: End) = {
            e.finish
        }
    }
   
    /* Start evaluating a line that starts with SERVES (the last line) */
    object SERVES {
        def apply(numberOfDiners: Int) = {
            currentOpType = O_SERVES
            if (numberOfDiners <= 0 || numberOfDiners > 5) {
                throw new RuntimeException("You can only serve 5 people and " +
                                            "you need to serve at least 1")
            }   
            intArg = (numberOfDiners - 1)
            new Ender(END)
        }

    }

    /* This class reads the keyword FROM in a line with REFRIGERATOR */
    class fromRefr {
        def FROM(r:refrigerator) = {
            new Ender(END)
        }
    }
    
    /* Object to read keyword REFRIGERATOR*/ 
    abstract sealed class refrigerator {
    }
    object REFRIGERATOR extends refrigerator{
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
    
    /* Classes to read keywords MIXING_BOWL WELL*/
    class mixWell{
        def MIXING_BOWL(w:well) = {
            new Ender(END);
        }
    }
    abstract sealed class well {
    }
    object WELL extends well{
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
                    case O_TAKE => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = Read(currentIngredient)
                    }
                    case O_PUT => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = PushStack(currentStack,currentIngredient)
                    }
                    case O_FOLD => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = PopStack(currentStack,currentIngredient)
                    }
                    case O_ADD => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = AddStack(currentStack,currentIngredient)
                    }
                    case O_COMBINE => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = MultiplyStack(currentStack,currentIngredient)
                    }
                    case O_DIVIDE => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = DivideStack(currentStack,currentIngredient)
                    }
                    case O_LIQUEFY => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = ToUnicode(currentIngredient)
                    }
                    case O_LIQUEFY2 => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = StackToUnicode(currentStack)
                    }
                    case O_MIX => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = MixStack(currentStack)
                    }
                    case O_POUR => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = StackToReturnStack(currentStack,currentDish)
                    }
                    case O_CLEAN => { 
                        /* pass necessary values to the current line */
                        lines(currentLine) = EmptyStack(currentStack)
                    }
                    case O_SERVES => {
                        /* pass necessary values to the current line */
                        if(!loopStack.isEmpty()){
                            throw new RuntimeException("Malformed Loop")
                        }
                        lines(currentLine) = PrintStacks(intArg)
                    }
                    case O_VERB => {
                        /* create a LoopInfo object*/
                        if(loopBindings.contains(currentVerb)){
                            throw new RuntimeException("Loop "+currentVerb +" already defined")
                        }
                        loopStack.push(currentVerb)
                        loopBindings(currentVerb) = new LoopInfo(currentIngredient,currentLine)
                        lines(currentLine) = LoopStart(currentVerb)
                    }
                    case O_VERBEND => {
                        /* edit the LoopInfo object*/
                        if(loopStack.isEmpty() || loopStack.pop() != currentVerb){
                            throw new RuntimeException("Malformed Loop")
                        }
                        
                        loopBindings(currentVerb).setEnd(currentIngredient,currentLine)
                        lines(currentLine) = LoopEnd(currentVerb)
                    }
                    case _ => {
                        throw new RuntimeException("currentOpType invalid")
                    }
                }


                /* get ready to read next line */
                currentOpType = O_NOTHING
                currentIngredient = null
                currentStack = NONE
                currentDish = NONE
                currentVerb = null
                stackType = T_NOTHING
                intArg = -1
                currentLine += 1
            } else {
                /* no right mode specified; die */
                throw new RuntimeException("incorrect mode")
            }
        }
    }
    
    
    //evaluator: might need a map to hold loop frames
    def evaluate(line : Int){
        lines(line) match{
            case Read(ingredient: Symbol) => {
                val in = new Scanner(System.in)
                val ingredientToAdd = new Ingredient(in.nextInt(), I_EITHER)
                variableBindings(ingredient) = ingredientToAdd
                in.close()
                evaluate(line+1)
            }
            case PushStack(stack: String , ingredient: Symbol) => {
                mixingStacks(stack).push(variableBindings(ingredient))
                evaluate(line+1)
            }
            case PopStack(stack:String, ingredient:Symbol) => {
                variableBindings(ingredient) = mixingStacks(stack).pop()
                evaluate(line+1)
            }
            case AddStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber + 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case SubtractStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((mixingStacks(stack).peek.asNumber - 
                                                    variableBindings(ingredient).asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case MultiplyStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber * 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case DivideStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber / 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            /*case AddDry() => {
                evaluate(line+1)
            }*/
            case ToUnicode(ingredient: Symbol) => {
                variableBindings(ingredient).changeInterpretation(I_LIQUID)
                evaluate(line+1)
            }
            case StackToUnicode(stack : String) => {
                val it = mixingStacks(stack).iterator()
                while(it.hasNext()){
                    it.next().changeInterpretation(I_LIQUID)
                }
                evaluate(line+1)
            }
            case MixStack(stack : String) => {
                val temp = new ArrayList(mixingStacks(stack))
                Collections.shuffle(temp)
                mixingStacks(stack) = new ArrayDeque(temp)
                evaluate(line+1)
            }
            case EmptyStack(stack : String) => {
                mixingStacks(stack).clear()
                evaluate(line+1)
            }
            /*case ArrangeStack() => {
                evaluate(line+1)
            }*/
            case StackToReturnStack(stack:String, dish:String) => {
                bakingStacks(dish).addAll(mixingStacks(stack))
                evaluate(line+1)
            }
            case PrintStacks(num : Int) => {
                val stackNumbers = Array(FIRST, SECOND, THIRD, FOURTH,
                                                     FIFTH)
                var i = 0
                for (i <- 0 to num) {
                    val stackToUse = mixingStacks(stackNumbers(i))
                    while (stackToUse.size != 0) {
                        val ingredient = stackToUse.pop
                        if (ingredient.state == I_DRY ||
                                ingredient.state == I_EITHER) {
                            printf("%d", ingredient.asNumber)
                        } else if (ingredient.state == I_LIQUID) {
                            printf("%c", ingredient.asChar)
                        }
                    }
                }
            }
            case LoopStart(verb: String) => {
                if(!loopBindings.contains(verb)){
                    throw new RuntimeException("Loop "+verb +" not defined")
                }
                val loop = loopBindings(verb)
                if(variableBindings(loop.loopIngredient).number==0){
                    evaluate(loop.end + 1)
                }
                else{
                    loopStack.push(verb)
                    evaluate(line+1)
                }
            }
            case LoopEnd(verb: String) => {
                if(loopStack.isEmpty() || loopStack.peekLast() != verb){
                    throw new RuntimeException("Malformed Loop")
                }
                
                val loop = loopBindings(verb)
                variableBindings(loop.decIngredient).number -= 1;
                
                if(variableBindings(loop.loopIngredient).number == 0){
                    loopStack.pop()
                    evaluate(line+1)
                }
                else{
                    evaluate(loop.start+1)
                }
            }
        }
    }
    
    def RUN() = evaluate(1)
}
