package executor.model;

import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;

import java.util.*;

public class State {
    private int currentPos;
    private Stack<Method> methodStack;
    private Set<Variable> variables;
    private List<Method> methods;
    private boolean isFinish;

    public State() {
        this.methodStack = new Stack<>();
        this.variables = new HashSet<>();
        this.methods = new LinkedList<>();
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public Method findMethodByName(String name) throws UnresolvedMethodException {
        return methods.stream()
                .filter(method -> (method.getName().equals(name)))
                .findFirst()
                .orElseThrow(UnresolvedMethodException::new);
    }

    public Variable findVariableByName(String name) throws UnresolvedVariableException {
        return variables.stream()
                .filter(variable -> variable.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void pushMethod(Method method) {
        methodStack.push(method);
    }

    public Method popMethod() {
        try {
            return methodStack.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public Method peekMethod() {
        try {
            return methodStack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public Stack<Method> getMethodStack() {
        return methodStack;
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int index) {
        this.currentPos = index;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
