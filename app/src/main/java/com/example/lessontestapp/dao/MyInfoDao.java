package com.example.lessontestapp.dao;


import java.util.List;

public class MyInfoDao {

    public int getInfoID() {
        return infoID;
    }

    public void setInfoID(int infoID) {
        this.infoID = infoID;
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

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public int getSpecialtyID() {
        return specialtyID;
    }

    public void setSpecialtyID(int specialtyID) {
        this.specialtyID = specialtyID;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    private int infoID;
    private String name;
    private String phone;
    private String gender;
    private List<String> hobbies;
    private String birthPlace;
    private int specialtyID;
    private String profile;

    @Override
    public String toString() {
        return "MyFriendInfoDao{" +
                "infoID=" + infoID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", hobbies=" + hobbies +
                ", birthPlace='" + birthPlace + '\'' +
                ", specialtyID=" + specialtyID +
                ", profile='" + profile + '\'' +
                '}';
    }


}
