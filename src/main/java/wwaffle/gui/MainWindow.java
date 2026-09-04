package wwaffle.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import wwaffle.WWaffle;

/**
 * Controls the main WWaffle chat window.
 */
public class MainWindow {
    private static final Duration EXIT_DELAY = Duration.millis(650);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Label taskStatus;

    private WWaffle wwaffle;

    /**
     * Configures automatic scrolling after the FXML controls are injected.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollToLatestMessage());
    }

    /**
     * Supplies the application instance used to process commands.
     *
     * @param wwaffle WWaffle application instance.
     */
    public void setWWaffle(WWaffle wwaffle) {
        this.wwaffle = wwaffle;
        refreshTaskStatus();
        addDialogBoxes(
                DialogBox.getWWaffleDialog("Hello! I'm WWaffle.\n" + wwaffle.getWelcomeMessage()));
    }

    /**
     * Moves keyboard focus to the command field.
     */
    public void focusCommandBox() {
        Platform.runLater(userInput::requestFocus);
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText().strip();
        if (input.isEmpty()) {
            return;
        }

        String response = wwaffle.getResponse(input);
        addDialogBoxes(DialogBox.getUserDialog(input), DialogBox.getWWaffleDialog(response));
        userInput.clear();
        refreshTaskStatus();

        if (input.equals("bye")) {
            userInput.setDisable(true);
            PauseTransition exitPause = new PauseTransition(EXIT_DELAY);
            exitPause.setOnFinished(event -> Platform.exit());
            exitPause.play();
        }
    }

    /**
     * Appends any number of dialog boxes to the conversation in the given order.
     *
     * @param dialogBoxes Dialog boxes to append.
     */
    private void addDialogBoxes(DialogBox... dialogBoxes) {
        for (DialogBox dialogBox : dialogBoxes) {
            dialogContainer.getChildren().add(dialogBox);
        }
    }

    private void refreshTaskStatus() {
        int taskCount = wwaffle.getTaskCount();
        String taskWord = taskCount == 1 ? "TASK" : "TASKS";
        taskStatus.setText(taskCount + " " + taskWord + " READY");
    }

    private void scrollToLatestMessage() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
