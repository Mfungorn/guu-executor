package executor.model.instructions;

import executor.exceptions.UnresolvedVariableException;
import executor.model.State;
import executor.model.Variable;

public class SetInstruction implements Instruction {
    private final State state;

    SetInstruction(State state) {
        this.state = state;
    }

    @Override
    public void execute(String... params) throws UnresolvedVariableException {
        Variable var = state.findVariableByName(params[0]);
        if (var == null)
            state.addVariable(new Variable(params[0], Integer.parseInt(params[1])));
        else
            state.setVariable(var, Integer.parseInt(params[1]));
        //state.getExecutor().setExecutePos(state.getExecutor().getExecutePos() + 1);
    }
}
