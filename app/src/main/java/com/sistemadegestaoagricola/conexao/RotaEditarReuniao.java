package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaEditarReuniao implements Callable<ConexaoAPI> {

    private String tema;
    private String data;
    private String local;
    private int idReuniao;

    public RotaEditarReuniao(String tema, String data, String local, int idReuniao){
        this.tema = tema;
        this.data = data;
        this.local = local;
        this.idReuniao = idReuniao;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "editar-agenda-reuniao";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());
        cabecalhos.put("Content-Type","multipart/form-data; charset=UTF-8; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("id",null,idReuniao));
        parametros.add(new Parametro("nome",null, Util.converterPraBase64(tema)));
        parametros.add(new Parametro("data",null,Util.converterPraBase64(data)));
        parametros.add(new Parametro("local",null,Util.converterPraBase64(local)));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);
        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;
    }
}
