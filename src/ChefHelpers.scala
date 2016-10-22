/* Holds an Ingredient value and how it is to be interpreted */
class Ingredient(value: Int, interpretation: IngredientInterpretation) {
    var number = value
    var state = interpretation
    
    /* change the interpretation of this ingredient */
    def changeInterpretation(newInterpretation: IngredientInterpretation) = {
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

/* Holds function info */
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
