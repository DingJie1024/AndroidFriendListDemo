package com.example.lessontestapp.dao;


import java.util.List;

public class MyFriendInfo {

    private boolean isFollow = false;
    private int infoID;
    private String name;
    private String phone;
    private String gender;
    private List<String> hobbies;
    private String specialty;
    private String birthPlace;

    @Override
    public String toString() {
        return "MyFriendInfo{" +
                "isFollow=" + isFollow +
                ", infoID=" + infoID +
                ", name=" + name +
                ", phone=" + phone +
                ", gender=" + gender +
                ", hobbies=" + hobbies +
                ", specialty=" + specialty +
                ", birthPlace=" + birthPlace +
                '}';
    }

    public int getInfoID() {
        return infoID;
    }

    public void setInfoID(int infoID) {
        this.infoID = infoID;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }


    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
}
