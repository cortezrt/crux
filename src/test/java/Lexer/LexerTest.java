package Lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    @Test
    void testLexer() {
        Lexer L = new Lexer("src/test/resources/lexerTest01.crx");
        assertEquals(18, L.getTokens().size());

    }
}