package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaAgendarReuniao;
import com.sistemadegestaoagricola.conexao.RotaExcluirReuniao;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;
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
    private TextView tvHora;
    private TextView tvMinuto;
    private TextView tvTurno;
    private TextView tvLocal;
    private LinearLayout llEditar;
    private LinearLayout llExcluir;
    private LinearLayout llVoltar;
    AgendamentoReuniao reuniao;
    private AlertDialog excluindo;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private ConexaoAPI conexao;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxima_reuniao_coordenador);

        tvTema = findViewById(R.id.tvTemaProximaReuniaoCoordenador);
        tvDiaSemana = findViewById(R.id.tvDiaSemanaProximaReuniaoCoordenador);
        tvDiaMes = findViewById(R.id.tvDiaMesProximaReuniaoCoordenador);
        tvMes = findViewById(R.id.tvMesProximaReuniaoCoordenador);
        tvAno = findViewById(R.id.tvAnoProximaReuniaoCoordenador);
        tvHora = findViewById(R.id.tvHoraProximaReuniaoCoordenador);
        tvMinuto = findViewById(R.id.tvMinutoProximaReuniaoCoordenador);
        tvTurno = findViewById(R.id.tvTurnoProximaReuniaoCoordenador);
        tvLocal = findViewById(R.id.tvLocalProximaReuniaoCoordenador);
        llEditar = findViewById(R.id.llEditarProximaReuniaoCoordenador);
        llExcluir = findViewById(R.id.llExcluirProximaReuniaoCoordenador);
        llVoltar = findViewById(R.id.llVoltarProximaReuniaoCoordenador);

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
                Intent intent = new Intent(ProximaReuniaoCoordenadorActivity.this,EditarReuniaoActivity.class);
                intent.putExtra("REUNIAO",reuniao);
                startActivity(intent);
            }
        });

        llExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluindo = carregarDialog.criarDialogExcluirInformacoes();
                excluindo.show();
                thread = new Thread( ProximaReuniaoCoordenadorActivity.this);
                thread.start();
            }
        });

    }

    private void preencherCampo(AgendamentoReuniao reuniao){
        tvTema.setText(reuniao.getNome());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reuniao.getData());
        tvDiaSemana.setText(Util.calendarioIntParaStringDia(calendar.get(Calendar.DAY_OF_WEEK)).
                substring(0,3));
        tvDiaMes.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        tvMes.setText(Util.calendarioIntParaStringMes(calendar.get(Calendar.MONTH)));
        tvHora.setText(Util.formatarDiaHoraCalendar(calendar.get(Calendar.HOUR)));
        tvMinuto.setText(Util.formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE)));
        int am_pm = calendar.get(Calendar.AM_PM);
        if(am_pm == 0){
            tvTurno.setText("manhã");
        } else {
            if(calendar.get(Calendar.HOUR) < 6){
                tvTurno.setText("tarde");
            } else {
                tvTurno.setText("noite");
            }
        }
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
        Intent intent = new Intent(ProximaReuniaoCoordenadorActivity.this,ReuniaoCoordenadorActivity.class);
        startActivity(intent);
    }
}