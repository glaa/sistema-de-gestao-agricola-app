package com.sistemadegestaoagricola.entidades;

public class Produtor {
    private static int id;
    private static String dataNascimento;
    private static String rg;
    private static String nomeConjuge;
    private static String nomeFilhos;
    private static boolean primeiroAcesso;
    private static int ocsId;

    /**
     * @param id
     * @param dataNascimento
     * @param rg
     * @param nomeConjuge
     * @param nomeFilhos
     * @param primeiroAcesso
     * @param ocsId
     */
    public Produtor(int id, String dataNascimento, String rg, String nomeConjuge,
                    String nomeFilhos, boolean primeiroAcesso, int ocsId) {
        this.id = id;
        this.dataNascimento = dataNascimento;
        this.rg = rg;
        this.nomeConjuge = nomeConjuge;
        this.nomeFilhos = nomeFilhos;
        this.primeiroAcesso = primeiroAcesso;
        this.ocsId =ocsId;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Produtor.id = id;
    }

    public static String getDataNascimento() {
        return dataNascimento;
    }

    public static void setDataNascimento(String dataNascimento) {
        Produtor.dataNascimento = dataNascimento;
    }

    public static String getRg() {
        return rg;
    }

    public static void setRg(String rg) {
        Produtor.rg = rg;
    }

    public static String getNomeConjuge() {
        return nomeConjuge;
    }

    public static void setNomeConjuge(String nomeConjuge) {
        Produtor.nomeConjuge = nomeConjuge;
    }

    public static String getNomeFilhos() {
        return nomeFilhos;
    }

    public static void setNomeFilhos(String nomeFilhos) {
        Produtor.nomeFilhos = nomeFilhos;
    }

    public static boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public static void setPrimeiroAcesso(boolean primeiroAcesso) {
        Produtor.primeiroAcesso = primeiroAcesso;
    }

    public static int getOcsId() {
        return ocsId;
    }

    public static void setOcsId(int ocsId) {
        Produtor.ocsId = ocsId;
    }
}
