package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

public class ExibirFotografiaMapaActivity extends AppCompatActivity {

    private ImageView ivFoto;
    private Button btExcluir;
    private Button btProximo;
    private Bitmap foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_fotografia_mapa);

        ivFoto = findViewById(R.id.ivFotoExibirFotografiaMapa);
        btExcluir = findViewById(R.id.btExcluirExibirFotografiaMapa);
        btProximo = findViewById(R.id.btProximoExibirFotografiaMapa);

        Uri uri;
        if(getIntent().hasExtra("PATH")){
            Bundle extras = getIntent().getExtras();
            uri = (Uri) extras.get("PATH");
            ivFoto.setImageURI(uri);
        }

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foto.recycle();
                onBackPressed();
                finish();
            }
        });

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarFoto();
                Intent intent = new Intent(getApplicationContext(),CadastroFonteAguaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void salvarFoto(){
        String mapa = Util.converterBitmapParaString(foto);
        Propriedade.setMapa(mapa);
        foto.recycle();
    }
}