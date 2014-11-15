/* Sample chef code that prints "hello world". Change formatting as necessary.
 *
 */
object ChefHelloWorld extends ScalaChef {
    def main(args: Array[String]): Unit = {
        INGREDIENTS
        72 g haricot_beans
        101 eggs
        108 g lard
        111 cups oil
        32 zucchinis
        119 ml water
        114 g red_salmon
        100 g dijon_mustard
        33 potatoes

        Method
        PUT ('potatoes) INTO FIRST MIXING_BOWL END
        PUT ('dijon_mustard) INTO FIRST MIXING_BOWL END
        PUT ('lard) INTO FIRST MIXING_BOWL END
        PUT ('red_salmon) INTO FIRST MIXING_BOWL END
        PUT ('oil) INTO FIRST MIXING_BOWL END
        PUT ('water) INTO FIRST MIXING_BOWL END
        PUT ('zucchinis) INTO FIRST MIXING_BOWL END
        PUT ('oil) INTO FIRST MIXING_BOWL END
        PUT ('lard) INTO FIRST MIXING_BOWL END
        PUT ('lard) INTO FIRST MIXING_BOWL END
        PUT ('eggs) INTO FIRST MIXING_BOWL END
        PUT ('haricot_beans) INTO FIRST MIXING_BOWL END
        LIQUEFY CONTENTS OF FIRST MIXING_BOWL END
        POUR CONTENTS OF FIRST MIXING_BOWL INTO FIRST BAKING_DISH END

        SERVES 1 END
    }
}