package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Produtor;
import com.sistemadegestaoagricola.entidades.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaGetUser implements Callable<ConexaoAPI> {

    private String[] mensagensExceptions = null;

    public RotaGetUser() {

    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-user";
        String parametros = "";
        String metodo = "GET";
        Map<String,String> propriedades = new HashMap<String,String>();
        propriedades.put("Accept","application/json");
        propriedades.put("Authorization","Bearer " + ConexaoAPI.getToken());
        ConexaoAPI con = new ConexaoAPI(rota,parametros,metodo,propriedades);
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
                    if (key.equals("user")) {
                        lerUsuario(jsonReader);
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

    private void lerUsuario(JsonReader jsonReader) throws IOException{
        int id = -1;
        String nome = null;
        String cpfCnpj = null;
        String email = null;
        int enderecoId = -1;
        String telefone = null;
        String perfil = null;

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            //jsonReader.nextName();
            //jsonReader.nextName();
            Log.d("testeXe","value: " + jsonReader.toString());
            String key = jsonReader.nextName();
            Log.d("testeXe","key: " + key + " value: teste");

            switch (key) {
                case "id":
                    id = jsonReader.nextInt();
                    Log.d("testeXe","key: " + key + " value: " + id);
                    break;
                case "nome":
                    nome = jsonReader.nextString();
                    Log.d("testeXe","key: " + key + " value: " + nome);

                    break;
                case "email":
                    cpfCnpj = jsonReader.nextString();
                    Log.d("testeXe","key: " + key + " value: " + cpfCnpj);

                    break;
                case "email2":
                    if(jsonReader.peek() != JsonToken.NULL){
                        email = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    Log.d("testeXe","key: " + key + " value: " + email);

                    break;
                case "endereco_id":
                    if(jsonReader.peek() != JsonToken.NULL){
                        enderecoId = jsonReader.nextInt();
                    } else {
                        jsonReader.skipValue();
                    }
                    Log.d("testeXe","key: " + key + " value: " + enderecoId);

                    break;
                case "telefone":
                    if(jsonReader.peek() != JsonToken.NULL){
                        telefone = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    Log.d("testeXe","key: " + key + " value: " + telefone);

                    break;
                case "tipo_perfil":
                    perfil = jsonReader.nextString();
                    Log.d("testeXe","key: " + key + " value: " + perfil);
                    break;
                case "produtor":
                    if(jsonReader.peek() != JsonToken.NULL){
                        lerProdutor(jsonReader);
                    } else {
                        Log.d("testeXe","produtor null");
                        jsonReader.skipValue();
                    }
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        jsonReader.close();
        new Usuario(id,nome,cpfCnpj,email,enderecoId,telefone,perfil);
    }

    private void lerProdutor(JsonReader jsonReader) throws IOException{
        int id = -1;
        String dataNascimento = null;
        String rg = null;
        String nomeConjuge = null;
        String nomeFilhos = null;
        boolean primeiroAcesso = false;
        int ocsId = -1;

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String key = jsonReader.nextName();
            switch (key){
                case "id":
                    if(jsonReader.peek() != JsonToken.NULL){
                        id = jsonReader.nextInt();
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                case "data_nascimento":
                    if(jsonReader.peek() != JsonToken.NULL){
                        dataNascimento = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                case "rg":
                    if(jsonReader.peek() != JsonToken.NULL){
                        rg = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                case "nome_conjugue":
                    if(jsonReader.peek() != JsonToken.NULL){
                        nomeConjuge = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                case "nome_filhos":
                    if(jsonReader.peek() != JsonToken.NULL){
                        nomeFilhos = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                case "primeiro_acesso":
                    primeiroAcesso = jsonReader.nextBoolean();
                    break;
                case "ocs_id":
                    ocsId = jsonReader.nextInt();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        new Produtor(id,dataNascimento,rg,nomeConjuge,nomeFilhos,primeiroAcesso,ocsId);
        Log.d("testeXe","leu Produtor");
    }
}
