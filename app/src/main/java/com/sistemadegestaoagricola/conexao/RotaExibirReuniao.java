package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Reuniao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaExibirReuniao implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;
    private int id;

    public RotaExibirReuniao(int id) {
        this.id = id;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-exibir-reuniao/"+id;
        String boundary = null;
        String metodo = "GET";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);
        Log.d("testeX","status user: " + con.getCodigoStatus());

        if(con.getCodigoStatus() == 200){
            try {
                InputStream responseBody = ConexaoAPI.getConexao().getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                jsonReader.beginObject();

                ArrayList<String> atas = new ArrayList<>();
                ArrayList<String> fotos = new ArrayList<>();

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("atas")) {
                        jsonReader.beginArray();
                        while(jsonReader.hasNext()){
                            String valor = jsonReader.nextString();
                            atas.add(valor);
                        }
                        jsonReader.endArray();
                    } else if(key.equals("fotos")){
                        jsonReader.beginArray();
                        while(jsonReader.hasNext()){
                            String valor = jsonReader.nextString();
                            fotos.add(valor);
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                jsonReader.close();

                Reuniao.setAtasBase64(atas);
                Reuniao.setFotosBase64(fotos);
            } catch (IOException e) {
                mensagensExceptions = new String[] {"Erro com os dados obtidos do Usuário","Tente novamente em alguns minutos"};
                con.setMensagensExceptions(mensagensExceptions);
                e.printStackTrace();
            }
        }
        return con;
    }
}
