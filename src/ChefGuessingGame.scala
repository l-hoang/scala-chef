/* created for presentation purposes */

object ChefGuessingGame extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Guessing Salad with Prompt Pudding, More, Less, and Equal") 
        
        // no parens required!
        START_INGREDIENTS
        
        // implicit conversion of 100 to an Ingredient getter
        100 ('random_ingredients) 
        
        1 ('secret_ingredient) 
        
        // true
        1 ('true) 
        
        // false
        0 ('false) 
        
        1 ('result) 
        
        1 ('greater) 
        
        1 ('less) 
        
        1 ('equal) 
        
        END_INGREDIENTS
        
        // implicit conversion of string to a loop builder
        "AMASS" THE ('random_ingredients) NOW
        
            // throw 100 numbers into the first stack
            PUT ('random_ingredients) INTO FIRST MIXING_BOWL 
        
        "AMASS" THE ('random_ingredients) UNTIL ("AMASSED") 
        
        // shuffle the first stack
        MIX THE FIRST MIXING_BOWL WELL 
        
        // grab the top value of the stack: it will be the number
        // we need to guess
        FOLD ('secret_ingredient) INTO FIRST MIXING_BOWL 
        
        // done with the stack; empty it
        CLEAN FIRST MIXING_BOWL 

        "GUESS" THE ('secret_ingredient) NOW
        
            PUT ('false) INTO SECOND MIXING_BOWL 
            
            // equal = 0
            FOLD ('equal) INTO SECOND MIXING_BOWL 
            
            PUT ('false) INTO SECOND MIXING_BOWL 
            
            // greater = 0
            FOLD ('greater) INTO SECOND MIXING_BOWL 
            
            PUT ('true) INTO SECOND MIXING_BOWL 
            
            // less =  1
            FOLD ('less) INTO SECOND MIXING_BOWL 
            
            SERVE WITH "Prompt Pudding" 
            
            // ask for a guess
            TAKE ('guess) FROM REFRIGERATOR 
            
            // grab guess, put into 2nd stack
            PUT ('guess) INTO SECOND MIXING_BOWL 

            DIVIDE ('secret_ingredient) INTO SECOND MIXING_BOWL 
            
            // get the result of guess / actual number
            FOLD ('result) INTO SECOND MIXING_BOWL 
            
            // guess > actual => result at least 1
            // guess = actual = result is 1
            // guess < actual = result is 0, don't go in this
            "CHECK" THE ('result) NOW

                PUT ('false) INTO SECOND MIXING_BOWL 
                
                // less = 0; i.e. set less to be false
                FOLD ('less) INTO SECOND MIXING_BOWL 
                
                PUT ('true) INTO SECOND MIXING_BOWL 
                
                // equal = 1
                FOLD ('equal) INTO SECOND MIXING_BOWL 

                PUT ('guess) INTO SECOND MIXING_BOWL 
                
                // subtract the guess form the secret ingredient
                REMOVE ('secret_ingredient) FROM SECOND MIXING_BOWL 
                
                // grab result
                FOLD ('result) INTO SECOND MIXING_BOWL 
                
                // if result is 0, then this loop will no happen
                // other wise it will; in other words we're basically
                // using the loop as a conditional
                "EQUALIZE" THE ('result) NOW
                
                    PUT ('false) INTO SECOND MIXING_BOWL 
                    
                    // equal = 0
                    FOLD ('equal) INTO SECOND MIXING_BOWL 
                    
                    PUT ('true) INTO SECOND MIXING_BOWL 
                    
                    // greater = 1
                    FOLD ('greater) INTO SECOND MIXING_BOWL 
                    
                    // break out of the loop
                    SET ASIDE 
                
                "EQUALIZE" UNTIL "EQUALIZED" 

                // break out of loop, i.e. this was a conditional
                SET ASIDE 
            
            "CHECK" UNTIL "CHECKED" 


            // at this point we know if the number is greater than
            // less than or equal to based on the settings of
            // less, greater, and equal; we then use loops as conditionals
            // to print out the correct option

            // only 1 of less, greater, or equal will be set
            
            "LOWER" THE ('less) NOW
            
                SERVE WITH ("More") 
                
                SET ASIDE 
            
            "LOWER" UNTIL "LOWERED" 
            

            "MAX" THE ('greater) NOW
            
                SERVE WITH "Less" 
                
                SET ASIDE 
            
            "MAX" UNTIL "MAXED" 

            
            "LEVEL" THE ('equal) NOW
            
                PUT ('false) INTO SECOND MIXING_BOWL 
                
                // make the secret ingredient 0, meaning we break out
                // of the guessing loop
                FOLD ('secret_ingredient) INTO SECOND MIXING_BOWL 
                
                SERVE WITH "Equal" 
                
                SET ASIDE 
            
            "LEVEL" UNTIL "LEVELED" 
        
        "GUESS" UNTIL "GUESSED" 
        
        
        // asks for a guess
        TITLE ("Prompt Pudding") 
        
        START_INGREDIENTS
        
        103 G ('white_sugar) 
        
        117 ('eggs) 
        
        101 CUPS ('corn_starch) 
        
        115 CUPS ('milk) 
        
        58 ('bananas) 
        
        END_INGREDIENTS

        
        // clear stack again just in case
        CLEAN FIRST MIXING_BOWL 
        
        PUT ('bananas) INTO FIRST MIXING_BOWL 
        
        PUT ('milk) INTO FIRST MIXING_BOWL 
        
        PUT ('milk) INTO FIRST MIXING_BOWL 
        
        PUT ('corn_starch) INTO FIRST MIXING_BOWL 
        
        PUT ('eggs) INTO FIRST MIXING_BOWL 
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL 
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 
        
        REFRIGERATE FOR 1 HOURS 
        



        TITLE ("More") 
        
        START_INGREDIENTS
        
        10 ('nl) 
        
        62 ('up) 
        
        END_INGREDIENTS
        
        CLEAN FIRST MIXING_BOWL 
        
        PUT ('nl) INTO FIRST MIXING_BOWL 
        
        PUT ('up) INTO FIRST MIXING_BOWL 
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 
        
        REFRIGERATE FOR 1 HOURS 
        
        


        TITLE ("Less") 
        
        START_INGREDIENTS
        
        10 ('nl) 
        
        60 ('down) 
        
        END_INGREDIENTS
        
        CLEAN FIRST MIXING_BOWL 
        
        PUT ('nl) INTO FIRST MIXING_BOWL 
        
        PUT ('down) INTO FIRST MIXING_BOWL 
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 
        
        REFRIGERATE FOR 1 HOURS 
        



        
        TITLE ("Equal") 
        
        START_INGREDIENTS
        
        10 ('nl) 
        
        61 ('eq) 
        
        END_INGREDIENTS
        
        CLEAN FIRST MIXING_BOWL 
        
        PUT ('nl) INTO FIRST MIXING_BOWL 
        
        PUT ('eq) INTO FIRST MIXING_BOWL 
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 
        
        REFRIGERATE FOR 1 HOURS 



        
        RUN
    }
}
