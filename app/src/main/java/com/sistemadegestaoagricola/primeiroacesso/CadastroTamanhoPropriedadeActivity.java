package com.sistemadegestaoagricola.primeiroacesso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.Propriedade;

public class CadastroTamanhoPropriedadeActivity extends AppCompatActivity {

    private EditText etHectare;
    private Button btProximo;
    private boolean bloquearBotao = true;
    private int valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tamanho_propriedade);

        etHectare = findViewById(R.id.edtTamanhoCadastroTamanhoPropriedade);
        btProximo = findViewById(R.id.btProximoCadastroTamanhoPropriedade);

        if(etHectare.getText().toString().isEmpty()){
            valor = 0;
        } else {
            valor = Integer.parseInt(etHectare.getText().toString());
        }

        etHectare.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String hectare = etHectare.getText().toString();
                //Quando preciona a tecla enter
                if(i == 66){
                    if(!hectare.isEmpty()){
                        valor = Integer.parseInt(hectare);
                        if(valor > 0){
                            bloquearBotao = false;
                            btProximo.setClickable(true);
                            btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                        } else {
                            Toast.makeText(CadastroTamanhoPropriedadeActivity.this, "Digite um valor maior que 0", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        bloquearBotao = true;
                        btProximo.setClickable(false);
                        btProximo.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.cinza_escuro));
                        Toast.makeText(CadastroTamanhoPropriedadeActivity.this, "Digite um valor", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bloquearBotao && valor > 0){
                    salvarDados();
                    Intent intent = new Intent(getApplicationContext(),CadastroMapaPropriedadeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CadastroTamanhoPropriedadeActivity.this, "Digite um valor maior que 0", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void salvarDados(){
        Propriedade.setTamanho(valor);
    }
}