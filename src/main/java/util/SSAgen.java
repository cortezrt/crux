package util;
import java.util.ArrayList;


public class SSAgen {
    static int basic_block_id;
    static int instr_cnt;
    static ArrayList<basicBlock> basicBlocks;
    static public basicBlock current_block;

    //variableMap maps String instructions to IIDs
    public SSAgen() {
        basic_block_id = 0;
        instr_cnt = 0;
        basicBlocks = new ArrayList<basicBlock>();
        basicBlocks.add(new basicBlock()); // basic block for the top level program
    }

    public void emit() {
        for (int i = 0; i < basicBlocks.size(); ++i)
            basicBlocks.get(i).emit();
    }

    public int add_block (int current_block_id) {
        basicBlocks.add(new basicBlock());
        if (current_block_id != 0)
            basicBlocks.get(current_block_id).link_block(basic_block_id);
        return basic_block_id;
    }

    public String parseResult(Result r) {
        if (r == null) return "";
        String result = Integer.toString(r.getResult());
        switch (r.getKind()) {
            case Constant:
                return "(" + addConstant(r) + ")";
            case Variable:
                return "(" + result + ")";
            default:
                return "$" + result;
        }
    }
    public int addConstant(Result constant) {
        Instruction I = new Instruction();
        I.constant(constant.getResult());
        return current_block.add_instr(I);
    }

    public int add_instruction(int instr, Result op1, Result op2) {
        String left = parseResult(op1);
        String right = parseResult(op2);
        Instruction I = new Instruction();
        switch(instr) {
            case 0:
                I.add(left, right);
                break;
            case 1:
                I.sub(left, right);
                break;
            case 2:
                I.mul(left, right);
                break;
            case 3:
                I.div(left, right);
                break;
            case 4:
                I.write(left);
                break;
            case 5:
                I.read();
                break;
            case 6:
                I.writeNewLine();
                break;
            /*
            case 7:
                I.lge(left, right);
                break;
            case 8:
                I.lge(left, right);
                break;
            case 9:
                I.lge(left, right);
                break;
            case 10:
                I.lge(left, right);
                break;
            case 11:
                I.lge(left, right);
                break;
            case 12:
                I.lge(left, right);
                break;
            */
            default:
                break;

        }
        return current_block.add_instr(I);
    }
    public boolean isCompileTimeOperation(Result operand1, Result operand2) {
        if (operand1 == null || operand2 == null)
            return false;
        return (operand1.getKind() == Result.kind.Constant && operand2.getKind() == Result.kind.Constant);
    }

    public Result doCompileTimeOperation(int instr, Result operand_1, Result operand_2) {
        int val;
        switch (instr) {
            case 0:
                val = operand_1.getResult() + operand_2.getResult();
                break;
            case 1:
                val = operand_1.getResult() - operand_2.getResult();
                break;
            case 2:
                val = operand_1.getResult() * operand_2.getResult();
                break;
            case 3:
                val = operand_1.getResult() / operand_2.getResult();
                break;
            default:
                val = -1;
                break;
        }
        Result r = new Result(Result.kind.Constant, val);
        addConstant(r);
        return r;
    }
}