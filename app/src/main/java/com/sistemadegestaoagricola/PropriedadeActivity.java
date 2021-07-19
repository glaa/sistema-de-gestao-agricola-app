package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

public class PropriedadeActivity extends AppCompatActivity {

    private CardView cvInformacoes;
    private CardView cvEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propriedade);

        cvInformacoes = findViewById(R.id.cvInformacoesPropriedade);
        cvEndereco = findViewById(R.id.cvEnderecoPropriedade);
    }
}