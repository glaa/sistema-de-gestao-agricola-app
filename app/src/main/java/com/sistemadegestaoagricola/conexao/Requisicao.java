package com.sistemadegestaoagricola.conexao;

import com.sistemadegestaoagricola.entidades.Parametro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Requisicao {
    private String metodo;
    private Map<String,String> cabecalhos = new HashMap<String,String>();
    private ArrayList<Parametro> corpo = new ArrayList<Parametro>();
    private String boundary;

    /*
    * Requisição com o protocolo HTTP
    * @param metodo Verbo da requisição
    * @param cabecalhos Cabeçalho da requisição
    * @param corpo Conteúdo da requisão para verbos POST e PUT
    * @param boundary Diretiva obrigatória para cabeçaho multipart utilizada nos verbos POST e PUT
    * */
    public Requisicao(String metodo, Map<String, String> cabecalhos, ArrayList<Parametro> corpo, String boundary) {
        this.metodo = metodo;
        this.cabecalhos = cabecalhos;
        this.corpo = corpo;
        this.boundary = boundary;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Map<String, String> getCabecalhos() {
        return cabecalhos;
    }

    public void setCabecalhos(Map<String, String> cabecalhos) {
        this.cabecalhos = cabecalhos;
    }

    public ArrayList<Parametro> getCorpo() {
        return corpo;
    }

    public void setCorpo(ArrayList<Parametro> corpo) {
        this.corpo = corpo;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }
}
