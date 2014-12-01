object ChefGuessingGame extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Guessing Salad with Prompt Pudding, More, Less, and Equal") END
        
        START_INGREDIENTS
        
        100 ('random_ingredients) END
        
        1 ('secret_ingredient) END
        
        1 ('user_ingredient) END
        
        // true
        1 ('true) END
        
        // false
        0 ('false) END
        
        1 ('result) END
        
        1 ('greater) END
        
        1 ('less) END
        
        1 ('equal) END
        
        END_INGREDIENTS
        
        "AMASS" THE ('random_ingredients) END
        
        PUT ('random_ingredients) INTO FIRST MIXING_BOWL END
        
        "AMASS" THE ('random_ingredients) UNTIL ("AMASSED") END
        
        MIX THE FIRST MIXING_BOWL WELL END
        
        FOLD ('secret_ingredient) INTO FIRST MIXING_BOWL END
        
        "GUESS" THE ('secret_ingredient) END
        
        PUT ('false) INTO SECOND MIXING_BOWL END
        
        FOLD ('equal) INTO SECOND MIXING_BOWL END
        
        PUT ('false) INTO SECOND MIXING_BOWL END
        
        FOLD ('greater) INTO SECOND MIXING_BOWL END
        
        PUT ('true) INTO SECOND MIXING_BOWL END
        
        FOLD ('less) INTO SECOND MIXING_BOWL END
        
        SERVE WITH "Prompt Pudding" END
        
        TAKE ('guess) FROM REFRIGERATOR END
        
        PUT ('guess) INTO SECOND MIXING_BOWL END

        DIVIDE ('secret_ingredient) INTO SECOND MIXING_BOWL END
        
        FOLD ('result) INTO SECOND MIXING_BOWL END
        
        "CHECK" THE ('result) END

        PUT ('false) INTO SECOND MIXING_BOWL END
        
        FOLD ('less) INTO SECOND MIXING_BOWL END
        
        PUT ('true) INTO SECOND MIXING_BOWL END
        
        FOLD ('equal) INTO SECOND MIXING_BOWL END
        
        PUT ('guess) INTO SECOND MIXING_BOWL END
        
        REMOVE ('secret_ingredient) FROM SECOND MIXING_BOWL END
        
        FOLD ('result) INTO SECOND MIXING_BOWL END
        
        "EQUALIZE" THE ('result) END
        
        PUT ('false) INTO SECOND MIXING_BOWL END
        
        FOLD ('equal) INTO SECOND MIXING_BOWL END
        
        PUT ('true) INTO SECOND MIXING_BOWL END
        
        FOLD ('greater) INTO SECOND MIXING_BOWL END
        
        SET ASIDE END
        
        "EQUALIZE" UNTIL "EQUALIZED" END
        
        SET ASIDE END
        
        "CHECK" UNTIL "CHECKED" END
        
        "LOWER" THE ('less) END
        
        SERVE WITH ("More") END
        
        SET ASIDE END
        
        "LOWER" UNTIL "LOWERED" END
        
        "MAX" THE ('greater) END
        
        SERVE WITH "Less" END
        
        SET ASIDE END
        
        "MAX" UNTIL "MAXED" END
        
        "LEVEL" THE ('equal) END
        
        PUT ('false) INTO SECOND MIXING_BOWL END
        
        FOLD ('secret_ingredient) INTO SECOND MIXING_BOWL END
        
        SERVE WITH "Equal" END
        
        SET ASIDE END
        
        "LEVEL" UNTIL "LEVELED" END
        
        "GUESS" UNTIL "GUESSED" END
        
        SERVES (1) END
        
        
        TITLE ("Prompt Pudding") END
        
        START_INGREDIENTS
        
        103 G ('white_sugar) END
        
        117 ('eggs) END
        
        101 CUPS ('corn_starch) END
        
        115 CUPS ('milk) END
        
        58 ('bananas) END
        
        END_INGREDIENTS
        
        CLEAN (FIRST) MIXING_BOWL END
        
        PUT ('bananas) INTO FIRST MIXING_BOWL END
        
        PUT ('milk) INTO FIRST MIXING_BOWL END
        
        PUT ('milk) INTO FIRST MIXING_BOWL END
        
        PUT ('corn_starch) INTO FIRST MIXING_BOWL END
        
        PUT ('eggs) INTO FIRST MIXING_BOWL END
        
        PUT ('white_sugar) INTO FIRST MIXING_BOWL END
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END
        
        REFRIGERATE FOR 1 HOURS END
        
        
        TITLE ("More") END
        
        START_INGREDIENTS
        
        10 ('nl) END
        
        62 ('up) END
        
        END_INGREDIENTS
        
        CLEAN (FIRST) MIXING_BOWL END
        
        PUT ('nl) INTO FIRST MIXING_BOWL END
        
        PUT ('up) INTO FIRST MIXING_BOWL END
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END
        
        REFRIGERATE FOR 1 HOURS END
        
        
        TITLE ("Less") END
        
        START_INGREDIENTS
        
        10 ('nl) END
        
        60 ('down) END
        
        END_INGREDIENTS
        
        CLEAN (FIRST) MIXING_BOWL END
        
        PUT ('nl) INTO FIRST MIXING_BOWL END
        
        PUT ('down) INTO FIRST MIXING_BOWL END
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END
        
        REFRIGERATE FOR 1 HOURS END
        
        
        TITLE ("Equal") END
        
        START_INGREDIENTS
        
        10 ('nl) END
        
        61 ('eq) END
        
        END_INGREDIENTS
        
        CLEAN (FIRST) MIXING_BOWL END
        
        PUT ('nl) INTO FIRST MIXING_BOWL END
        
        PUT ('eq) INTO FIRST MIXING_BOWL END
        
        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL END
        
        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH END
        
        REFRIGERATE FOR 1 HOURS END
        
        RUN
    }
}