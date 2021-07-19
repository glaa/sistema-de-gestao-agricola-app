package com.sistemadegestaoagricola.conexao;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaRegistrarReuniao implements Callable<ConexaoAPI> {
    private String[] mensagensExceptions = null;
    private int id;
    private ArrayList<String> ata = new ArrayList<>();
    private ArrayList<String> fotos = new ArrayList<>();

    public RotaRegistrarReuniao(int idAgendaReuniao, ArrayList<String> ata, ArrayList<String> fotos){
        this.id = idAgendaReuniao;
        this.ata = ata;
        this.fotos = fotos;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "registrar-reuniao";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        //cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","charset=UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; image/jpeg; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("id_agenda",null, this.id));

        for(int i = 0; i < this.ata.size(); i++){
            parametros.add(new Parametro("ata", new Date().getTime()+".jpg", ata.get(i)));
            Log.d("testeX", "adicionou = " + new Date().getTime()+".jpg + " +  ata.get(i));
        }

        for(int i = 0; i < this.fotos.size(); i++){
            parametros.add(new Parametro("foto", new Date().getTime()+".jpg", fotos.get(i)));
            Log.d("testeX", "adicionou = " + new Date().getTime()+".jpg + " +  fotos.get(i));

        }

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
