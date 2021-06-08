package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import com.sistemadegestaoagricola.entidades.Propriedade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class CepAPI {

    private HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "https://%s/ws/%s/json";
    private final String host = "viacep.com.br";
    private static String token = null;
    private String[] mensagensExceptions = null;
    private int codigoStatus;
    private boolean sucesso = false;
    private boolean encontrou = true;

    public CepAPI(String cep){
        this.url = String.format(url,host,cep);
    }

    public boolean buscar(){
        try{
            this.agroEndpoint = new URL(this.url);
            this.conexao = (HttpURLConnection) this.agroEndpoint.openConnection();

            this.conexao.setConnectTimeout(20000);

            this.codigoStatus = this.conexao.getResponseCode();

            if(codigoStatus == 200){
                criarPropriedade();
            }
            Log.d("testeX","cep status " + this.codigoStatus);

            this.sucesso = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.conexao.disconnect();
            return this.sucesso;
        }
    }

    private void criarPropriedade() {
        String cep = null;
        String logradouro = null;
        String bairro = null;
        String cidade = null;
        String estado = null;

        try {
            InputStream responseBody = conexao.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("erro")) {
                    if(jsonReader.nextBoolean() == true){
                        Log.d("testeX","cep erro");
                    }
                } else if(key.equals("cep")){
                    cep = jsonReader.nextString();
                } else if(key.equals("logradouro")){
                    logradouro = jsonReader.nextString();
                } else if(key.equals("bairro")){
                    bairro = jsonReader.nextString();
                } else if(key.equals("localidade")){
                    cidade = jsonReader.nextString();
                } else if(key.equals("uf")){
                    estado = jsonReader.nextString();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();

            Log.d("testeX",cep);
            Log.d("testeX",logradouro);

            Propriedade.setCep(cep);
            Propriedade.setLogradouro(logradouro);
            Propriedade.setBairro(bairro);
            Propriedade.setCidade(cidade);
            Propriedade.setEstado(estado);

            if(estado == null || cidade == null){
                encontrou = false;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean encontrouCidade(){
        return encontrou;
    }

    private String nomeEstado(String sigla){
        if(sigla.equals("AC")){
            return "Acre";
        } else if(sigla.equals("AL")){
            return "Alagoas";
        } else if(sigla.equals("AP")){
            return "Amapá";
        } else if(sigla.equals("AM")){
            return "Amazonas";
        } else if(sigla.equals("BA")){
            return "Bahia";
        } else if(sigla.equals("CE")){
            return "Ceará";
        } else if(sigla.equals("DF")){
            return "Distrito Federal";
        } else if(sigla.equals("ES")){
            return "Espírito Santo";
        } else if(sigla.equals("GO")){
            return "Goiás";
        } else if(sigla.equals("MA")){
            return "Maranhão";
        } else if(sigla.equals("MT")){
            return "Mato Grosso";
        } else if(sigla.equals("MS")){
            return "Mato Grosso do Sul";
        } else if(sigla.equals("MG")){
            return "Minas Gerais";
        } else if(sigla.equals("PA")){
            return "Pará";
        } else if(sigla.equals("PB")){
            return "Paraíba";
        } else if(sigla.equals("PR")){
            return "Paraná";
        } else if(sigla.equals("PE")){
            return "Pernambuco";
        } else if(sigla.equals("PI")){
            return "Piauí";
        } else if(sigla.equals("RJ")){
            return "Rio de Janeiro";
        } else if(sigla.equals("RN")){
            return "Rio Grande do Norte";
        } else if(sigla.equals("RS")){
            return "Rio Grande do Sul";
        } else if(sigla.equals("RO")){
            return "Rodônia";
        } else if(sigla.equals("RR")){
            return "Roraima";
        } else if(sigla.equals("SC")){
            return "Santa Catarina";
        } else if(sigla.equals("SP")){
            return "São Paulo";
        } else if(sigla.equals("SE")){
            return "Sergipe";
        } else if(sigla.equals("TO")){
            return "Tocantins";
        } else {
            return null;
        }
    }
}
