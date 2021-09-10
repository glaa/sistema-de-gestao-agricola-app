package com.sistemadegestaoagricola.conexao;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Mapa;
import com.sistemadegestaoagricola.entidades.Parametro;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Reuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaGetMapas implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaGetMapas(){}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-mapas";
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

                ArrayList<Mapa> mapas = new ArrayList<>();

                jsonReader.beginObject();
                while(jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    if(key.equals("mapas")){
                        jsonReader.beginArray();
                        while(jsonReader.hasNext()){
                            jsonReader.beginArray();
                            jsonReader.hasNext();

                            Mapa mapa = new Mapa();
                            mapa.setData(Util.converterStringDate(jsonReader.nextString()));

                            jsonReader.hasNext();
                            mapa.setId(jsonReader.nextInt());
                            mapas.add(mapa);
                            Log.d("testeX","getmapas" + mapa.getId());
                            jsonReader.endArray();
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
//                while (jsonReader.hasNext()) {
//                    String key = jsonReader.nextName();
//                    if (key.equals("mapas")) {
//                        jsonReader.beginArray();
//                        while(jsonReader.hasNext()){
//                            jsonReader.beginArray();
//                            jsonReader.hasNext();
//
//                            Mapa mapa = new Mapa();
//
//                            String foto = jsonReader.nextString();
//                            Bitmap bitmap = Util.converterStringParaBitmap(foto);
//                            mapa.setFoto(bitmap);
//
//                            jsonReader.hasNext();
//                            String data = jsonReader.nextString();
//                            Date date = Util.converterStringDate(data);
//                            mapa.setData(date);
//
//                            mapas.add(mapa);
//
//                            jsonReader.endArray();
//                        }
//                        jsonReader.endArray();
//                    } else {
//                        jsonReader.skipValue();
//                    }
//                }
                jsonReader.endObject();
                jsonReader.close();

                Mapa.setMapas(mapas);
            } catch (IOException e) {
                mensagensExceptions = new String[] {"Erro com os dados obtidos do Usuário","Tente novamente em alguns minutos"};
                con.setMensagensExceptions(mensagensExceptions);
                e.printStackTrace();
            }
        }
        return con;
    }
}
