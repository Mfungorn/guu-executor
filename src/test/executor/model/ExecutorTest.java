package test.executor.model;

import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;
import executor.model.Code;
import executor.model.Executor;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutorTest {

    @Test
    public void handle() {
        Executor executor = new Executor(Code.getSample());
        try {
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
        } catch (UnresolvedVariableException | UnresolvedMethodException e) {
            e.printStackTrace();
        }
    }
}