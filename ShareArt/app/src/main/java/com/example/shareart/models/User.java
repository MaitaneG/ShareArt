package com.example.shareart.models;

public class User {
    private String id;
    private String erabiltzailea;
    private String email;
    private String pasahitza;

    public User() {

    }

    public User(String id, String erabiltzailea, String email) {
        this.id = id;
        this.erabiltzailea = erabiltzailea;
        this.email = email;
    }



    public User(String id, String erabiltzailea, String email, String pasahitza) {
        this.id = id;
        this.erabiltzailea = erabiltzailea;
        this.email = email;
        this.pasahitza = pasahitza;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }
}
