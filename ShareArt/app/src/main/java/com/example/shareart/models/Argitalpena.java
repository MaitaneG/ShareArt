package com.example.shareart.models;

import java.util.Date;

public class Argitalpena {

    private String id;
    private String id_user;
    private String deskribapena;
    private String url_argazkia;
    private String kategoria;
    private String data;

    public Argitalpena() {
    }

    public Argitalpena(String id, String id_user, String deskribapena, String url_argazkia, String kategoria, String data) {
        this.id = id;
        this.id_user = id_user;
        this.deskribapena = deskribapena;
        this.url_argazkia = url_argazkia;
        this.kategoria = kategoria;
        this.data=data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public String getUrl_argazkia() {
        return url_argazkia;
    }

    public void setUrl_argazkia(String url_argazkia) {
        this.url_argazkia = url_argazkia;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
