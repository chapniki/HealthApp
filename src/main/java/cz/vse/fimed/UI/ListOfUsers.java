package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListOfUsers implements IController {

    // UI
    @FXML public AnchorPane doctorListAnchorPane;
    @FXML public AnchorPane patientListAnchorPane;
    @FXML public ListView doctorList;
    @FXML public ListView patientsList;

    // Patient
    @FXML public TextField selectedPatientAddress;
    @FXML public TextField selectedPatientEmail;
    @FXML public TextField selectedPatientTelephone;
    @FXML public TextField selectedPatientSymptoms;

    // Doctor
    @FXML public TextField selectedDoctorEmail;
    @FXML public TextField selectedDoctorTelephone;
    @FXML public TextField selectedDoctorHospital;
    @FXML public TextField selectedDoctorAddress;
    @FXML public Label alreadyADoctor;
    @FXML public Button chooseDoctor;

    /**
     * Metoda zobrazí seznam lékařů.
     *
     */
    @FXML
    public void initialize() throws SQLException {
        init();
    }
    private void init() throws SQLException {
        if(JDBCDao.patient_id != -1) {
            chooseDoctor.setVisible(false);
            alreadyADoctor.setVisible(false);

            ArrayList<Doctor> docs = JDBCDao.getDoctors();

            doctorList.getItems().addAll(docs);

            doctorList.setCellFactory(param -> new ListCell<Doctor>() {
                @Override
                protected void updateItem(Doctor item, boolean empty) {
                    super.updateItem(item, empty);
            //Pokud seznam není prázdní zobrazí informace o konkretním lekaři//
                    if (!empty) {
                        setText(item.getEducation() + " " + item.getName() + " " + item.getSurname() + "\n" + item.getSpecialization() + " " +
                                item.getExperience() + " let praxe");
                    } else {
                    }
                }
            });//Zobrazí seznam pacientů, pokud uživatel je příhlášený jako lékař//
        } else if (JDBCDao.doctor_id != -1) {
            ArrayList<Pacient> patients = JDBCDao.getPatients();

            patientsList.getItems().addAll(patients);

            patientsList.setCellFactory(param -> new ListCell<Pacient>() {
                @Override
                protected void updateItem(Pacient item, boolean empty) {
                    super.updateItem(item, empty);
            //Pokud seznam není prazdný zobrazí údaje lékaře//
                    if (!empty) {
                        setText(item.getName() + " " + item.getSurname());
                    } else {
                    }
                }
            });

        }
    }
    /**
     * Metoda přepiná na profil lékaře, pokud uživatel je registrováný jako lékař.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToDoctorProfile(MouseEvent mouseEvent) {
        if(JDBCDao.doctor_id != -1) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorProfile.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                patientListAnchorPane.getChildren().clear();
                patientListAnchorPane.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Metoda přepiná na profil pacienta, pokud uživatel je registrováný jako pacient.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void goToPatientProfile(MouseEvent mouseEvent) {
        if(JDBCDao.patient_id != -1) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientForm.fxml"));
            Pane registrationPane = null;
            try {
                registrationPane = (Pane) fxmlLoader.load();
                doctorListAnchorPane.getChildren().clear();
                doctorListAnchorPane.getChildren().setAll(registrationPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Metoda zobrazí seznam pacientůa umožnuje si nahlédnout na údaje konkretního pacienta.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void handleMouseClickedOnPatientInList(MouseEvent mouseEvent) {
        Pacient selectedPatient = (Pacient) patientsList.getSelectionModel().getSelectedItem();
        selectedPatientAddress.setText(selectedPatient.getAddress());
        selectedPatientEmail.setText(selectedPatient.getEmail());
        selectedPatientTelephone.setText(selectedPatient.getPhone());
        selectedPatientSymptoms.setText(selectedPatient.getSymptoms().stream().map(Symptom::getSymptomName).collect(Collectors.joining(",")));
    }
    /**
     * Metoda zobrazí seznam lékařů a umožnuje si nahlédnout na údaje konkretního lékaře.
     *</p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void handleMouseClickedOnDoctorInList(MouseEvent mouseEvent) throws SQLException {
        Doctor selectedDoctor = (Doctor) doctorList.getSelectionModel().getSelectedItem();
        if(!JDBCDao.checkAppointment(selectedDoctor.getEmail())) {
            chooseDoctor.setVisible(true);
            alreadyADoctor.setVisible(false);
        } else {
            chooseDoctor.setVisible(false);
            alreadyADoctor.setVisible(true);
        }
        selectedDoctorEmail.setText(selectedDoctor.getEmail());
        selectedDoctorTelephone.setText(selectedDoctor.getPhone());
        selectedDoctorHospital.setText(selectedDoctor.getHospital());
        selectedDoctorAddress.setText(selectedDoctor.getAddress());
    }
    /**
     * Metoda si umožnuje pacientovi zvolit lékaře.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void handleMouseClickedOnChooseDoctorButton(MouseEvent mouseEvent) throws SQLException {
        JDBCDao.makeAppointment(selectedDoctorEmail.getText());
    }
}
