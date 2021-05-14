package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ConexaoAPI conexao;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("testeXa", ConexaoAPI.getToken());
        new Conectar().execute();

    }

    private class Conectar extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String rota = "get-user";
            String parametros = "";
            String metodo = "GET";
            Map<String,String> propriedades = new HashMap<String,String>();
            propriedades.put("Accept","application/json");
            propriedades.put("Authorization","Bearer " + ConexaoAPI.getToken());

            conexao = new ConexaoAPI(rota,parametros,metodo,propriedades);
            String[] mensagensErro = conexao.iniciar();

            if(mensagensErro == null){
                status = conexao.getCodigoStatus();
            } else {
                String titulo = mensagensErro[0];
                String subtitulo = mensagensErro[1];

                Intent intent = new Intent(getApplicationContext(), ErroActivity.class);
                intent.putExtra("TITULO", titulo);
                intent.putExtra("SUBTITULO", subtitulo);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(status == 200){
                try {
                    InputStream responseBody = conexao.getConexaoHttp().getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    jsonReader.beginObject();

                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("user")) {
                            jsonReader.beginObject();

                            while(jsonReader.hasNext()){
                                key = jsonReader.nextName();
                                String value = jsonReader.nextString();
                                Log.d("testeXb", "key: " + key + " value: " + value);
                            }
                            jsonReader.endObject();
                            jsonReader.close();
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    //jsonReader.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Erro com os dados obtidos",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            } else if(status == 401){
                Toast.makeText(getApplicationContext(),"CPF / CNPJ ou Senha inválido",Toast.LENGTH_LONG).show();

//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setMessage("CPF / CNPJ ou Senha inválido.").setTitle("Aviso");
//                AlertDialog alerta = builder.create();
//                alerta.show();
            } else {
                Toast.makeText(getApplicationContext(),"Erro com a conexão" + status,Toast.LENGTH_LONG).show();
            }
            conexao.fechar();
        }
    }
}