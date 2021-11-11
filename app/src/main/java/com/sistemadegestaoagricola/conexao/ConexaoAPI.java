package com.sistemadegestaoagricola.conexao;

import android.util.Log;

import com.sistemadegestaoagricola.entidades.Parametro;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ConexaoAPI {
    public static HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "http://%s/api/%s";
    private final String host = "192.168.0.100/site/sistema-de-gestao-agricola/public";
    private String rota;
    private String parametros;
    private String metodo;
    private Map<String,String> cabecalhos;
    private ArrayList<Parametro> corpo = new ArrayList<Parametro>();
    private String boundary;
    private static String token = null;
    private String[] mensagensExceptions = null;
    private int codigoStatus;

//    /**
//     * Criar uma conexão com a API
//     * @param rota rota para o recurso desejado
//     * @param parametros parametros a serem passado junto com a rota
//     * @param metodo verbo da URL
//     * @param cabecalhos cabeçalho que acompanha a requisão, será null caso não haja nenhum
//     */
//    public ConexaoAPI(String rota, String parametros, String metodo, Map<String,String> cabecalhos){
//        this.rota = rota;
//        this.parametros = parametros;
//        this.url = String.format(url,this.host,this.rota,this.parametros);
//        this.metodo = metodo;
//        this.cabecalhos = cabecalhos;
//        this.iniciar();
//    }

    public ConexaoAPI(String rota, Requisicao requisicao){
        this.rota = rota;
        this.url = String.format(url,this.host,this.rota);
        this.metodo = requisicao.getMetodo();
        this.cabecalhos = requisicao.getCabecalhos();
        this.boundary = requisicao.getBoundary();
        this.corpo = requisicao.getCorpo();
        this.iniciar();
    }
    /**
     * @return a função retorna null em caso de sucesso com a conexção caso contrário retorna o erro
     */
    private String[] iniciar(){
        try {
            this.agroEndpoint = new URL(this.url);
            this.conexao = (HttpURLConnection) this.agroEndpoint.openConnection();

            if(cabecalhos != null){
                this.setCabecalhos(cabecalhos);
            }

            this.conexao.setConnectTimeout(90000);
            this.conexao.setDoInput(true);
            if(this.metodo == "POST"){
                this.conexao.setDoOutput(true);
                this.conexao.setChunkedStreamingMode(0);
                this.conexao.setRequestMethod("POST");
                criarCorpo();
            } else if(this.metodo == "PUT"){
                this.conexao.setDoOutput(true);
                this.conexao.setChunkedStreamingMode(0);
                this.conexao.setRequestMethod("PUT");
                criarCorpo();
            }

            this.codigoStatus = this.conexao.getResponseCode();

        } catch (MalformedURLException e) {
            String[] m = {"Erro com a url da conexão!", "Tente novamente em alguns minutos"};
            this.mensagensExceptions = m;
        } catch (ConnectException ce) {
            /*
             * Servidor desligado ou host incorreto
             */
            String[] m = {"Falha ao conectar ao servidor!", "Tente novamente em alguns minutos"};
            this.mensagensExceptions = m;

        }catch (SocketTimeoutException ste) {
            /*
             * Host incorreto e estorou o tempo ou conexao lenta
             */
            String[] m = {"Tempo excedido com a conexão!", "Tente novamente em alguns minutos"};
            this.mensagensExceptions = m;

        } catch (Exception e){
            String[] m = {"Erro com a conexão!", "Tente novamente em alguns minutos"};
            e.printStackTrace();
            this.mensagensExceptions = m;
        }
        return mensagensExceptions;
    }

    /** Fecha a conexão */
    public void fechar(){
        this.conexao.disconnect();
    }

    /** Cria o cabeçalho para a requisão http */
    private void setCabecalhos(Map<String,String> cabecalhos){
        for(String key : cabecalhos.keySet()){
            String value = cabecalhos.get(key);
            this.conexao.setRequestProperty(key,value);
        }
    }

    /** Retorna null em caso de sucesso com a conexão, caso contrário os erros */
    public String[] getMensagensExceptions() {
        return this.mensagensExceptions;
    }

    public void setMensagensExceptions(String[] mensagensExceptions) {
        this.mensagensExceptions = mensagensExceptions;
    }

    /** Retorna o código de status da recebido na resposta da requisão */
    public int getCodigoStatus() {
        return codigoStatus;
    }

    /** Retorna a conexão da ultima instância criada */
    public static HttpURLConnection getConexao() {
        return conexao;
    }

    /** Retorna o token recebido no login */
    public static String getToken() {
        return token;
    }

    /** Set no atributo token após a login realizado com sucesso */
    public static void setToken(String token) {
        ConexaoAPI.token = token;
    }


    private void criarCorpo() throws IOException {
        String fim = "\r\n";
        String doisTracos = "--";
        String boundary = this.boundary;

        DataOutputStream ds = new DataOutputStream(this.conexao.getOutputStream());
        for(Parametro parametro : this.corpo){
            ds.writeBytes(doisTracos + boundary + fim);
            if(parametro.getFilename() == null){
                ds.writeBytes("Content-Disposition: form-data; name=\""+parametro.getName()+"\""+
                        fim+fim+parametro.getValue()+fim);
                Log.d("testeX",parametro.getName() + " = " + parametro.getValue());
            } else {
                ds.writeBytes("Content-Disposition: form-data; name=\""+parametro.getName()+"\"; filename=\""+parametro.getFilename()+"\"" + fim);
                ds.writeBytes(fim);
                Log.d("testeX",parametro.getName() + " = " + parametro.getValue());

                //ds.write((byte[]) parametro.getValue());
                FileInputStream fStream = new FileInputStream((String) parametro.getValue());
                Log.d("testeX","hhh"+fStream.available());
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;

                while((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(fim);
                fStream.close();
            }

        }
        ds.writeBytes(doisTracos + boundary + doisTracos + fim);
        ds.flush();
        ds.close();
    }

    private void novoCorpo()throws Exception{

        
    }
}
