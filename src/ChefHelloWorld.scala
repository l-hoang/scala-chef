/*Sample chef code that prints "hello world". Change formatting as necessary.*/

object ChefHelloWorld extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Hello World") END



        START_INGREDIENTS
        
        72 G ('haricot_beans) END
        
        101 ('eggs) END
        
        108 G ('lard) END

        111 CUPS ('oil) END

        32 ('zucchinis) END

        119 ML ('water) END

        114 G ('red_salmon) END

        100 G ('dijon_mustard) END

        33 ('potatoes) END

        END_INGREDIENTS



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
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END

        SERVES (1) END
        
        RUN
    }
}
