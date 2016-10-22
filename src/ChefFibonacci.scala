/*
 * original implementation of this from http://www.dangermouse.net/esoteric/chef_fib.html,
 * but it was broken, so this is the fixed version of it
 *
 * To change # of fibonacchi #s printed, change the flour count in the main recipe.
 * Be warned that you may need to give the VM more memory....
 */
object ChefFibonacci extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Fibonacci Numbers With Caramel Sauce") 
        

        START_INGREDIENTS
        
        6 G ('flour) 
        
        250 G ('butter) 
        
        44 ('eggs) 

        END_INGREDIENTS


        LIQUEFY ('eggs) 

        "SIFT" THE ('flour) NOW

        PUT ('flour) INTO FIRST MIXING_BOWL 
        
        SERVE WITH "Caramel Sauce" 
        
        STIR THE FIRST MIXING_BOWL FOR (1) MINUTES 

        FOLD ('butter) INTO FIRST MIXING_BOWL 

        PUT ('eggs) INTO FIRST MIXING_BOWL 

        "RUB" THE ('flour) UNTIL "SIFTED" 

        FOLD ('butter) INTO FIRST MIXING_BOWL 

        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 
        
        SERVES (1) 
        


        
        TITLE ("Caramel Sauce") 
        

        START_INGREDIENTS
        
        1 CUP ('white_sugar) 
        
        1 CUP ('brown_sugar) 
        
        1 ('vanilla_bean) 

        1 ('dummy) 

        END_INGREDIENTS
        

        FOLD ('white_sugar) INTO FIRST MIXING_BOWL 
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        FOLD ('brown_sugar) INTO FIRST MIXING_BOWL 
        
        CLEAN FIRST MIXING_BOWL 
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        REMOVE ('vanilla_bean) FROM FIRST MIXING_BOWL 
        
        FOLD ('white_sugar) INTO FIRST MIXING_BOWL 
        
        // if non zero (i.e. != 1)
        "MELT" THE ('white_sugar) NOW

        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        REMOVE ('vanilla_bean) FROM FIRST MIXING_BOWL 
        
        FOLD ('white_sugar) INTO FIRST MIXING_BOWL 
       
        
        // != 2
        "CARAMELIZE" THE ('white_sugar) NOW
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        SERVE WITH "Caramel Sauce" 
        
        FOLD ('brown_sugar) INTO FIRST MIXING_BOWL 

        // get rid of other thing
        FOLD ('dummy) INTO FIRST MIXING_BOWL 
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        ADD ('vanilla_bean) TO FIRST MIXING_BOWL 
        
        SERVE WITH "Caramel Sauce" 
        
        ADD ('brown_sugar) TO FIRST MIXING_BOWL 

        FOLD ('white_sugar) INTO FIRST MIXING_BOWL 

        // get rid of other thing
        FOLD ('dummy) INTO FIRST MIXING_BOWL 

        PUT ('white_sugar) INTO FIRST MIXING_BOWL 

        REFRIGERATE NOW
        
        "COOK" THE ('white_sugar) UNTIL "CARAMELIZED" 
        

        PUT ('vanilla_bean) INTO FIRST MIXING_BOWL 
        
        REFRIGERATE NOW
        
        "HEAT" THE ('white_sugar) UNTIL "MELTED" 


        // if it is 0 do this
        PUT ('vanilla_bean) INTO FIRST MIXING_BOWL 
        
        REFRIGERATE NOW
   
        RUN
    }
}
