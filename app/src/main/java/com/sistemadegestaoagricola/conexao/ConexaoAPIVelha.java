package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

public class ConexaoAPIVelha {

    public static HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "http://%s/api/%s%s";
    private final String host = "192.168.0.106/site/sistema-de-gestao-agricola/public";
    private String rota;
    private String parametros;
    private String metodo;
    private Map<String,String> propriedades;
    private static String token = null;
    private int codigoStatus;

    /*
     * Criar uma conexão com a API
     * @param rota rota para o recurso desejado
     * @param parametros parametros a serem passado junto com a rota
     * @param metodo verbo da URL
     * @param propriedades cabeçalho que acompanha a requisão, será null caso não haja nenhum
     */
    public ConexaoAPIVelha(String rota, String parametros, String metodo, Map<String,String> propriedades){
        this.rota = rota;
        this.parametros = parametros;
        this.url = String.format(url,this.host,this.rota,this.parametros);
        this.metodo = metodo;
        this.propriedades = propriedades;
    }

    public String[] iniciar(){
        String[] mensagensErro = null;

        try {
            this.agroEndpoint = new URL(this.url);
            this.conexao = (HttpURLConnection) this.agroEndpoint.openConnection();

            if(propriedades != null){
                this.setPropriedades(propriedades);
            }

            this.conexao.setConnectTimeout(3000);
            if(this.metodo == "POST"){
                this.conexao.setRequestMethod("POST");
            }
            this.codigoStatus = this.conexao.getResponseCode();

        } catch (MalformedURLException e) {
            String[] m = {"Erro com a url da conexão!", "Tente novamente em alguns minutos"};
            mensagensErro = m;
        } catch (ConnectException ce) {
            /*
             * Servidor desligado ou host incorreto
             */
            String[] m = {"Falha ao conectar ao servidor!", "Tente novamente em alguns minutos"};
            mensagensErro = m;

        }catch (SocketTimeoutException ste) {
            /*
             * Host incorreto e estorou o tempo ou conexao lenta
            */
            String[] m = {"Tempo excedido com a conexão!", "Tente novamente em alguns minutos"};
            mensagensErro = m;

        } catch (Exception e){
            String[] m = {"Erro com a conexão!", "Tente novamente em alguns minutos"};
            mensagensErro = m;
        }
        return mensagensErro;
    }

    public void fechar(){
        this.conexao.disconnect();
    }


    public int getCodigoStatus(){
        return codigoStatus;
    }

    public HttpURLConnection getConexaoHttp(){
        return this.conexao;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ConexaoAPIVelha.token = token;
    }

    private void setPropriedades(Map<String,String> propriedades){
        for(String key : propriedades.keySet()){
            String value = propriedades.get(key);
            this.conexao.setRequestProperty(key,value);
        }
    }

    public void consultarUser(){
        if(codigoStatus == 200){
            try {
                InputStream responseBody = this.conexao.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                jsonReader.beginObject();

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("user")) {
                        jsonReader.beginObject();

                        while(jsonReader.hasNext()){
                            key = jsonReader.nextName();
                            String value = jsonReader.nextString();
                            if(key.equals("nome")){
                                //saudacao.setText("Olá, " + value + "!");
                            }
                            Log.d("testeXb", "key: " + key + " value: " + value);
                        }
                        jsonReader.endObject();
                        jsonReader.close();
                        break;
                    } else {
                        jsonReader.skipValue();
                    }
                }
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Erro com os dados obtidos",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }//
        } else if(codigoStatus == 401){
            //Toast.makeText(getApplicationContext(),"CPF / CNPJ ou Senha inválido",Toast.LENGTH_LONG).show();

//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setMessage("CPF / CNPJ ou Senha inválido.").setTitle("Aviso");
//                AlertDialog alerta = builder.create();
//                alerta.show();
        } else {
            //Toast.makeText(getApplicationContext(),"Erro com a conexão" + status,Toast.LENGTH_LONG).show();
        }
        fechar();
    }
}
