package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText tvCpfCnjp;
    private EditText tvSenha;
    private Button btEntrar;
    private String email = "";
    private String password = "";
    private ConexaoAPI conexao;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCpfCnjp= findViewById(R.id.edtCpfEmailMain);
        tvSenha = findViewById(R.id.edtSenhaMain);
        btEntrar = findViewById(R.id.btEntrarMain);

        tvCpfCnjp.setText("00011122233344");
        tvSenha.setText("123456");

        tvCpfCnjp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                email = tvCpfCnjp.getText().toString();
                if(!hasFocus){
                    if(email.isEmpty()){
                        findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.GONE);
                }
            }
        });

        tvSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password = tvSenha.getText().toString();
                if(!hasFocus){
                    if(password.isEmpty()){
                        findViewById(R.id.tvSenhaInvalidaMain).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvSenhaInvalidaMain).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvSenhaInvalidaMain).setVisibility(View.GONE);
                }
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = tvCpfCnjp.getText().toString();
                password = tvSenha.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
//                    AsyncTask.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            String rota = "login";
//                            String parametros = "?email="+email+"&password="+password;
//                            String metodo = "POST";
//                            ConexaoAPI conexao = new ConexaoAPI(rota,parametros,metodo);
//                            String[] mensagens = conexao.iniciar();
//                            Log.d("testeX",String.valueOf(conexao.getCodigoStatus()));
//                            status = conexao.getCodigoStatus();
//                        }
//                    });
                    new Conectar().execute();
                } else {
                    Toast.makeText(getApplicationContext(),"CPF / CNPJ e Senha devem ser preenchidos!",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private class Conectar extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String rota = "login";
            String parametros = "?email="+email+"&password="+password;
            String metodo = "POST";
            conexao = new ConexaoAPI(rota,parametros,metodo,null);
            String[] mensagens = conexao.iniciar();
            status = conexao.getCodigoStatus();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("testeX", "status: " + status);
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
                        if (key.equals("token")) {
                            String value = jsonReader.nextString();
                            conexao.setToken(value);
                            //ConexaoAPI.setToken(value);
                            break;
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Erro com os dados obtidos",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
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

    private void alerta(){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alerta,null);

    }

}