package com.sistemadegestaoagricola.coordenador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.AtaReuniaoActivity;
import com.sistemadegestaoagricola.FotosReuniaoActivity;
import com.sistemadegestaoagricola.conexao.RotaCadastrarPropriedade;
import com.sistemadegestaoagricola.conexao.RotaRegistrarReuniao;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.primeiroacesso.CadastroLocalizacaoActivity;
import com.sistemadegestaoagricola.reuniao.EditarReuniaoActivity;
import com.sistemadegestaoagricola.ErroActivity;
import com.sistemadegestaoagricola.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaExcluirReuniao;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;
import com.sistemadegestaoagricola.reuniao.ReuniaoActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProximaReuniaoCoordenadorActivity extends AppCompatActivity implements Runnable{

    private TextView tvTema;
    private TextView tvDiaSemana;
    private TextView tvDiaMes;
    private TextView tvMes;
    private TextView tvAno;
    private TextView tvHorario;
    private TextView tvLocal;
    private LinearLayout llEditar;
    private LinearLayout llExcluir;
    private LinearLayout llAta;
    private LinearLayout llFotos;
    private LinearLayout llVoltar;
    private Button btRegistrar;
    AgendamentoReuniao reuniao;
    private AlertDialog excluindo,confirmando,salvando;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private ConexaoAPI conexao;
    private int status;
    private final int CODE_ATA = 1;
    private final int CODE_FOTOS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxima_reuniao_coordenador);

        tvTema = findViewById(R.id.tvTemaProximaReuniaoCoordenador);
        tvDiaSemana = findViewById(R.id.tvDiaSemanaProximaReuniaoCoordenador);
        tvDiaMes = findViewById(R.id.tvDiaMesProximaReuniaoCoordenador);
        tvMes = findViewById(R.id.tvMesProximaReuniaoCoordenador);
        tvAno = findViewById(R.id.tvAnoProximaReuniaoCoordenador);
        tvHorario = findViewById(R.id.tvHorarioProximaReuniaoCoordenador);
        tvLocal = findViewById(R.id.tvLocalProximaReuniaoCoordenador);
        llEditar = findViewById(R.id.llEditarProximaReuniaoCoordenador);
        llExcluir = findViewById(R.id.llExcluirProximaReuniaoCoordenador);
        llAta = findViewById(R.id.llAtaProximaReuniaoCoordenador);
        llFotos = findViewById(R.id.llFotosProximaReuniaoCoordenador);
        llVoltar = findViewById(R.id.llVoltarProximaReuniaoCoordenador);
        btRegistrar = findViewById(R.id.btRegistrarProximaReuniao);

        CarregarDialog carregarDialog = new CarregarDialog(this);

         reuniao = null;
        if(getIntent().hasExtra("REUNIAO")){
            Bundle bundle = getIntent().getExtras();
            reuniao = (AgendamentoReuniao) bundle.get("REUNIAO");
            preencherCampo(reuniao);
        }

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        llEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testeX", "id reuniao = " + reuniao.getId());
                Intent intent = new Intent(ProximaReuniaoCoordenadorActivity.this, EditarReuniaoActivity.class);
                intent.putExtra("REUNIAO",reuniao);
                startActivity(intent);
                finish();
            }
        });

        llExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmando = carregarDialog.criarDialogExcluirReuniao();
                confirmando.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluindo = carregarDialog.criarDialogExcluirInformacoes();
                        excluindo.show();
                        thread = new Thread( ProximaReuniaoCoordenadorActivity.this);
                        thread.start();
                    }
                });

                confirmando.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                confirmando.show();
            }
        });

        llAta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permitiRegistro()){
                    /*
                     * experimento
                     */
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, CODE_ATA);

                    /**/

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Selecione suas fotos"), CODE_ATA);
                } else {
                    Log.d("testeX","não permitida");
                    carregarDialog.criarDialogRegistroNaoPermitido().show();
                }
            }
        });

        llFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permitiRegistro()){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Selecione suas fotos"), CODE_FOTOS);
                } else {
                    carregarDialog.criarDialogRegistroNaoPermitido().show();
                }
            }
        });

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permitiRegistro()){
                    ArrayList<String> ata = Util.getAta();
                    ArrayList<String> fotos = Util.getFotos();
//                    ArrayList<String> arquivosAta = new ArrayList<>();
//                    ArrayList<String> arquivosFotos = new ArrayList<>();

                    if(ata.isEmpty()){
                        Toast.makeText(ProximaReuniaoCoordenadorActivity.this,"Adicione a ata da reunião",Toast.LENGTH_LONG).show();
                    } else if(fotos.isEmpty()){
                        Toast.makeText(ProximaReuniaoCoordenadorActivity.this,"Adicione as fotos da reunião",Toast.LENGTH_LONG).show();
                    } else {
//                        for(Uri uri : ata){
//                            //File file = new File(uri.getPath());
//
//                            arquivosAta.add(getImagePath(uri));
//                            Log.d("testeX", "ata get: " +  getImagePath(uri));
//                        }
//                        for(Uri uri : fotos){
//                            //File file = new File(getImagePath(uri));
//                            arquivosFotos.add(getImagePath(uri));
//                            Log.d("testeX", "foto get: " +  getImagePath(uri));
//
//                        }
                        salvando = carregarDialog.criarDialogSalvarInformacoes();
                        salvando.show();
                        salvarCadastroAPI();
                    }
                } else {
                    carregarDialog.criarDialogRegistroNaoPermitido().show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = null;

        if(requestCode == CODE_ATA && resultCode == Activity.RESULT_OK) {
            ArrayList<String> imagesPathString = new ArrayList<>();

            if(data.getClipData() != null){
                //Multiplos itens foram selecionados
                int j = data.getClipData().getItemCount();

                for (int i = 0; i < j; i++){
                    String path = getImagePath(data.getClipData().getItemAt(i).getUri());
                    imagesPathString.add(path);
                }
            } else if(data.getData() != null){
                //Apenas um item selecionado
                String path = getImagePath(data.getData());
                imagesPathString.add(path);
            }
            Util.setAta(imagesPathString);

        } else if(requestCode == CODE_FOTOS && resultCode == Activity.RESULT_OK){
            ArrayList<String> imagesPathString = new ArrayList<>();
            if(data.getClipData() != null){
                //Multiplos itens foram selecionados
                int j = data.getClipData().getItemCount();

                for (int i = 0; i < j; i++){
                    String path = getImagePath(data.getClipData().getItemAt(i).getUri());
                    imagesPathString.add(path);
                }
            } else if( data.getData() != null){
                //Apenas um item selecionado
                String path = getImagePath(data.getData());
                imagesPathString.add(path);
            }
            Util.setFotos(imagesPathString);
        }

        //Abrir Activity mesmo que não tenha selecionado imagens
        if(requestCode == CODE_ATA){
            intent = new Intent(ProximaReuniaoCoordenadorActivity.this, AtaReuniaoActivity.class);
        } else if(requestCode == CODE_FOTOS){
            intent = new Intent(ProximaReuniaoCoordenadorActivity.this, FotosReuniaoActivity.class);
        }
        startActivity(intent);
    }

    private void preencherCampo(AgendamentoReuniao reuniao){
        tvTema.setText(reuniao.getNome());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reuniao.getData());
        tvDiaSemana.setText(Util.calendarioIntParaStringDia(calendar.get(Calendar.DAY_OF_WEEK)).
                substring(0,3));
        tvDiaMes.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        tvMes.setText(Util.calendarioIntParaStringMes(calendar.get(Calendar.MONTH)));
        tvHorario.setText(Util.formatarDiaHoraCalendar(calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + Util.formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE)));
        tvAno.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvLocal.setText(reuniao.getLocal());
    }
    @Override
    public void run() {

        /**
         *  Chama a classe RotaAgendarReuniao que irá se conectar com a rota /api/agendar-reuniao em uma thread
         */
        RotaExcluirReuniao agendarReuniao = new RotaExcluirReuniao(reuniao.getId());
        Future<ConexaoAPI> future1 = threadpool.submit(agendarReuniao);

        while(!future1.isDone()){
            //Aguardando
        }

        ConexaoAPI conexao = null;
        try {
            conexao = future1.get();

            mensagensExceptions = conexao.getMensagensExceptions();

            if(mensagensExceptions == null){
                //Sem erro de conexão
                status = conexao.getCodigoStatus();
                Log.d("testeX"," st : " + status);

                if(status == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Login realizado com sucesso
                            Toast.makeText(getApplicationContext(),"Excluído com sucesso", Toast.LENGTH_LONG).show();
                            Util.esvaziarAgendamentos();
                            new HomeActivity().buscarReunioes();
                            voltar();
                            finishAffinity();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Erro ao excluir", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                //Lançou exceção
                Erro(mensagensExceptions[0],mensagensExceptions[1]);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Erro("Falha na execução da conexão","Tente novamente em alguns minutos");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Erro("Interrupção da conexão","Tente novamente em alguns minutos");
        } finally {
            /** Fechar conexão caso esteja aberta */
            if(conexao != null){
                conexao.fechar();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    excluindo.dismiss();
                }
            });
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }

    private void voltar(){
        Intent intent = new Intent(ProximaReuniaoCoordenadorActivity.this, ReuniaoActivity.class);
        startActivity(intent);
    }

    private boolean permitiRegistro(){
        Date dataAtual = new Date();
        dataAtual.setHours(23);
        dataAtual.setMinutes(59);

        if(reuniao.getData().before(dataAtual)){
            return true;
        } else {
            return false;
        }
    }

    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void salvarCadastroAPI(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("testeX","iniciou registro ");

                RotaRegistrarReuniao rotaRegistrarReuniao = new RotaRegistrarReuniao(reuniao.getId());
                Future<ConexaoAPI> future = threadpool.submit(rotaRegistrarReuniao);

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
                        Log.d("testeX","status registro = " + status);
                        if(status == 200){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Salvo com sucesso
                                    Toast.makeText(ProximaReuniaoCoordenadorActivity.this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Erro ao salvar
                                    Toast.makeText(ProximaReuniaoCoordenadorActivity.this, "Dados não puderam ser salvos", Toast.LENGTH_LONG).show();
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
                    salvando.dismiss();
                    Intent intent = new Intent(ProximaReuniaoCoordenadorActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        thread.start();
    }
}