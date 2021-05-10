package com.sistemadegestaoagricola;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ConexaoAPI {

    private HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "http://%s/api/%s/%s";
    private String host = "192.168.0.108:8000";
    private String rota;
    private String parametros;
    private String metodo;
    private String token;
    private int codigoStatus;

    /*
     *Criar uma conexão com a API
     * @param rota rota para o recurso desejado
     * @param parametros parametros a serem passado junto com a rota
     * @param metodo verbo da URL
     */
    public ConexaoAPI(String rota, String parametros, String metodo){
        this.rota = rota;
        this.parametros = parametros;
        this.url = String.format(url,this.host,this.rota,this.parametros);
        this.metodo = metodo;
    }

    public String[] iniciar(){
        String[] mensagens = null;

        try {
            this.agroEndpoint = new URL(this.url);
            this.conexao = (HttpURLConnection) this.agroEndpoint.openConnection();
            this.conexao.setConnectTimeout(30000);
            if(this.metodo == "POST"){
                this.conexao.setRequestMethod("POST");
            }
            this.codigoStatus = this.conexao.getResponseCode();

        } catch (MalformedURLException e) {
            String[] m = {"Erro com a url da conexão!", "Tente novamente em alguns minutos"};
            mensagens = m;
        } catch (ConnectException ce) {
            /*
             * Servidor desligado ou host incorreto
             */
            String[] m = {"Falha ao conectar ao servidor!", "Tente novamente em alguns minutos"};
            mensagens = m;

        }catch (SocketTimeoutException ste) {
            /*
             * Host incorreto e estorou o tempo ou conexao lenta
            */
            String[] m = {"Tempo excedido com a conexão!", "Tente novamente em alguns minutos"};
            mensagens = m;

        } catch (Exception e){
            String[] m = {"Erro com a conexão!", "Tente novamente em alguns minutos"};
            mensagens = m;
        }
        return mensagens;
    }

    public void fechar(){
        this.conexao.disconnect();
    }


    public int getCodigoStatus(){
        return codigoStatus;
    }
}
