package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.sistemadegestaoagricola.entidades.Util.converterStringDate;

public class RotaListarReunioes implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaListarReunioes() {

    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "listar-reunioes";
        String boundary = null;
        String metodo = "GET";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Accept","text/html,application/json");
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
                    if (key.equals("reunioes")) {
                        jsonReader.beginArray();
                        while(jsonReader.hasNext()){
                            lerReuniao(jsonReader);
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                //Log.d("testeX","users");
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

    private void lerReuniao(JsonReader jsonReader) throws IOException{
        int id = -1;
        String nome = null;
        String data = null;
        String local = null;
        int ocs_id = -1;
        boolean registrada = false;

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            //Log.d("testeXe","value: " + jsonReader.toString());
            String key = jsonReader.nextName();
            //Log.d("testeXe","key: " + key + " value: teste");

            switch (key) {
                case "id":
                    id = jsonReader.nextInt();
                    //Log.d("testeXe","key: " + key + " value: " + id);
                    break;
                case "nome":
                    nome = jsonReader.nextString();
                    //Log.d("testeXe","key: " + key + " value: " + nome);

                    break;
                case "data":
                    data = jsonReader.nextString();
                    //Log.d("testeXe","key: " + key + " value: " + data);

                    break;
                case "local":
                    local = jsonReader.nextString();
                    //Log.d("testeXe","key: " + key + " value: " + local);

                    break;
                case "registrada":
                    registrada = jsonReader.nextBoolean();
                    //Log.d("testeXe","key: " + key + " value: " + registrada);

                    break;
                case "ocs_id":
                    ocs_id = jsonReader.nextInt();
                    //Log.d("testeXe","key: " + key + " value: " + ocs_id);

                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        Date date = Util.converterStringDate(data);
        Util.addAgendamentos(new AgendamentoReuniao(id,nome,date,local,registrada,ocs_id));
    }

}
