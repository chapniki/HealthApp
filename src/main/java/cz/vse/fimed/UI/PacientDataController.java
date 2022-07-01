package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PacientDataController implements IController {

    @FXML public Label patientName;
    @FXML public Label patientSurname;
    @FXML public Label patientAge;
    @FXML public Label patientWeight;
    @FXML public Label patientFullNameRightCorner;
    @FXML public Label patientHeight;
    @FXML public Label patientGender;
    @FXML public ListView<String> patientSymptoms;
    @FXML public Label patientAddress;
    @FXML public Label patientEmail;
    @FXML public Label patientTelephone;
    @FXML public AnchorPane anchorPanePatientForm;

    /**
     * Metoda vypiše informace o pacientovi na profilu.
     *
     */
    private void init() throws SQLException {
        if(JDBCDao.patient_id != -1) {
            Pacient patient = (Pacient) JDBCDao.getUserInfo(JDBCDao.patient_id);
            patientName.setText(patient.getName());
            patientSurname.setText(patient.getSurname());
            patientAge.setText(""+patient.getAge()+"");
            patientWeight.setText(""+patient.getWeight()+" kg");
            patientHeight.setText(""+patient.getHeight()+" cm");
            patientGender.setText(patient.getGender());
            //Vypišou se symptomy pacienta na profilu //
            ArrayList<Symptom> syms = JDBCDao.getSymthoms(JDBCDao.patient_id);
            ArrayList<String> symsString = (ArrayList<String>) syms.stream().map(Symptom::getSymptomName)
                    .collect(Collectors.toList());
            ObservableList<String> obsStrings = FXCollections.observableList(symsString);

            patientSymptoms.setItems(obsStrings);
            patientAddress.setText(patient.getAddress());
            patientEmail.setText(patient.getEmail());
            patientTelephone.setText(patient.getPhone());
            patientFullNameRightCorner.setText(patient.getName() + " " + patient.getSurname());
        }
    }

    /**
     * Metoda přepiná na okenko pro editaci údajú pacientů.
     *
     */

    @FXML
    public void initialize() throws SQLException {
        init();
    }

    /**
     * Metoda edituje údaje uživatele - pacienta.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    public void editPatientData(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editPatient.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPanePatientForm.getChildren().clear();
            anchorPanePatientForm.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Metoda se umožnuje pacientovi odhlásit ze svého profilu.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void logoutUser(MouseEvent mouseEvent) {
        JDBCDao.patient_id = -1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPanePatientForm.getChildren().clear();
            anchorPanePatientForm.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda umožnuje pacientovi otevřit panel Nastávení.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToSettings(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPanePatientForm.getChildren().clear();
            anchorPanePatientForm.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda otevírá seznam lékařů.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToListOfDoctors(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorList.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            anchorPanePatientForm.getChildren().clear();
            anchorPanePatientForm.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
