package com.example.shareart.models;

public class Like {

    private String id;
    private String idErabiltzaile;
    private String idArgiltapen;
    private String data;

    public Like() {
    }

    public Like(String id, String idErabiltzaile, String idArgiltapen, String data) {
        this.id = id;
        this.idErabiltzaile = idErabiltzaile;
        this.idArgiltapen = idArgiltapen;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdErabiltzaile() {
        return idErabiltzaile;
    }

    public void setIdErabiltzaile(String idErabiltzaile) {
        this.idErabiltzaile = idErabiltzaile;
    }

    public String getIdArgiltapen() {
        return idArgiltapen;
    }

    public void setIdArgiltapen(String idArgiltapen) {
        this.idArgiltapen = idArgiltapen;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
