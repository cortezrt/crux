package util;

import java.util.ArrayList;

public class basicBlock {
    ArrayList<Instruction> scopedInstructions;
    private ArrayList<Integer> linkedBlocks;
    basicBlock() {
        scopedInstructions = new ArrayList<Instruction>();
        linkedBlocks = new ArrayList<Integer>();
        SSAgen.basic_block_id++;
        SSAgen.current_block = this;
    }
    // Will add the candidate to scopedInstructions if it is a unique instruction
    // Else, it will merely return the instruction ID of the duplicate
    public void emit() {
        // must eventually deal with branching instr somehow
        for (int i = 0; i < scopedInstructions.size(); ++i) {
            Instruction current = scopedInstructions.get(i);
            current.printInstr();
        }
    }
    // retrieve instruction at index of block
    public Instruction getInstruction(int index){
        return scopedInstructions.get(index);
    }
    public int add_instr(Instruction candidate_instruction) {
        for (int i = 0; i < scopedInstructions.size(); ++i) {
            Instruction test = scopedInstructions.get(i);
            if (test.equivalentInstruction(candidate_instruction)) {
                SSAgen.instr_cnt--;    // duplicate instr, lower instr_cnt
                return test.getIID();
            }
        }
        scopedInstructions.add(candidate_instruction);
        return candidate_instruction.getIID();
    }

    public void link_block(Integer child) {
        SSAgen.basicBlocks.get(child).scopedInstructions.addAll(scopedInstructions);
        linkedBlocks.add(child);
    }
}