package cz.vse.fimed.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Pacient extends User {
private ArrayList<Symptom> symptoms;
private int weight;
private int height;

    public Pacient(String email, String password, String phone, String name, String surname, boolean status, int age, String gender, ArrayList<Symptom> symptoms, int weight, int height, String address) {
        super(email, password, phone, name, surname, status, age, gender, address);
        this.symptoms = symptoms;
        this.weight = weight;
        this.height = height;
    }


    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
