package executor.model.instructions;

import executor.exceptions.InfiniteCallException;
import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;

public interface Instruction {
    void execute(String... params) throws UnresolvedMethodException, UnresolvedVariableException, InfiniteCallException;
}
