package com.sistemadegestaoagricola.conexao;

import android.content.Context;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaLogin implements Callable<ConexaoAPI> {
    Context context;
    private String email;
    private String password;

    public RotaLogin(Context context, String email, String password){
        this.context = context;
        this.email = email;
        this.password = password;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "login";
        String parametros = "?email="+email+"&password="+password;
        String metodo = "POST";
        Map<String,String> propriedades = null;
        ConexaoAPI con = new ConexaoAPI(rota,parametros,metodo,propriedades);
        Log.d("testeX","conectou");

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
                Toast.makeText(context,"Erro com os dados obtidos",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return con;
    }

    public void setEmail(String email){this.email = email;}

    public void setPassword(String password){this.password = password;}
}
