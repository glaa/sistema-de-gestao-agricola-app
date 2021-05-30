package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ExibirFotografiaMapaActivity extends AppCompatActivity {

    private ImageView ivFoto;
    private Button btExcluir;
    private Button btProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_fotografia_mapa);

        ivFoto = findViewById(R.id.ivFotoExibirFotografiaMapa);
        btExcluir = findViewById(R.id.btExcluirExibirFotografiaMapa);
        btProximo = findViewById(R.id.btProximoExibirFotografiaMapa);

        if(getIntent().hasExtra("FOTO")){
            Bundle extras = getIntent().getExtras();
            Bitmap foto = (Bitmap) extras.get("FOTO");
            ivFoto.setImageBitmap(foto);
        }

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CadastroFonteAguaActivity.class);
                startActivity(intent);
            }
        });

    }
}