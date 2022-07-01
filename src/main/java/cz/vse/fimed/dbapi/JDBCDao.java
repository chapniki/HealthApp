package cz.vse.fimed.dbapi;


import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.Symptom;
import cz.vse.fimed.profile.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class JDBCDao {
    public static int patient_id = -1;
    public static int doctor_id = -1;

    static Connection connection;
    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/cz/vse/fimed/db/main.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Metoda odstraní symptomy.
     *
     */
    public static void clearSymps() throws SQLException {
        Statement statementDel = connection.createStatement();
        String queryDel = "DELETE FROM symthom WHERE symthom_id IN (SELECT symthom_symthom_id FROM relation_1 WHERE pacient_pacient_id = " + patient_id + ")";
        statementDel.executeUpdate(queryDel);
    }
    /**
     * Metoda umožnuje změnu hesla.
     *
     */
    public static void changePassword(int userId, String newPassword) throws SQLException {
        Statement statementUpd = connection.createStatement();
        if(patient_id != -1) {
            String queryUpd = "UPDATE pacient SET passwordpac = '" + newPassword + "' WHERE pacient_id = " + userId + "";
            statementUpd.executeUpdate(queryUpd);
        } else if(doctor_id != -1) {
            String queryUpd = "UPDATE doctor SET passworddoc = '" + newPassword + "' WHERE doctor_id = " + userId + "";
            statementUpd.executeUpdate(queryUpd);
        }
    }
    /**
     * Metoda umožnuje přidat lékaře do databaze.
     *
     */
    public static ArrayList<Doctor> getDoctors() throws SQLException {
        ArrayList<Doctor> doctors = new ArrayList<>();
        Statement statement = connection.createStatement();
        String queryPat = "SELECT * FROM doctor";
        ResultSet doctorsFromDb = statement.executeQuery(queryPat);
        while (doctorsFromDb.next()) {
             doctors.add(new Doctor(doctorsFromDb.getString("emaildoc"), doctorsFromDb.getString("passworddoc"),
                     doctorsFromDb.getString("telefonedoc"), doctorsFromDb.getString("namedoc"),
                     doctorsFromDb.getString("surnamedoc"),
                     true, doctorsFromDb.getInt("yearsolddoc"), doctorsFromDb.getString("genderdoc"),
                     doctorsFromDb.getString("specialization"), doctorsFromDb.getString("education"),
                     doctorsFromDb.getString("adress"), doctorsFromDb.getString("idhospital"), doctorsFromDb.getInt("experiencedoc")));
        }
        return doctors;
    }
    /**
     * Metoda přidat pacienta do databaze.
     *
     */
    public static ArrayList<Pacient> getPatients() throws SQLException {
        ArrayList<Pacient> patients = new ArrayList<>();
        Statement statement = connection.createStatement();
        String queryPat = "SELECT * FROM pacient WHERE pacient_id IN (SELECT pacient_pacient_id FROM relation_2 WHERE doctor_doctor_id = " + doctor_id + ")";
        ResultSet patientsFromDb = statement.executeQuery(queryPat);
        while (patientsFromDb.next()) {
            patients.add(new Pacient(patientsFromDb.getString("emailpac"), patientsFromDb.getString("passwordpac"),
                    patientsFromDb.getString("telefonepac"), patientsFromDb.getString("namepac"),
                    patientsFromDb.getString("surnamepac"),
                    true, patientsFromDb.getInt("yearsoldpac"), patientsFromDb.getString("genderpac"),
                    getSymthoms(patientsFromDb.getInt("pacient_id")), patientsFromDb.getInt("weight"), patientsFromDb.getInt("height"), patientsFromDb.getString("addresspac")));
        }
        return patients;
    }
    /**
     * Metoda odstraní lékaře či pacienta z databaze.
     *
     */
    public static void deleteUser(int userId) throws SQLException {
        Statement statementDel = connection.createStatement();
        if(patient_id != -1) {
            String queryDel = "DELETE FROM pacient WHERE pacient_id = " + userId + "";
            statementDel.executeUpdate(queryDel);
            patient_id = -1;
        } else if(JDBCDao.doctor_id != -1) {
            doctor_id = -1;
            String queryDel = "DELETE FROM doctor WHERE doctor_id = " + userId + "";
            statementDel.executeUpdate(queryDel);
        }
    }
    /**
     * Metoda přidává symptomy do databaze.
     *
     */
    public static void addSymthom(String sympthom) throws SQLException {
        Statement statementSymt = connection.createStatement();
        Statement statementRel = connection.createStatement();
        String querySymt = "INSERT INTO symthom (symthom1) VALUES ('" + sympthom.strip() + "')";
        statementSymt.executeUpdate(querySymt);
        int addedSympthomId = statementSymt.getGeneratedKeys().getInt(1);
        String queryRel = "INSERT INTO relation_1 (symthom_symthom_id, pacient_pacient_id) VALUES (" + addedSympthomId + ", " + patient_id + ")";
        statementRel.executeUpdate(queryRel);
    }
    /**
     * Metoda vrátí Metoda vrátí informce o uživateli pomocí hesla a email.
     *</p>
     *
     * @param email
     * @param password
     */
    public static User checkUserLoginData(String email, String password) throws SQLException {
        String queryPat = "SELECT * FROM pacient WHERE passwordpac LIKE '"+password+"' AND emailpac LIKE '"+email+"' LIMIT 1";
        String queryDoc = "SELECT * FROM doctor WHERE passworddoc LIKE '"+password+"' AND emaildoc LIKE '"+email+"' LIMIT 1";
        Statement statementPat = connection.createStatement();
        Statement statementDoc = connection.createStatement();
        ResultSet dataPat = statementPat.executeQuery(queryPat);
        ResultSet dataDoc = statementDoc.executeQuery(queryDoc);
        if (dataPat.next()) {
            patient_id = dataPat.getInt("pacient_id");
            return new Pacient(dataPat.getString("emailpac"), dataPat.getString("passwordpac"),
                    dataPat.getString("telefonepac"), dataPat.getString("namepac"),
                    dataPat.getString("surnamepac"),
                    true, dataPat.getInt("yearsoldpac"), dataPat.getString("genderpac"),
                    null, dataPat.getInt("weight"), dataPat.getInt("height"), dataPat.getString("addresspac"));
        } else if (dataDoc.next()) {
            doctor_id = dataDoc.getInt("doctor_id");
            return new Doctor(dataDoc.getString("emaildoc"), dataDoc.getString("passworddoc"),
                    dataDoc.getString("telefonedoc"), dataDoc.getString("namedoc"),
                    dataDoc.getString("surnamedoc"),
                    true, dataDoc.getInt("yearsolddoc"), dataDoc.getString("genderdoc"),
                    dataDoc.getString("specialization"), dataDoc.getString("education"),
                    dataDoc.getString("adress"), dataDoc.getString("idhospital"), dataDoc.getInt("experiencedoc"));
        } else {
            return null;
        }
    }
    /**
     * Metoda vrátí informce o uživateli.
     *</p>
     *
     * @param userId vrátí informace pomocí userID.
     */
    public static User getUserInfo(int userId) throws SQLException {
        String queryPat = "SELECT * FROM pacient WHERE pacient_id = "+userId+" LIMIT 1";
        String queryDoc = "SELECT * FROM doctor WHERE doctor_id = "+userId+" LIMIT 1";
        Statement statementPat = connection.createStatement();
        Statement statementDoc = connection.createStatement();
        ResultSet dataPat = statementPat.executeQuery(queryPat);
        ResultSet dataDoc = statementDoc.executeQuery(queryDoc);
        if (dataPat.next() && JDBCDao.patient_id != -1) {
            patient_id = dataPat.getInt("pacient_id");
            return new Pacient(dataPat.getString("emailpac"), dataPat.getString("passwordpac"),
                    dataPat.getString("telefonepac"), dataPat.getString("namepac"),
                    dataPat.getString("surnamepac"),
                    true, dataPat.getInt("yearsoldpac"), dataPat.getString("genderpac"),
                    getSymthoms(patient_id), dataPat.getInt("weight"), dataPat.getInt("height"), dataPat.getString("addresspac"));
        } else if (dataDoc.next() && JDBCDao.doctor_id != -1) {
            doctor_id = dataDoc.getInt("doctor_id");
            return new Doctor(dataDoc.getString("emaildoc"), dataDoc.getString("passworddoc"),
                    dataDoc.getString("telefonedoc"), dataDoc.getString("namedoc"),
                    dataDoc.getString("surnamedoc"),
                    true, dataDoc.getInt("yearsolddoc"), dataDoc.getString("genderdoc"),
                    dataDoc.getString("specialization"), dataDoc.getString("education"),
                    dataDoc.getString("adress"), dataDoc.getString("idhospital"), dataDoc.getInt("experiencedoc"));
        } else {
            return null;
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    /**
     * Metoda registruje uživatele.
     *
     */
    public static void registerUser(Pacient pac, Doctor doc) throws SQLException {
        Statement statement = connection.createStatement();
        if (doc == null){
            String query1 = "INSERT INTO Pacient " + "VALUES ('"+pac.getPassword()+"', '"+pac.getEmail()+"', null,  null, null, null, null, null, null, null, null)";
            statement.executeUpdate(query1);
            patient_id = statement.getGeneratedKeys().getInt(1);
        }
        else {
            String query2 = "INSERT INTO Doctor " + "VALUES ('"+doc.getPassword()+"', '"+doc.getEmail()+"', null,  null, null, null, null, null, null, null, null, null, null)";
            statement.executeUpdate(query2);
            doctor_id = statement.getGeneratedKeys().getInt(1);
        }
    }
    /**
     * Metoda registruje pacienta.
     *
     */
    public static void registerPacient(Pacient pac) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "UPDATE pacient " +
                "SET telefonepac = '"+ pac.getPhone() + "', namepac = '" + pac.getName() + "', yearsoldpac = " + pac.getAge() + "," +
                " genderpac = '" + pac.getGender() + "', weight = " + pac.getWeight() + ", height = " + pac.getHeight() + "," +
                " surnamepac = '" + pac.getSurname() + "', addresspac = '" + pac.getAddress() + "', emailpac = " +
                "'" + pac.getEmail() + "' WHERE pacient_id = " + patient_id;
        statement.executeUpdate(query);
    }
    /**
     * Metoda registruje lékaře.
     *
     */
    public static void registerDoctor(Doctor doc) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "UPDATE doctor " +
                "SET telefonedoc = '"+ doc.getPhone() + "', namedoc = '" + doc.getName() + "', yearsolddoc = " + doc.getAge() + "," +
                " genderdoc = '" + doc.getGender() + "', specialization = '" + doc.getSpecialization() + "', education = '" + doc.getEducation() + "'," +
                " surnamedoc = '" + doc.getSurname() + "', adress = '" + doc.getAddress() + "', experiencedoc = " + doc.getExperience() + "," +
                " idhospital = '" + doc.getHospital() + "', emaildoc =  '" + doc.getEmail() + "'" +
                " WHERE doctor_id = " + doctor_id;
        statement.executeUpdate(query);
    }

    public static void makeAppointment(Pacient user, int docID) throws SQLException {
        String query2 = "INSERT INTO relation_2 VALUES("+user.getId()+", "+docID+")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query2);
    }
    /**
     * Metoda si umožnuje pacientovi zvolit lékaře.
     *
     */

    public static void makeAppointment(String docEmail) throws SQLException {
        Statement statementFind = connection.createStatement();
        String queryFind = "SELECT doctor_id FROM doctor WHERE emaildoc = '" + docEmail + "' LIMIT 1";
        int docId = statementFind.executeQuery(queryFind).getInt("doctor_id");
        String queryMake = "INSERT INTO relation_2 VALUES (" + patient_id + ", " + docId + ")";
        Statement statementMake = connection.createStatement();
        statementMake.executeUpdate(queryMake);
    }
    /**
     * Metoda kontroluje, jestli pacient vybral lékaře.
     *</p>
     *
     * @param docEmail
     */
    public static boolean checkAppointment(String docEmail) throws SQLException {
        Statement statementFind = connection.createStatement();
        String queryFind = "SELECT doctor_id FROM doctor WHERE emaildoc = '" + docEmail + "' LIMIT 1";
        int docId = statementFind.executeQuery(queryFind).getInt("doctor_id");
        String queryMake = "SELECT * FROM relation_2 WHERE pacient_pacient_id = " + patient_id + " AND doctor_doctor_id = " + docId + " ";
        Statement statementMake = connection.createStatement();
        ResultSet data = statementMake.executeQuery(queryMake);

        return data.next();
    }

    /**
     * Metoda zobrazí symptomy pacinta pomocí userID.
     *</p>
     *
     * @param pac_id
     */

    public static ArrayList<Symptom> getSymthoms(int pac_id) throws SQLException {
        String query2 = "SELECT symthom1 FROM symthom WHERE symthom_id IN " +
                "(SELECT symthom_symthom_id FROM relation_1 WHERE pacient_pacient_id = "+ pac_id +")";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query2);
        ArrayList<Symptom> sympt = new ArrayList<>();

        while (resultSet.next()) {
            String sympthom = resultSet.getString(1);
            sympt.add(new Symptom(sympthom));
        }
        return sympt;
    }

    public static int getUserId(Pacient pacient, Doctor doctor) throws SQLException {
        int id = 0;
        Statement statementFind = connection.createStatement();
        if(pacient == null){

            String queryFind = "SELECT doctor_id FROM doctor WHERE emaildoc = '" + doctor.getEmail() + "' LIMIT 1";
            ResultSet resultSet = statementFind.executeQuery(queryFind);
            id = resultSet.getInt(1);
        } else {String queryFind = "SELECT pacient_id FROM pacient WHERE emailpac = '" + pacient.getEmail() + "' LIMIT 1";
            ResultSet resultSet = statementFind.executeQuery(queryFind);
            id = resultSet.getInt(1);}
        return id;
    }


}

