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
    private String[] mensagensExceptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        thread = new Thread(this);
        thread.start();
    }


    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","AberturaActivity");
        this.startActivity(intent);
    }

    @Override
    public void run() {
        /**
         *  Chama a classe Index que irá se conectar com a rota /api/index em uma thread
         */
        RotaIndex index = new RotaIndex();
        Future<ConexaoAPI> future = threadpool.submit(index);

        ConexaoAPI conexao = null;
        try {
            thread.sleep(3000);
            conexao = future.get();
            mensagensExceptions = conexao.getMensagensExceptions();

            /** mensagensExceptions será null no caso em que a conexão não gerou exceções */
            if(mensagensExceptions == null){
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                finish();
            } else {
                Erro(mensagensExceptions[0], mensagensExceptions[1]);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Erro("Falha na execução da conexão","Tente novamente em alguns minutos");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Erro("Interrupção na conexão","Tente novamente em alguns minutos");
        }

        /** Encerrando a conexão pelo atributo static */
        if(conexao != null){
            conexao.fechar();
        }
    }
}