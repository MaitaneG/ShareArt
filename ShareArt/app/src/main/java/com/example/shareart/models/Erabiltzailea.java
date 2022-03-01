package com.example.shareart.models;

public class Erabiltzailea {
    private String id;
    private String erabiltzaileIzena;
    private String email;
    private String pasahitza;
    private String argazkiaProfilaUrl;
    private long sortzeData;

    public Erabiltzailea() {

    }

    public Erabiltzailea(String id, String erabiltzaileIzena, String email, String pasahitza, String argazkiaProfilaUrl, long sortzeData) {
        this.id = id;
        this.erabiltzaileIzena = erabiltzaileIzena;
        this.email = email;
        this.pasahitza = pasahitza;
        this.argazkiaProfilaUrl = argazkiaProfilaUrl;
        this.sortzeData = sortzeData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErabiltzaileIzena() {
        return erabiltzaileIzena;
    }

    public void setErabiltzaileIzena(String erabiltzaileIzena) {
        this.erabiltzaileIzena = erabiltzaileIzena;
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

    public String getArgazkiaProfilaUrl() {
        return argazkiaProfilaUrl;
    }

    public void setArgazkiaProfilaUrl(String argazkiaProfilaUrl) {
        this.argazkiaProfilaUrl = argazkiaProfilaUrl;
    }

    public long getSortzeData() {
        return sortzeData;
    }

    public void setSortzeData(long sortzeData) {
        this.sortzeData = sortzeData;
    }
}
