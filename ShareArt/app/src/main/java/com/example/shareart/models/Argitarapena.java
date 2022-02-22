package com.example.shareart.models;

public class Argitarapena {

    private String id;
    private String deskribapena;
    private String url_argazkia;
    private String kategoria;

    public Argitarapena(String id, String deskribapena, String url_argazkia, String kategoria) {
        this.id = id;
        this.deskribapena = deskribapena;
        this.url_argazkia = url_argazkia;
        this.kategoria = kategoria;
    }

    public Argitarapena() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
