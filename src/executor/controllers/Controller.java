package executor.controllers;

import executor.exceptions.InfiniteCallException;
import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;
import executor.model.Executor;
import executor.model.Method;
import executor.model.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller implements Observer {
    private Executor executor;
    private String program;
    private ObservableList<String> lines;
    private FileChooser fileChooser;

    @FXML private VBox container;
    @FXML private TextArea outputArea;
    @FXML private Button startButton, stopButton, stepOverButton, stepIntoButton;
    @FXML private ListView<String> inputLines, methodStack, variablesList;
    @FXML private MenuItem openFileMenuItem, closeFileMenuItem, clearMenuItem, aboutHelpMenuItem;

    @FXML
    public void initialize() {
        fileChooser = new FileChooser();
        openFileMenuItem.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(container.getScene().getWindow());
            if (file != null) {
                readFile(file);
            }
        });

        clearMenuItem.setOnAction(e -> clearInput());
        closeFileMenuItem.setOnAction(e -> clearInput());

        initInput();
        disableButtons(true);

        startButton.setOnMouseClicked(e -> {
            try {
                clearResults();
                executor = new Executor(program);
                executor.getState().addObserver(this);
                inputLines.scrollTo(executor.getState().getCurrentPos());
                inputLines.getSelectionModel().select(executor.getState().getCurrentPos());
                disableButtons(false);
            } catch (Exception ex) {
                showErrorDialog("Internal program exception", ex.getMessage());
                clearResults();
            }
        });

        stopButton.setOnMouseClicked(e -> {
            executor.getState().deleteObserver(this);
            executor = null;
            disableButtons(true);
        });

        stepIntoButton.setOnMouseClicked(getStepButtonHandler(true));
        stepOverButton.setOnMouseClicked(getStepButtonHandler(false));
    }

    private void initInput() {
        inputLines.setEditable(true);
        inputLines.setCellFactory(TextFieldListCell.forListView());
        inputLines.setOnMouseClicked(event -> {
            int idx = inputLines.getSelectionModel().getSelectedIndex();
            if (idx == -1) {
                inputLines.getItems().add("");
                inputLines.getSelectionModel().select(inputLines.getItems().indexOf(""));
            }
            inputLines.edit(idx);
        });
        inputLines.setOnEditCommit(t -> {
            inputLines.getItems().set(t.getIndex(), t.getNewValue());
            if (inputLines.getItems().get(t.getIndex()).isEmpty())
                inputLines.getItems().remove(t.getIndex());
            if (!inputLines.getItems().get(t.getIndex()).contains("\n"))
                inputLines.getItems().set(t.getIndex(), t.getNewValue() + "\n");
            inputLines.getSelectionModel().clearSelection();
            StringBuilder stringBuilder = new StringBuilder();
            inputLines.getItems().forEach(stringBuilder::append);
            program = stringBuilder.toString();
        });
    }

    private void readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
            this.lines = FXCollections.observableList(lines.map(l -> l + "\n").collect(Collectors.toList()));
            this.lines.forEach(stringBuilder::append);
            program = stringBuilder.toString();
            inputLines.getItems().clear();
            inputLines.setItems(this.lines);
        } catch (IOException ex) {
            showErrorDialog("I/O Exception", "Cannot read the file " + file.getAbsolutePath());
            ex.printStackTrace();
        }
    }

    private EventHandler<MouseEvent> getStepButtonHandler(boolean isStepInto) {
        return event -> {
            try {
                executor.handle(isStepInto);
                inputLines.scrollTo(executor.getState().getCurrentPos());
                inputLines.getSelectionModel().select(executor.getState().getCurrentPos());
            } catch (UnresolvedVariableException ex) {
                showErrorDialog("Runtime Error", "Uninitialized variable: " + ex.getMessage());
            } catch (UnresolvedMethodException ex) {
                showErrorDialog("Runtime Error", "Unresolved method call: " + ex.getMessage());
            } catch (UnsupportedOperationException ex) {
                showErrorDialog("Runtime Error", "Unsupported instruction: " + ex.getMessage());
            } catch (InfiniteCallException ex) {
                showErrorDialog("Runtime Error", "Infinite " + ex.getMessage());
            } catch (Exception ex) {
                showErrorDialog("Internal program exception", ex.getMessage());
            }
            if (executor.getState().isFinish()) {
                disableButtons(true);
                inputLines.getSelectionModel().clearSelection();
            }
        };
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void disableButtons(boolean isDisable) {
        startButton.setDisable(!isDisable);
        stopButton.setDisable(isDisable);
        stepIntoButton.setDisable(isDisable);
        stepOverButton.setDisable(isDisable);
    }

    private void clearInput() {
        inputLines.getItems().clear();
        methodStack.getItems().clear();
        variablesList.getItems().clear();
        outputArea.clear();
    }

    private void clearResults() {
        methodStack.getItems().clear();
        variablesList.getItems().clear();
        outputArea.clear();
    }

    @Override
    public void update(String results) {
        outputArea.appendText(results + "\n");
    }

    @Override
    public void update(List<Variable> variables) {
            variablesList.getItems().clear();
            variablesList.getItems().addAll(variables.stream()
                    .map(Variable::toString)
                    .collect(Collectors.toList())
            );
    }

    @Override
    public void update(Stack<Method> methodStack) {
        this.methodStack.getItems().clear();
        this.methodStack.getItems().addAll(methodStack.stream()
                .map(Method::toString)
                .collect(Collectors.toList())
        );
    }
}
