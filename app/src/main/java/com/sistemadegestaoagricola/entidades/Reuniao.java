package com.sistemadegestaoagricola.entidades;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Reuniao {

    private static ArrayList<String> atasBase64;
    private static ArrayList<String> fotosBase64;
    private static ArrayList<Bitmap> atasBitmap;
    private static ArrayList<Bitmap> fotosBitmap;


    public Reuniao() {
    }

    public static ArrayList<String> getAtasBase64() {
        return atasBase64;
    }

    public static void setAtasBase64(ArrayList<String> atasBase64) {
        Reuniao.atasBase64 = atasBase64;
    }

    public static ArrayList<String> getFotosBase64() {
        return fotosBase64;
    }

    public static void setFotosBase64(ArrayList<String> fotosBase64) {
        Reuniao.fotosBase64 = fotosBase64;
    }

    public static ArrayList<Bitmap> getAtasBitmap() {
        return atasBitmap;
    }

    public static void setAtasBitmap(ArrayList<Bitmap> atasBitmap) {
        Reuniao.atasBitmap = atasBitmap;
    }

    public static ArrayList<Bitmap> getFotosBitmap() {
        return fotosBitmap;
    }

    public static void setFotosBitmap(ArrayList<Bitmap> fotosBitmap) {
        Reuniao.fotosBitmap = fotosBitmap;
    }
}
