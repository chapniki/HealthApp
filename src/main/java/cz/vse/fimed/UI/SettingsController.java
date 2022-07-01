package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;

public class SettingsController implements IController {

    @FXML public AnchorPane anchorPaneSettings;
    @FXML public AnchorPane anchorPaneDeleteProfile;
    @FXML public TextField passwordField;
    @FXML public TextField passwordConfirmField;

    @FXML
    public void initialize() throws SQLException {
        init();
    }

    private void init() throws SQLException {

    }

    /**
     * Metoda sloužící pro přechod z obrazovky profilu uživatele
     * do nastavení jeho profilu.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToProfile(MouseEvent mouseEvent) {
        if(JDBCDao.patient_id != -1) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientForm.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                anchorPaneSettings.getChildren().clear();
                anchorPaneSettings.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(JDBCDao.doctor_id != -1) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorProfile.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                anchorPaneSettings.getChildren().clear();
                anchorPaneSettings.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda sloužící pro změnu hesla uživatele.
     * Kontroluje aby uživatel uvaděl jen platná hesla.
     * Jinak vypíše chybovou hlášku.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void changePassword(MouseEvent mouseEvent) throws IOException, SQLException {
        if(passwordField.getText().length() >= 8 &&
                passwordField.getText().equals(passwordConfirmField.getText())) {
            if(JDBCDao.patient_id != -1) {
            JDBCDao.changePassword(JDBCDao.patient_id, passwordField.getText());
            } else if(JDBCDao.doctor_id != -1) {
                JDBCDao.changePassword(JDBCDao.doctor_id, passwordField.getText());
            }
            goToProfile(mouseEvent);
        } else {
            String validationString = "Passwords should be identical and consist of at least 8 characters.";
            WarningController warn = new WarningController(validationString, anchorPaneSettings.getScene().getWindow());
        }
    }

    /**
     * Metoda sloužící pro smazání profilu uživatele.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void deleteProfile(MouseEvent mouseEvent) throws SQLException {
        if(JDBCDao.patient_id != -1) {
            JDBCDao.deleteUser(JDBCDao.patient_id);
        } else if(JDBCDao.doctor_id != -1) {
            JDBCDao.deleteUser(JDBCDao.doctor_id);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPaneSettings.getChildren().clear();
            anchorPaneSettings.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
