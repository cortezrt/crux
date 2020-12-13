package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SSAgenTest {
    @Test
    public void basicArithmeticTest() {
        // test addition, subtraction, multiplication, and division
        SSAgen out = new SSAgen();
        out.add_block(0); // Only one basic block - linear flow of execution
        // var x = 3, var y = 5;
        out.addConstant(new Result(Result.kind.Constant, 3));
        out.addConstant(new Result(Result.kind.Constant, 5));
        // assign variables x and y to prior constant - job of parser, but emulated here
        Result x = new Result(Result.kind.Variable, 0);
        Result y = new Result(Result.kind.Variable, 1);
        // addition, sub, mul, div
        out.add_instruction(0, x, y);
        out.add_instruction(1, x, y);
        out.add_instruction(2, x, y);
        out.add_instruction(3, x, y);

        Instruction curInst;
        String [] correct = {"const #3\n", "const #5\n", "add (0) (1)\n", "sub (0) (1)\n", "mul (0) (1)\n", "div (0) (1)\n"};
        for (int i = 0; i < 6; ++i) {
            curInst = out.current_block.getInstruction(i);
            assertEquals(curInst.getInstrString(), correct[i]);
        }
    }

    @Test
    public void basicCompileTimeArithmatic() {
        SSAgen out = new SSAgen();
        out.add_block(0);

        Result constant1 = new Result(Result.kind.Constant, 4);
        Result constant2 = new Result(Result.kind.Constant, 2);
        out.addConstant(constant1);
        out.addConstant(constant2);
        assertTrue(out.isCompileTimeOperation(constant1,constant2));

        out.doCompileTimeOperation(0, constant1, constant2);    // addition
        out.doCompileTimeOperation(1, constant1, constant2);    // subtraction
        out.doCompileTimeOperation(2, constant1, constant2);    // Mul
        out.doCompileTimeOperation(3, constant1, constant2);    // Div

        Instruction curInst;
        // 4 2 6 2 8 2 are the total results , but duplicates are removed due to SSA conventions to result in the below 4 (no point in emitting duplicate constants or duplicate instructions
        String [] correct = {"const #4\n", "const #2\n", "const #6\n", "const #8\n"};
        for (int i = 0; i < 4; ++i) {
            curInst = out.current_block.getInstruction(i);
            assertEquals(curInst.getInstrString(), correct[i]);
        }
    }
}