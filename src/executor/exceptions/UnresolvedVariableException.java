package executor.exceptions;

public class UnresolvedVariableException extends Exception {
    private final String message;

    public UnresolvedVariableException(String varName) {
        this.message = varName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
