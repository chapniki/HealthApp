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
     * Metoda slou????c?? pro kontrolu zad??n?? platn??ch emailu a hesel.
     */
    public boolean validateInputs() {
        return emailField.getText().length() >= 7 &&
               passwordField.getText().length() >= 8 &&
               passwordField.getText().equals(passwordConfirmField.getText());
    }


    /**
     * Metoda slou????c?? pro vytva??en?? nov??ho u??ivatele.
     * Kontroluje aby u??ivatel uvad??l jen platn?? hesla a email.
     * Kontroluje aby si u??ivatel zvolil roli.
     * Jinak vyp????e chybovou hl????ku.
     * <p>
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
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
            String warnText = "Zvolte, pros??m V????i role.";
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        } else {
            String warnText = "Zkontrolujte, prosim, zda m?? v???? e-mail alespo?? 7 znak??,\n" +
                                "heslo m?? nejm??n?? 8 znak?? a je podobn?? potvrzovac??mu.";
            WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
        }
    }

    /**
     * Metoda slou????c?? pro kontrolu zad??n?? platn??ch ??daj?? o pacientovi.
     */
    public String patientValidation() {
        String validations = "";
        if(patientName.getText().equals("") || patientName.getLength() < 2) {
            validations += "Jm??no je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        if(patientSurname.getText().equals("") || patientSurname.getLength() < 2) {
            validations += "P????jmen?? je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        if(patientGender.getText().equals("") || patientGender.getLength() < 1) {
            validations += "Pohlav?? je povinn?? pole. M??lo by obsahovat alespo?? 1 znak.\n";
        }
        if(patientAge.getText().equals("")) {
            validations += "V??k je povinn?? pole.\n";
        }
        try {
            if (Integer.parseInt(patientAge.getText()) < 12) {
                validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 12.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 12.\n";
        }
        if(patientAddress.getText().equals("") || patientAddress.getLength() < 15) {
            validations += "Adresa je povinn?? pole. M??lo by obsahovat alespo?? 15 znak??.\n";
        }
        if(patientEmail.getText().equals("") || patientEmail.getLength() < 7) {
            validations += "E-mail je povinn?? pole. M??lo by obsahovat alespo?? 7 znak??.\n";
        }
        if(patientTelephone.getText().equals("") || patientTelephone.getLength() < 12 || !patientTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinn?? pole. M??lo by obsahovat alespo?? 12 ????sel a za????nat na +420.\n";
        }
        if(patientWeight.getText().equals("")) {
            validations += "Hmotnost je povinn?? pole.\n";
        }
        try {
            if (Integer.parseInt(patientWeight.getText()) < 20) {
                validations += "Hmotnost by m??la b??t alespo?? 20 kg.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Hmotnost je ??islo.\n";
        }
        if(patientHeight.getText().equals("")) {
            validations += "V????ka je povinn?? pole.\n";
        }
        try {
            if (Integer.parseInt(patientHeight.getText()) < 100) {
                validations += "V????ka by m??la b??t alespo?? 100 cm.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V????ka by m??la b??t ??islo.\n";
        }
        if(patientSymptoms.getText().equals("")) {
            validations += "Symptomy jsou povinn??.\n";
        }
        return validations;
    }

    /**
     * Metoda slou????c?? pro kontrolu zad??n?? platn??ch ??daj?? o doktorovi.
     */
    public String doctorValidation() {
        String validations = "";
        if(docName.getText().equals("") || docName.getLength() < 2) {
            validations += "Jm??no je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        if(docSurname.getText().equals("") || docSurname.getLength() < 2) {
            validations += "P????jmen?? je povinn?? pole. M??lo by obsahovat alespo?? 2 znaky.\n";
        }
        if(docGender.getText().equals("") || docGender.getLength() < 1) {
            validations += "Pohlav?? je povinn?? pole. M??lo by obsahovat alespo?? 1 znak.\n";
        }
        if(docAge.getText().equals("")) {
            validations += "V??k je povinn?? pole.\n";
        }
        try {
            if (Integer.parseInt(docAge.getText()) < 20) {
                validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 20.\n";
            }
        } catch (NumberFormatException e) {
            validations += "V??k by m??l b??t kladn?? ????slo v??t???? ne?? 20.\n";
        }
        if(docAddress.getText().equals("") || docAddress.getLength() < 15) {
            validations += "Adresa je povinn?? pole. M??lo by obsahovat alespo?? 15 znak??.\n";
        }
        if(docEmail.getText().equals("") || docEmail.getLength() < 7) {
            validations += "E-mail je povinn?? pole. M??lo by obsahovat alespo?? 7 znak??.\n";
        }
        if(docTelephone.getText().equals("") || docTelephone.getLength() < 12 || !docTelephone.getText().startsWith("+420")) {
            validations += "Telefon je povinn?? pole. M??lo by obsahovat alespo?? 12 ????sel a za????nat na +420.\n";
        }
        if(docSpecialization.getText().equals("") || docSpecialization.getLength() < 4) {
            validations += "Specializace je povinn?? pole. M??lo by obsahovat alespo?? 4 znaky.\n";
        }
        if(docEducation.getText().equals("") || docEducation.getLength() < 3) {
            validations += "Vzd??l??n?? je povinn?? pole. M??lo by obsahovat alespo?? 3 znaky.\n";
        }
        if(docHospital.getText().equals("") || docHospital.getLength() < 10) {
            validations += "Nemocnice je povinn?? pole. M??lo by obsahovat alespo?? 10 znaky.\n";
        }
        if(docExperience.getText().equals("")) {
            validations += "Zku??enost je povinn?? pole.\n";
        }
        try {
            if (Integer.parseInt(docExperience.getText()) < 5) {
                validations += "Zku??enost m?? b??t minim??ln?? 5 let.\n";
            }
        } catch (NumberFormatException e) {
            validations += "Zku??enost je ??islo.\n";
        }
        return validations;
    }

    /**
     * Metoda slou????c?? pro registraci ??daj?? o nov??m u??ivateli - pacientovi.
     * Kontroluje aby u??ivatel uvad??l jen platn?? ??daje.
     * Jinak vyp????e chybovou hl????ku.
     * <p>
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
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
     * Metoda slou????c?? pro registraci ??daj?? o nov??m u??ivateli - doktorovi.
     * Kontroluje aby u??ivatel uvad??l jen platn?? ??daje.
     * Jinak vyp????e chybovou hl????ku.
     * <p>
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
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
     * Metoda slou????c?? pro p??id??n?? do DB symptom?? pacienta.
     * <p>
     *
     * @param symptoms seznam symptom??
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
     * Metoda slou????c?? pro p??echod na obrazovku p??ihla??en??,
     * pokud u??ivatel u?? m?? ????et.
     * <p>
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
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
     * Metoda slou????c?? pro p??echod na dal???? obrazovku pro registraci doktora.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    public void changeStageToDocReg(MouseEvent mouseEvent) {
        anchorPaneRegistration.getChildren().clear();
        anchorPaneRegistration.getChildren().setAll(doctorReg);
    }

    /**
     * Metoda slou????c?? pro p??echod na dal???? obrazovku pro registraci pacienta.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    public void changeStageToPacReg(MouseEvent mouseEvent) {
        anchorPaneRegistration.getChildren().clear();
        anchorPaneRegistration.getChildren().setAll(pacientReg);
    }

    /**
     * Metoda slou????c?? pro kontrolu, zda si u??ivatel nevybral ob?? role.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    @FXML
    public void checkIfOtherIsSetDoc(MouseEvent mouseEvent) {
        if(patientCheckbox.isSelected()) {
            patientCheckbox.setSelected(false);
        }
    }

    /**
     * Metoda slou????c?? pro kontrolu, zda si u??ivatel nevybral ob?? role.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    @FXML
    public void checkIfOtherIsSetPatient(MouseEvent mouseEvent) {
        if(doctorCheckbox.isSelected()) {
            doctorCheckbox.setSelected(false);
        }
    }

    /**
     * Metoda slou????c?? pro vyps??n?? n??pov??dy k aplikaci.
     *
     * @param mouseEvent kliknut?? m?????? na p????slu??n?? tla????tko
     */
    @FXML
    public void handleHelpClick(MouseEvent mouseEvent) throws IOException {
        String warnText = "Tahle aplikace je speci??ln?? vyvinut?? proto, aby ulevila\n" +
                "komunikaci mezi l??ka??em a potenci??ln??m pacientem.\n" +
                "Abyste aplikaci mohl vyu??ivat, mus??te se nejrpve zaregistrovat.\n" +
                "1) Uve??te sv??j email\n" +
                "2) Vypl??te si heslo\n" +
                "3) Dopln??te ??daje o sob??\n";
        WarningController warn = new WarningController(warnText, anchorPaneRegistration.getScene().getWindow());
    }
}
