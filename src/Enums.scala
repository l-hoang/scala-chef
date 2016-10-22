object Enums {
    // ingredient interpretations
    abstract sealed class IngredientInterpretation
    case object I_DRY extends IngredientInterpretation
    case object I_LIQUID extends IngredientInterpretation
    case object I_EITHER extends IngredientInterpretation
    case object I_NONE extends IngredientInterpretation
    
    // operation types
    abstract sealed class OperationType
    case object O_NOTHING extends OperationType
    case object O_TAKE extends OperationType
    case object O_PUT extends OperationType
    case object O_FOLD extends OperationType
    case object O_ADD extends OperationType
    case object O_REMOVE extends OperationType
    case object O_COMBINE extends OperationType
    case object O_DIVIDE extends OperationType
    case object O_ADDDRY extends OperationType
    case object O_LIQUEFY extends OperationType
    case object O_LIQUEFY2 extends OperationType
    case object O_STIR extends OperationType
    case object O_STIR2 extends OperationType
    case object O_MIX extends OperationType
    case object O_CLEAN extends OperationType
    case object O_POUR extends OperationType
    case object O_VERB extends OperationType
    case object O_VERBEND extends OperationType
    case object O_SET extends OperationType
    case object O_SERVE extends OperationType
    case object O_REFR extends OperationType
    case object O_SERVES extends OperationType
    case object O_TITLE extends OperationType
    
    
    // the stack to use; currently only a limited number of stacks are supported
    // unlike Chef
    abstract sealed class StackNumber
    case object NONE extends StackNumber
    case object FIRST extends StackNumber
    case object SECOND extends StackNumber
    case object THIRD extends StackNumber
    case object FOURTH extends StackNumber
    case object FIFTH extends StackNumber

    // stack type 
    abstract sealed class StackType
    case object T_NOTHING extends StackType
    case object T_BOWL extends StackType
    case object T_DISH extends StackType
}
