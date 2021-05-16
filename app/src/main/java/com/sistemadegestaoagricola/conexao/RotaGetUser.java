package com.sistemadegestaoagricola.conexao;

import android.content.Context;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.sistemadegestaoagricola.entidades.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RotaGetUser implements Callable<ConexaoAPI> {

    private Context context;

    public RotaGetUser(Context context) {
        this.context = context;
    }

    @Override
    public ConexaoAPI call() throws Exception {
        String rota = "get-user";
        String parametros = "";
        String metodo = "GET";
        Map<String,String> propriedades = new HashMap<String,String>();
        propriedades.put("Accept","application/json");
        propriedades.put("Authorization","Bearer " + ConexaoAPI.getToken());
        ConexaoAPI con = new ConexaoAPI(rota,parametros,metodo,propriedades);
        Log.d("testeX","status: " + con.getCodigoStatus());

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
                    if (key.equals("user")) {
                        jsonReader.beginObject();
                        Usuario usuario = new Usuario();
                        while(jsonReader.hasNext()){
                            key = jsonReader.nextName();
                            String value = jsonReader.nextString();
                            switch (key){
                                case "id":
                                    usuario.setId(value);
                                    break;
                                case "nome":
                                    usuario.setNome(value);
                                    break;
                                case "email":
                                    usuario.setCpfCnpj(value);
                                    break;
                                case "email2":
                                    usuario.setEmail(value);
                                    break;
                                case "telefone":
                                    usuario.setEmail(value);
                                case "tipo_perfil":
                                    usuario.setPerfil(value);
                                    break;
                                default:
                                    break;
                            }
                            Log.d("testeXb", "key: " + key + " value: " + value);
                        }
                        jsonReader.endObject();
                        jsonReader.close();
                        break;
                    } else {
                        jsonReader.skipValue();
                    }
                }
                Log.d("testeX","users");
            } catch (IOException e) {
                Toast.makeText(context,"Erro com os dados obtidos",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return con;
    }
}
