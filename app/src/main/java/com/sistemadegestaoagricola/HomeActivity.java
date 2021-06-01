package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
    private CardView btMinhaPropriedade;
    private CardView btMinhasInformacoes;
    private CardView btReuniao;
    private CardView btSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvSaudacao = findViewById(R.id.tvNomeHome);
        tvSaudacao.setText(Usuario.getNome() + "!");

        btMinhaPropriedade = findViewById(R.id.cvPropriedadeHome);
        //btMinhasInformacoes = findViewById(R.id.btMinhasInformacoesHome);
        btReuniao = findViewById(R.id.cvReunioesHome);
        btSair = findViewById(R.id.cvSairHome);

        btMinhaPropriedade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MinhaPropriedadeActivity.class);
                startActivity(intent);
            }
        });

//        btMinhasInformacoes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),MinhasInformacoesActivity.class);
//                startActivity(intent);
//            }
//        });

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