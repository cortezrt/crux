package Lexer;
import java.util.Arrays;
import java.util.Hashtable;

public class lexeme {
    static String[] reserved = {"AND", "OR", "NOT", "LET", "VAR", "ARRAY", "FUNC", "IF", "ELSE", "WHILE", "TRUE", "FALSE", "RETURN"};
    static String[] special = {"(", ")", "{", "}", "[", "]", "+", "-", "*", "/",
            ">=", "<=", "!=", "==", ">", "<", "=", ",",
            ";", ":", "::"};
    private static Hashtable<String, Integer> id_table = null;
    static int id_cnt = reserved.length;

    private String name;
    private int id;
    private int val;

    public int lexeme_index;
    public lexeme(String s, int idx) {
        name = s;
        lexeme_index = idx;
        // implement a mechanism for holding an IID value
        initialize_identifier_table();
        findID();
    }

    public boolean isIdentifier() {
        // it is an identifier if it does not start with a digit and is not a reserved keyword
        return !name.matches("^[0-9]") && id >= reserved.length;
        //return !Arrays.asList(reserved).contains(name.toUpperCase()) && !name.matches("^[0-9] | TRUE | FALSE");
    }

    private boolean isReserved() {
        for (int i = 0; i < reserved.length; ++i) {
            var upper = name.toUpperCase();
            if (upper.equals(reserved[i]))
                return true;
            }
        return false;
    }
    public int getVal() {return val;}
    public int getID() {return id;}
    // returns name (in lowercase)
    public String getName() {return name.toLowerCase();}


    // privates
    private void initialize_identifier_table() {
        // initialized id_table to contain all reserved words
        if (id_table == null) {
            id_table = new Hashtable<String, Integer>();
            for (int i = 0; i < reserved.length; ++i) {
                id_table.put(reserved[i], i);
            }
        }
    }
    public void findID() {
        if (isReserved())
        {
            for (int i = 0; i < reserved.length; ++i)
                if (reserved[i].equals(name.toUpperCase()))
                    id = i;
        }
        else if (!name.matches("^[0-9][0-9]*")) {
            // if string name is in the table,  set the identifier to that string'ssvalue
            if (id_table.containsKey(name)) {
                id = id_table.get(name);
            }
            // else create new identifier ID
            else {
                id_table.put(name, id_cnt);
                id = id_cnt;
                id_cnt++;
            }
        }
        // is a number... - merely set id to -1 and store the value of the integer in val
        else {
            //System.out.println(name);
            id = -1;
            val = Integer.parseInt(name);
        }
    }
}