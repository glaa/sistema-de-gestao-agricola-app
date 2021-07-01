package com.sistemadegestaoagricola.primeiroacesso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sistemadegestaoagricola.R;

public class ExemploMapaActivity extends AppCompatActivity {

    private Button btFechar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exemplo_mapa);

        btFechar = findViewById(R.id.btFecharExemploMapa);

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}