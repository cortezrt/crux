package frontend;

import frontend.Lexer.*;
import frontend.Parser.*;

public class main {
    public static void main(String[] args) throws Exception{
        var L = new Parser(args[0]);
        L.parse();
    }
}