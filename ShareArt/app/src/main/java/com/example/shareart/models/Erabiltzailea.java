package com.example.shareart.models;

public class Erabiltzailea {
    private String id;
    private String erabiltzaile_izena;
    private String email;
    private String pasahitza;
    private String deskribapena;
    private String argazkia_profila_url;
    private boolean egiaztatua;
    private long sortze_data;

    public Erabiltzailea() {
    }

    public Erabiltzailea(String id, String erabiltzaile_izena, String email, String pasahitza, String deskribapena, String argazkia_profila_url, boolean egiaztatua, long sortze_data) {
        this.id = id;
        this.erabiltzaile_izena = erabiltzaile_izena;
        this.email = email;
        this.pasahitza = pasahitza;
        this.deskribapena = deskribapena;
        this.argazkia_profila_url = argazkia_profila_url;
        this.egiaztatua = egiaztatua;
        this.sortze_data = sortze_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErabiltzaile_izena() {
        return erabiltzaile_izena;
    }

    public void setErabiltzaile_izena(String erabiltzaile_izena) {
        this.erabiltzaile_izena = erabiltzaile_izena;
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

    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public String getArgazkia_profila_url() {
        return argazkia_profila_url;
    }

    public void setArgazkia_profila_url(String argazkia_profila_url) {
        this.argazkia_profila_url = argazkia_profila_url;
    }

    public long getSortze_data() {
        return sortze_data;
    }

    public void setSortze_data(long sortze_data) {
        this.sortze_data = sortze_data;
    }

    public boolean isEgiaztatua() {
        return egiaztatua;
    }

    public void setEgiaztatua(boolean egiaztatua) {
        this.egiaztatua = egiaztatua;
    }
}
