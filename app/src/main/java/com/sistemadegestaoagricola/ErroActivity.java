package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ErroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erro);

        TextView titulo = findViewById(R.id.tvTituloErro);
        TextView subtitulo = findViewById(R.id.tvSubtituloErro);

        if(getIntent().hasExtra("TITULO")){
            Bundle extras = getIntent().getExtras();
            titulo.setText(extras.getString("TITULO"));
            subtitulo.setText(extras.getString("SUBTITULO"));
        }
    }
}