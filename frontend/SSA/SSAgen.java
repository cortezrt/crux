package frontend.SSA;
import java.util.ArrayList;

public class SSAgen {
    static int basic_block_id;
    private ArrayList<basic_block> basic_blocks;
    public SSAgen() {
        basic_block_id = 0;
    }
    private class Instruction {
        private String out;
        Instruction() {
            out = new String();
        }
        private void write(String x) {
            out += "write " + x + "\n";
        }
        private void read() {
            out += "read\n";
        }
        private void writeNewLine() {
            out += "writeNewLine\n";
        }
        private void unary_minus(String x) {
            out += "neg (" + x + ")\n";
        }
        private void add(String left, String right) { out += "add (" + left + ")  (" + right + ")\n"; }
        private void sub(String left, String right) { out += "sub (" + left + ") (" + right + ")\n"; }
        private void div(String left, String right) {
            out += "div (" + left + ") (" + right + ")\n";
        }
        private void cmp (String left, String right) {
            out += "cmp (" + left + ") (" + right + ")\n";
        }
        private void adda (String left, String right) {
            out += "adda (" + left + ") (" + right + ")\n";
        }
        private void load(String addr) {
            out += "load (" + addr + ")\n";
        }
        private void store(String val, String addr) {
            out += "store (" + val + ") (" + addr + ")\n";
        }
        private void phi(String left, String right) { out += "phi (" + left + ") (" + right + ")\n"; }
        private void end() {
            out += "end\n";
        }
        private void bra(String target) {
            out += "bra " + target + "\n";
        }
        private void bne(String left, String target) {
            out += "bne " + left + " " + target + "\n";
        }
        private void beq(String left, String target) {
            out += "beq " + left + " " + target + "\n";
        }
        private void ble(String left, String target) {
            out += "ble " + left + " " + target +"\n";
        }
        private void bge(String left, String target) {
            out += "bge " + left + " " + target + "\n";
        }
        private void bgt(String left, String target) {
            out += "bgt " + left + " " + target + "\n";
        }
    }

    private class basic_block{
        private ArrayList<Instruction> Instructions;
        public int id;
        basic_block() {
            id = basic_block_id;
            basic_block_id++;
            Instructions = new ArrayList<Instruction>();
        }
        private void add_instr(Instruction I) {
            Instructions.add(I);
        }
    }
    public int add_block () {
        basic_blocks.add(new basic_block());
        return basic_block_id;
    }
    public void add_instruction(String instr, ArrayList<String> parameters) {
        Instruction I = new Instruction();
        switch(instr) {
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
                break;
            case "add":
                I.add(parameters.get(0), parameters.get(1));
                break;
            case "sub":
                I.sub(parameters.get(0), parameters.get(1));
                break;
            case "div":
                I.div(parameters.get(0), parameters.get(1));
                break;
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
            default:
                break;

        }
        basic_blocks.get(basic_block_id).add_instr(I);
    }
}