package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;
import com.sistemadegestaoagricola.entidades.Usuario;


public class HomeActivity extends AppCompatActivity {

    private ConexaoAPIVelha conexao;
    private int status;
    private String nome;
    private TextView saudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        saudacao = findViewById(R.id.tvSaudacaoHome);
        saudacao.setText("Olá," + Usuario.getNome() + "!");


    }
}