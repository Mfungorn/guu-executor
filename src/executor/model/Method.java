package executor.model;

import java.util.Objects;

public class Method {
    private final int pos;
    private int callPos;
    private final String name;
    private Method context;

    public Method(int pos, String name) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }
    public int getPos() {
        return pos;
    }

    public int getCallPos() {
        return callPos;
    }

    public void setCallPos(int callPos) {
        this.callPos = callPos;
    }

    public Method getContext() {
        return context;
    }

    public void setContext(Method context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  Method)
            return pos == ((Method) obj).pos && Objects.equals(getName(), ((Method) obj).getName());
        return false;
    }
}
