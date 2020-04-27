package crux.frontend.Lexer;

import java.io.*;

public class Lexer {
	byte[] characters;
	Lexer(String in) throws IOException, FileNotFoundException {
		FileReader fr = new FileReader(in);
		int i;
		while ( (i = fr.read()) != -1) {
				characters[i] = (byte) i;
			}
		}
	public byte[] getCharacters() {
		return characters;
		}
	// simply an array of characters, for now
	}
