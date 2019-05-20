package executor.model.instructions;

import executor.exceptions.UnresolvedVariableException;
import executor.model.State;
import executor.model.Variable;

public class PrintInstruction implements Instruction {
    private final State state;

    PrintInstruction(State state) {
        this.state = state;
    }

    @Override
    public void execute(String... params) throws UnresolvedVariableException {
        Variable var = state.findVariableByName(params[0]);
        if (var == null) throw new UnresolvedVariableException(params[0]);
        System.out.println(var);
        state.addToResult(var.toString());
        //state.getExecutor().setExecutePos(state.getExecutor().getExecutePos() + 1);
    }
}
