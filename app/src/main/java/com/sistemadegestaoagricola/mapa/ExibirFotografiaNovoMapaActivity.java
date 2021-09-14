package com.sistemadegestaoagricola.mapa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.principal.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaSalvarMapa;
import com.sistemadegestaoagricola.entidades.CarregarDialog;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExibirFotografiaNovoMapaActivity extends AppCompatActivity {

    private ImageView ivFoto;
    private Button btExcluir;
    private Button btConcluir;
    private Uri uri;
    private String path;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    CarregarDialog carregarDialog = new CarregarDialog(ExibirFotografiaNovoMapaActivity.this);
    AlertDialog salvando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_fotografia_novo_mapa);

        ivFoto = findViewById(R.id.ivFotoExibirFotografiaNovoMapa);
        btExcluir = findViewById(R.id.btExcluirExibirFotografiaNovoMapa);
        btConcluir = findViewById(R.id.btConcluirExibirFotografiaNovoMapa);

        if(getIntent().hasExtra("PATH")){
            Bundle extras = getIntent().getExtras();
            uri = (Uri) extras.get("URI");
            path = (String) extras.get("PATH");
            ivFoto.setImageURI(uri);
        }

        //Criando o AlertDialog do salvamento
        salvando = carregarDialog.criarDialogSalvarInformacoes();

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvando.show();
                salvarFotoAPI();
            }
        });
    }

    private void salvarFotoAPI(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RotaSalvarMapa rotaSalvarMapa = new RotaSalvarMapa(path);
                Future<ConexaoAPI> future = threadpool.submit(rotaSalvarMapa);

                while(!future.isDone()){
                    //Aguardando
                }

                ConexaoAPI conexao = null;
                try{
                    conexao = future.get();

                    mensagensExceptions = conexao.getMensagensExceptions();

                    if(mensagensExceptions == null){
                        //Sem erro de conexão
                        status = conexao.getCodigoStatus();
                        if(status == 200){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Salvo com sucesso
                                    Toast.makeText(ExibirFotografiaNovoMapaActivity.this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Erro ao salvar
                                    Toast.makeText(ExibirFotografiaNovoMapaActivity.this, "Dados não puderam ser salvos", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Erro(mensagensExceptions[0],mensagensExceptions[1]);
                    }

                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                    Erro("Falha na conexão","Tente novamente em alguns minutos");
                } finally {
                    if(conexao != null){
                        conexao.fechar();
                    }
                    salvando.dismiss();
                    Intent intent = new Intent(ExibirFotografiaNovoMapaActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
        thread.start();
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }
}