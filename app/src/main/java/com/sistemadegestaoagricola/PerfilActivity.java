package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaAtualizarInformacaoProdutor;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Produtor;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PerfilActivity extends AppCompatActivity {

    private LinearLayout llVoltar;
    private CardView cvFoto;
    private ImageView ivFoto;
    private TextView tvNome;
    private TextView tvCpf;
    private EditText edtDataNascimento;
    private EditText edtRg;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtConjuge;
    private EditText edtNomesDosFilhos;
    private Button btAtualizar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    private String pathFoto;
    CarregarDialog carregarDialog = new CarregarDialog(PerfilActivity.this);
    AlertDialog atualizando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        llVoltar = findViewById(R.id.llVoltarPerfil);
        cvFoto = findViewById(R.id.cvFotoPerfil);
        ivFoto = findViewById(R.id.ivFotoHome);
        tvNome = findViewById(R.id.tvNomePerfil);
        tvCpf = findViewById(R.id.tvCpfPerfil);
        edtDataNascimento = findViewById(R.id.edtDataNascimentoPerfil);
        edtDataNascimento = findViewById(R.id.edtDataNascimentoPerfil);
        edtTelefone = findViewById(R.id.edtTelefonePerfil);
        edtEmail = findViewById(R.id.edtEmailPerfil);
        edtConjuge = findViewById(R.id.edtConjugePerfil);
        edtNomesDosFilhos = findViewById(R.id.edtNomeFilhosPerfil);
        edtRg = findViewById(R.id.edtRgPerfil);
        btAtualizar = findViewById(R.id.btAtualizarPerfil);

        preencherCampos();

        Log.d("testeX","foto "+Usuario.getFoto());
        //Inserindo a imagem do usuario
        if(Usuario.getFoto() != null){
            ivFoto.setImageBitmap(Usuario.getFoto());
        }

        //Pegando o path do imagem
        //Recuperando string do bitmap
        String strBitmap = MediaStore.Images.Media.insertImage(this.getContentResolver(),Usuario.getFoto(),"Foto","");
        //Trasformando string em uri
        Uri uri = Uri.parse(strBitmap);
        pathFoto = getImagePath(uri);
        Log.d("testeX","foto path"+pathFoto);

        cvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Selecione sua foto"), 0);
            }
        });

        edtDataNascimento.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exibirDatePicker();
                return false;
            }
        });

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizando = carregarDialog.criarAtualizando();
                atualizando.show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        atualizar();
                    }
                });
                thread.start();

            }
        });
    }

    private void preencherCampos(){
        tvNome.setText(Usuario.getNome());
        tvCpf.setText(Util.cpfFormatado(Usuario.getCpfCnpj()));
        edtDataNascimento.setText(Produtor.getDataNascimento());
        edtRg.setText(Produtor.getRg());
        edtTelefone.setText(Usuario.getTelefone());
        edtEmail.setText(Usuario.getEmail());
        edtConjuge.setText(Produtor.getNomeConjuge());
        edtNomesDosFilhos.setText(Produtor.getNomeFilhos());
    }

    private void atualizar(){
        Produtor.setDataNascimento(edtDataNascimento.getText().toString());
        Produtor.setRg(edtRg.getText().toString());
        Usuario.setTelefone(edtTelefone.getText().toString());
        Usuario.setEmail(edtEmail.getText().toString());
        Produtor.setNomeConjuge(edtConjuge.getText().toString());
        Produtor.setNomeFilhos(edtNomesDosFilhos.getText().toString());
        atualizarInformacoes();
    }

    private void exibirDatePicker(){
        if(datePickerDialog != null && datePickerDialog.isShowing()){
            datePickerDialog.dismiss();
        }
        calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(PerfilActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                String diaString = Util.formatarDiaHoraCalendar(dia);
                String mesString = Util.formatarMesCalendar(mes);
                edtDataNascimento.setText(diaString+"/"+mesString+"/"+ano);
            }
        },ano,mes,dia);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = null;

        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if(data.getData() != null){
                //Apenas um item selecionado
                //ivFoto.setImageURI(data.getData());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    ivFoto.setImageBitmap(bitmap);
                    Usuario.setFoto(bitmap);
                    pathFoto = getImagePath(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

    private void atualizarInformacoes(){
        RotaAtualizarInformacaoProdutor rotaAtualizarInformacaoProdutor = new RotaAtualizarInformacaoProdutor(pathFoto);
        Future<ConexaoAPI> future = threadpool.submit(rotaAtualizarInformacaoProdutor);

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
                Log.d("testeX","statusSalvar =" + status);
                if(status == 200){
                    //Salvo com sucesso
                    //Toast.makeText(this, "Busca de reuniões realizada", Toast.LENGTH_SHORT).show();
                } else {
                    //Erro ao salvar
                    Toast.makeText(this, "Erro na busca de reuniões", Toast.LENGTH_SHORT).show();
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
            atualizando.dismiss();
            finish();
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }
}