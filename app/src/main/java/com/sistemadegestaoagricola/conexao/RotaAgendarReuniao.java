package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaAgendarReuniao implements Callable<ConexaoAPI> {
    private String tema;
    private Date data;
    private String local;
    private String[] mensagensExceptions = null;

    public RotaAgendarReuniao(String tema, Date data, String local){
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
        cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("nome",null,tema));
        parametros.add(new Parametro("data",null,data));
        parametros.add(new Parametro("local",null,local));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);
        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;
    }
}
