package com.app.voicePrescription.model;

import com.google.gson.annotations.SerializedName;

public class Prescription {

    @SerializedName("name")
    private String name;

    @SerializedName("age")
    private String age;

    @SerializedName("mobile")
    private String mobile;
    @SerializedName("email")
    private String email;
    @SerializedName("symptoms")
    private String symptoms;
    @SerializedName("medicine")
    private String medicine;

    public String getName() {
        return name;
    }

    public Prescription(String name, String age, String mobile, String email, String symptoms, String medicine) {
        this.name = name;
        this.age = age;
        this.mobile = mobile;
        this.email = email;
        this.symptoms = symptoms;
        this.medicine = medicine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

}
