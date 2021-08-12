package com.sistemadegestaoagricola.conexao;

import android.net.Uri;
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

    public RotaRegistrarReuniao(int idAgendaReuniao){
        this.id = idAgendaReuniao;
        this.ata = Util.getAta();
        this.fotos = Util.getFotos();
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
            parametros.add(new Parametro(
                    "ata[]", "ata_" + i + "_" + new Date().getTime() + ".jpg", ata.get(i)));
            Log.d("testeX", "adicionou = " +"ata_" + i + "_" + new Date().getTime() + ".jpg " +  ata.get(i));
        }

        for(int i = 0; i < this.fotos.size(); i++){
            parametros.add(new Parametro(
                    "fotos[]", "fotos_" + i + "_" + new Date().getTime() + ".jpg", fotos.get(i)));
            Log.d("testeX", "adicionou = " + "fotos_" + i + "_" + new Date().getTime() + ".jpg " +  fotos.get(i));

        }

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        return con;

    }
}
