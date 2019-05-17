package executor.controllers;

import executor.model.Method;
import executor.model.Variable;

import java.util.List;
import java.util.Stack;

public interface Observer {
    void update(String results);
    void update(List<Variable> variables);
    void update(Stack<Method> methodStack);
}
