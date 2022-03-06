package com.example.shareart.models;


public class Komentarioa {
    private String id;
    private String mezua;
    private String idErabiltzailea;
    private String idArgitalpen;
    private long data;

    public Komentarioa() {
    }

    public Komentarioa(String id, String mezua, String idErabiltzailea, String idArgitalpen, long data) {
        this.id = id;
        this.mezua = mezua;
        this.idErabiltzailea = idErabiltzailea;
        this.idArgitalpen = idArgitalpen;
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
