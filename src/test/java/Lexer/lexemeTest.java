package Lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class lexemeTest {
    // ensure that detection of identifiers and reserved keywords function properly
    @Test
    public void lexemeTest() {
        lexeme reserved = new lexeme("AND");
        lexeme identifier = new lexeme("Larry");
        lexeme notIdentifier = new lexeme("123Larry");

        assertTrue(reserved.isReserved());
        assertTrue(identifier.isIdentifier());
        assertFalse(notIdentifier.isIdentifier());
        assertFalse(reserved.isIdentifier());
    }

}