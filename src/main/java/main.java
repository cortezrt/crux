import Lexer.*;
import Parser.*;

public class main {
    public static void main(String[] args) throws Exception {
        //System.out.println(args[0]);
        var P = new Parser(args[0]);
        P.parse();
        }
    }