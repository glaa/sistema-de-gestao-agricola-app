package com.sistemadegestaoagricola;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetUser;
import com.sistemadegestaoagricola.conexao.RotaLogin;
import com.sistemadegestaoagricola.entidades.Produtor;
import com.sistemadegestaoagricola.entidades.Usuario;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements Runnable {

    private EditText tvCpfCnjp;
    private EditText tvSenha;
    private Button btEntrar;
    private TextView tvEsqueceuSenha;
    private String email = "";
    private String password = "";
    private ConexaoAPI conexao;
    private int status;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private AlertDialog carregamento;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_login);

        tvCpfCnjp= findViewById(R.id.edtCpfCnpjLogin);
        tvSenha = findViewById(R.id.edtSenhaLogin);
        btEntrar = findViewById(R.id.btEntrarLogin);
        tvEsqueceuSenha = findViewById(R.id.tvEsqueceuSenhaLogin);

        tvCpfCnjp.setText("00011122233344");
        tvSenha.setText("123456");

        CarregarDialog carregarDialog = new CarregarDialog(MainActivity.this);
        carregamento = carregarDialog.criarDialogCarregamento();

        /** Sublinhar o texto "Esqueceu a senha?" */
        tvEsqueceuSenha.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvCpfCnjp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                email = tvCpfCnjp.getText().toString();
                /** Apresenta mensagem de campo obrigatório */
                if(!hasFocus){
                    if(email.isEmpty()){
                        findViewById(R.id.tvCpfCnpjInvalidoLogin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvCpfCnpjInvalidoLogin).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvCpfCnpjInvalidoLogin).setVisibility(View.GONE);
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
                        findViewById(R.id.tvSenhaInvalidaLogin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvSenhaInvalidaLogin).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvSenhaInvalidaLogin).setVisibility(View.GONE);
                }
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = tvCpfCnjp.getText().toString();
                password = tvSenha.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    carregamento.show();

                    thread = new Thread(MainActivity.this);
                    thread.start();
                    btEntrar.setClickable(false);

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
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }

    @Override
    public void run() {
        /**
         *  Chama a classe RotaLogin que irá se conectar com a rota /api/login em uma thread
         */
        RotaLogin login = new RotaLogin(email,password);
        Future<ConexaoAPI> future1 = threadpool.submit(login);

        while(!future1.isDone()){
            //Aguardando
        }

        //** Chama a classe RotaGetUser que ira se conectar coma a rota /api/get-user em uma thread
        RotaGetUser getUser = new RotaGetUser();
        Future<ConexaoAPI> future2 = threadpool.submit(getUser);

        ConexaoAPI conexao = null;
        try {
            conexao = future1.get();

            mensagensExceptions = conexao.getMensagensExceptions();

            if(mensagensExceptions == null){
                //Sem erro de conexão
                status = conexao.getCodigoStatus();
                Log.d("testeX"," st : " + status);

                if(status == 200){
                    //Login realizado com sucesso
                    if(conexao.getToken() != null){
                        conexao.fechar();

                        //Recuperando dados do usuario
                        conexao = future2.get();

                        mensagensExceptions = conexao.getMensagensExceptions();

                        if(mensagensExceptions == null){
                            //Sem erro de conexão
                            status = conexao.getCodigoStatus();

                            if(status == 200){
                                //Dados obitidos com sucesso
                                conexao.fechar();
                                if(Usuario.getPerfil().equals("Coordenador") || Usuario.getPerfil().equals("Produtor")){
                                    if(Produtor.isPrimeiroAcesso()){
                                        Intent intent = new Intent(getApplicationContext(),CadastroInicioActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new CarregarDialog(MainActivity.this).criarDialogAvisoPerfil().show();
                                        }
                                    });
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"Houve falha na comunicação com o servidor",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } else {
                            //Lançou exceção
                            Erro(mensagensExceptions[0],mensagensExceptions[1]);
                        }
                    }
                } else if(status == 401){
                    //Login não autenticado
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"CPF / CNPJ ou Senha inválidos!",Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    //Erro ao realizar login
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Erro ao realizar login!",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            } else {
                //Lançou exceção
                Erro(mensagensExceptions[0],mensagensExceptions[1]);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Erro("Falha na execução da conexão","Tente novamente em alguns minutos");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Erro("Interrupção da conexão","Tente novamente em alguns minutos");
        } finally {
            /** Fechar conexão caso esteja aberta */
            if(conexao != null){
                conexao.fechar();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    carregamento.dismiss();
                    btEntrar.setClickable(true);
                }
            });
        }
    }
}