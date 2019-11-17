package com.sf.evento;

public class User
{
    public String full_name;
    public String password;
    public  String phoneNumber;

    public User(String full_name, String password, String phoneNumber) {
        this.full_name = full_name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
