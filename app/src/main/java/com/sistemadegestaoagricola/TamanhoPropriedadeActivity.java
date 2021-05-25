package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TamanhoPropriedadeActivity extends AppCompatActivity {

    private EditText etHectare;
    private Button btProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamanho_propriedade);

        etHectare = findViewById(R.id.etHectareamanhoPropriedade);
        btProximo = findViewById(R.id.btProximoTamanhoPropriedade);

        btProximo.setClickable(false);

        etHectare.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == 66){
                    if(!etHectare.getText().toString().equals("")){
                        btProximo.setClickable(true);
                        btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.botao_verde));
                    } else {
                        btProximo.setClickable(false);
                        btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.cinza_claro));
                    }
                }
                return false;
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                Log.d("testeX","Clicou");
            }
        });
    }
}