package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sistemadegestaoagricola.entidades.Mapa;
import com.sistemadegestaoagricola.entidades.Reuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.File;

public class ExibirFotoActivity extends AppCompatActivity {

    private ImageView ivFoto;
    private Button btFechar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_foto);

        ivFoto = findViewById(R.id.ivExibirFoto);
        btFechar = findViewById(R.id.btFecharExibirFoto);

        if(getIntent().hasExtra("MAPA")){
            Bundle extras = getIntent().getExtras();
            int posicao = (int) extras.get("MAPA");
            ivFoto.setImageBitmap(Mapa.getMapas().get(posicao).getFoto());
        } else if(getIntent().hasExtra("ATA")){
            Bundle extras = getIntent().getExtras();
            int posicao = (int) extras.get("ATA");
            ivFoto.setImageBitmap(Reuniao.getAtasBitmap().get(posicao));
        } else if(getIntent().hasExtra("REUNIAO")){
            Bundle extras = getIntent().getExtras();
            int posicao = (int) extras.get("REUNIAO");
            ivFoto.setImageBitmap(Reuniao.getFotosBitmap().get(posicao));
        }

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}