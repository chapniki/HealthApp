package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProfileController implements IController {


    @FXML public Label doctorName;
    @FXML public Label doctorSurname;
    @FXML public Label doctorAge;
    @FXML public Label doctorGender;
    @FXML public Label doctorExperience;
    @FXML public Label doctorSpecialization;
    @FXML public Label doctorEducation;
    @FXML public Label doctorFullnameRightCorner;
    @FXML public Label doctorHospital;
    @FXML public Label doctorAddress;
    @FXML public Label doctorEmail;
    @FXML public Label doctorTelephone;

    @FXML public AnchorPane doctorProfileAnchorPane;

    /**
     * Metoda sloužící pro inicializaci nového uživatele.
     */
    private void init() throws SQLException {
        if(JDBCDao.doctor_id != -1) {
            Doctor doctor = (Doctor) JDBCDao.getUserInfo(JDBCDao.doctor_id);
            doctorName.setText(doctor.getName());
            doctorSurname.setText(doctor.getSurname());
            doctorFullnameRightCorner.setText(doctor.getName() + " " + doctor.getSurname());
            doctorAge.setText(""+doctor.getAge()+"");
            doctorGender.setText(""+doctor.getGender()+"");
            doctorExperience.setText(""+doctor.getExperience()+" let");
            doctorSpecialization.setText(doctor.getSpecialization());
            doctorEducation.setText(doctor.getEducation());
            doctorHospital.setText(doctor.getHospital());
            doctorAddress.setText(doctor.getAddress());
            doctorEmail.setText(doctor.getEmail());
            doctorTelephone.setText(doctor.getPhone());
        }
    }

    @FXML
    public void initialize() throws SQLException {
        init();
    }

    /**
     * Metoda sloužící pro editaci údajů o doktorovi.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    public void editDoctorData(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editDoctor.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            doctorProfileAnchorPane.getChildren().clear();
            doctorProfileAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda sloužící pro odhlášení z profilu doktora.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void logoutDoctor(MouseEvent mouseEvent) {
        JDBCDao.doctor_id = -1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            doctorProfileAnchorPane.getChildren().clear();
            doctorProfileAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda sloužící pro přechod do nastavení profilu doktora.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToSettings(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            doctorProfileAnchorPane.getChildren().clear();
            doctorProfileAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda sloužící pro přechod na obrazovku se seznamem pacientů.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToListOfPatients(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientList.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            doctorProfileAnchorPane.getChildren().clear();
            doctorProfileAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
