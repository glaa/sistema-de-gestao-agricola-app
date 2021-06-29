package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaCadastrarPropriedade implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaCadastrarPropriedade(){

    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "cadastrar-propriedade";
//        //String parametros =
//                "?tamanho_total=" + Propriedade.getTamanho() +
//                "&fonte_de_agua="+ Propriedade.getFonteAgua() +
//                "&nome_rua=" + Propriedade.getLogradouro() +
//                "&numero_casa=" + Propriedade.getNumero() +
//                "&bairro=" + Propriedade.getBairro() +
//                "&cidade=" + Propriedade.getCidade() +
//                "&estado=" + Propriedade.getEstado() +
//                "&cep=" + Propriedade.getCep() +
//                "&ponto_referencia=" + Propriedade.getReferencia();
//                //"&mapa=" + Propriedade.getMapa();
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        //cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","charset=UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; image/jpeg; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("tamanho_total",null,Propriedade.getTamanho()));
        parametros.add(new Parametro("fonte_de_agua",null, Util.converterPraBase64(Propriedade.getFonteAgua())));
        parametros.add(new Parametro("nome_rua",null,Util.converterPraBase64(Propriedade.getLogradouro())));
        parametros.add(new Parametro("numero_casa",null,Propriedade.getNumero()));
        parametros.add(new Parametro("bairro",null,Util.converterPraBase64(Propriedade.getBairro())));
        parametros.add(new Parametro("cidade",null,Util.converterPraBase64(Propriedade.getCidade())));
        parametros.add(new Parametro("estado",null,Util.converterPraBase64(Propriedade.getEstado())));
        parametros.add(new Parametro("cep",null,Util.converterPraBase64(Propriedade.getCep())));
        parametros.add(new Parametro("ponto_referencia",null,Util.converterPraBase64(Propriedade.getReferencia())));
        parametros.add(new Parametro("mapa", new Date().getTime()+".jpg", Propriedade.getMapa()));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
