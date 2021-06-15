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
    private static String referencia;
    private static String mapa;

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

    public static String getReferencia() {
        return referencia;
    }

    public static void setReferencia(String referencia) {
        Propriedade.referencia = referencia;
    }

    public static String getMapa() {
        return mapa;
    }

    public static void setMapa(String mapa) {
        Propriedade.mapa = mapa;
    }

    public static void zerarAtributos(){
        tamanho = 0;
        fonteAgua = null;
        logradouro = null;
        id = -1;
        numero = 0;
        bairro = null;
        cidade = null;
        estado = null;
        cep = null;
        referencia = null;
    }

    public static String siglaEstado(String nomeEstado){
        if(nomeEstado.equals("Acre")){
            return "AC";
        } else if(nomeEstado.equals("Alagoas")){
            return "AL";
        } else if(nomeEstado.equals("Amapá")){
            return "AP";
        } else if(nomeEstado.equals("Amazonas")){
            return "AM";
        } else if(nomeEstado.equals("Bahia")){
            return "BA";
        } else if(nomeEstado.equals("Ceará")){
            return "CE";
        } else if(nomeEstado.equals("Distrito Federal")){
            return "DF";
        } else if(nomeEstado.equals("Espírito Santo")){
            return "ES";
        } else if(nomeEstado.equals("Goiás")){
            return "GO";
        } else if(nomeEstado.equals("Maranhão")){
            return "MA";
        } else if(nomeEstado.equals("Mato Grosso")){
            return "MT";
        } else if(nomeEstado.equals("Mato Grosso do Sul")){
            return "MS";
        } else if(nomeEstado.equals("Minas Gerais")){
            return "MG";
        } else if(nomeEstado.equals("Pará")){
            return "PA";
        } else if(nomeEstado.equals("Paraíba")){
            return "PB";
        } else if(nomeEstado.equals("Paraná")){
            return "PR";
        } else if(nomeEstado.equals("Pernambuco")){
            return "PE";
        } else if(nomeEstado.equals("Piauí")){
            return "PI";
        } else if(nomeEstado.equals("Rio de Janeiro")){
            return "RJ";
        } else if(nomeEstado.equals("Rio Grande do Norte")){
            return "RN";
        } else if(nomeEstado.equals("Rio Grande do Sul")){
            return "RS";
        } else if(nomeEstado.equals("Rondônia")){
            return "RO";
        } else if(nomeEstado.equals("Roraima")){
            return "RR";
        } else if(nomeEstado.equals("Santa Catarina")){
            return "SC";
        } else if(nomeEstado.equals("São Paulo")){
            return "SP";
        } else if(nomeEstado.equals("Sergipe")){
            return "SE";
        } else if(nomeEstado.equals("Tocantins")){
            return "TO";
        } else {
            return null;
        }
    }
}
