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
 * Stir for (number) minutes :  
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
    case class AddDry(stack: String) extends ChefLine
    case class ToUnicode(ingredient:Symbol) extends ChefLine
    case class StackToUnicode(stack: String) extends ChefLine
    case class MixStack(stack: String) extends ChefLine
    case class EmptyStack(stack: String) extends ChefLine
    case class ArrangeStack(stack: String, num: Int) extends ChefLine
    case class ArrangeStack2(stack: String, ingredient: Symbol) extends ChefLine
    case class StackToReturnStack(stack: String, dish: String) extends ChefLine
    case class LoopStart(verb: String) extends ChefLine
    case class LoopEnd(verb: String) extends ChefLine
    case class Break() extends ChefLine
    case class CallFunction(title: String) extends ChefLine
    case class Return(dishes: Int) extends ChefLine
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
    val O_REFR = 19
    val O_SERVES = 20
    val O_TITLE = 21


    /* ingredient in use; tracks the ingredient that is being used in some line */  
    var currentIngredient: Symbol = null

    /* this argument is used to hold integer arguments for a line */
    var intArg: Int = -1

    var stringArg: String = ""

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
    var variableBindings = new mutable.HashMap[Symbol, Ingredient]

    /* This structure holds mixing bowl stacks */
    var mixingStacks = new mutable.HashMap[String, ArrayDeque[Ingredient]]

    /* set up mixingStacks */
    mixingStacks.put(FIRST, new ArrayDeque)
    mixingStacks.put(SECOND, new ArrayDeque)
    mixingStacks.put(THIRD, new ArrayDeque)
    mixingStacks.put(FOURTH, new ArrayDeque)
    mixingStacks.put(FIFTH, new ArrayDeque)

    /* This structure holds baking dish stacks */
    var bakingStacks = new mutable.HashMap[String, ArrayDeque[Ingredient]]

    /* set up bakingStacks */
    bakingStacks.put(FIRST, new ArrayDeque)
    bakingStacks.put(SECOND, new ArrayDeque)
    bakingStacks.put(THIRD, new ArrayDeque)
    bakingStacks.put(FOURTH, new ArrayDeque)
    bakingStacks.put(FIFTH, new ArrayDeque)



    /* holds the name of the main recipe (i.e. the first one) */
    var mainRecipe = ""

    /* name of currently running recipe */
    var currentRecipe = ""

    class FunctionInfo {
        var startLine = -1
        var endLine = -1

        def setStart(s: Int) = {
            startLine = s
        }

        def setEnd(e: Int) = {
            endLine = e
        }
    }

    /* holds the starting vars for some recipe */
    val startingIngredients = new mutable.HashMap[String, 
                                    mutable.HashMap[Symbol, Ingredient]]

    /* maps a function to a class that tells you the line it starts at and the
     * line it ends at (i.e. the start of the next function) */
    val functionStartEnd = new mutable.HashMap[String, FunctionInfo]


    /* What follows are structures needed to keep a "function call stack" */

    /* stores return line */
    val returnLineStack = new ArrayDeque[Int]
    /* stores line to end at */
    val endLineStack = new ArrayDeque[Int]
    /* stack that stores loopStacks */
    val loopStackStack = new ArrayDeque[ArrayDeque[String]]
    /* stack that stores mixing bowls */
    val mixingBowlStack = new ArrayDeque[mutable.HashMap[String, 
                                                         ArrayDeque[Ingredient]]]
    /* stack that stores baking dishes */
    val bakingDishStack = new ArrayDeque[mutable.HashMap[String, 
                                                         ArrayDeque[Ingredient]]]
    /* stack that stores ingredient bindings */
    val ingredientStack = new ArrayDeque[mutable.HashMap[Symbol, Ingredient]]
    /* stack that stores loop binding info */
    val loopBindingsStack = new ArrayDeque[mutable.HashMap[String, LoopInfo]]


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
    var loopStack = new ArrayDeque[String]
    
    /* This structure stores the program's loop info*/
    var loopBindings = new mutable.HashMap[String, LoopInfo]

    /* This structure holds the loop info for a particular recipe */
    val allLoopBindings = new mutable.HashMap[String,
                                    mutable.HashMap[String, LoopInfo]]
    
    /* The current verb */
    var currentVerb = ""
    
    /* mode the program parsing is in; starts by parsing a title */
    val M_TITLE = 0
    val M_INGREDIENT = 1
    val M_PROGRAM =  2
    var currentMode = M_TITLE
    var newRecipe = false

    /* tells you if you can start parsing ingredients */
    var canParseIngredients = 0
    var ingredientType = -1

    /* tells if there has been a SERVES line yet */
    var oneServes = false
    /* tells if RUN has finished running */
    var programFinished = false
    /* tells if run has been called */
    var calledRun = false

    /*********************************/
    /* Here begins keywords for Chef */
    /*********************************/

    /* Dummy Tokens for taking up keywords*/
    object INGREDIENTS;
    object OF;
    object INTO;
    object WELL;
    
    /* Start evaluating a line that starts with TITLE */
    object TITLE {
        def apply(title: String):Ender = {
            if (currentMode == M_INGREDIENT) {
                /* can't declare another recipe before declaring ingredients */
                throw new RuntimeException("can't parse a title in ingredient mode")
            }

            if (currentMode == M_PROGRAM) {
                /* this means you are declaring a new recipe */
                 newRecipe = true
            }

            if (title.equals("")) {
                throw new RuntimeException("can't make a recipe title blank")
            }

            stringArg = title
            currentOpType = O_TITLE
            currentMode = M_TITLE
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

        def PINCH(ingredient: Symbol) = {
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
        
        def DASH(ingredient: Symbol) = {
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
        
        def CUP(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }

        def CUPS(ingredient: Symbol) = {
            currentIngredient = ingredient
            ingredientType = I_EITHER
            intArg = num
            new Ender(END)
        }
        
        def TEASPOON(ingredient: Symbol) = {
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
        
        def TABLESPOON(ingredient: Symbol) = {
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

        /* make sure currentRecipe is set */
        if (currentRecipe.equals("")) {
            throw new RuntimeException("no recipe specified during ingredient parsing")
        }

        /* copies the default ingredients of the current recipe */
        val stuffToGet = variableBindings.iterator
        val bindingCopy = new mutable.HashMap[Symbol, Ingredient]
        while (stuffToGet.hasNext) {
            var pair = stuffToGet.next

            val name = pair._1
            val oldIngredient = pair._2
            /* has to make a copy of the ingredients as well */
            val ingredientCopy = new Ingredient(oldIngredient.number,
                                        oldIngredient.state)

            bindingCopy(name) = ingredientCopy
        }

        /* set it as "default ingredients" for the recipe*/
        startingIngredients(currentRecipe) = bindingCopy
        currentMode = M_PROGRAM;
    }

    /* This builds loops*/
    case class LoopBuilder(verb:String){
        if(verb.toUpperCase().last == 'E'){
            currentVerb = (verb + "D").toUpperCase()
        } else {
            currentVerb = (verb + "ED").toUpperCase()
        }
        
        def THE(ingredient:Symbol) = {
            currentIngredient = ingredient
            new LoopFollow
        }
        
        def UNTIL(verbed:String) = {
            currentVerb = (verbed).toUpperCase()
            currentOpType = O_VERBEND
            currentIngredient = null
            new Ender(END)
        }
    }
    
    class LoopFollow(){
        def END() = {
            currentOpType = O_VERB
            ScalaChef.this.END.finish
        }
        
        def UNTIL(verbed: String) = {
            currentVerb = (verbed).toUpperCase()
            currentOpType = O_VERBEND
            new Ender(ScalaChef.this.END)
        }
    }
    
    implicit def string2LoopBuilder(verb: String) = LoopBuilder(verb);
    
    /* Start evaluating a line that starts with SET*/
    object SET {
        def ASIDE(e:End) = {
            currentOpType = O_SET
            e.finish
        }
    }

    /* Start evaluating a line that starts with TAKE */
    object TAKE {
        def apply(ingredient: Symbol) = {
            currentOpType = O_TAKE
            currentIngredient = ingredient
            new fromRefr;
        }
    }

    /* This class reads the keyword FROM in a line with REFRIGERATOR */
    class fromRefr {
        def FROM(r:refrigerator) = {
            new Ender(END)
        }
    }

    /* Object to read keyword REFRIGERATOR*/ 
    abstract sealed class refrigerator {}
    object REFRIGERATOR extends refrigerator {}

    
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

        def DRY(ingredients: INGREDIENTS.type) {
            currentOpType = O_ADDDRY
            new To
        }
    }

    /* Start evaluating a line that starts with REMOVE */
    object REMOVE {
        def apply(ingredient: Symbol) = {
            currentOpType = O_REMOVE
            currentIngredient = ingredient
            new From
        }
    }

    /* This class exists to read the keyword FROM for REMOVE */
    class From {
        def FROM(stack: String) = {
            currentStack = stack
            new BowlOrDish
        }
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
        def CONTENTS(of: OF.type) = {
            currentOpType = O_LIQUEFY2
            new The
        }
    }    

    /* Start evaluating a line that starts with STIR */
    object STIR {
        /* Stir the nth mixing.... */
        def THE(stack: String):StirBowl = {
            currentOpType = O_STIR
            currentStack = stack
            new StirBowl
        }

        /* Stir ingredient ... */
        def apply(ingredient: Symbol) = {
            currentOpType = O_STIR2
            // MISSING
        }
    }
    class StirBowl {
        def MIXING_BOWL(f: STIR_FOR.type) = {
            stackType = T_BOWL
            new Minutes
        }
    }
    
    class Minutes{
         def MINUTES(e:End) = {
             e.finish
         } 
    }
   
    // dummy return value so that StirBowl requires FOR to take an arg
    object STIR_FOR;
    object FOR {
        def apply(num: Int) = {
            if (num <= 0) {
                throw new RuntimeException("can't STIR non-positive")
            }

            intArg = num
            STIR_FOR
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
        def CONTENTS(of: OF.type) = {
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
        def MIXING_BOWL(into:INTO.type) = {
            stackType = T_BOWL
            new PourThe2
        }
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

    /* start parsing line that starts with SERVE (function call) */
    object SERVE {
        def WITH(recipe: String): Ender = {
            currentOpType = O_SERVE
            if (recipe.equals("")) {
                throw new RuntimeException("can't parse blank recipes")
            }

            if (recipe.equals(mainRecipe)) {
                throw new RuntimeException("can't SERVE main recipe")
            }

            stringArg = recipe
            new Ender(END)
        }
    }
   
    /* Start parsing a line that starts with REFRIGERATE */
    object REFRIGERATE {
        def apply(e:End) = {
            currentOpType = O_REFR
            e.finish
        }
        
        def FOR(num: Int): Hours = {
            currentOpType = O_REFR
            /* must refrigerate for a number 0 through 5 */
            if (num < 0 || num > 5) {
                throw new RuntimeException("REFRIGERATE needs # from 0 - 5")
            }
            intArg = num
            new Hours    
        }
    }
    
    class Hours {
        def HOURS(e:End) = {
            e.finish
        }
    }
    
    /* Start parsing a line that starts with SERVES (the last line) */
    object SERVES {
        def apply(numberOfDiners: Int) = {
            /* note in this implementation SERVES must be in the main recipe */
            if (oneServes) {
                throw new RuntimeException("There can only be 1 SERVES in a " +
                                            "program")
            }

            currentOpType = O_SERVES
            if (numberOfDiners <= 0 || numberOfDiners > 5) {
                throw new RuntimeException("You can only serve 5 people and " +
                                            "you need to serve at least 1")
            }   
            intArg = (numberOfDiners - 1)
            oneServes = true
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
    
    /* Class to read keywords MIXING_BOWL WELL*/
    class mixWell{
        def MIXING_BOWL(w:WELL.type) = {
            new Ender(END);
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
            if (programFinished) {
                throw new RuntimeException("trying to parse after program " +
                                           "has already run")
            }

            /* this mode = program parsing */
            if (currentMode == M_TITLE) {
                if (currentOpType != O_TITLE) {
                    throw new RuntimeException(
                            "doing something besides declaring " +
                            "a title or running a program in " +
                            "title mode")
                }

                /* if it's the first recipe it's the main recipe */
                if (mainRecipe.equals("")) {
                    mainRecipe = stringArg
                    newRecipe = true
                }

                /* make sure the same title isn't being used */
                if (startingIngredients.contains(stringArg) ||
                        functionStartEnd.contains(stringArg)) {
                    throw new RuntimeException("using the same title for a recipe")
                }

                if (newRecipe) {
                    if (!currentRecipe.equals("")) {
                        /* mark end of current recipe as the start of the 
                           new one */
                        functionStartEnd(currentRecipe).setEnd(currentLine)

                        /* clear the ingredient bindings */
                        variableBindings = new mutable.HashMap[Symbol, Ingredient]

                        /* save the loop info of this recipe before switch */
                        allLoopBindings(currentRecipe) = loopBindings
                        /* clear loop info of old recipe by creating new struct */
                        loopBindings = new mutable.HashMap[String, LoopInfo]
                    }

                    /* create an entry for new recipe in the function info table */
                    functionStartEnd(stringArg) = new FunctionInfo
                    functionStartEnd(stringArg).setStart(currentLine)
                    currentRecipe = stringArg
                    newRecipe = false
                }

                stringArg = ""
                
                /* go to ingredient parsing mode */
                currentMode = M_INGREDIENT
            } else if (currentMode == M_INGREDIENT) {
                if (intArg < 0) {
                    throw new RuntimeException("negative ingredient values " +
                                               "not allowed")
                }
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
                    case O_ADDDRY => {
                        /* pass necessary values to the current line */
                        lines(currentLine) = AddDry(currentStack)
                    }
                    case O_REMOVE => {
                        lines(currentLine) = SubtractStack(currentStack,
                                                currentIngredient)
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
                    case O_STIR => {
                        /* pass necessary values to line */
                        lines(currentLine) = ArrangeStack(currentStack, intArg)
                    }
                    case O_STIR2 => {
                        lines(currentLine) = ArrangeStack2(currentStack, 
                                             currentIngredient)
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
                    case O_SERVE => {
                        lines(currentLine) = CallFunction(stringArg)
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
                        if(loopStack.isEmpty() || !loopStack.pop().equals(currentVerb)){
                            throw new RuntimeException("Malformed Loop")
                        }
                        
                        loopBindings(currentVerb).setEnd(currentIngredient,currentLine)
                        lines(currentLine) = LoopEnd(currentVerb)
                    }
                    case O_SET => {
                        lines(currentLine) = Break()
                    }
                    case O_REFR => {
                        lines(currentLine) = Return(intArg)
                    }
                    case _ => {
                        println(currentOpType)
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
                stringArg = ""
                currentLine += 1
            } else {
                /* no right mode specified; die */
                throw new RuntimeException("incorrect mode")
            }
        }
    }
    
    
    //evaluator: might need a map to hold loop frames
    def evaluate(line : Int){
//        println(line)
        /* check to see if this function has finished */
        if (line == endLineStack.peek) {
            /* function done; restore previous state */

            /* get line to jump back to */
            val jumpBack = returnLineStack.pop

            /* pop the endLine off of the stack */
            endLineStack.pop
            /* restore loop stack */
            loopStack = loopStackStack.pop

            /* throw everything in this current function's mixing bowl into
             * the mixing bowl of the function being returned to */
            val mixingBowlToCopy = mixingStacks(FIRST)
            mixingStacks = mixingBowlStack.pop

            val copyIter = mixingBowlToCopy.descendingIterator
            /* push the auxiliary recipe's first bowl to the caller's
             * bowl */
            while (copyIter.hasNext) {
                mixingStacks(FIRST).push(copyIter.next)
            }

            /* restore baking dishes */
            bakingStacks = bakingDishStack.pop

            /* restore var bindings */
            variableBindings = ingredientStack.pop

            /* restore loop bindings */
            loopBindings = loopBindingsStack.pop

            /* jump back */
            evaluate(jumpBack)
        }
        /* end of program */
        if(!lines.contains(line)){
            programFinished = true
            return
        }

        lines(line) match{
            case Read(ingredient: Symbol) => {
                val in = new Scanner(System.in)
                val ingredientToAdd = new Ingredient(in.nextInt(), I_EITHER)
                variableBindings(ingredient) = ingredientToAdd
                in.close()
                evaluate(line+1)
            }
            case PushStack(stack: String , ingredient: Symbol) => {
                /* make a copy of the ingredient */
                val ingredientToCopy = variableBindings(ingredient)
                val ingredientCopy = new Ingredient(ingredientToCopy.number,
                                                    ingredientToCopy.state)
                mixingStacks(stack).push(ingredientCopy)
                evaluate(line+1)
            }
            case PopStack(stack:String, ingredient:Symbol) => {
                /* get the ingredient */
                val poppedIngredient = mixingStacks(stack).pop
                /* it only gets the value of the ingredient */
                variableBindings(ingredient).number = poppedIngredient.number
                evaluate(line+1)
            }
            case AddStack(stack: String, ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber + 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).pop
                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case SubtractStack(stack: String, ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((mixingStacks(stack).peek.asNumber - 
                                                    variableBindings(ingredient).asNumber),
                                                    variableBindings(ingredient).state)
                if (ingredientToPush.number < 0) {
                    throw new RuntimeException("an ingredient can't have a " +
                                               "negative value after REMOVE")
                }
                mixingStacks(stack).pop


                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case MultiplyStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber * 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).pop

                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case DivideStack(stack: String , ingredient: Symbol) => {
                val ingredientToPush = new Ingredient((variableBindings(ingredient).asNumber / 
                                                    mixingStacks(stack).peek.asNumber),
                                                    variableBindings(ingredient).state)
                mixingStacks(stack).pop

                mixingStacks(stack).push(ingredientToPush)
                evaluate(line+1)
            }
            case AddDry(stack: String) => {
                var currentSum = 0
                val stuff =  variableBindings.valuesIterator

                while (stuff.hasNext) {
                    /* if the ingredient is dry, then get its value and add it
                     * to the sum */
                    val ingr = stuff.next
                    if (ingr.state == I_DRY) {
                        currentSum += ingr.number
                    }
                }

                /* create a new ingredient with the value sum */
                val ingredientToPush = new Ingredient(currentSum, I_EITHER)
                /* push on stack */
                mixingStacks(stack).push(ingredientToPush)

                evaluate(line+1)
            }
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
            case ArrangeStack(stack: String, num: Int) => {
                val theStack = mixingStacks(stack)
                val stackSize = theStack.size

                if (stackSize > 1) {
                    if (num >= stackSize) {
                        /* this means the top will go to the bottom */
                        val popped = theStack.pop
                        /* add adds to the end of the stack */
                        theStack.add(popped)
                    } else {
                        val stackElements = theStack.iterator
                        val first = stackElements.next
                        var left = num
                        val newStack = new ArrayDeque[Ingredient]

                        /* calling next num times will work since this
                         * only goes to this section of code when num is 
                         * less than stackSize; in worst case, you will
                         * iterate through the entire thing */
                        while (left > 0) {
                            newStack.add(stackElements.next)
                            left -= 1
                        }

                        /* add the first element in at this point */
                        newStack.add(first)
                        
                        /* add the rest of the elements in the old stack
                         * iterator */
                        while (stackElements.hasNext) {
                            newStack.add(stackElements.next)
                        }
                    }
                } else if (stackSize == 1 || stackSize == 0) {
                    /* no matter what the number is, nothing is going to 
                     * happen; do nothing */
                }

                evaluate(line+1)
            }
            case ArrangeStack2(stack: String, ingredient: Symbol) => {
                /* get the number */
                val num = variableBindings(ingredient).asNumber

                /* proceed exactly as ArrangeStack above */
                val theStack = mixingStacks(stack)
                val stackSize = theStack.size

                if (stackSize > 1) {
                    if (num >= stackSize) {
                        /* this means the top will go to the bottom */
                        val popped = theStack.pop
                        /* add adds to the end of the stack */
                        theStack.add(popped)
                    } else {
                        val stackElements = theStack.iterator
                        val first = stackElements.next
                        var left = num
                        val newStack = new ArrayDeque[Ingredient]

                        /* calling next num times will work since this
                         * only goes to this section of code when num is 
                         * less than stackSize; in worst case, you will
                         * iterate through the entire thing */
                        while (left > 0) {
                            newStack.add(stackElements.next)
                            left -= 1
                        }

                        /* add the first element in at this point */
                        newStack.add(first)
                        
                        /* add the rest of the elements in the old stack
                         * iterator */
                        while (stackElements.hasNext) {
                            newStack.add(stackElements.next)
                        }
                    }
                } else if (stackSize == 1 || stackSize == 0) {
                    /* no matter what the number is, nothing is going to 
                     * happen; do nothing */
                }

                evaluate(line+1)
            }
            case CallFunction(title: String) => {
                if (!functionStartEnd.contains(title)) {
                    throw new RuntimeException("calling non-existent recipe")
                }
                val functionInfo = functionStartEnd(title)
                if (functionInfo.startLine == -1 || functionInfo.endLine == -1) {
                    throw new RuntimeException("recipe trying to serve not " +
                                               "fully parsed")
                }

                if (!startingIngredients.contains(title)) {
                    throw new RuntimeException("recipe has no default ingre.")
                }                  

                if (!allLoopBindings.contains(title)) {
                    println(title)
                    throw new RuntimeException("recipe has no entry in loop bindings")
                }

                /* push stuff to data structures for function return later */
                returnLineStack.push(line + 1)
                endLineStack.push(functionInfo.endLine)
                loopStackStack.push(loopStack)
                mixingBowlStack.push(mixingStacks)
                bakingDishStack.push(bakingStacks)
                ingredientStack.push(variableBindings)
                loopBindingsStack.push(loopBindings)

                /* make a copy of all the ingredients in all of the mixing
                 * and baking stacks for the function to use */
                val stackNumbers = Array(FIRST, SECOND, THIRD, FOURTH,
                                                     FIFTH)
                var i = 0
                val mixingStacksCopy = new mutable.HashMap[String,
                                        ArrayDeque[Ingredient]]
                /* copy mixing stacks */
                for (i <- 0 to 4) {
                    val theStack = mixingStacks(stackNumbers(i))
                    val stackIter = theStack.descendingIterator

                    val stackCopy = new ArrayDeque[Ingredient]
                    while (stackIter.hasNext) {
                        val ingredient = stackIter.next
                        stackCopy.push(new Ingredient(ingredient.number,
                                            ingredient.state))
                    }
                    mixingStacksCopy.put(stackNumbers(i), stackCopy)
                }
                mixingStacks = mixingStacksCopy

                /* copy baking stacks */
                i = 0
                val bakingStacksCopy = new mutable.HashMap[String,
                                        ArrayDeque[Ingredient]]
                for (i <- 0 to 4) {
                    val theStack = bakingStacks(stackNumbers(i))
                    val stackIter = theStack.descendingIterator

                    val stackCopy = new ArrayDeque[Ingredient]
                    while (stackIter.hasNext) {
                        val ingredient = stackIter.next
                        stackCopy.push(new Ingredient(ingredient.number,
                                            ingredient.state))
                    }
                    bakingStacksCopy.put(stackNumbers(i), stackCopy)
                }
                bakingStacks = bakingStacksCopy


                /* load the default var bindings for the function being
                 * called */
                val defaultIngredients = startingIngredients(title)
                val functionBindings = new mutable.HashMap[Symbol, Ingredient]

                val keyIter = defaultIngredients.keysIterator

                /* make a copy of the default ingredients and assign it to the
                 * bindings the function will use */
                while (keyIter.hasNext) {
                    val key = keyIter.next
                    val defaultIngredient = defaultIngredients(key)
                    val ingredientCopy = new Ingredient(
                                           defaultIngredient.number,
                                           defaultIngredient.state)
                    functionBindings.put(key, ingredientCopy)
                }

                variableBindings = functionBindings

                /* create a new loop stack for the function */
                loopStack = new ArrayDeque[String]

                /* load this function's loop bindings */
                loopBindings = allLoopBindings(title)

                /* jump to the line that the function starts at */
                evaluate(functionInfo.startLine)
            }
            case StackToReturnStack(stack:String, dish:String) => {
                /* get an iterator that starts at the bottom of the stack */
                val it = mixingStacks(stack).descendingIterator()

                while(it.hasNext()){
                    val ingredient = it.next()

                    /* push an ingredient copy onto the baking dish */
                    bakingStacks(dish).push(new Ingredient(ingredient.number,
                                            ingredient.state))
                }
                println("made it here")
                evaluate(line+1)
            }
            case PrintStacks(num : Int) => {
                val stackNumbers = Array(FIRST, SECOND, THIRD, FOURTH,
                                                     FIFTH)
                var i = 0
                for (i <- 0 to num) {
                    val stackToUse = bakingStacks(stackNumbers(i))

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

                /* program is finished after a SERVES call */
                programFinished = true
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
                if(loopStack.isEmpty() || !loopStack.peek().equals(verb) ){
                    throw new RuntimeException("Malformed Loop")
                }
                
                val loop = loopBindings(verb)
                
                if(loop.decIngredient != null)
                    variableBindings(loop.decIngredient).number -= 1;
                
                if(variableBindings(loop.loopIngredient).number == 0){
                    loopStack.pop()
                    evaluate(line+1)
                }
                else{
                    evaluate(loop.start+1)
                }
            }
            case Return(dishes: Int) => {
                if (dishes > 0) {
                    /* print however many baking dishes specified */
                    val stackNumbers = Array(FIRST, SECOND, THIRD, FOURTH,
                                                     FIFTH)
                    var i = 0
                    for (i <- 0 to (dishes - 1)) {
                        val stackToUse = bakingStacks(stackNumbers(i))
    
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
                if (endLineStack.size == 0) {
                    /* no function to return from; this is the main recipe */
                    programFinished = true
                    return
                } else {
                    /* function done; restore previous state */

                    /* get line to jump back to */
                    val jumpBack = returnLineStack.pop

                    /* pop the endLine off of the stack */
                    endLineStack.pop
                    /* restore loop stack */
                    loopStack = loopStackStack.pop

                    /* throw everything in this current function's mixing bowl into
                     * the mixing bowl of the function being returned to */
                    val mixingBowlToCopy = mixingStacks(FIRST)
                    mixingStacks = mixingBowlStack.pop

                    val copyIter = mixingBowlToCopy.descendingIterator
                    /* push the auxiliary recipe's first bowl to the caller's
                     * bowl */
                    while (copyIter.hasNext) {
                        mixingStacks(FIRST).push(copyIter.next)
                    }

                    /* restore baking dishes */
                    bakingStacks = bakingDishStack.pop

                    /* restore var bindings */
                    variableBindings = ingredientStack.pop

                    /* restore loop bindings */
                    loopBindings = loopBindingsStack.pop

                    /* jump back */
                    evaluate(jumpBack)
                }
            }
            case Break() => {
                if(loopStack.isEmpty()){
                    throw new RuntimeException("Not in Loop")
                }
                
                val verb = loopStack.pop()
                val loop = loopBindings(verb)
                evaluate(loop.end + 1)
            }
        }
    }
    
    def RUN() = {
        if (!allLoopBindings.contains(currentRecipe)) {
            /* save the loop info of this recipe since it hasn't been saved yet
             * (they get saved during a title switch) */
            allLoopBindings(currentRecipe) = loopBindings
        }

        if (calledRun) {
            throw new RuntimeException("can't call RUN twice")
        }

        calledRun = true

        if (currentMode == M_INGREDIENT) {
            throw new RuntimeException("can't call RUN during ingredient " +
                                        "parsing")
        }

        if (mainRecipe.equals("")) {
            throw new RuntimeException("no main recipe even declared yet")
        }
        
        functionStartEnd(currentRecipe).setEnd(currentLine)
            
        /* load the main recipe's var bindings */
        variableBindings = startingIngredients(mainRecipe)

        /* load main recipe's loop bindings */
        if (!currentRecipe.equals(mainRecipe)) {
            // check needed as they'll only be in the table if another recipe was
            // declared
            loopBindings = allLoopBindings(mainRecipe)
        }

        evaluate(1)
    }
}
