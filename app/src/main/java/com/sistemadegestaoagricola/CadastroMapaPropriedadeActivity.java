package com.sistemadegestaoagricola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CadastroMapaPropriedadeActivity extends AppCompatActivity {

    private Button btVerExemplo;
    private Button btFotografar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_mapa_propriedade);

        btVerExemplo = findViewById(R.id.btVerExemploCadastroMapaPropriedade);
        btFotografar = findViewById(R.id.btFotografarCadastroMapaPropriedade);

        btVerExemplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExemploMapaActivity.class);
                startActivity(intent);
            }
        });

        btFotografar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    //Pedindo permissão ao primeiro acesso
                    ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.CAMERA},0);
                } else {
                    //Já tem permissão
                    tirarFoto();
                    Log.d("testeX","oi else");

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Acesso permitido após solicitação
            tirarFoto();

        } else {
            //Acesso negado após a solicitação
            CarregarDialog dialog = new CarregarDialog(CadastroMapaPropriedadeActivity.this);
            AlertDialog alertDialog = dialog.criarDialogPermissaoCamera();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Permitir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.CAMERA},0);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Pular etapa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("testeX","Proxima tela");

                }
            });
            alertDialog.show();

        }

        Log.d("testeX","requestCode: " + requestCode + " grant: " + grantResults[0]);

    }

    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }

    /*Recebe a imagem que foi capturada com a câmera */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap foto = (Bitmap) extras.get("data");
            Log.d("testeX","recebeu foto");

        }
    }
}