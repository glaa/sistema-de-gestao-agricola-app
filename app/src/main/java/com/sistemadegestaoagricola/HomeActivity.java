package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;
import com.sistemadegestaoagricola.entidades.Usuario;


public class HomeActivity extends AppCompatActivity {

    private ConexaoAPIVelha conexao;
    private int status;
    private String nome;
    private TextView tvSaudacao;
    private Button btMinhaPropriedade;
    private Button btMinhasInformacoes;
    private Button btReuniao;
    private Button btSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvSaudacao = findViewById(R.id.tvSaudacaoHome);
        tvSaudacao.setText("Olá, " + Usuario.getNome() + "!");

        btMinhaPropriedade = findViewById(R.id.btMinhaPropriedadeHome);
        btMinhasInformacoes = findViewById(R.id.btMinhasInformacoesHome);
        btReuniao = findViewById(R.id.btReuniaoHome);
        btSair = findViewById(R.id.btSairHome);

        btMinhaPropriedade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MinhaPropriedadeActivity.class);
                startActivity(intent);
            }
        });

        btMinhasInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MinhasInformacoesActivity.class);
                startActivity(intent);
            }
        });

        btReuniao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReuniaoActivity.class);
                startActivity(intent);
            }
        });

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}