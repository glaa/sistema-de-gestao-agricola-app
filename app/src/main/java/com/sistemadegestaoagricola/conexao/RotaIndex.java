package com.sistemadegestaoagricola.conexao;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaIndex implements Callable<ConexaoAPI> {

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "index";
        String boundary = null;
        String metodo = "GET";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Accept","application/json");

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);
        Log.d("testeX","conectou");
        return con;
    }
}
