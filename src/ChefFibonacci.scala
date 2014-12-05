/*
 * original implementation of this from http://www.dangermouse.net/esoteric/chef_fib.html,
 * but it was broken, so this is the fixed version of it
 *
 * To change # of fibonacchi #s printed, change the flour count in the main recipe.
 * Be warned that you may need to give the VM more memory....
 */
object ChefFibonacci extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Fibonacci Numbers With Caramel Sauce") END
        

        START_INGREDIENTS
        
        6 G ('flour) END
        
        250 G ('butter) END
        
        44 ('eggs) END

        END_INGREDIENTS


        LIQUEFY ('eggs) END

        "SIFT" THE ('flour) END

        PUT ('flour) INTO FIRST MIXING_BOWL END
        
        SERVE WITH "Caramel Sauce" END
        
        STIR THE FIRST MIXING_BOWL FOR (1) MINUTES END

        FOLD ('butter) INTO FIRST MIXING_BOWL END 

        PUT ('eggs) INTO FIRST MIXING_BOWL END

        "RUB" THE ('flour) UNTIL "SIFTED" END

        FOLD ('butter) INTO FIRST MIXING_BOWL END

        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END
        
        SERVES (1) END
        


        
        TITLE ("Caramel Sauce") END
        

        START_INGREDIENTS
        
        1 CUP ('white_sugar) END
        
        1 CUP ('brown_sugar) END
        
        1 ('vanilla_bean) END

        1 ('dummy) END

        END_INGREDIENTS
        

        FOLD ('white_sugar) INTO FIRST MIXING_BOWL END
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        FOLD ('brown_sugar) INTO FIRST MIXING_BOWL END
        
        CLEAN (FIRST) MIXING_BOWL END
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        REMOVE ('vanilla_bean) FROM FIRST MIXING_BOWL END
        
        FOLD ('white_sugar) INTO FIRST MIXING_BOWL END
        
        // if non zero (i.e. != 1)
        "MELT" THE ('white_sugar) END

        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        REMOVE ('vanilla_bean) FROM FIRST MIXING_BOWL END
        
        FOLD ('white_sugar) INTO FIRST MIXING_BOWL END
       
        
        // != 2
        "CARAMELIZE" THE ('white_sugar) END
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        SERVE WITH "Caramel Sauce" END
        
        FOLD ('brown_sugar) INTO FIRST MIXING_BOWL END 

        // get rid of other thing
        FOLD ('dummy) INTO FIRST MIXING_BOWL END
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        ADD ('vanilla_bean) TO FIRST MIXING_BOWL END 
        
        SERVE WITH "Caramel Sauce" END
        
        ADD ('brown_sugar) TO FIRST MIXING_BOWL END 

        FOLD ('white_sugar) INTO FIRST MIXING_BOWL END

        // get rid of other thing
        FOLD ('dummy) INTO FIRST MIXING_BOWL END

        PUT ('white_sugar) INTO FIRST MIXING_BOWL END

        REFRIGERATE END
        
        "COOK" THE ('white_sugar) UNTIL "CARAMELIZED" END
        

        PUT ('vanilla_bean) INTO FIRST MIXING_BOWL END
        
        REFRIGERATE END
        
        "HEAT" THE ('white_sugar) UNTIL "MELTED" END 


        // if it is 0 do this
        PUT ('vanilla_bean) INTO FIRST MIXING_BOWL END
        
        REFRIGERATE END
   

        
        RUN
    }
}
