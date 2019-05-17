package executor.model;

public class Method {
    private int pos;
    private int prevPos;
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

    public int getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(int prevPos) {
        this.prevPos = prevPos;
    }

    @Override
    public String toString() {
        return name;
    }
}
