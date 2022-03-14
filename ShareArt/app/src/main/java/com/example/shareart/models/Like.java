package com.example.shareart.models;

public class Like {

    private String id;
    private String id_erabiltzaile;
    private String id_argitalpen;
    private long data;

    public Like() {
    }

    public Like(String id, String id_erabiltzaile, String id_argitalpen, long data) {
        this.id = id;
        this.id_erabiltzaile = id_erabiltzaile;
        this.id_argitalpen = id_argitalpen;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_erabiltzaile() {
        return id_erabiltzaile;
    }

    public void setId_erabiltzaile(String id_erabiltzaile) {
        this.id_erabiltzaile = id_erabiltzaile;
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
