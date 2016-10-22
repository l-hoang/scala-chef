/*
The MIT License (MIT)

Copyright (c) 2014 Zane Urbanski, Eric Yu, Loc Hoang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

object DoesItCompile extends ScalaChef {
    def main(args: Array[String]): Unit = {       
        TITLE ("tester") END

        START_INGREDIENTS

        111 ('potatoes) END

        END_INGREDIENTS


        PUT ('potatoes) INTO FIRST MIXING_BOWL END

        FOLD ('potatoes) INTO FIRST MIXING_BOWL END

        ADD ('potatoes) TO FIRST MIXING_BOWL END

        COMBINE ('potatoes) INTO FIRST MIXING_BOWL END

        DIVIDE ('potatoes) INTO FIRST MIXING_BOWL END

        LIQUEFY ('potatoes) END

        "COOK" THE ('potatoes) END
        
        "COOK" UNTIL "COOKED" END
        
        "BAKE" THE ('potatoes) END
        
        "BAKE" THE ('potatoes) UNTIL "BAKED" END

        STIR THE FIRST MIXING_BOWL FOR (3) MINUTES END

        STIR ('potatoes) INTO THE FIRST MIXING_BOWL END

        CLEAN (FIRST) MIXING_BOWL END
        SERVES (1) END
    }
}
