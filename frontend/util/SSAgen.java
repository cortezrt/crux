package frontend.util;
import java.util.ArrayList;


public class SSAgen {
    static int basic_block_id;
    static int instr_cnt;
    static ArrayList<basicBlock> basicBlocks;
    static basicBlock current_block;

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
    // Should the parser be able to parse results?
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
    public int add_instruction(int instr, Result operand_1, Result operand_2) {
        // if both operands are constants - do the operation at compile time
        String left = parseResult(operand_1);
        String right = parseResult(operand_2);
        Instruction I = new Instruction();
        switch(instr) {
            /*
            case "write":
                I.write(parameters.get(0));
                break;
            case "read":
                I.read();
                break;
            case "writeNewLine":
                I.writeNewLine();
                break;
            case "unaryMinus":
                I.unary_minus(parameters.get(0));
                break;*/
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
            /*
            case "cmp":
                I.cmp(parameters.get(0), parameters.get(1));
                break;
            case "adda":
                I.adda(parameters.get(0), parameters.get(1));
                break;
            case "load":
                I.load(parameters.get(0));
                break;
            case "store":
                I.store(parameters.get(0), parameters.get(1));
                break;
            case "phi":
                I.phi(parameters.get(0), parameters.get(1));
                break;
            case "end":
                I.end();
                break;
            case "bra":
                I.bra(parameters.get(0));
                break;
            case "bne":
                I.bne(parameters.get(0), parameters.get(1));
                break;
            case "beq":
                I.beq(parameters.get(0), parameters.get(1));
                break;
            case "ble":
                I.ble(parameters.get(0), parameters.get(1));
                break;
            case "bge":
                I.bge(parameters.get(0), parameters.get(1));
                break;
            case "bgt":
                I.bgt(parameters.get(0), parameters.get(1));
                break;
            */
            default:
                break;

        }
        return current_block.add_instr(I);
    }
    public boolean isCompileTimeOperation(Result operand1, Result operand2) {
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
        return new Result(Result.kind.Constant, val);
    }
}