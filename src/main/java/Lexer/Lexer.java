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
		String lexString = "";
		int i;
		for (i = 0; i < characters.size(); ++i) {
			char currentChar = characters.get(i);
			// delimits lexemes by spaces, new lines, and tabs
			if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
				add_lexeme(lexString);
				lexString = "";
			}
			// Rather convoluted - If the current lexString is in the special or reserved list, we should add lexString as a lexeme and create a new lexString of the currentChar
			// Furthermore, if the current character is a special character (third boolean statement in the else if), we treat is as a delimiter - adding lexString as a lexeme and creating a new lexString
			// While the above two are separate cases, they contain identical code solutions (lookAhead will return false for all non-special lexStrings)
			else if (Arrays.asList(special).contains(lexString) || Arrays.asList(reserved).contains(lexString) || Arrays.asList(special).contains(Character.toString(currentChar))) {
				if (lookAhead(currentChar, lexString)) {
					lexString += currentChar;
				}
				else {
					add_lexeme(lexString);
					lexString = Character.toString(currentChar);
				}
			}
			// aggregate lexString char by char
			else
				lexString += currentChar;
			}
		add_lexeme(lexString);
		}
	// in cases where we must evaluate the next char (lex = "<" vs candidateLex = "<=") to get the desired lexeme, return true else false
	private boolean lookAhead(char currentChar, String lex) {
		String candidateLex = lex + currentChar;
		return Arrays.asList(special).contains(candidateLex);
	}


	private void add_lexeme(String name) {
		if (name.length() > 0) {
			tokens.add(new lexeme(name, lexeme_count));
			lexeme_count++;
		}
	}
}



