package com.sistemadegestaoagricola.mapa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.principal.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.MapasAdapter;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetMapas;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Mapa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapasActivity extends AppCompatActivity {

    private RecyclerView rvMapas;
    private LinearLayout llVoltar;
    private Button btAdicionar;
    ArrayList<Mapa> mapas = new ArrayList<>();
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    private String[] permisoes = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int CODIGO_PERMISSOES = 1;
    private final int CAMERA = 1;
    private Uri uri;
    private File arquivo;
    CarregarDialog dialog = new CarregarDialog(MapasActivity.this);
    AlertDialog carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        rvMapas = findViewById(R.id.rvMapas);
        btAdicionar = findViewById(R.id.btAdicionarMapaMapas);
        llVoltar = findViewById(R.id.llVoltarMapas);

        carregando = dialog.criarDialogCarregamento();

        buscarMapas();

        mapas = Mapa.getMapas();
        Collections.reverse(mapas);

        MapasAdapter mapasAdapter = new MapasAdapter(this,mapas,carregando);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMapas.setLayoutManager(layoutManager);
        rvMapas.setAdapter(mapasAdapter);

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando.show();
                Intent intent = new Intent(MapasActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Acesso negado após a solicitação
        AlertDialog alertDialog = dialog.criarDialogPermissaoCamera();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Permitir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                verificarPermisoes();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("testeX","Proxima tela");

            }
        });

        btAdicionar.setOnClickListener(new View.OnClickListener() {
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

    private void buscarMapas(){
        RotaGetMapas rotaGetMapas = new RotaGetMapas();
        Future<ConexaoAPI> future = threadpool.submit(rotaGetMapas);

        while(!future.isDone()){
            //Aguardando
        }

        ConexaoAPI conexao = null;
        try{
            conexao = future.get();

            mensagensExceptions = conexao.getMensagensExceptions();

            if(mensagensExceptions == null){
                //Sem erro de conexão
                status = conexao.getCodigoStatus();
                if(status != 200){
                    //Erro ao salvar
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapasActivity.this, "Erro na busca dos mapas", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } else {
                Erro(mensagensExceptions[0],mensagensExceptions[1]);
            }

        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Erro("Falha na conexão","Tente novamente em alguns minutos");
        } finally {
            if(conexao != null){
                conexao.fechar();
            }
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
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

            Intent intent = new Intent(this, ExibirFotografiaNovoMapaActivity.class);
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

    @Override
    protected void onPause() {
        Log.d("testeX","pausou");
        carregando.dismiss();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}