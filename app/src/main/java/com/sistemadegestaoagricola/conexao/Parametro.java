package com.sistemadegestaoagricola.conexao;

public class Parametro {
    private String name;
    private String filename;
    private Object value;

    /*
    * Paramentros a serem acrescentados no corpo da requisão
    * @name Nome do campo esperado pelo servidor
    * @filename Nome do arquivo como será salvo no servidor
    * @value Valor do campo podendo ser qualquer dado
    * */
    public Parametro(String name, String filename, Object value) {
        this.name = name;
        this.filename = filename;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
