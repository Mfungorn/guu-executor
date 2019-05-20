package executor.model.instructions;

import executor.exceptions.UnresolvedMethodException;
import executor.model.Method;
import executor.model.State;

public class SubInstruction implements Instruction {
    private State state;

    SubInstruction(State state) {
        this.state = state;
    }

    @Override
    public void execute(String... params) throws UnresolvedMethodException {
        Method method = state.findMethodByName(params[0]);
        state.pushMethod(method);
        //state.getExecutor().setExecutePos(state.getExecutor().getExecutePos() + 1);
    }
}
