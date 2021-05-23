package com.sistemadegestaoagricola.entidades;

public class Usuario {
    private static int id;
    private static String nome;
    private static String cpfCnpj;
    private static String email;
    private static int enderecoId;
    private static String telefone;
    private static String perfil;

    /**
     * @param id
     * @param nome
     * @param cpfCnpj
     * @param email
     * @param enderecoId
     * @param telefone
     * @param perfil
     */
    public Usuario(int id, String nome, String cpfCnpj, String email,
                   int enderecoId, String telefone, String perfil){
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.enderecoId = enderecoId;
        this.telefone = telefone;
        this.perfil = perfil;
    }


    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Usuario.id = id;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        Usuario.nome = nome;
    }

    public static String getCpfCnpj() {
        return cpfCnpj;
    }

    public static void setCpfCnpj(String cpfCnpj) {
        Usuario.cpfCnpj = cpfCnpj;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Usuario.email = email;
    }

    public static String getTelefone() {
        return telefone;
    }

    public static void setTelefone(String telefone) {
        Usuario.telefone = telefone;
    }

    public static String getPerfil() {
        return perfil;
    }

    public static void setPerfil(String perfil) {
        Usuario.perfil = perfil;
    }

    public static int getEnderecoId() {
        return enderecoId;
    }

    public static void setEnderecoId(int enderecoId) {
        Usuario.enderecoId = enderecoId;
    }
}
