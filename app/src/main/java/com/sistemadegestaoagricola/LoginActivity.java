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

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetUser;
import com.sistemadegestaoagricola.conexao.RotaLogin;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Produtor;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.primeiroacesso.CadastroInicioActivity;
import com.sistemadegestaoagricola.principal.HomeActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity implements Runnable {

    private EditText edtCpfEmail;
    private EditText edtSenha;
    private Button btEntrar;
    private TextView tvEsqueceuSenha;
    private String cpfEmail = "";
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
        setContentView(R.layout.activity_login);

        edtCpfEmail = findViewById(R.id.edtCpfEmailMain);
        edtSenha = findViewById(R.id.edtSenhaMain);
        btEntrar = findViewById(R.id.btEntrarMain);
        tvEsqueceuSenha = findViewById(R.id.tvEsqueceuSenhaMain);

        edtCpfEmail.setText("41045345369");
        edtSenha.setText("123456");

        CarregarDialog carregarDialog = new CarregarDialog(LoginActivity.this);
        carregamento = carregarDialog.criarDialogCarregamento();

        /** Sublinhar o texto "Esqueceu a senha?" */
        tvEsqueceuSenha.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        edtCpfEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cpfEmail = edtCpfEmail.getText().toString();
                /** Apresenta mensagem de campo obrigatório */
                if(!hasFocus){
                    if(cpfEmail.isEmpty() || !CPFValido()){
                        findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.tvCpfCnpjInvalidoMain).setVisibility(View.GONE);
                }
            }
        });

        edtSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password = edtSenha.getText().toString();
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
                cpfEmail = edtCpfEmail.getText().toString();
                password = edtSenha.getText().toString();

                if(!cpfEmail.isEmpty() && !password.isEmpty() && CPFValido()){
                    carregamento.show();

                    thread = new Thread(LoginActivity.this);
                    thread.start();
                    btEntrar.setClickable(false);

                } else {
                    Toast.makeText(getApplicationContext(),"CPF e Senha devem ser preenchidos corretamente!",Toast.LENGTH_LONG).show();
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
        RotaLogin login = new RotaLogin(cpfEmail,password);
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
                                        Intent intent = new Intent(getApplicationContext(), CadastroInicioActivity.class);
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
                                            new CarregarDialog(LoginActivity.this).criarDialogAvisoPerfil().show();
                                        }
                                    });
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"Houve falha na comunicação com o servidor",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(LoginActivity.this,"CPF ou Senha inválidos!",Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    //Erro ao realizar login
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"Erro ao realizar login!",Toast.LENGTH_LONG).show();

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

    private boolean CPFValido(){
        String cpf = edtCpfEmail.getText().toString();
        //Caso a quantidade de dígito esteja incorreto
        if(cpf.length() != 11){
            return false;
        }
        int digito1 = Integer.parseInt(String.valueOf(cpf.charAt(9)));
        int digito2 = Integer.parseInt(String.valueOf(cpf.charAt(10)));

        //Verifica primeiro dígito
        int somatorio = 0;
        for(int i = 0; i < 9; i++){
            int digito = Integer.parseInt(String.valueOf(cpf.charAt(i)));
            int multiplicador = 10 - i;
            somatorio += digito * multiplicador;
        }
        int resto  = somatorio % 11;
        Log.d("testeX","s = " + somatorio + " d = " + digito1 + " r = " + resto);
        int verificador;
        if((11 - resto) > 10){
            verificador = 0;
        } else {
            verificador = 11 - resto;
        }

        if(verificador == digito1){
            //Verifica segungo dígito
            somatorio = 0;
            for(int i = 0; i < 10; i++){
                int digito = Integer.parseInt(String.valueOf(cpf.charAt(i)));
                int multiplicador = 11 - i;
                somatorio += digito * multiplicador;
            }
            resto  = somatorio % 11;
            Log.d("testeX","s = " + somatorio + " d2 = " + digito2 + " r = " + resto);
            if(11 - resto > 10){
                verificador = 0;
            } else {
                verificador = 11 - resto;
            }
            if(verificador == digito2){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}