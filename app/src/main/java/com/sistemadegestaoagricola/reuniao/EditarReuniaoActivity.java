package com.sistemadegestaoagricola.reuniao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sistemadegestaoagricola.ErroActivity;
import com.sistemadegestaoagricola.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaEditarReuniao;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EditarReuniaoActivity extends AppCompatActivity implements Runnable{

    private LinearLayout llVoltar;
    private EditText etTema;
    private EditText etData;
    private EditText etHora;
    private EditText etLocal;
    private Button btSalvar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private String tema;
    private String local;
    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;
    private Thread thread;
    private ConexaoAPI conexao;
    private int status;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private AlertDialog salvamento;
    private AgendamentoReuniao reuniao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_reuniao);

        llVoltar = findViewById(R.id.llVoltarEditarReuniao);
        etTema = findViewById(R.id.etTemaEditarReuniao);
        etData = findViewById(R.id.etDataEditarReuniao);
        etHora = findViewById(R.id.etHoraEditarReuniao);
        etLocal = findViewById(R.id.etLocalEditarReuniao);
        btSalvar = findViewById(R.id.btSalvarEditarReuniao);

        CarregarDialog carregarDialog = new CarregarDialog(this);

        if(getIntent().hasExtra("REUNIAO")){
            Bundle bundle = getIntent().getExtras();
            reuniao = (AgendamentoReuniao) bundle.get("REUNIAO");
            preencherCampo(reuniao);
        }

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

        etData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exirDatePicker();
                return false;
            }
        });

        etHora.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exibirTimePicker();
                return false;
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificarCampos()){
                    salvamento = carregarDialog.criarDialogSalvarInformacoes();
                    salvamento.show();
                    thread = new Thread( EditarReuniaoActivity.this);
                    thread.start();
                }
            }
        });

    }

    private void voltar(){
        onBackPressed();
        finish();
    }

    private void exirDatePicker(){
        if(datePickerDialog != null && datePickerDialog.isShowing()){
            datePickerDialog.dismiss();
        }
        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(EditarReuniaoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                String diaString = Util.formatarDiaHoraCalendar(dia);
                String mesString = Util.formatarMesCalendar(mes);
                etData.setText(diaString+"/"+mesString+"/"+ano);
            }
        },ano,mes,dia);
        datePickerDialog.show();
    }

    private void exibirTimePicker(){
        if(timePickerDialog != null && timePickerDialog.isShowing()){
            timePickerDialog.dismiss();
        }
        Calendar calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener  timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                String horaString = Util.formatarDiaHoraCalendar(hora);
                String minutoString = Util.formatarDiaHoraCalendar(minuto);
                etHora.setText(horaString+ ":" +minutoString);
            }
        };
        timePickerDialog = new TimePickerDialog(EditarReuniaoActivity.this, timeSetListener,hora, minuto, true);
        timePickerDialog.show();
    }

    /*Retorna false no caso de haver algum campo vazio e true caso contrário*/
    private boolean verificarCampos(){
        tema = etTema.getText().toString();
        String data = etData.getText().toString();
        String hora = etHora.getText().toString();
        local = etLocal.getText().toString();

        if(tema.isEmpty() || data.isEmpty() || hora.isEmpty() || local.isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void preencherCampo(AgendamentoReuniao reuniao){
        etTema.setText(reuniao.getNome());
        etData.setText(Util.converterDateString(reuniao.getData()));
        etHora.setText(Util.extrairHoraDeData(reuniao.getData()));
        etLocal.setText(reuniao.getLocal());
    }

    @Override
    public void run() {
        String data = etData.getText().toString() + " " + etHora.getText().toString() + ":00";
        //Date date = Util.converterStringDate(ano+"-"+mes+"-"+dia+" "+hora+":"+minuto+":"+00);
        Log.d("testeX",""+data);
        /**
         *  Chama a classe RotaAgendarReuniao que irá se conectar com a rota /api/agendar-reuniao em uma thread
         */
        RotaEditarReuniao agendarReuniao = new RotaEditarReuniao(tema,data,local,reuniao.getId());
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
                            Toast.makeText(getApplicationContext(),"Salvo com sucesso", Toast.LENGTH_LONG).show();
                            Util.esvaziarAgendamentos();
                            new HomeActivity().buscarReunioes();
                            voltar();
                            finish();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Erro ao salvar", Toast.LENGTH_LONG).show();
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
                    salvamento.dismiss();
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
}