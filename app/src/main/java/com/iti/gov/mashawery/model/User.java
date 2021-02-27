package com.iti.gov.mashawery.model;

public class User {

    private String userName;
    private String email;
    private String password;


    public String getUserName() {
        return userName;
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

   /* public User(String email, String password) {
        this.email = email;
        this.password = password;
    }*/

    public User() {

    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
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
}
