package executor.model;

import java.util.*;

public class State {
    private int currentIndex;
    private Stack<Method> methodStack;
    private Set<Variable> variables;
    private List<Method> methods;

    public State() {
        this.methodStack = new Stack<>();
        this.variables = new HashSet<>();
        this.methods = new LinkedList<>();
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public Method findMethodByName(String name) {
        return methods.stream()
                .filter(method -> (method.getName().equals(name)))
                .findFirst()
                .orElseThrow();
    }

    public Variable findVariableByName(String name) {
        return variables.stream()
                .filter(variable -> variable.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public void pushMethod(Method method) {
        methodStack.push(method);
    }

    public void popMethod() {
        try {
            if (!methodStack.peek().getName().equals("main"))
                methodStack.pop();
        } catch (EmptyStackException e) {
            // do nothing
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }
}
