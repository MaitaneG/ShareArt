package com.example.shareart.models;


public class Komentarioa {
    private String id;
    private String mezua;
    private String id_erabiltzailea;
    private String id_argitalpen;
    private long data;

    public Komentarioa() {
    }

    public Komentarioa(String id, String mezua, String id_erabiltzailea, String id_argitalpen, long data) {
        this.id = id;
        this.mezua = mezua;
        this.id_erabiltzailea = id_erabiltzailea;
        this.id_argitalpen = id_argitalpen;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMezua() {
        return mezua;
    }

    public void setMezua(String mezua) {
        this.mezua = mezua;
    }

    public String getId_erabiltzailea() {
        return id_erabiltzailea;
    }

    public void setId_erabiltzailea(String id_erabiltzailea) {
        this.id_erabiltzailea = id_erabiltzailea;
    }

    public String getId_argitalpen() {
        return id_argitalpen;
    }

    public void setId_argitalpen(String id_argitalpen) {
        this.id_argitalpen = id_argitalpen;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
