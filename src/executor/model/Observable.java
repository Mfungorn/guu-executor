package executor.model;

import executor.controllers.Observer;

import java.util.List;
import java.util.Stack;

public interface Observable {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void notifyHasChanges(Stack<Method> methodStackChanged);
    void notifyHasChanges(List<Variable> variablesChanged);
    void notifyHasChanges(String resultsChanges);
}
