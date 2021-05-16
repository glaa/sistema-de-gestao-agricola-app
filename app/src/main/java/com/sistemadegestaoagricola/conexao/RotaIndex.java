package com.sistemadegestaoagricola.conexao;

import android.util.Log;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaIndex implements Callable<ConexaoAPI> {

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "index";
        String parametros = "";
        String metodo = "GET";
        Map<String,String> propriedades = null;
        ConexaoAPI con = new ConexaoAPI(rota,parametros,metodo,propriedades);
        Log.d("testeX","conectou");
        return con;
    }
}
