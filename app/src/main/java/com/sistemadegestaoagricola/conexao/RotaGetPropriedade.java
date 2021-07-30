package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaGetPropriedade implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaGetPropriedade(){}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-propriedade";
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

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("propriedade")) {
                        jsonReader.beginObject();
                        int i = 0;
                        while(jsonReader.hasNext()){
                            Log.d("testeX", i++ + "");
                            String chave = jsonReader.nextName();
                            if(chave.equals("tamanho_total")){
                                Propriedade.setTamanho(jsonReader.nextInt());
                                continue;
                            } else if(chave.equals("fonte_de_agua")){
                                Propriedade.setFonteAgua(jsonReader.nextString());
                                continue;
                            }
                            Log.d("testeX", "propriedade = " + chave );
                            jsonReader.skipValue();
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }

                jsonReader.endObject();
                jsonReader.close();
            } catch (IOException e) {
                mensagensExceptions = new String[] {"Erro com os dados obtidos do Usuário","Tente novamente em alguns minutos"};
                con.setMensagensExceptions(mensagensExceptions);
                e.printStackTrace();
            }
        }
        return con;
    }
}
