package com.sistemadegestaoagricola;

import android.os.AsyncTask;

import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;

public class Conectar extends AsyncTask {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ConexaoAPIVelha c = (ConexaoAPIVelha) objects[0];
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
