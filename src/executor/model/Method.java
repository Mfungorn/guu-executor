package executor.model;

public class Method {
    private int pos;
    private String name;

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
}
