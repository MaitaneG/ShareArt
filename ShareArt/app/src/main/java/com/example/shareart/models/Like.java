package com.example.shareart.models;

public class Like {

    private String id;
    private String idErabiltzaile;
    private String idArgitalpen;
    private String data;

    public Like() {
    }

    public Like(String id, String idErabiltzaile, String idArgitalpen, String data) {
        this.id = id;
        this.idErabiltzaile = idErabiltzaile;
        this.idArgitalpen = idArgitalpen;
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

    public String getIdArgitalpen() {
        return idArgitalpen;
    }

    public void setIdArgiltapen(String idArgitalpen) {
        this.idArgitalpen = idArgitalpen;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
