package executor.model;

import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;

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
    private int callPos = -1;

    private List<String> instructions;
    private State state = new State();

    public State getState() {
        return state;
    }

    public Executor(String program) {
        String instruction;
        //instructions = new LinkedList<>(Arrays.asList(program.split("\n")));
        instructions = Stream.of(program.split("\n")).map(String::trim).collect(Collectors.toList());
        for (int i = 0; i < instructions.size(); i++) {
            instruction = instructions.get(i);
            if (instruction.startsWith(SUB_TOKEN))
                state.getMethods().add(
                        new Method(i, instruction.substring(instruction.lastIndexOf(INSTRUCTION_DELIMITER) + 1))
                );
        }
    }

    public void handle(boolean isStepInto) throws UnresolvedVariableException, UnresolvedMethodException {
        if (executePos < instructions.size()) {
            String instruction = instructions.get(executePos);

            if (instruction.startsWith(SUB_TOKEN)) {
                execute(instruction);
                if (isStepInto) {
                    state.setCurrentPos(executePos);
                } else {
                    while (!state.getMethodStack().empty()) {
                        instruction = instructions.get(executePos);
                        execute(instruction);
                        if (instruction.startsWith(CALL_TOKEN)) {
                            state.setCurrentPos(executePos);
                            break;
                        }
                        if (executePos >= instructions.size() || instructions.get(executePos).startsWith(SUB_TOKEN)) {
                            state.popMethod();
                            if (state.peekMethod() != null) {
                                executePos = state.peekMethod().getPrevPos() + 1;
                            }
                        }
                    }
                }
            } else {
                //instruction = instructions.get(executePos);
                execute(instruction);
                if (instruction.startsWith(CALL_TOKEN)) {
                    state.setCurrentPos(executePos);
                    return;
                }
                if (executePos >= instructions.size() || instructions.get(executePos).startsWith(SUB_TOKEN)) {
                    state.popMethod();
                    if (state.peekMethod() != null) {
                        executePos = state.peekMethod().getPrevPos() + 1;
                    } else return;
                }
                state.setCurrentPos(executePos);
            }
        }
    }

    private void execute(final String instruction) throws UnresolvedVariableException, UnresolvedMethodException {
        String[] tokens = instruction.split(INSTRUCTION_DELIMITER);
        Variable var;
        Method method;
        switch (tokens[0]) {
            case SET_TOKEN:
                var = state.findVariableByName(tokens[1]);
                if (var == null)
                    state.addVariable(new Variable(tokens[1], Integer.parseInt(tokens[2])));
                else
                    var.setValue(Integer.parseInt(tokens[2]));
                executePos++;
                break;
            case PRINT_TOKEN:
                var = state.findVariableByName(tokens[1]);
                System.out.println(var);
                executePos++;
                break;
            case SUB_TOKEN:
                method = state.findMethodByName(tokens[1]);
                state.pushMethod(method);
                executePos++;
                break;
            case CALL_TOKEN:
                //callPos = executePos;
                if (state.peekMethod() != null) {
                    state.peekMethod().setPrevPos(executePos);
                }
                method = state.findMethodByName(tokens[1]);
                executePos = method.getPos();
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
