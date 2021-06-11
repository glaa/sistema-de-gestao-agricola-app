package com.sistemadegestaoagricola.entidades;

import java.util.Date;

public class AgendamentoReuniao {

    private int id;
    private String nome;
    private Date data;
    private String local;
    private boolean registrada;
    private int ocs_id;

    public AgendamentoReuniao(int id, String nome, Date data, String local, boolean registrada, int ocs_id) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.local = local;
        this.registrada = registrada;
        this.ocs_id = ocs_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public boolean isRegistrada() {
        return registrada;
    }

    public void setRegistrada(boolean registrada) {
        this.registrada = registrada;
    }

    public int getOcs_id() {
        return ocs_id;
    }

    public void setOcs_id(int ocs_id) {
        this.ocs_id = ocs_id;
    }
}
