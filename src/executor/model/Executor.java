package executor.model;

public class Executor {
    private final String INSTRUCTION_DELIMITER = " ";
    private final String SUB_TOKEN = "sub";
    private final String CALL_TOKEN = "call";
    private final String PRINT_TOKEN = "print";
    private final String SET_TOKEN = "set";

    private State state;

    public Executor(State state) {
        this.state = state;
    }

    public void execute(final String instruction) {
        String[] tokens = instruction.split(INSTRUCTION_DELIMITER);
        switch (tokens[0]) {
            case SET_TOKEN:
                Variable var = state.findVariableByName(tokens[1]);
                if (var == null)
                    state.addVariable(new Variable(tokens[1], Integer.parseInt(tokens[2])));
                else
                    var.setValue(Integer.parseInt(tokens[2]));
                break;
            case PRINT_TOKEN:
                System.out.println(state.findVariableByName(tokens[1]));
                break;
            case SUB_TOKEN:
                state.popMethod();
                break;
            case CALL_TOKEN:
                state.pushMethod(state.findMethodByName(tokens[1]));
                break;
        }
    }
}
