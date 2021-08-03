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

public class RotaGetEndereco implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaGetEndereco(){}

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-endereco";
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
                    if (key.equals("endereco")) {
                        jsonReader.beginObject();
                        while(jsonReader.hasNext()){
                            String chave = jsonReader.nextName();
                            if(chave.equals("estado")){
                                Propriedade.setEstado(jsonReader.nextString());
                                continue;
                            } else if(chave.equals("cidade")){
                                Propriedade.setCidade(jsonReader.nextString());
                                continue;
                            } else if(chave.equals("bairro")){
                                Propriedade.setBairro(jsonReader.nextString());
                                continue;
                            } else if(chave.equals("nome_rua")){
                                Propriedade.setLogradouro(jsonReader.nextString());
                                continue;
                            } else if(chave.equals("cep")){
                                Propriedade.setCep(jsonReader.nextString());
                                continue;
                            } else if(chave.equals("numero_casa")){
                                Propriedade.setNumero(jsonReader.nextInt());
                                continue;
                            } else if(chave.equals("ponto_referencia")){
                                Propriedade.setReferencia(jsonReader.nextString());
                                continue;
                            }
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
