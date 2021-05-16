package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;
import com.sistemadegestaoagricola.conexao.RotaGetUser;
import com.sistemadegestaoagricola.conexao.RotaLogin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private EditText tvCpfCnjp;
    private EditText tvSenha;
    private Button btEntrar;
    private String email = "";
    private String password = "";
    private ConexaoAPI conexao;
    private int status;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensErro;


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
                /** Apresenta mensagem de campo obrigatório */
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
                /** Apresenta mensagem de campo obrigatório */
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

                    /**
                     *  Chama a classe Login que irá se conectar com a rota /api/login em uma thread
                     */
                    RotaLogin login = new RotaLogin(getApplicationContext(),email,password);
                    Future<ConexaoAPI> future1 = threadpool.submit(login);

                    //** Aguarda a classe Index terminar a conexao */
                    while(!future1.isDone()){
                        //Aguardando o termino da conexão
                    }
                    RotaGetUser getUser = new RotaGetUser(getApplicationContext());
                    Future<ConexaoAPI> future2 = threadpool.submit(getUser);
                    while(!future2.isDone()){
                        //Aguardando o termino da conexão
                    }

                    ConexaoAPI conexao = null;
                    try {
                        conexao = future1.get();
                        mensagensErro = conexao.getMensagensErro();
                        if(mensagensErro == null && conexao.getToken() != null){
                            conexao.fechar();
                            Log.d("testeX","token: " + conexao.getToken());
                            conexao = future2.get();
                            Log.d("testeX","get: " + conexao.getMensagensErro());

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Erro(mensagensErro[0],mensagensErro[1]);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Erro("Erro","Falha com a conexão, tente novamente em alguns minutos");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Erro("Erro","Falha com a conexão, tente novamente em alguns minutos");
                    }
                    /** Fechar conexão caso esteja aberta */
                    if(conexao != null){
                        conexao.fechar();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"CPF / CNPJ e Senha devem ser preenchidos!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        this.startActivity(intent);
    }
}