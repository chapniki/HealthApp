package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import cz.vse.fimed.profile.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditUserDataController implements IController {
    // Patient
                // Anchor
    @FXML public AnchorPane patientEditAnchorPane;

                // Patient data
    @FXML public TextField editedPatientAge;
    @FXML public TextField editedPatientGender;
    @FXML public TextField editedPatientName;
    @FXML public TextField editedPatientSurname;
    @FXML public TextField editedPatientAddress;
    @FXML public TextField editedPatientEmail;
    @FXML public TextField editedPatientTelephone;
    @FXML public TextField editedPatientHeight;
    @FXML public TextField editedPatientWeight;
    @FXML public Label patientFullNameRightCorner;
    @FXML public TextArea editedPatientSymptoms;

    // Doctor
                // Anchor
    @FXML public AnchorPane editDoctorAnchorPane;

                // Doctor data
    @FXML public Label editedDoctorFullnameRightCorner;
    @FXML public TextField editedDoctorAge;
    @FXML public TextField editedDoctorGender;
    @FXML public TextField editedDoctorName;
    @FXML public TextField editedDoctorSurname;
    @FXML public TextField editedDoctorHospital;
    @FXML public TextField editedDoctorAddress;
    @FXML public TextField editedDoctorEmail;
    @FXML public TextField editedDoctorTelephone;
    @FXML public TextField editedDoctorExperience;
    @FXML public TextField editedDoctorSpecialization;
    @FXML public TextField editedDoctorEducation;


    /**
     * Metoda kontroluje jestli pacient je registrovan??, pak zobraz?? ji?? ulo??enou v databazi informace,
     * kterou se pak d?? editovat.
     *
     *
     */
    @FXML
    public void initialize() throws SQLException {
        init();
    }

    private void init() throws SQLException {
        if(JDBCDao.patient_id != -1) {
            Pacient patient = (Pacient) JDBCDao.getUserInfo(JDBCDao.patient_id);
            patientFullNameRightCorner.setText(patient.getName() + " " + patient.getSurname());
            editedPatientName.setText(patient.getName());
            editedPatientSurname.setText(patient.getSurname());
            editedPatientAge.setText(""+patient.getAge()+"");
            editedPatientWeight.setText(""+patient.getWeight()+"");
            editedPatientHeight.setText(""+patient.getHeight()+"");
            editedPatientGender.setText(patient.getGender());

            ArrayList<Symptom> syms = JDBCDao.getSymthoms(JDBCDao.patient_id);
            ArrayList<String> symsString = (ArrayList<String>) syms.stream().map(Symptom::getSymptomName)
                    .collect(Collectors.toList());
            StringBuffer sb = new StringBuffer();
            int symsSize = symsString.size();
            for (int i = 0; i < symsString.size(); i++) {
                sb.append(symsString.get(i));
                if(i != symsSize-1) {
                    sb.append(", ");
                }
            }

            String str = sb.toString();

            editedPatientSymptoms.setText(str);
            editedPatientAddress.setText(patient.getAddress());
            editedPatientEmail.setText(patient.getEmail());
            editedPatientTelephone.setText(patient.getPhone());
            //Kontroluje jestli doktor je registrovan??, pak zobraz?? ji?? ulo??enou v databazi informace, kterou se pak d?? editovat.//
        } else if (JDBCDao.doctor_id != -1) {
            Doctor doctor = (Doctor) JDBCDao.getUserInfo(JDBCDao.doctor_id);
            editedDoctorName.setText(doctor.getName());
            editedDoctorSurname.setText(doctor.getSurname());
            editedDoctorFullnameRightCorner.setText(doctor.getName() + " " + doctor.getSurname());
            editedDoctorAge.setText(""+doctor.getAge()+"");
            editedDoctorGender.setText(""+doctor.getGender()+"");
            editedDoctorExperience.setText(""+doctor.getExperience()+"");
            editedDoctorSpecialization.setText(doctor.getSpecialization());
            editedDoctorEducation.setText(doctor.getEducation());
            editedDoctorHospital.setText(doctor.getHospital());
            editedDoctorAddress.setText(doctor.getAddress());
            editedDoctorEmail.setText(doctor.getEmail());
            editedDoctorTelephone.setText(doctor.getPhone());
        }
    }


    /**
     * Metoda kontroluje jestli ??daje, kter?? zadav?? u??ivatel jsou spr??vn?? a odpov??d??j?? po??adovan??mu form??tu.
     *<p>
     *
     */
    public String patientValidation() {
        String validations = "";
        //Kontroluje jestli d??lka jm??na je v??t???? ne?? 2//
        if(editedPatientName.getText().equals("") || editedPatientName.getLength() < 2) {
            validations += "Jm??no je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        //Kontroluje jestli d??lka p??ijmen?? je v??t???? ne?? 2//
        if(editedPatientSurname.getText().equals("") || editedPatientSurname.getLength() < 2) {
            validations += "P????jmen?? je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        //Kontroluje jestli d??lka pohlav?? je v??t???? ne?? 1//
        if(editedPatientGender.getText().equals("") || editedPatientGender.getLength() < 1) {
            validations += "Pohlav?? je povinn?? pole. M??lo by obsahovat alespo?? 1 znak.\n";
        }
        //Kontroluje jestli polo??ka v??k nen?? pr??zdn??//
        if(editedPatientAge.getText().equals("")) {
            validations += "V??k je povinn?? pole.\n";
        }
        try {
            //Kontroluje jestli polo??ka v??k je v??t???? ne?? 12 //
            if (Integer.parseInt(editedPatientAge.getText()) < 12) {
                validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 12.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 12.\n";
        }
        //Kontroluje jestli adresa obsahuje nejm??n?? 15 symbol??//
        if(editedPatientAddress.getText().equals("") || editedPatientAddress.getLength() < 15) {
            validations += "Adresa je povinn?? pole. M??lo by obsahovat alespo?? 15 znak??.\n";
        }
        //Kontroluje jestli email obsahuje nejm??n?? 7 symbol??//
        if(editedPatientEmail.getText().equals("") || editedPatientEmail.getLength() < 7) {
            validations += "E-mail je povinn?? pole. M??lo by obsahovat alespo?? 7 znak??.\n";
        }
        //Kontroluje jestli telefonn?? ????slo obsahuje nejm??n?? 12 symbol?? a za??in??  ????slicemi +420//
        if(editedPatientTelephone.getText().equals("") || editedPatientTelephone.getLength() < 12 || !editedPatientTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinn?? pole. M??lo by obsahovat alespo?? 12 ????sel a za????nat na +420.\n";
        }
        //Kontroluje jestli polo??ka vaha nen?? pr??zdn??//
        if(editedPatientWeight.getText().equals("")) {
            validations += "Hmotnost je povinn?? pole.\n";
        }
        try {
            //Kontroluje jestli polo??ka vaha je v??t???? nebo rovn?? ne?? 20kg//
            if (Integer.parseInt(editedPatientWeight.getText()) < 20) {
                validations += "Hmotnost by m??la b??t alespo?? 20 kg.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Hmotnost je ??islo.\n";
        }
        //Kontroluje jestli polo??ka v????ka nen?? pr??zdn??//
        if(editedPatientHeight.getText().equals("")) {
            validations += "V????ka je povinn?? pole.\n";
        }
        try {
            //Kontroluje jestli polo??ka v????ka je alespo?? 100cm//
            if (Integer.parseInt(editedPatientHeight.getText()) < 100) {
                validations += "V????ka by m??la b??t alespo?? 100 cm.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V????ka by m??la b??t ??islo.\n";
        }
        //Kontroluje jestli polo??ka Sypmtomy nen?? pr??zdn??//
        if(editedPatientSymptoms.getText().equals("")) {
            validations += "Symptomy jsou povinn??.\n";
        }
        return validations;
    }
    /**
     * Metoda kontroluje jestli ??daje, kter?? zadav?? u??ivatel jsou spr??vn?? a odpov??d??j?? po??adovan??mu form??tu.
     *<p>
     *
     */
    public String doctorValidation() {
        String validations = "";
        //Kontroluje jestli d??lka jm??na je v??t???? ne?? 2//
        if(editedDoctorName.getText().equals("") || editedDoctorName.getLength() < 2) {
            validations += "Jm??no je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        //Kontroluje jestli d??lka p??ijmen?? je v??t???? ne?? 2//
        if(editedDoctorSurname.getText().equals("") || editedDoctorSurname.getLength() < 2) {
            validations += "P????jmen?? je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        //Kontroluje jestli d??lka pohlav?? je v??t???? ne?? 1//
        if(editedDoctorGender.getText().equals("") || editedDoctorGender.getLength() < 1) {
            validations += "Pohlav?? je povinn?? pole. M??lo by obsahovat alespo?? 1 znak.\n";
        }
        //Kontroluje jestli polo??ka v??k nen?? pr??zdn??//
        if(editedDoctorAge.getText().equals("")) {
            validations += "V??k je povinn?? pole.\n";
        }
        try {
            //Kontroluje jestli polo??ka v??k je v??t???? ne?? 20//
            if (Integer.parseInt(editedDoctorAge.getText()) < 20) {
                validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 20.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 20.\n";
        }
        //Kontroluje jestli adresa obsahuje nejm??n?? 15 symbol??//
        if(editedDoctorAddress.getText().equals("") || editedDoctorAddress.getLength() < 15) {
            validations += "Adresa je povinn?? pole. M??lo by obsahovat alespo?? 15 znak??.\n";
        }
        //Kontroluje jestli email obsahuje nejm??n?? 7 symbol??//
        if(editedDoctorEmail.getText().equals("") || editedDoctorEmail.getLength() < 7) {
            validations += "E-mail je povinn?? pole. M??lo by obsahovat alespo?? 7 znak??.\n";
        }
        //Kontroluje jestli telefonn?? ????slo obsahuje nejm??n?? 12 symbol?? a za??in??  ????slicemi +420//
        if(editedDoctorTelephone.getText().equals("") || editedDoctorTelephone.getLength() < 12 || !editedDoctorTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinn?? pole. M??lo by obsahovat alespo?? 12 ????sel a za????nat na +420.\n";
        }
        //Kontroluje jestli polo??ka specializace m?? delkou v??t???? nebo rovnou 4//
        if(editedDoctorSpecialization.getText().equals("") || editedDoctorSpecialization.getLength() < 4) {
            validations += "Specializace je povinn?? pole. M??lo by obsahovat alespo?? 4 znaky.\n";
        }
        //Kontroluje jestli polo??ka vzd??l??n?? m?? d??lku v??t???? nebo rovnou 3//
        if(editedDoctorEducation.getText().equals("") || editedDoctorEducation.getLength() < 3) {
            validations += "Vzd??l??n?? je povinn?? pole. M??lo by obsahovat alespo?? 3 znaky.\n";
        }
        //Kontroluje jestli polo??ka n??mocnice m?? d??lku v??t???? nebo rovnou 10//
        if(editedDoctorHospital.getText().equals("") || editedDoctorHospital.getLength() < 10) {
            validations += "Nemocnice je povinn?? pole. M??lo by obsahovat alespo?? 10 znaky.\n";
        }
        //Kontroluje jestli polo??ka zku??enost nen?? pr??zdn??//
        if(editedDoctorExperience.getText().equals("")) {
            validations += "Zku??enost je povinn?? pole.\n";
        }
        try {
            //Kontroluje jestli polo??ka zku??enost m?? dobu del???? ne?? 5 let //
            if (Integer.parseInt(editedDoctorExperience.getText()) < 5) {
                validations += "Zku??enost m?? b??t minim??ln?? 5 let.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Zku??enost je ??islo.\n";
        }
        return validations;
    }
    /**
     * Ulklad?? zadan?? data do databaze v p????pad??, ??e v??echny polo??ky nejsou pr??zdn?? a odpov??d??j?? uveden??mu form??tu.
     * Jinak se zobraz?? okenko Warning.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    @FXML
    public void saveEditedPatientData(MouseEvent mouseEvent) throws SQLException, IOException {
        User pacientPreData = JDBCDao.getUserInfo(JDBCDao.patient_id);
        Pacient pacPre = (Pacient) pacientPreData;
        String validations = patientValidation();
        if(validations == null || validations.equals("")) {
            Pacient pacient = new Pacient(
                    editedPatientEmail.getText(), pacientPreData.getPassword(), editedPatientTelephone.getText(), editedPatientName.getText(),
                    editedPatientSurname.getText(), true, Integer.parseInt(editedPatientAge.getText()), editedPatientGender.getText(),
                    parseUpdatedSymptoms(editedPatientSymptoms.getText()), Integer.parseInt(editedPatientWeight.getText()), Integer.parseInt(editedPatientHeight.getText()),
                    editedPatientAddress.getText()
            );
            JDBCDao.registerPacient(pacient);
            changeStageToPatientForm(mouseEvent);
        } else {
            //Jinak se zobraz?? okenko Warning. //
            String warnText = validations;
            WarningController warn = new WarningController(warnText, patientEditAnchorPane.getScene().getWindow());
        }
    }
    /**
     * Ulklad?? zadan?? data do databaze v p????pad??, ??e v??echny polo??ky nejsou pr??zdn?? a odpov??d??j?? uveden??mu form??tu.
     * Jinak se zobraz?? okenko Warning.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */

    @FXML
    public void saveEditedDoctorData(MouseEvent mouseEvent) throws SQLException, IOException {
        User doctorPreData = JDBCDao.getUserInfo(JDBCDao.doctor_id);
        String validations = doctorValidation();
        if(validations == null || validations.equals("")) {
            Doctor doctor = new Doctor(
                    editedDoctorEmail.getText(), doctorPreData.getPassword(), editedDoctorTelephone.getText(), editedDoctorName.getText(),
                    editedDoctorSurname.getText(), true, Integer.parseInt(editedDoctorAge.getText()), editedDoctorGender.getText(),
                    editedDoctorSpecialization.getText(), editedDoctorEducation.getText(), editedDoctorAddress.getText(),
                    editedDoctorHospital.getText(), Integer.parseInt(editedDoctorExperience.getText())
            );
            //??daje se ukl??d??j?? do databaze//
            JDBCDao.registerDoctor(doctor);
            changeStageToDoctorProfile(mouseEvent);
        } else {
            //Jinak se zobraz?? okenko Warning. //
            String warnText = validations;
            WarningController warn = new WarningController(warnText, editDoctorAnchorPane.getScene().getWindow());
        }
    }
    /**
     * Dovol?? editovat uveden?? symptomy, kter?? se zapisuj?? pomoc?? datov??ho typu String a odd??luj?? se ????rkou.
     * <p>
     *
     * @param symptoms
     */
    public ArrayList<Symptom> parseUpdatedSymptoms(String symptoms) throws SQLException {
        JDBCDao.clearSymps();
        ArrayList<Symptom> symptomsList = new ArrayList<>();

        String[] elements = symptoms.split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        ArrayList<String> listOfString = new ArrayList<>(fixedLenghtList);

        for (String str : listOfString) {
            JDBCDao.addSymthom(str);
            symptomsList.add(new Symptom(str));
        }
        return symptomsList;
    }
    /**
     * P??epin?? na dal???? okenko registrace pacienta
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     *
     */
    public void changeStageToPatientForm(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientForm.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            patientEditAnchorPane.getChildren().clear();
            patientEditAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * P??epin?? na dal???? okenko registrace l??ka??e.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     *
     */
    public void changeStageToDoctorProfile(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorProfile.fxml"));
        Pane registrationPane = null;
        try {
            registrationPane = (Pane) fxmlLoader.load();
            editDoctorAnchorPane.getChildren().clear();
            editDoctorAnchorPane.getChildren().setAll(registrationPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
