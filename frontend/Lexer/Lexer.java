package frontend.Lexer;

import frontend.SSA.SSAgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.Hashtable;

public class Lexer {
	static String[] reserved = {"AND", "OR", "NOT", "LET", "VAR", "ARRAY", "FUNC", "IF", "ELSE", "WHILE", "TRUE", "FALSE", "RETURN"};
	static String[] special = {"(", ")", "{", "}", "[", "]", "+", "-", "*", "/",
								">=", "<=", "!=", "==", ">", "<", "=", ",",
								";", ":", "::"};
	private Hashtable<Integer, String> id_table = new Hashtable<Integer, String>();
	// static integer where an identifier will "start" at - incremented every time an ID is added
	static int id_cnt = reserved.length;
	static int lexeme_count = 0;
	public class lexeme {
		private String name; private int pos; private int id;
		private int lexeme_index;
		public lexeme(String s, int position) {
			pos = position; name = s; lexeme_index = lexeme_count;
			findID();
		}
		public boolean last_sym() {
			return lexeme_index == tokens.size() - 1;
		}
		public boolean isIdentifier() {
			return !Arrays.asList(reserved).contains(name.toUpperCase()) && !name.matches("^[0-9] | TRUE | FALSE");
		}
		public lexeme next_lexeme() {
			try {
				return tokens.get(lexeme_index + 1); }
			catch(IndexOutOfBoundsException e){
				System.out.println("next_lexeme() has went out of bounds!");
				return null;
			}
		}
		public boolean isKeyword() {return !isIdentifier();}
		public int getPos() {return pos;}
		public int getID() {return id;}
		// returns name (in lowercase)
		public String getName() {return name.toLowerCase();}
		// privates
		public void findID() {
			if (isIdentifier()) {
				id_table.put(id_cnt ,name);
				id = id_cnt;
				id_cnt++;
			}
			else {
				for (int i = 0; i < reserved.length; ++i)
					if (reserved[i] == name.toUpperCase())
						id = i;
			}
		}
	}
	ArrayList characters = new ArrayList();
	public ArrayList<lexeme> tokens = new ArrayList<>();
	public Lexer(String in) {
		try {
			FileReader fr = new FileReader(in);
			int i;
			while ((i = fr.read()) != -1) {
				characters.add((char) i);
			}
		} catch (Exception e) {
			if (e instanceof FileNotFoundException)
				System.out.println("File " + in + " Not found");
			else if (e instanceof IOException)
				System.out.println("IOException in parsing input file");
		}
		initialize_identifier_table();
		findLexemes();
	}

	public ArrayList getCharacters() {
		return characters;
	}
	public ArrayList<lexeme> getTokens() {return tokens;}
	// Private
	private void initialize_identifier_table() {
		// initialized id_table to contain all reserved words
		for (int i = 0; i < reserved.length; ++i) {
			id_table.put(i, reserved[i]);
		}
	}
	private void findLexemes() {
		String lex = new String();
		int i;
		for (i = 0; i < characters.size(); ++i) {
			char current_char = (char) characters.get(i);
			if (current_char == ' ' || Arrays.asList(reserved).contains(lex)) {
				add_lexeme(lex, i);
			}
			else
				lex += current_char;
			}
		if (lex != "")
			add_lexeme(lex, i);
		}
	private void add_lexeme(String name, int pos) {
		tokens.add(new lexeme(name, pos - name.length()));
		lexeme_count++;
	}
}



