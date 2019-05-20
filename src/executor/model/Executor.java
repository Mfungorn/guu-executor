package executor.model;

import executor.exceptions.InfiniteCallException;
import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;
import executor.model.instructions.InstructionBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Executor {
    private final String INSTRUCTION_DELIMITER = " ";
    private final String SUB_TOKEN = "sub";
    private final String CALL_TOKEN = "call";
    private final String PRINT_TOKEN = "print";
    private final String SET_TOKEN = "set";

    private int executePos;

    private List<String> instructions;
    private String instruction;
    private final State state;

    public State getState() {
        return state;
    }

    public Executor(String program) {
        state = new State(this);
        String instruction;
        instructions = Stream.of(program.split("\n"))
                .map(String::trim)
                .collect(Collectors.toList());
        if (instructions.isEmpty() || program.isEmpty()) throw new IllegalArgumentException("Empty program");
        for (int i = 0; i < instructions.size(); i++) {
            instruction = instructions.get(i);
            if (instruction.startsWith(SUB_TOKEN))
                state.getMethods()
                        .add(new Method(i, instruction.substring(instruction.lastIndexOf(INSTRUCTION_DELIMITER) + 1))
                );
        }
        state.setCurrentPos(executePos);
    }

    public void handle(boolean isStepInto) throws UnresolvedVariableException, UnresolvedMethodException,
            UnsupportedOperationException, InfiniteCallException {

        if (isStepInto) {
            instruction = instructions.get(executePos);
            execute(instruction);
            System.out.println(instruction);
            if (!instruction.startsWith(CALL_TOKEN)) {
                executePos++;
                if (executePos >= instructions.size() || instructions.get(executePos).startsWith(SUB_TOKEN)) {
                    state.popMethod();
                    if (state.peekMethod() != null)
                        executePos = state.peekMethod().getCallPos() + 1;
                }
            }

            state.setCurrentPos(executePos);
        } else {
            if (state.peekMethod() == null)
                executeOver(state.getCurrentPos(), state.getMethods().get(0));
            else
                executeOver(state.getCurrentPos(), state.peekMethod());
        }
    }

    private void executeOver(int startPos, Method context) throws UnresolvedVariableException,
            UnresolvedMethodException, InfiniteCallException {
        //String instruction;
        executePos = startPos;
        do {
            instruction = instructions.get(executePos);
            execute(instruction);
            System.out.println(instruction);
            if (context.getCallPos() != 0 && instruction.startsWith(CALL_TOKEN)) {
                instruction = instructions.get(executePos);
                execute(instruction);
                System.out.println(instruction);
                executeOver(executePos + 1, state.peekMethod());
                executePos = context.getCallPos() + 1;
            } else {
                executePos++;
            }
        } while (executePos < instructions.size() && !instructions.get(executePos).startsWith(SUB_TOKEN));
        state.popMethod();
    }

    private void execute(final String instruction) throws UnresolvedVariableException, UnresolvedMethodException,
            InfiniteCallException {
        String[] tokens = instruction.split(INSTRUCTION_DELIMITER);
        switch (tokens[0]) {
            case SET_TOKEN:
                InstructionBuilder.buildSetInstruction(state).execute(tokens[1], tokens[2]);
                break;
            case PRINT_TOKEN:
                InstructionBuilder.buildPrintInstruction(state).execute(tokens[1]);
                break;
            case SUB_TOKEN:
                InstructionBuilder.buildSubInstruction(state).execute(tokens[1]);
                break;
            case CALL_TOKEN:
                InstructionBuilder.buildCallInstruction(state).execute(tokens[1]);
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public int getExecutePos() {
        return executePos;
    }

    public void setExecutePos(int executePos) {
        this.executePos = executePos;
    }
}
