package executor.model.instructions;

import executor.model.State;

public class InstructionBuilder {
    private InstructionBuilder(){}

    public static Instruction buildSubInstruction(State state) {
        return new SubInstruction(state);
    }

    public static Instruction buildSetInstruction(State state) {
        return new SetInstruction(state);
    }

    public static Instruction buildPrintInstruction(State state) {
        return new PrintInstruction(state);
    }

    public static Instruction buildCallInstruction(State state) {
        return new CallInstruction(state);
    }
}
