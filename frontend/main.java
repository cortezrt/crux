package frontend;

import frontend.Lexer.*;
import frontend.Parser.*;

public class main {
    public static void main(String[] args) throws Exception {
        var P = new Parser(args[0]);
        P.parse();
        }
    }