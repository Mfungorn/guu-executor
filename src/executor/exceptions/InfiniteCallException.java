package executor.exceptions;

public class InfiniteCallException extends Exception {
    private final String message;
    public InfiniteCallException(String methodName) {
        this.message = methodName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
