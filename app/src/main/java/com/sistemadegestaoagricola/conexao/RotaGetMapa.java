package com.sistemadegestaoagricola.conexao;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Mapa;
import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaGetMapa implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;
    private int id;

    public RotaGetMapa(int id){ this.id = id;}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-mapa/" + id;
        String boundary = null;
        String metodo = "GET";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Accept","application/json");
        cabecalhos.put("Authorization","Bearer " + ConexaoAPI.getToken());

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);

        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        if(con.getCodigoStatus() == 200){
            try {
                InputStream responseBody = ConexaoAPI.getConexao().getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                jsonReader.beginObject();
                while(jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    if(key.equals("mapa")){
                        while(jsonReader.hasNext()){
                            ArrayList<Mapa> mapas = Mapa.getMapas();
                            for(Mapa mapa : mapas){
                                if(mapa.getId() == id){
                                    String foto = jsonReader.nextString();
                                    Bitmap bitmap = Util.converterStringParaBitmap(foto);
                                    mapa.setFoto(bitmap);
                                }
                            }
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
