package executor.model;

import executor.controllers.Observer;
import executor.exceptions.UnresolvedMethodException;

import java.util.*;

public class State implements Observable {
    private int currentPos;
    private Stack<Method> methodStack;
    private List<Variable> variables;
    private List<Method> methods;

    private Set<Observer> observers;

    public State() {
        this.methodStack = new Stack<>();
        this.variables = new LinkedList<>();
        this.methods = new LinkedList<>();
        this.observers = new HashSet<>();
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
        notifyHasChanges(variables);
    }

    public void setVariable(Variable variable, int value) {
        variables.get(variables.indexOf(variable)).setValue(value);
        notifyHasChanges(variables);
    }

    public Method findMethodByName(String name) throws UnresolvedMethodException {
        return methods.stream()
                .filter(method -> (method.getName().equals(name)))
                .findFirst()
                .orElseThrow(() -> new UnresolvedMethodException(name));
    }

    public Variable findVariableByName(String name) {
        return variables.stream()
                .filter(variable -> variable.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void pushMethod(Method method) {
        methodStack.push(method);
        notifyHasChanges(methodStack);
    }

    public Method popMethod() {
        try {
            Method method = methodStack.pop();
            notifyHasChanges(methodStack);
            return method;
        } catch (EmptyStackException e) {
            notifyHasChanges(methodStack);
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

    public List<Variable> getVariables() {
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
        return methodStack.isEmpty();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyHasChanges(Stack<Method> methodStackChanged) {
        observers.forEach(o -> o.update(methodStackChanged));
    }

    @Override
    public void notifyHasChanges(List<Variable> variablesChanged) {
        observers.forEach(o -> o.update(variablesChanged));
    }

    @Override
    public void notifyHasChanges(String resultsChanges) {
        observers.forEach(o -> o.update(resultsChanges));
    }


}
