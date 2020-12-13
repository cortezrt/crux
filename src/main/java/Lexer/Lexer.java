package Lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;


public class Lexer {
	static String[] reserved = {"AND", "OR", "NOT", "LET", "VAR", "ARRAY", "FUNC", "IF", "ELSE", "WHILE", "TRUE", "FALSE", "RETURN"};
	static String[] special = {"(", ")", "{", "}", "[", "]", "+", "-", "*", "/",
								">=", "<=", "!=", "==", ">", "<", "=", ",",
								";", ":", "::"};
	// static integer where an identifier will "start" at - incremented every time an ID is added
	int lexeme_count = 0;

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
	private int ignoreComment(int i) {
		while (characters.get(i) != '\n') {
			++i;
		}
		return i;
	}

	private boolean shouldDelimitSpecialChar(char currentChar, String lexString) {
		// we delimit by special characters when currentChar is a special char and our current lexString does not demand that we lookAhead to the currentChar
		return Arrays.asList(special).contains(Character.toString(currentChar)) && !lookAhead(currentChar, lexString);
	}
	private void findLexemes() {
		String lexString = "";
		int i;
		for (i = 0; i < characters.size(); ++i) {
			char currentChar = characters.get(i);
			// ignores any commented lines by moving character index i to next line
			if (currentChar == '/' && characters.get(i+1) == '/') {
				i = ignoreComment(i);
				continue;
			}
			// delimits lexemes by spaces, new lines, and tabs
			if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
				add_lexeme(lexString);
				lexString = "";
			}
			else if (Arrays.asList(reserved).contains(lexString) || shouldDelimitSpecialChar(currentChar, lexString)) {
				add_lexeme(lexString);
				lexString = Character.toString(currentChar);
			}

			else if (Arrays.asList(special).contains(lexString)) {
				if (lookAhead(currentChar, lexString)) {
					lexString += currentChar;
					add_lexeme(lexString);
					lexString = "";
				}
				// don't need to look ahead to next char - add lexeme and set lexString to currentChar
				else {
					add_lexeme(lexString);
					lexString = Character.toString(currentChar);
				}
			}
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



