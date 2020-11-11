package frontend.Lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;


public class Lexer {
	static String[] reserved = {"AND", "OR", "NOT", "LET", "VAR", "ARRAY", "FUNC", "IF", "ELSE", "WHILE", "TRUE", "FALSE", "RETURN"};
	static String[] special = {"(", ")", "{", "}", "[", "]", "+", "-", "*", "/",
								">=", "<=", "!=", "==", ">", "<", "=", ",",
								";", ":", "::"};
	// static integer where an identifier will "start" at - incremented every time an ID is added
	static int lexeme_count = 0;

	ArrayList<Character> characters;
	public ArrayList<lexeme> tokens;
	public Lexer(String in) {
		characters = new ArrayList<>();
		tokens = new ArrayList<>();
		try {
			FileReader fr = new FileReader(in);
			int i;
			while ((i = fr.read()) != -1) {
				characters.add((char) i);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Input file" + in +  "not found");
			return;
		}
		catch(IOException e) {
			System.out.println("IOException in Lexer constr");
			return;
		}
		findLexemes();
	}

	public ArrayList<lexeme> getTokens() {return tokens;}


	//returns if the passed in lexeme is the last token
	public boolean last_sym(lexeme l) {
		return l.lexeme_index == tokens.size() - 1;
	}
	// print all lexeme..
	public void print_lexemes() {
		for (int i = 0; i < lexeme_count; ++i) {
			System.out.println(tokens.get(i).getName());
		}
	}
	private void findLexemes() {
		String lex = "";
		int i;
		for (i = 0; i < characters.size(); ++i) {
			char current_char = characters.get(i);
			if (current_char == ' ' || current_char == '\n') {
				add_lexeme(lex);
				lex = "";
			}
			else if (Arrays.asList(special).contains(Character.toString(current_char)) || Arrays.asList(special).contains(lex) || Arrays.asList(reserved).contains(lex)) {
				add_lexeme(lex);
				lex = Character.toString(current_char);
				}
			else
				lex += current_char;
			}
		add_lexeme(lex);
		}
	private void add_lexeme(String name) {
		if (name.length() > 0) {
			tokens.add(new lexeme(name, lexeme_count));
			lexeme_count++;
		}
	}
}



