package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText tvCpfCnjp;
    private EditText tvSenha;
    private Button btEntrar;
    private String email = "";
    private String password = "";
    private static String tokenSessao = "";
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCpfCnjp= findViewById(R.id.edtCpfEmailMain);
        tvSenha = findViewById(R.id.edtSenhaMain);
        btEntrar = findViewById(R.id.btEntrarMain);

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
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            String rota = "login";
                            String parametros = "?email="+email+"&password="+password;
                            String metodo = "POST";
                            ConexaoAPI conexao = new ConexaoAPI(rota,parametros,metodo);
                            String[] mensagens = conexao.iniciar();
                            Log.d("testeX",String.valueOf(conexao.getCodigoStatus()));
                            status = conexao.getCodigoStatus();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),"CPF / CNPJ e Senha devem ser preenchidos!",Toast.LENGTH_LONG).show();
                }

                Log.d("testeX", "stat: "+ status);
                if(status == 200){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                } else if(status == 401){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("CPF / CNPJ ou Senha inválido.").setTitle("Aviso");
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
            }
        });
    }

}