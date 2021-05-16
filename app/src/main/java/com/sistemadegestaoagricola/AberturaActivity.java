package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaIndex;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AberturaActivity extends AppCompatActivity implements Runnable{

    Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        Log.d("testeX","iniciou");
        thread = new Thread(this);
        thread.start();
    }


    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        this.startActivity(intent);
    }

    @Override
    public void run() {
        /**
         *  Chama a classe Index que irá se conectar com a rota /api/index em uma thread
         */
        RotaIndex index = new RotaIndex();
        Future<ConexaoAPI> future = threadpool.submit(index);

        //** Aguarda a classe Index terminar a conexao */
        while(!future.isDone()){
            //Aguardando o termino da conexão
        }

        ConexaoAPI conexao = null;
        try {
            thread.sleep(3000);
            conexao = future.get();
            mensagensErro = conexao.getMensagensErro();
            if(mensagensErro == null){
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            } else {
                Erro(mensagensErro[0],mensagensErro[1]);
            }
            Log.d("testeX","meio");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Erro("Erro","Falha com a conexão, tente novamente em alguns minutos");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Erro("Erro","Falha com a conexão, tente novamente em alguns minutos");
        }

        /** Encerrando a conexão pelo atributo static */
        if(conexao != null){
            conexao.fechar();
        }

        Log.d("testeX","terminou");
        finish();
    }
}