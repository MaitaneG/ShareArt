package com.example.shareart.models;


import java.util.Date;

public class Komentarioa {
    private int id;
    private String mezua;
    private String idErabiltzailea;
    private String idArgitalpen;
    private String data;

    public Komentarioa() {
    }

    public Komentarioa(int id, String mezua, String idErabiltzailea, String idArgitalpen, String data) {
        this.id = id;
        this.mezua = mezua;
        this.idErabiltzailea = idErabiltzailea;
        this.idArgitalpen = idArgitalpen;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMezua() {
        return mezua;
    }

    public void setMezua(String mezua) {
        this.mezua = mezua;
    }

    public String getIdErabiltzailea() {
        return idErabiltzailea;
    }

    public void setIdErabiltzailea(String idErabiltzailea) {
        this.idErabiltzailea = idErabiltzailea;
    }

    public String getIdArgitalpen() {
        return idArgitalpen;
    }

    public void setIdArgitalpen(String idArgitalpen) {
        this.idArgitalpen = idArgitalpen;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
