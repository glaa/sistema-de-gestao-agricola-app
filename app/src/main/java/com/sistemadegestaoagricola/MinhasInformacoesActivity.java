package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MinhasInformacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);

        Toolbar toolbar = findViewById(R.id.toolbarMinhasInformacoes);
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