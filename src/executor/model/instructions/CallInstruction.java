package executor.model.instructions;

import executor.exceptions.UnresolvedMethodException;
import executor.model.Method;
import executor.model.State;

public class CallInstruction implements Instruction {
    private State state;

    CallInstruction(State state) {
        this.state = state;
    }

    @Override
    public void execute(String... params) throws UnresolvedMethodException {
        if (state.peekMethod() != null) {
            state.peekMethod().setPrevPos(state.getExecutor().getExecutePos());
        }
        Method method = state.findMethodByName(params[0]);
        state.getExecutor().setExecutePos(method.getPos());
    }
}
