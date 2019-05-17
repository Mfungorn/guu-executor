package executor.exceptions;

public class UnresolvedMethodException extends NoSuchMethodException {
    private String message;
    public UnresolvedMethodException(String methodName) {
        this.message = methodName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
