package cz.vse.fimed.profile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class User {
    private int id;
    private String email;
    private String password;
    private String phone;
    private String name;
    private String surname;
    private String address;
    private boolean status;
    private int age;
    private String gender;

    public User(String email, String password, String phone, String name, String surname, boolean status, int age, String gender, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
