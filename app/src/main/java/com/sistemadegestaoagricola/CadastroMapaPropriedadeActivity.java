 package com.sistemadegestaoagricola;

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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
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
//                if(ContextCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                    //Pedindo permissão ao primeiro acesso
//                    ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.CAMERA},0);
//
//                } else if(ContextCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                    //Pedindo permissão de armazenamento ao primeiro acesso
//                    ActivityCompat.requestPermissions(CadastroMapaPropriedadeActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
//                } else {
//                    //Já tem permissão
//                    tirarFoto();
//                    Log.d("testeX","oi else");
//
//                }
                if(verificarPermisoes()){
                    tirarFoto();
                } else {
                    //Acesso negado após a solicitação
                    CarregarDialog dialog = new CarregarDialog(CadastroMapaPropriedadeActivity.this);
                    AlertDialog alertDialog = dialog.criarDialogPermissaoCamera();
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Permitir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pedirPermissoes();
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
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            //Acesso permitido após solicitação
//            tirarFoto();
//
//        } else {
//            //Acesso negado após a solicitação
//            CarregarDialog dialog = new CarregarDialog(CadastroMapaPropriedadeActivity.this);
//            AlertDialog alertDialog = dialog.criarDialogPermissaoCamera();
//            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Permitir", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    pedirPermissoes();
//                }
//            });
//            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Pular etapa", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Log.d("testeX","Proxima tela");
//
//                }
//            });
//            alertDialog.show();
//
//        }

        //Log.d("testeX","requestCode: " + requestCode + " grant: " + grantResults[0]);

    }

    private void tirarFoto(){
        File arquivo = criarArquivo();
        if(arquivo != null){

            if (Build.VERSION.SDK_INT>=24)
            {
                uri = FileProvider.getUriForFile(this,"com.studio.cameraalbumtest.fileprovider",arquivo);
            }
            else
            {
                uri = Uri.fromFile(arquivo);
            }

            uri = Uri.fromFile(arquivo);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,CAMERA);
        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager()) != null){
//            startActivityForResult(intent,1);
//        }
    }

    /*Recebe a imagem que foi capturada com a câmera */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap foto = (Bitmap) extras.get("data");
//
//            Intent intent = new Intent(this,ExibirFotografiaMapaActivity.class);
//            intent.putExtra("FOTO", foto);
//            startActivity(intent);
//            Log.d("testeX","recebeu foto");
//
//        }
        if(requestCode == CAMERA && resultCode == RESULT_OK){
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            sendBroadcast(intent);
            Log.d("testeX", "caminho imagem = " + uri.getPath());
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

        for(String permisao : permisoes){
            if(ContextCompat.checkSelfPermission(this,permisao) != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permisao);
            }

            if(!permissoesRequeridas.isEmpty()){
                ActivityCompat.requestPermissions(this,permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), CODIGO_PERMISSOES);
            }
        }
        permissoesRequeridas.clear();

        for(String permisao : permisoes){
            if(ContextCompat.checkSelfPermission(this,permisao) != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permisao);
                return false;
            }
        }

        return true;
    }

    private File criarArquivo(){
        File diretorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"agroecologia");
        if(!diretorio.exists()){
            if(!diretorio.mkdirs()){
                Log.d("testeX","falha ao criar diretorio");
                return null;
            }
        }
        String filename = System.currentTimeMillis() + ".jpg";
        File arquivo = new File(diretorio.getPath()+File.separator+filename);

        return arquivo;
    }
}