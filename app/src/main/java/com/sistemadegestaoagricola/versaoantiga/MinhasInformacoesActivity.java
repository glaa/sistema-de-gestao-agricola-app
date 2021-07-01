package com.sistemadegestaoagricola.versaoantiga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sistemadegestaoagricola.R;

public class MinhasInformacoesActivity extends AppCompatActivity {

    private Button btInformacoesPessoais;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);

        Toolbar toolbar = findViewById(R.id.toolbarMinhasInformacoes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Voltar");

        btInformacoesPessoais = findViewById(R.id.btInformacoesPessoaisMinhasInformacoes);

        btInformacoesPessoais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExibirInformacoesPessoaisActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }
}