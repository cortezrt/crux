package Parser;

import org.junit.jupiter.api.Test;
import util.Instruction;
import util.SSAgen;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    static void testInstrString(String [] correct, SSAgen out) {
        Instruction curInst;
        for (int i = 0; i < correct.length; ++i) {
            curInst = out.current_block.getInstruction(i);
            assertEquals(curInst.getInstrString(), correct[i]);
        }
    }

    @Test
    public void arithmaticTest() throws Exception {
        Parser P = new Parser("src/test/resources/simple_arithmatic.crx");
        P.parse();
        assertEquals(P.tokens.size(), 63);


        String [] correct = {"const #1\n", "const #4\n", "add (0) (1)\n", "mul (0) (1)\n",
                            "mul (1) (3)\n", "div (4) (0)\n", "add (0) (5)\n"};
        testInstrString(correct, P.out);
    }

    @Test
    public void builtInFunctions() throws Exception {
        Parser P = new Parser("src/test/resources/builtInFunctions.crx");
        P.parse();
        P.emitSSA();
        String [] correct = {"const #5\n", "write (0)\n", "read\n", "writeNL\n"};
        testInstrString(correct, P.out);
    }

}