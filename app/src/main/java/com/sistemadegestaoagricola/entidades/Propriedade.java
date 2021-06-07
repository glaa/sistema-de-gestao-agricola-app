package com.sistemadegestaoagricola.entidades;

public class Propriedade {

    private static int id;
    private static int tamanho;
    private static String fonteAgua;
    private static String logradouro;
    private static int numero;
    private static String bairro;
    private static String cidade;
    private static String estado;
    private static String cep;

    public Propriedade(){

    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Propriedade.id = id;
    }

    public static int getTamanho() {
        return tamanho;
    }

    public static void setTamanho(int tamanho) {
        Propriedade.tamanho = tamanho;
    }

    public static String getFonteAgua() {
        return fonteAgua;
    }

    public static void setFonteAgua(String fonteAgua) {
        Propriedade.fonteAgua = fonteAgua;
    }

    public static String getLogradouro() {
        return logradouro;
    }

    public static void setLogradouro(String logradouro) {
        Propriedade.logradouro = logradouro;
    }

    public static int getNumero() {
        return numero;
    }

    public static void setNumero(int numero) {
        Propriedade.numero = numero;
    }

    public static String getBairro() {
        return bairro;
    }

    public static void setBairro(String bairro) {
        Propriedade.bairro = bairro;
    }

    public static String getCidade() {
        return cidade;
    }

    public static void setCidade(String cidade) {
        Propriedade.cidade = cidade;
    }

    public static String getEstado() {
        return estado;
    }

    public static void setEstado(String estado) {
        Propriedade.estado = estado;
    }

    public static String getCep() {
        return cep;
    }

    public static void setCep(String cep) {
        Propriedade.cep = cep;
    }

    public static void zerarAtributos(){
        tamanho = -1;
        fonteAgua = null;
        logradouro = null;
        id = -1;
        numero = -1;
        bairro = null;
        cidade = null;
        estado = null;
        cep = null;
    }
}
