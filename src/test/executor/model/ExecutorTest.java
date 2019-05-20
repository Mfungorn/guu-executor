package test.executor.model;

import executor.exceptions.InfiniteCallException;
import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;
import executor.model.Executor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExecutorTest {

    private Executor executor;

    @Test
    public void handle_SetInstruction_VariableAdded() {
        executor = new Executor("sub main\n    set a 1\n");
        try {
            executor.handle(false);
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            e.printStackTrace();
        }
        assertEquals(1, executor.getState().getVariables().size());
        assertEquals("a = 1", executor.getState().getVariables().get(0).toString());
        assertTrue(executor.getState().isFinish());
    }

    @Test
    public void handle_PrintInstruction_ResultsAdded() {
        executor = new Executor("sub main\n    set a 1\n    print a");
        try {
            executor.handle(false);
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            e.printStackTrace();
        }
        assertEquals(1, executor.getState().getVariables().size());
        assertEquals("a = 1\n", executor.getState().getResult());
        assertTrue(executor.getState().isFinish());
    }

    @Test
    public void handle_CallInstruction_MethodPushed() {
        executor = new Executor("sub main\n    call foo\nsub foo\n    set a 1");
        try {
            executor.handle(true);
            executor.handle(true);
            executor.handle(true);
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            e.printStackTrace();
        }
        assertEquals(2, executor.getState().getMethodStack().size());
        assertEquals(Arrays.asList("main", "foo").toString(), executor.getState().getMethodStack().toString());
        assertFalse(executor.getState().isFinish());
    }

    @Test
    public void handle_StepOverSimpleProgram_Finish() {
        executor = new Executor("sub main\n    set a 1\n   call foo\n   print a\nsub foo\n   set a 2\n");
        try {
            executor.handle(false);
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            e.printStackTrace();
        }
        assertEquals("a = 2\n", executor.getState().getResult());
        assertTrue(executor.getState().isFinish());
    }

    @Test
    public void handle_StepIntoSimpleProgram_Finish() {
        executor = new Executor("sub main\nset a 1\ncall foo\nset a 2\nsub foo\nset a 3\ncall bar" +
                "\nset a 4\nsub bar\n set a 5");
        try {
            executor.handle(true);
            assertEquals("main", executor.getState().peekMethod().getName());
            executor.handle(true);
            assertEquals("a = 1", executor.getState().getVariables().get(0).toString());
            executor.handle(true);
            executor.handle(true);
            assertEquals("foo", executor.getState().peekMethod().getName());
            executor.handle(true);
            assertEquals("a = 3", executor.getState().getVariables().get(0).toString());
            executor.handle(true);
            executor.handle(true);
            assertEquals("bar", executor.getState().peekMethod().getName());
            executor.handle(true);
            assertEquals("a = 5", executor.getState().getVariables().get(0).toString());
            executor.handle(true);
            assertEquals("a = 4", executor.getState().getVariables().get(0).toString());
            executor.handle(true);
            assertEquals("a = 2", executor.getState().getVariables().get(0).toString());
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            e.printStackTrace();
        }
        assertTrue(executor.getState().isFinish());
    }

    @Test
    public void handle_SimpleRecursion_NotFinish() {
        executor = new Executor("sub main\n    call main");
        try {
            executor.handle(false);
        } catch (UnresolvedVariableException | UnresolvedMethodException | InfiniteCallException e) {
            assertEquals("main", executor.getState().peekMethod().getName());
            assertEquals("main loop call", e.getMessage());
        }

        assertFalse(executor.getState().isFinish());
    }
}