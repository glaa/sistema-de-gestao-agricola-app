package com.sistemadegestaoagricola.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Mapa implements Serializable, Comparable{

    private Date data;
    private Bitmap foto;
    private int id;
    private static ArrayList<Mapa> mapas = new ArrayList<>();

    public Mapa(){}

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public static ArrayList<Mapa> getMapas() {
        return mapas;
    }

    public static void setMapas(ArrayList<Mapa> mapas) {
        Mapa.mapas = mapas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        Mapa agenda = (Mapa) o;
        if(this.data.compareTo(agenda.getData()) > 0){
            return 1;
        } else if(this.data.compareTo(agenda.getData()) < 0){
            return -1;
        }
        return 0;
    }
}
