package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaAtualizarPropriedade implements Callable<ConexaoAPI> {

    public RotaAtualizarPropriedade(){}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "atualizar-propriedade";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        //cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","charset=UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("tamanho_total",null, Propriedade.getTamanho()));
        parametros.add(new Parametro("fonte_de_agua",null, Util.converterPraBase64(Propriedade.getFonteAgua())));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
