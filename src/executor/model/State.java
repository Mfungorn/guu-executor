package executor.model;

import executor.controllers.Observer;
import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;

import java.util.*;

public class State implements Observable {
    private int currentPos;
    private Stack<Method> methodStack;
    private List<Variable> variables;
    private List<Method> methods;
    private String result;

    private Set<Observer> observers;
    private Executor executor;

    public State(Executor executor) {
        this.methodStack = new Stack<>();
        this.variables = new LinkedList<>();
        this.methods = new LinkedList<>();
        this.observers = new HashSet<>();
        this.executor = executor;
    }

    public void addVariable(Variable variable) throws UnresolvedVariableException {
        if (variable != null) {
            variables.add(variable);
            notifyHasChanges(variables);
        } else throw new UnresolvedVariableException("Cannot add null variable");
    }

    public void setVariable(Variable variable, int value) throws UnresolvedVariableException {
        if (variable != null) {
            variables.get(variables.indexOf(variable)).setValue(value);
            notifyHasChanges(variables);
        } else throw new UnresolvedVariableException("Cannot set null variable");
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

    public void pushMethod(Method method) throws UnresolvedMethodException {
        if (method != null) {
            methodStack.push(method);
            notifyHasChanges(methodStack);
        } else throw new UnresolvedMethodException("Cannot push null method");
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

    public Executor getExecutor() {
        return executor;
    }

    public String getResult() {
        return result;
    }

    public void addToResult(String result) {
        this.result = this.result + "\n" + result;
        notifyHasChanges(result);
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
