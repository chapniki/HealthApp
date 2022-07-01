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
     * Metoda kontroluje jestli pacient je registrovaný, pak zobrazí již uloženou v databazi informace,
     * kterou se pak dá editovat.
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
            //Kontroluje jestli doktor je registrovaný, pak zobrazí již uloženou v databazi informace, kterou se pak dá editovat.//
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
     * Metoda kontroluje jestli údaje, které zadavá uživatel jsou správné a odpovídájí požadovanému formátu.
     *<p>
     *
     */
    public String patientValidation() {
        String validations = "";
        //Kontroluje jestli délka jména je většé než 2//
        if(editedPatientName.getText().equals("") || editedPatientName.getLength() < 2) {
            validations += "Jméno je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        //Kontroluje jestli délka přijmení je většé než 2//
        if(editedPatientSurname.getText().equals("") || editedPatientSurname.getLength() < 2) {
            validations += "Příjmení je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        //Kontroluje jestli délka pohlaví je většé než 1//
        if(editedPatientGender.getText().equals("") || editedPatientGender.getLength() < 1) {
            validations += "Pohlaví je povinné pole. Mělo by obsahovat alespoň 1 znak.\n";
        }
        //Kontroluje jestli položka věk není prázdná//
        if(editedPatientAge.getText().equals("")) {
            validations += "Věk je povinné pole.\n";
        }
        try {
            //Kontroluje jestli položka věk je větší než 12 //
            if (Integer.parseInt(editedPatientAge.getText()) < 12) {
                validations += "Věk by měl být kladné číslo větší než 12.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Věk by měl být kladné číslo větší než 12.\n";
        }
        //Kontroluje jestli adresa obsahuje nejméně 15 symbolů//
        if(editedPatientAddress.getText().equals("") || editedPatientAddress.getLength() < 15) {
            validations += "Adresa je povinné pole. Mělo by obsahovat alespoň 15 znaků.\n";
        }
        //Kontroluje jestli email obsahuje nejméně 7 symbolů//
        if(editedPatientEmail.getText().equals("") || editedPatientEmail.getLength() < 7) {
            validations += "E-mail je povinné pole. Mělo by obsahovat alespoň 7 znaků.\n";
        }
        //Kontroluje jestli telefonní číslo obsahuje nejméně 12 symbolů a začiná  číslicemi +420//
        if(editedPatientTelephone.getText().equals("") || editedPatientTelephone.getLength() < 12 || !editedPatientTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinné pole. Mělo by obsahovat alespoň 12 čísel a začínat na +420.\n";
        }
        //Kontroluje jestli položka vaha není prázdná//
        if(editedPatientWeight.getText().equals("")) {
            validations += "Hmotnost je povinné pole.\n";
        }
        try {
            //Kontroluje jestli položka vaha je větší nebo rovná než 20kg//
            if (Integer.parseInt(editedPatientWeight.getText()) < 20) {
                validations += "Hmotnost by měla být alespoň 20 kg.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Hmotnost je čislo.\n";
        }
        //Kontroluje jestli položka výška není prázdná//
        if(editedPatientHeight.getText().equals("")) {
            validations += "Výška je povinné pole.\n";
        }
        try {
            //Kontroluje jestli položka výška je alespoň 100cm//
            if (Integer.parseInt(editedPatientHeight.getText()) < 100) {
                validations += "Výška by měla být alespoň 100 cm.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Výška by měla být čislo.\n";
        }
        //Kontroluje jestli položka Sypmtomy není prázdná//
        if(editedPatientSymptoms.getText().equals("")) {
            validations += "Symptomy jsou povinné.\n";
        }
        return validations;
    }
    /**
     * Metoda kontroluje jestli údaje, které zadavá uživatel jsou správné a odpovídájí požadovanému formátu.
     *<p>
     *
     */
    public String doctorValidation() {
        String validations = "";
        //Kontroluje jestli délka jména je většé než 2//
        if(editedDoctorName.getText().equals("") || editedDoctorName.getLength() < 2) {
            validations += "Jméno je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        //Kontroluje jestli délka přijmení je většé než 2//
        if(editedDoctorSurname.getText().equals("") || editedDoctorSurname.getLength() < 2) {
            validations += "Příjmení je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        //Kontroluje jestli délka pohlaví je většé než 1//
        if(editedDoctorGender.getText().equals("") || editedDoctorGender.getLength() < 1) {
            validations += "Pohlaví je povinné pole. Mělo by obsahovat alespoň 1 znak.\n";
        }
        //Kontroluje jestli položka věk není prázdná//
        if(editedDoctorAge.getText().equals("")) {
            validations += "Věk je povinné pole.\n";
        }
        try {
            //Kontroluje jestli položka věk je větší než 20//
            if (Integer.parseInt(editedDoctorAge.getText()) < 20) {
                validations += "Věk by měl být kladné číslo větší než 20.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Věk by měl být kladné číslo větší než 20.\n";
        }
        //Kontroluje jestli adresa obsahuje nejméně 15 symbolů//
        if(editedDoctorAddress.getText().equals("") || editedDoctorAddress.getLength() < 15) {
            validations += "Adresa je povinné pole. Mělo by obsahovat alespoň 15 znaků.\n";
        }
        //Kontroluje jestli email obsahuje nejméně 7 symbolů//
        if(editedDoctorEmail.getText().equals("") || editedDoctorEmail.getLength() < 7) {
            validations += "E-mail je povinné pole. Mělo by obsahovat alespoň 7 znaků.\n";
        }
        //Kontroluje jestli telefonní číslo obsahuje nejméně 12 symbolů a začiná  číslicemi +420//
        if(editedDoctorTelephone.getText().equals("") || editedDoctorTelephone.getLength() < 12 || !editedDoctorTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinné pole. Mělo by obsahovat alespoň 12 čísel a začínat na +420.\n";
        }
        //Kontroluje jestli položka specializace má delkou větší nebo rovnou 4//
        if(editedDoctorSpecialization.getText().equals("") || editedDoctorSpecialization.getLength() < 4) {
            validations += "Specializace je povinné pole. Mělo by obsahovat alespoň 4 znaky.\n";
        }
        //Kontroluje jestli položka vzdělání má délku větší nebo rovnou 3//
        if(editedDoctorEducation.getText().equals("") || editedDoctorEducation.getLength() < 3) {
            validations += "Vzdělání je povinné pole. Mělo by obsahovat alespoň 3 znaky.\n";
        }
        //Kontroluje jestli položka němocnice má délku větší nebo rovnou 10//
        if(editedDoctorHospital.getText().equals("") || editedDoctorHospital.getLength() < 10) {
            validations += "Nemocnice je povinné pole. Mělo by obsahovat alespoň 10 znaky.\n";
        }
        //Kontroluje jestli položka zkušenost není prázdná//
        if(editedDoctorExperience.getText().equals("")) {
            validations += "Zkušenost je povinné pole.\n";
        }
        try {
            //Kontroluje jestli položka zkušenost má dobu delší než 5 let //
            if (Integer.parseInt(editedDoctorExperience.getText()) < 5) {
                validations += "Zkušenost má být minimálně 5 let.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Zkušenost je čislo.\n";
        }
        return validations;
    }
    /**
     * Ulkladá zadaná data do databaze v případě, že všechny položky nejsou prázdné a odpovídájí uvedenému formátu.
     * Jinak se zobrazí okenko Warning.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
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
            //Jinak se zobrazí okenko Warning. //
            String warnText = validations;
            WarningController warn = new WarningController(warnText, patientEditAnchorPane.getScene().getWindow());
        }
    }
    /**
     * Ulkladá zadaná data do databaze v případě, že všechny položky nejsou prázdné a odpovídájí uvedenému formátu.
     * Jinak se zobrazí okenko Warning.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
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
            //Údaje se ukládájí do databaze//
            JDBCDao.registerDoctor(doctor);
            changeStageToDoctorProfile(mouseEvent);
        } else {
            //Jinak se zobrazí okenko Warning. //
            String warnText = validations;
            WarningController warn = new WarningController(warnText, editDoctorAnchorPane.getScene().getWindow());
        }
    }
    /**
     * Dovolí editovat uvedené symptomy, které se zapisují pomocí datového typu String a oddělují se čárkou.
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
     * Přepiná na další okenko registrace pacienta
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
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
     * Přepiná na další okenko registrace lékaře.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
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
