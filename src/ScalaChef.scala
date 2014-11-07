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

class ScalaChef{
    abstract sealed class ChefLine
    case class PrintStack() extends ChefLine
    case class PushStack() extends ChefLine
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
}
