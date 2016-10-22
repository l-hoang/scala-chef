/* Sample chef code that prints "hello world".    
 * http://www.dangermouse.net/esoteric/chef_hello.html
 */

object ChefHelloWorld extends ScalaChef {
    def main(args: Array[String]): Unit = {
        TITLE ("Hello World") 


        START_INGREDIENTS
        
        72 G ('haricot_beans) 
        
        101 ('eggs) 
        
        108 G ('lard) 

        111 CUPS ('oil) 

        32 ('zucchinis) 

        119 ML ('water) 

        114 G ('red_salmon) 

        100 G ('dijon_mustard) 

        33 ('potatoes) 

        END_INGREDIENTS

        
        PUT ('potatoes) INTO FIRST MIXING_BOWL 

        PUT ('dijon_mustard) INTO FIRST MIXING_BOWL 

        PUT ('lard) INTO FIRST MIXING_BOWL 

        PUT ('red_salmon) INTO FIRST MIXING_BOWL 

        PUT ('oil) INTO FIRST MIXING_BOWL 

        PUT ('water) INTO FIRST MIXING_BOWL 

        PUT ('zucchinis) INTO FIRST MIXING_BOWL 

        PUT ('oil) INTO FIRST MIXING_BOWL 

        PUT ('lard) INTO FIRST MIXING_BOWL 

        PUT ('lard) INTO FIRST MIXING_BOWL 

        PUT ('eggs) INTO FIRST MIXING_BOWL 

        PUT ('haricot_beans) INTO FIRST MIXING_BOWL 

        LIQUEFY CONTENTS OF THE FIRST MIXING_BOWL 

        POUR CONTENTS OF THE FIRST MIXING_BOWL INTO THE FIRST BAKING_DISH 

        SERVES (1) 
        
        RUN
    }
}
