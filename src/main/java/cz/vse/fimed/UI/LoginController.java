package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController implements IController {

    @FXML public AnchorPane anchorPaneLogin;
    @FXML public TextField emailLogin;
    @FXML public PasswordField passwordLogin;

    @FXML
    public void initialize() {

    }
    /**
     * Metoda přepiná na okenko registrace.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void changeStageToRegistration(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPaneLogin.getChildren().clear();
            anchorPaneLogin.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Metoda ověří, zda uživatel je registrovaný a pokud ano, přepiná na příslušný profil.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void loginUser(MouseEvent mouseEvent) throws SQLException, IOException {
        User loggedUser = JDBCDao.checkUserLoginData(emailLogin.getText(), passwordLogin.getText());
        if (loggedUser instanceof Pacient) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientForm.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                anchorPaneLogin.getChildren().clear();
                anchorPaneLogin.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (loggedUser instanceof Doctor) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorProfile.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                anchorPaneLogin.getChildren().clear();
                anchorPaneLogin.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
    } else {
            //Pokud uživtel není registrovaný, vypiše se táto zpráva://
            String warnText = "Sorry, we have don't have such a user.\nPossible reasons: wrong email or password.";
            WarningController warn = new WarningController(warnText, anchorPaneLogin.getScene().getWindow());
        }
    }
}
