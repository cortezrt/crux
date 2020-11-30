package util;

public class Instruction {
    private String out;
    private int instruction_id;
    Instruction() {
        this.instruction_id = SSAgen.instr_cnt;
        SSAgen.instr_cnt++;
        out = new String();
        //instr_cnt++;
    }
    public void printInstr() {
        System.out.print(Integer.toString(instruction_id) + ": ");
        System.out.print(out);
    }
    public int getIID() { return instruction_id;}
    public boolean equivalentInstruction(Instruction candidate) {
        return out.equals(candidate.out);
    }
    public void write(String x) {
        out += "write " + x + "\n";
    }
    public void read() {
        out += "read\n";
    }
    public void writeNewLine() {
        out += "writeNL\n";
    }
    public void unary_minus(String x) {
        out += "neg " + x + "\n";
    }
    public void constant(int x) {
        out += "const #" + x + "\n";
    }
    public void add(String left, String right) { out += "add " + left + " " + right + "\n"; }
    public void sub(String left, String right) { out += "sub " + left + " " + right + "\n"; }
    public void div(String left, String right) {
        out += "div " + left + " " + right + "\n";
    }
    public void mul(String left, String right) {
        out += "mul " + left + " " + right + "\n";
    }
    public void cmp (String left, String right) {
        out += "cmp " + left + " " + right + "\n";
    }
    public void adda (String left, String right) {
        out += "adda " + left + " " + right + "\n";
    }
    public void load(String addr) {
        out += "load (" + addr + ")\n";
    }
    public void store(String val, String addr) {
        out += "store " + val + " " + addr + "\n";
    }
    public void phi(String left, String right) { out += "phi " + left + " " + right + "\n"; }
    public void end() {
        out += "end\n";
    }
    public void bra(String target) {
        out += "bra " + target + "\n";
    }
    public void bne(String left, String target) {
        out += "bne " + left + " " + target + "\n";
    }
    public void beq(String left, String target) {
        out += "beq " + left + " " + target + "\n";
    }
    public void ble(String left, String target) {
        out += "ble " + left + " " + target +"\n";
    }
    public void bge(String left, String target) {
        out += "bge " + left + " " + target + "\n";
    }
    public void bgt(String left, String target) {
        out += "bgt " + left + " " + target + "\n";
    }
}