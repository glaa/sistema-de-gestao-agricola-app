package com.sistemadegestaoagricola.conexao;

import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaSalvarMapa implements Callable<ConexaoAPI>{

    private String[] mensagensExceptions = null;
    private String path;

    public RotaSalvarMapa(String path){
        this.path = path;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "salvar-mapa";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String, String> cabecalhos = new HashMap<String, String>();
        //cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type", "charset=UTF-8");
        cabecalhos.put("Content-Type", "multipart/form-data; image/jpeg; boundary=" + boundary);
        cabecalhos.put("Accept", "application/json");
        cabecalhos.put("Authorization", "Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("mapa", new Date().getTime() + ".jpg", this.path));

        Requisicao requisicao = new Requisicao(metodo, cabecalhos, parametros, boundary);

        ConexaoAPI con = new ConexaoAPI(rota, requisicao);

        return con;
    }
}
