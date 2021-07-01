 package com.sistemadegestaoagricola.primeiroacesso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

 public class CadastroMapaPropriedadeActivity extends AppCompatActivity {

    private Button btVerExemplo;
    private Button btFotografar;
    private String[] permisoes = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int CODIGO_PERMISSOES = 1;
    private final int CAMERA = 1;
    private Uri uri;
    private File arquivo;

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

        //Acesso negado após a solicitação
        CarregarDialog dialog = new CarregarDialog(CadastroMapaPropriedadeActivity.this);
        AlertDialog alertDialog = dialog.criarDialogPermissaoCamera();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Permitir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                verificarPermisoes();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Pular etapa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("testeX","Proxima tela");

            }
        });

        btFotografar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(verificarPermisoes()){
                    tirarFoto();
                } else {
                    alertDialog.show();
                }
            }
        });
    }

    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            arquivo = criarArquivo();
            if(arquivo != null){
                uri = FileProvider.getUriForFile(this,"com.sistemadegestaoagricola.provider",arquivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,CAMERA);
            }
        }
    }

    /*Recebe a imagem que foi capturada com a câmera */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Log.d("testeX", "byte =" + extras.getSize("data"));
            //Adiciona foto na galeria
            Intent mediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            sendBroadcast(mediaIntent);

            Intent intent = new Intent(this,ExibirFotografiaMapaActivity.class);
            intent.putExtra("PATH", arquivo.getAbsolutePath());
            intent.putExtra("URI", uri);
            startActivity(intent);

            Log.d("testeX", "caminho imagem = " + uri.getPath());
        } else {
            //Excluir arquivo no caso de não haver fotografia
            arquivo.delete();
        }
        Log.d("testeX", "entrou no activity result ");

    }

    private void pedirPermissoes(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //Pedindo permissão ao primeiro acesso
            ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.CAMERA},0);
            
        } else if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Pedindo permissão de armazenamento ao primeiro acesso
            ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    private boolean verificarPermisoes(){
        List<String> permissoesRequeridas = new ArrayList<>();
        boolean permissao = false;

        for(String permisao : permisoes){
            //Pedi permissão caso ainda não tenha
            if(ContextCompat.checkSelfPermission(this,permisao) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{permisao},CODIGO_PERMISSOES);
            }
            //Acrescenta no array as permissões não concedidas
            if(ContextCompat.checkSelfPermission(this,permisao) != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permisao);
            }
        }

        if(permissoesRequeridas.isEmpty()){
            permissao = true;
        }

        return permissao;
    }

    private File criarArquivo(){
        String filename = "Mapa_" + System.currentTimeMillis();
        File diretorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File arquivo = null;
        try {
            arquivo = File.createTempFile(filename,".jpg",diretorio);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!diretorio.exists()){
            if(!diretorio.mkdirs()){
                Log.d("testeX","falha ao criar diretorio");
                return null;
            }
        }

        return arquivo;
    }
}