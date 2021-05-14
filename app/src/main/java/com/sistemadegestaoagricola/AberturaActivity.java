package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AberturaActivity extends AppCompatActivity implements Runnable{

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {
        String[] mensagens = null;
        try{
            Thread.sleep(3000);

            String rota = "index";
            String parametros = "";
            String metodo = "GET";
            ConexaoAPI con = new ConexaoAPI(rota,parametros,metodo,null);
            String[] m = con.iniciar();
            mensagens = m;
            con.fechar();

        } catch (Exception e){
            String[] m = {"Erro ao iniciar!", "Tente novamente em alguns minutos"};
            mensagens = m;
        }
        finish();
        if(mensagens == null){
            startActivity(new Intent(this,MainActivity.class));
        } else {
            String titulo = mensagens[0];
            String subtitulo = mensagens[1];

            Intent intent = new Intent(this, ErroActivity.class);
            intent.putExtra("TITULO", titulo);
            intent.putExtra("SUBTITULO", subtitulo);
            this.startActivity(intent);
        }
    }
}