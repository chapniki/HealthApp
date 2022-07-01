package cz.vse.fimed.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class WarningController implements IController {

    @FXML public TextArea warningText;
    @FXML public AnchorPane warningWindow;
    @FXML public Button closeButton;
    private static String warnText;

    public WarningController(String text, Window ownerWindow) throws IOException {
        warnText = text;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(WarningController.class.getResource("warning.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ownerWindow);
        stage.setScene(scene);
        stage.setTitle("Warning!");
        stage.show();
    }

    public WarningController() {}

    @FXML
    public void initialize() throws IOException {
        init();
    }

    /**
     * Metoda sloužící pro zavření okna sloužícího pro upozornění
     * o špatném chování uživatele.
     *
     * @param event kliknutí na tlačítko
     */
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Metoda, která vrátí upozornění o příslušné chybové hlášce.
     */
    private void init() {
        warningText.setText(warnText);
    }
}
