package com.sistemadegestaoagricola.versaoantiga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.sistemadegestaoagricola.R;

public class ExibirInformacoesPessoaisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_informacoes_pessoais);

        Toolbar toolbar = findViewById(R.id.toolbarExibirInformacoesPessoais);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Voltar");


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }
}