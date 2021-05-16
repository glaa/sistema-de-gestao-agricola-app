package com.sistemadegestaoagricola.conexao;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

public class ConexaoAPI {
    public static HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "http://%s/api/%s%s";
    private final String host = "192.168.0.106/site/sistema-de-gestao-agricola/public";
    private String rota;
    private String parametros;
    private String metodo;
    private Map<String,String> propriedades;
    private static String token = null;
    private String[] mensagensErro = null;
    private int codigoStatus;

    /**
     * Criar uma conexão com a API
     * @param rota rota para o recurso desejado
     * @param parametros parametros a serem passado junto com a rota
     * @param metodo verbo da URL
     * @param propriedades cabeçalho que acompanha a requisão, será null caso não haja nenhum
     */
    public ConexaoAPI(String rota, String parametros, String metodo, Map<String,String> propriedades){
        this.rota = rota;
        this.parametros = parametros;
        this.url = String.format(url,this.host,this.rota,this.parametros);
        this.metodo = metodo;
        this.propriedades = propriedades;
        this.iniciar();
    }
    /**
     * @return a função retorna null em caso de sucesso com a conexção caso contrário retorna o erro
     */
    private String[] iniciar(){
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

    /** Fecha a conexão da instancia atual*/
    public void fechar(){
        this.conexao.disconnect();
    }

    /** Cria o cabeçalho para a requisão http */
    private void setPropriedades(Map<String,String> propriedades){
        for(String key : propriedades.keySet()){
            String value = propriedades.get(key);
            this.conexao.setRequestProperty(key,value);
        }
    }

    /** Retorna null em caso de sucesso com a conexão, caso contrário os erros */
    public String[] getMensagensErro() {
        return mensagensErro;
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
}
