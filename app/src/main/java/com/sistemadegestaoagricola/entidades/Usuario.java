package com.sistemadegestaoagricola.entidades;

public class Usuario {
    public static String id;
    public static String nome;
    public static String cpfCnpj;
    public static String email;
    public static String telefone;
    public static String perfil;

    public Usuario(){}

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
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
}
