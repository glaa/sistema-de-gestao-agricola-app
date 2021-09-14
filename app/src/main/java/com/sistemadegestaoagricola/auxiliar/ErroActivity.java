package com.sistemadegestaoagricola.auxiliar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sistemadegestaoagricola.AberturaActivity;
import com.sistemadegestaoagricola.R;

public class ErroActivity extends AppCompatActivity {

    private Button btTentarNovamente;
    private Button btSair;
    private String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erro);



        TextView titulo = findViewById(R.id.tvTituloErro);
        TextView subtitulo = findViewById(R.id.tvSubtituloErro);

        btTentarNovamente = findViewById(R.id.btTentarNovamenteErro);
        btSair = findViewById(R.id.btSairErro);

        if(getIntent().hasExtra("TITULO")){
            Bundle extras = getIntent().getExtras();
            titulo.setText(extras.getString("TITULO"));
            subtitulo.setText(extras.getString("SUBTITULO"));
            activity = extras.getString("ACTIVITY");
        }
        btTentarNovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity.equals("AberturaActivity")){
                    Intent intent = new Intent(getApplicationContext(), AberturaActivity.class);
                    startActivity(intent);
                } else {
                    onBackPressed();
                }
                finish();
            }
        });

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}