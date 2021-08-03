package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaAtualizarEndereco implements Callable<ConexaoAPI> {

    public RotaAtualizarEndereco(){}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "atualizar-endereco";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        //cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","charset=UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("nome_rua",null,Util.converterPraBase64(Propriedade.getLogradouro())));
        parametros.add(new Parametro("numero_casa",null,Propriedade.getNumero()));
        parametros.add(new Parametro("bairro",null,Util.converterPraBase64(Propriedade.getBairro())));
        parametros.add(new Parametro("cidade",null,Util.converterPraBase64(Propriedade.getCidade())));
        parametros.add(new Parametro("estado",null,Util.converterPraBase64(Propriedade.getEstado())));
        parametros.add(new Parametro("cep",null,Util.converterPraBase64(Propriedade.getCep())));
        parametros.add(new Parametro("ponto_referencia",null,Util.converterPraBase64(Propriedade.getReferencia())));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
