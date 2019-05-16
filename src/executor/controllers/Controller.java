package executor.controllers;

import executor.exceptions.UnresolvedMethodException;
import executor.exceptions.UnresolvedVariableException;
import executor.model.Executor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private Executor executor;
    private String program;
    private ObservableList<String> lines;
    private FileChooser fileChooser;

    private Alert alert;
    @FXML private VBox container;
    @FXML private TextArea outputArea;
    @FXML private Button startButton, stopButton, stepOverButton, stepIntoButton;
    @FXML private ListView inputLines, methodStack, variablesList;
    @FXML private MenuItem openFileMenuItem, closeFileMenuItem, aboutHelpMenuItem;

    @FXML
    public void initialize() {
        fileChooser = new FileChooser();
        openFileMenuItem.setOnAction(e -> {
            inputLines.getItems().clear();
            StringBuffer stringBuffer = new StringBuffer();
            File file = fileChooser.showOpenDialog(container.getScene().getWindow());
            if (file != null) {
                try (Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
                    this.lines = FXCollections.observableList(lines.map(l -> l + "\n").collect(Collectors.toList()));
                    this.lines.forEach(stringBuffer::append);
                    program = stringBuffer.toString();
                    inputLines.setItems(this.lines);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        closeFileMenuItem.setOnAction(e -> inputLines.getItems().clear());

        stopButton.setDisable(true);
        stepIntoButton.setDisable(true);
        stepOverButton.setDisable(true);

        startButton.setOnMouseClicked(e -> {
            executor = new Executor(program);
            inputLines.scrollTo(executor.getState().getCurrentPos());
            inputLines.getSelectionModel().select(executor.getState().getCurrentPos());
            startButton.setDisable(true);
            stopButton.setDisable(false);
            stepIntoButton.setDisable(false);
            stepOverButton.setDisable(false);
        });

        stopButton.setOnMouseClicked(e -> {
            executor = null;
            startButton.setDisable(false);
            stopButton.setDisable(true);
            stepIntoButton.setDisable(true);
            stepOverButton.setDisable(true);
        });

        stepIntoButton.setOnMouseClicked(e -> {
            try {
                executor.handle(true);
                inputLines.scrollTo(executor.getState().getCurrentPos());
                inputLines.getSelectionModel().select(executor.getState().getCurrentPos());
            } catch (UnresolvedVariableException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Uninitialized variable!");

                alert.showAndWait();
            } catch (UnresolvedMethodException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unresolved method call!");

                alert.showAndWait();
            }

            //methodStack.getItems().add(0, );
        });

        stepOverButton.setOnMouseClicked(e -> {
            try {
                executor.handle(false);
                inputLines.scrollTo(executor.getState().getCurrentPos());
                inputLines.getSelectionModel().select(executor.getState().getCurrentPos());
            } catch (UnresolvedVariableException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Uninitialized variable!");

                alert.showAndWait();
            } catch (UnresolvedMethodException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unresolved method call!");

                alert.showAndWait();
            }

        });
    }

}
