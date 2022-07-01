package cz.vse.fimed.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Doctor extends User {
    private String specialization;
    private String education;
    private String hospital;
    private int experience;

    public Doctor(String email, String password, String phone, String name, String surname, boolean status, int age, String gender, String specialization, String education, String address, String hospital, int experience) {
        super(email, password, phone, name, surname, status, age, gender, address);
        this.specialization = specialization;
        this.education = education;
        this.hospital = hospital;
        this.experience = experience;
    }


    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
