package cz.vse.fimed.UI;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import cz.vse.fimed.profile.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistrationController implements IController {

    @FXML public AnchorPane anchorPaneRegistration;
    @FXML public AnchorPane mainRegister;
    @FXML public AnchorPane doctorReg;
    @FXML public AnchorPane pacientReg;

    @FXML public TextField emailField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField passwordConfirmField;
    @FXML public CheckBox patientCheckbox;
    @FXML public CheckBox doctorCheckbox;

    // Patient info
    @FXML public TextField patientName;
    @FXML public TextField patientSurname;
    @FXML public TextField patientGender;
    @FXML public TextField patientAge;
    @FXML public TextField patientAddress;
    @FXML public TextArea patientAddInfo;
    @FXML public TextField patientEmail;
    @FXML public TextField patientTelephone;
    @FXML public TextField patientWeight;
    @FXML public TextField patientHeight;
    @FXML public TextArea patientSymptoms;


    // Doctor info
    @FXML public TextField docGender;
    @FXML public TextField docAge;
    @FXML public TextField docSurname;
    @FXML public TextField docName;
    @FXML public TextField docHospital;
    @FXML public TextField docAddress;
    @FXML public TextField docEmail;
    @FXML public TextField docTelephone;
    @FXML public TextField docExperience;
    @FXML public TextField docSpecialization;
    @FXML public TextField docEducation;

    @FXML
    public void initialize() {
        anchorPaneRegistration.getChildren().clear();
        anchorPaneRegistration.getChildren().setAll(mainRegister);
    }

    /**
     * Metoda sloužící pro kontrolu zadání platných emailu a hesel.
     */
    public boolean validateInputs() {
        return emailField.getText().length() >= 7 &&
               passwordField.getText().length() >= 8 &&
               passwordField.getText().equals(passwordConfirmField.getText());
    }


    /**
     * Metoda sloužící pro vytvaření nového uživatele.
     * Kontroluje aby uživatel uvaděl jen platná hesla a email.
     * Kontroluje aby si uživatel zvolil roli.
     * Jinak vypíše chybovou hlášku.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void createUser(MouseEvent mouseEvent) throws SQLException, IOException {
        if (validateInputs() && patientCheckbox.isSelected()) {
            Pacient user1 = new Pacient(emailField.getText(), passwordField.getText(), null, null, null, false, -1, null, null, -1, -1, null);
            JDBCDao.registerUser(user1, null);
            changeStageToPacReg(mouseEvent);
        } else if(validateInputs() && doctorCheckbox.isSelected()) {
            Doctor user1 = new Doctor(emailField.getText(), passwordField.getText(), null, null, null, false, -1, null, null, null, null, null, -1);
            JDBCDao.registerUser(null, user1);
            changeStageToDocReg(mouseEvent);
        } else if(validateInputs() && !(patientCheckbox.isSelected() || doctorCheckbox.isSelected())) {
            String warnText = "Zvolte, prosím Váši role.";
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        } else {
            String warnText = "Zkontrolujte, prosim, zda má váš e-mail alespoň 7 znaků,\n" +
                                "heslo má nejméně 8 znaků a je podobné potvrzovacímu.";
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        }
    }

    /**
     * Metoda sloužící pro kontrolu zadání platných údajů o pacientovi.
     */
    public String patientValidation() {
        String validations = "";
        if(patientName.getText().equals("") || patientName.getLength() < 2) {
            validations += "Jméno je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        if(patientSurname.getText().equals("") || patientSurname.getLength() < 2) {
            validations += "Příjmení je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        if(patientGender.getText().equals("") || patientGender.getLength() < 1) {
            validations += "Pohlaví je povinné pole. Mělo by obsahovat alespoň 1 znak.\n";
        }
        if(patientAge.getText().equals("")) {
            validations += "Věk je povinné pole.\n";
        }
        try {
            if (Integer.parseInt(patientAge.getText()) < 12) {
                validations += "Věk by měl být kladné číslo větší než 12.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Věk by měl být kladné číslo větší než 12.\n";
        }
        if(patientAddress.getText().equals("") || patientAddress.getLength() < 15) {
            validations += "Adresa je povinné pole. Mělo by obsahovat alespoň 15 znaků.\n";
        }
        if(patientEmail.getText().equals("") || patientEmail.getLength() < 7) {
            validations += "E-mail je povinné pole. Mělo by obsahovat alespoň 7 znaků.\n";
        }
        if(patientTelephone.getText().equals("") || patientTelephone.getLength() < 12 || !patientTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinné pole. Mělo by obsahovat alespoň 12 čísel a začínat na +420.\n";
        }
        if(patientWeight.getText().equals("")) {
            validations += "Hmotnost je povinné pole.\n";
        }
        try {
            if (Integer.parseInt(patientWeight.getText()) < 20) {
                validations += "Hmotnost by měla být alespoň 20 kg.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Hmotnost je čislo.\n";
        }
        if(patientHeight.getText().equals("")) {
            validations += "Výška je povinné pole.\n";
        }
        try {
            if (Integer.parseInt(patientHeight.getText()) < 100) {
                validations += "Výška by měla být alespoň 100 cm.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Výška by měla být čislo.\n";
        }
        if(patientSymptoms.getText().equals("")) {
            validations += "Symptomy jsou povinné.\n";
        }
        return validations;
    }

    /**
     * Metoda sloužící pro kontrolu zadání platných údajů o doktorovi.
     */
    public String doctorValidation() {
        String validations = "";
        if(docName.getText().equals("") || docName.getLength() < 2) {
            validations += "Jméno je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        if(docSurname.getText().equals("") || docSurname.getLength() < 2) {
            validations += "Příjmení je povinné pole. Mělo by obsahovat alespoň 2 znaky.\n";
        }
        if(docGender.getText().equals("") || docGender.getLength() < 1) {
            validations += "Pohlaví je povinné pole. Mělo by obsahovat alespoň 1 znak.\n";
        }
        if(docAge.getText().equals("")) {
            validations += "Věk je povinné pole.\n";
        }
        try {
            if (Integer.parseInt(docAge.getText()) < 20) {
                validations += "Věk by měl být kladné číslo větší než 20.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Věk by měl být kladné číslo větší než 20.\n";
        }
        if(docAddress.getText().equals("") || docAddress.getLength() < 15) {
            validations += "Adresa je povinné pole. Mělo by obsahovat alespoň 15 znaků.\n";
        }
        if(docEmail.getText().equals("") || docEmail.getLength() < 7) {
            validations += "E-mail je povinné pole. Mělo by obsahovat alespoň 7 znaků.\n";
        }
        if(docTelephone.getText().equals("") || docTelephone.getLength() < 12 || !docTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinné pole. Mělo by obsahovat alespoň 12 čísel a začínat na +420.\n";
        }
        if(docSpecialization.getText().equals("") || docSpecialization.getLength() < 4) {
            validations += "Specializace je povinné pole. Mělo by obsahovat alespoň 4 znaky.\n";
        }
        if(docEducation.getText().equals("") || docEducation.getLength() < 3) {
            validations += "Vzdělání je povinné pole. Mělo by obsahovat alespoň 3 znaky.\n";
        }
        if(docHospital.getText().equals("") || docHospital.getLength() < 10) {
            validations += "Nemocnice je povinné pole. Mělo by obsahovat alespoň 10 znaky.\n";
        }
        if(docExperience.getText().equals("")) {
            validations += "Zkušenost je povinné pole.\n";
        }
        try {
            if (Integer.parseInt(docExperience.getText()) < 5) {
                validations += "Zkušenost má být minimálně 5 let.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Zkušenost je čislo.\n";
        }
        return validations;
    }

    /**
     * Metoda sloužící pro registraci údajů o novém uživateli - pacientovi.
     * Kontroluje aby uživatel uvaděl jen platné údaje.
     * Jinak vypíše chybovou hlášku.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void registerPatient(MouseEvent mouseEvent) throws IOException, SQLException {
        User pacientPreData = JDBCDao.getUserInfo(JDBCDao.patient_id);
        String validations = patientValidation();
        if(validations == null || validations.equals("")) {
            Pacient pacient = new Pacient(
                    pacientPreData.getEmail(), pacientPreData.getPassword(), patientTelephone.getText(), patientName.getText(),
                    patientSurname.getText(), true, Integer.parseInt(patientAge.getText()), patientGender.getText(),
                    parseSymptoms(patientSymptoms.getText()), Integer.parseInt(patientWeight.getText()), Integer.parseInt(patientHeight.getText()),
                    patientAddress.getText()
            );
            JDBCDao.registerPacient(pacient);
            changeStageToLogin(mouseEvent);
        } else {
            String warnText = validations;
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        }
    }

    /**
     * Metoda sloužící pro registraci údajů o novém uživateli - doktorovi.
     * Kontroluje aby uživatel uvaděl jen platné údaje.
     * Jinak vypíše chybovou hlášku.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void registerDoctor(MouseEvent mouseEvent) throws IOException, SQLException {
        User doctorPreData = JDBCDao.getUserInfo(JDBCDao.doctor_id);
        String validations = doctorValidation();
        if(validations == null || validations.equals("")) {
            Doctor doctor = new Doctor(
                    doctorPreData.getEmail(), doctorPreData.getPassword(), docTelephone.getText(), docName.getText(),
                    docSurname.getText(), true, Integer.parseInt(docAge.getText()), docGender.getText(),
                    docSpecialization.getText(), docEducation.getText(), docAddress.getText(),
                    docHospital.getText(), Integer.parseInt(docExperience.getText())
            );
            JDBCDao.registerDoctor(doctor);
            changeStageToLogin(mouseEvent);
        } else {
            String warnText = validations;
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        }
    }

    /**
     * Metoda sloužící pro přidání do DB symptomů pacienta.
     * <p>
     *
     * @param symptoms seznam symptomů
     */
    public ArrayList<Symptom> parseSymptoms(String symptoms) throws SQLException {
        ArrayList<Symptom> symptomsList = new ArrayList<>();

        String[] elements = symptoms.split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        ArrayList<String> listOfString = new ArrayList<>(fixedLenghtList);

        for (String sym : listOfString) {
            JDBCDao.addSymthom(sym);
            symptomsList.add(new Symptom(sym));
        }
        return symptomsList;
    }

    /**
     * Metoda sloužící pro přechod na obrazovku přihlašení,
     * pokud uživatel už má účet.
     * <p>
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void changeStageToLogin(MouseEvent mouseEvent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Pane loginPane = null;
            try {
                loginPane = (Pane) fxmlLoader.load();
                anchorPaneRegistration.getChildren().clear();
                anchorPaneRegistration.getChildren().setAll(loginPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    /**
     * Metoda sloužící pro přechod na další obrazovku pro registraci doktora.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    public void changeStageToDocReg(MouseEvent mouseEvent) {
        anchorPaneRegistration.getChildren().clear();
        anchorPaneRegistration.getChildren().setAll(doctorReg);
    }

    /**
     * Metoda sloužící pro přechod na další obrazovku pro registraci pacienta.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    public void changeStageToPacReg(MouseEvent mouseEvent) {
        anchorPaneRegistration.getChildren().clear();
        anchorPaneRegistration.getChildren().setAll(pacientReg);
    }

    /**
     * Metoda sloužící pro kontrolu, zda si uživatel nevybral obě role.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void checkIfOtherIsSetDoc(MouseEvent mouseEvent) {
        if(patientCheckbox.isSelected()) {
            patientCheckbox.setSelected(false);
        }
    }

    /**
     * Metoda sloužící pro kontrolu, zda si uživatel nevybral obě role.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void checkIfOtherIsSetPatient(MouseEvent mouseEvent) {
        if(doctorCheckbox.isSelected()) {
            doctorCheckbox.setSelected(false);
        }
    }

    /**
     * Metoda sloužící pro vypsání nápovědy k aplikaci.
     *
     * @param mouseEvent kliknutí mýší na příslušné tlačítko
     */
    @FXML
    public void handleHelpClick(MouseEvent mouseEvent) throws IOException {
        String warnText = "Tahle aplikace je speciálně vyvinutá proto, aby ulevila\n" +
                "komunikaci mezi lékařem a potenciálním pacientem.\n" +
                "Abyste aplikaci mohl využivat, musíte se nejrpve zaregistrovat.\n" +
                "1) Uveďte svůj email\n" +
                "2) Vyplňte si heslo\n" +
                "3) Doplníte údaje o sobě\n";
        WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
    }
}
