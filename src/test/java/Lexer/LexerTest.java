package Lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    @Test
    // no look-ahead lexemes
    void basicLexerTest() {
        Lexer L = new Lexer("src/test/resources/lexerTest01.crx");
        assertEquals(18, L.getTokens().size());
        assertTrue(L.last_sym(L.getTokens().get(17)));
    }

    // look-ahead-lexemes - accounts for situations where we must look ahead one character
    // to determine which special char it is ('<=' vs '<' then '=')
    @Test
    void lookAheadLexerTest() {
        Lexer L = new Lexer("src/test/resources/lexerTest02.crx");
        assertEquals(37 + 8, L.getTokens().size());
        assertEquals("<=", L.getTokens().get(26).getName());
        assertEquals("<", L.getTokens().get(33).getName());
        assertEquals("::", L.getTokens().get(39).getName());
    }

    // ensure that comments are working as intended
    @Test
    void commentLexerTest() {
        Lexer L = new Lexer("src/test/resources/lexerCommentTest01.crx");
        assertEquals(8, L.getTokens().size());
    }

}