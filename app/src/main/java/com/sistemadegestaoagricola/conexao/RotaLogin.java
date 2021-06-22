package com.sistemadegestaoagricola.conexao;

import android.os.StrictMode;
import android.util.JsonReader;

import com.sistemadegestaoagricola.entidades.Parametro;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RotaLogin implements Callable<ConexaoAPI> {
    private String email;
    private String password;
    private String[] mensagensExceptions = null;

    public RotaLogin(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "login";
        String boundary = UUID.randomUUID().toString();
        String metodo = "POST";

        Map<String,String> cabecalhos = new HashMap<String,String>();
        cabecalhos.put("Charset", "UTF-8");
        cabecalhos.put("Content-Type","multipart/form-data; boundary=" + boundary);
        cabecalhos.put("Accept","application/json");

        ArrayList<Parametro> parametros = new ArrayList<Parametro>();
        parametros.add(new Parametro("email",null,email));
        parametros.add(new Parametro("password",null,password));

        Requisicao requisicao = new Requisicao(metodo,cabecalhos,parametros,boundary);
        ConexaoAPI con = new ConexaoAPI(rota,requisicao);

        if(con.getCodigoStatus() == 200){
            try {
                InputStream responseBody = ConexaoAPI.getConexao().getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                jsonReader.beginObject();

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("token")) {
                        String value = jsonReader.nextString();
                        con.setToken(value);
                        break;
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.close();
            } catch (IOException e) {
                mensagensExceptions = new String[]{"Erro com os dados obtidos no Login", "Tente novamente em alguns minutos"};
                con.setMensagensExceptions(mensagensExceptions);
                e.printStackTrace();
            }
        }
        return con;
    }

    public void setEmail(String email){this.email = email;}

    public void setPassword(String password){this.password = password;}

}
