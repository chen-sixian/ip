package wwaffle.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Displays one user or WWaffle message with a compact speaker avatar.
 */
public class DialogBox extends HBox {
    @FXML
    private Label avatar;
    @FXML
    private Label speaker;
    @FXML
    private Label dialog;
    @FXML
    private VBox bubble;

    private DialogBox(String text) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load a dialog box.", e);
        }
        dialog.setText(text);
    }

    /**
     * Creates a right-aligned message written by the user.
     *
     * @param text User's command.
     * @return User dialog box.
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text);
        dialogBox.speaker.setText("You");
        dialogBox.avatar.setText("YOU");
        dialogBox.avatar.getStyleClass().add("user-avatar");
        dialogBox.bubble.getStyleClass().add("user-bubble");
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Creates a left-aligned message written by WWaffle.
     *
     * @param text WWaffle's response.
     * @return WWaffle dialog box.
     */
    public static DialogBox getWWaffleDialog(String text) {
        DialogBox dialogBox = new DialogBox(text);
        dialogBox.speaker.setText("WWaffle");
        dialogBox.avatar.setText("W");
        dialogBox.avatar.getStyleClass().add("wwaffle-avatar");
        dialogBox.bubble.getStyleClass().add("wwaffle-bubble");
        if (text.startsWith("[!]")) {
            dialogBox.bubble.getStyleClass().add("error-bubble");
        }
        return dialogBox;
    }

    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_RIGHT);
        getStyleClass().add("user-dialog");
    }
}
