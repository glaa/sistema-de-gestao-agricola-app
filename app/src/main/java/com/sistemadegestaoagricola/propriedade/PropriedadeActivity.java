package com.sistemadegestaoagricola.propriedade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetEndereco;
import com.sistemadegestaoagricola.conexao.RotaGetPropriedade;
import com.sistemadegestaoagricola.entidades.CarregarDialog;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PropriedadeActivity extends AppCompatActivity {

    private CardView cvInformacoes;
    private CardView cvEndereco;
    private LinearLayout llVoltar;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    private Thread thread;
    CarregarDialog carregarDialog = new CarregarDialog(PropriedadeActivity.this);
    AlertDialog carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propriedade);

        cvInformacoes = findViewById(R.id.cvInformacoesPropriedade);
        cvEndereco = findViewById(R.id.cvEnderecoPropriedade);
        llVoltar = findViewById(R.id.llVoltarPropriedade);

        carregando = carregarDialog.criarDialogCarregamento();

        cvInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando.show();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        buscarPropriedade();
                        Intent intent = new Intent(PropriedadeActivity.this, InformacoesActivity.class);
                        startActivity(intent);
                    }
                });
                thread.start();
            }
        });

        cvEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando.show();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        buscarEndereco();
                        Intent intent = new Intent(PropriedadeActivity.this, AlterarLocalizacaoActivity.class);
                        startActivity(intent);
                    }
                });
                thread.start();
            }
        });

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { voltar(); }
        });
    }

    private void voltar(){
        onBackPressed();
        finish();
    }

    private void buscarPropriedade(){
        RotaGetPropriedade rotaGetPropriedade = new RotaGetPropriedade();
        Future<ConexaoAPI> future = threadpool.submit(rotaGetPropriedade);

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
                Log.d("testeX","statusSalvar =" + status);
                if(status == 200){
                    //Salvo com sucesso
                    //Toast.makeText(this, "Busca de reuniões realizada", Toast.LENGTH_SHORT).show();
                } else {
                    //Erro ao salvar
                    Toast.makeText(this, "Erro na busca de reuniões", Toast.LENGTH_SHORT).show();
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
            carregando.dismiss();
        }
    }

    private void buscarEndereco(){
        RotaGetEndereco rotaGetEndereco = new RotaGetEndereco();
        Future<ConexaoAPI> future = threadpool.submit(rotaGetEndereco);

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
                Log.d("testeX","statusSalvar =" + status);
                if(status == 200){
                    //Salvo com sucesso
                    //Toast.makeText(this, "Busca de reuniões realizada", Toast.LENGTH_SHORT).show();
                } else {
                    //Erro ao salvar
                    Toast.makeText(this, "Erro na busca de reuniões", Toast.LENGTH_SHORT).show();
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
            carregando.dismiss();
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }
}