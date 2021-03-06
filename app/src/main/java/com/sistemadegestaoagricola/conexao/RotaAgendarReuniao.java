package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaAgendarReuniao implements Callable<ConexaoAPI> {
    private String tema;
    private String data;
    private String local;

    public RotaAgendarReuniao(String tema, String data, String local){
        this.tema = tema;
        this.data = data;
        this.local = local;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "agendar-reuniao";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());
        cabecalhos.put("Content-Type","multipart/form-data; charset=UTF-8; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("nome",null, Util.converterPraBase64(tema)));
        parametros.add(new Parametro("data",null,Util.converterPraBase64(data)));
        parametros.add(new Parametro("local",null,Util.converterPraBase64(local)));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);
        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;
    }
}
