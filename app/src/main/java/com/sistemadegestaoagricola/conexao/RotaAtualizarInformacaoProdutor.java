package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Produtor;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaAtualizarInformacaoProdutor implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;
    private String pathFoto;

    public RotaAtualizarInformacaoProdutor(String pathFoto){
        this.pathFoto = pathFoto;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "atualizar-informacao-produtor";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Content-Type","charset=UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("telefone",null, Usuario.getTelefone()));
        parametros.add(new Parametro("email",null, Usuario.getEmail()));
        parametros.add(new Parametro("data_nascimento",null, Util.converterPraBase64(Produtor.getDataNascimento())));
        parametros.add(new Parametro("rg",null, Produtor.getRg()));
        parametros.add(new Parametro("nome_conjuge",null, Util.converterPraBase64(Produtor.getNomeConjuge())));
        parametros.add(new Parametro("nome_filhos",null, Util.converterPraBase64(Produtor.getNomeFilhos())));
        parametros.add(new Parametro("foto",new Date().getTime() + ".jpg", this.pathFoto));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
