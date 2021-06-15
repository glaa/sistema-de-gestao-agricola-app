package com.sistemadegestaoagricola.conexao;

import android.util.Log;

import com.sistemadegestaoagricola.entidades.Propriedade;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class ConexaoAPI {
    public static HttpURLConnection conexao;
    private URL agroEndpoint;
    private String url = "http://%s/api/%s%s";
    private final String host = "192.168.0.104/site/sistema-de-gestao-agricola/public";
    private String rota;
    private String parametros;
    private String metodo;
    private Map<String,String> propriedades;
    private static String token = null;
    private String[] mensagensExceptions = null;
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

            this.conexao.setConnectTimeout(10000);
            if(this.metodo == "POST"){
                this.conexao.setDoOutput(true);
                this.conexao.setChunkedStreamingMode(0);
                this.conexao.setRequestMethod("POST");
            }

            if(rota.equals("cadastrar-propriedade")){
                //criarCorpo();
            }
            if(this.rota.equals("login")){
                //corpoLogin();
            }

            this.codigoStatus = this.conexao.getResponseCode();
//            Log.d("testeX","url: " + url + this.conexao.getInputStream().toString());

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
    private void setPropriedades(Map<String,String> propriedades){
        for(String key : propriedades.keySet()){
            String value = propriedades.get(key);
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
        Log.d("testeX","Corpo");
        String boundary = UUID.randomUUID().toString();
        DataOutputStream request = new DataOutputStream(this.conexao.getOutputStream());
        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"tamanho_total\"\r\n\r\n");
        request.writeBytes(Propriedade.getTamanho() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"fonte_de_agua\"\r\n\r\n");
        request.writeBytes(Propriedade.getFonteAgua() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"nome_rua\"\r\n\r\n");
        request.writeBytes(Propriedade.getLogradouro() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"numero_casa\"\r\n\r\n");
        request.writeBytes(Propriedade.getNumero() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"bairro\"\r\n\r\n");
        request.writeBytes(Propriedade.getBairro() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"cidade\"\r\n\r\n");
        request.writeBytes(Propriedade.getCidade() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"estado\"\r\n\r\n");
        request.writeBytes(Propriedade.getEstado() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"cep\"\r\n\r\n");
        request.writeBytes(Propriedade.getCep() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"ponto_referencia\"\r\n\r\n");
        request.writeBytes(Propriedade.getReferencia() + "\r\n");

        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"mapa\"\r\n\r\n");
        request.writeBytes(Propriedade.getMapa() + "\r\n");
        request.writeBytes("--" + boundary + "\r\n");
        request.flush();
    }

    private void corpoLogin() throws IOException {
//        this.conexao.getOutputStream().write("email=11111111111".getBytes());
//        this.conexao.getOutputStream().write("password=123123123".getBytes());
        String boundary = "----------GLEISON----------";
        DataOutputStream request = new DataOutputStream(this.conexao.getOutputStream());
        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"email\"\r\n\r\n");
        request.writeBytes("11111111111" + "\r\n");
        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"password\"\r\n\r\n");
        request.writeBytes("123123123" + "\r\n");
        request.writeBytes("--" + boundary + "\r\n");
        request.flush();
    }
}
