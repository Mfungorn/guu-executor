package executor.model.instructions;

import executor.exceptions.InfiniteCallException;
import executor.exceptions.UnresolvedMethodException;
import executor.model.Method;
import executor.model.State;

public class CallInstruction implements Instruction {
    private final State state;

    CallInstruction(State state) {
        this.state = state;
    }

    @Override
    public void execute(String... params) throws UnresolvedMethodException, InfiniteCallException {
        if (state.peekMethod() != null) {
            state.peekMethod().setCallPos(state.getExecutor().getExecutePos());
            Method method = state.findMethodByName(params[0]);
            if (state.peekMethod().equals(method)) throw new InfiniteCallException(params[0] + " loop call");
            method.setContext(state.peekMethod());
            state.getExecutor().setExecutePos(method.getPos());
        }
    }
}
