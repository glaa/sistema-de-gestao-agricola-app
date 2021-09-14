package com.sistemadegestaoagricola.principal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;
import com.sistemadegestaoagricola.conexao.RotaListarReunioes;
import com.sistemadegestaoagricola.mapa.MapasActivity;
import com.sistemadegestaoagricola.propriedade.PropriedadeActivity;
import com.sistemadegestaoagricola.reuniao.ReuniaoActivity;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class HomeActivity extends AppCompatActivity {

    private ConexaoAPIVelha conexao;
    private int status;
    private String nome;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardView cvFoto;
    private ImageView ivFoto;
    private TextView tvSaudacao;
    private CardView cvProximaReuniao;
    private CardView btPropriedade;
    private CardView btMinhasInformacoes;
    private CardView btReuniao;
    private CardView btMapas;
    private CardView btSair;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    //Para o card reunião
    private TextView tvTema;
    private TextView tvDiaDaSemana;
    private TextView tvDiaDoMes;
    private TextView tvMesDoAno;
    private TextView tvHorario;
    private TextView tvLocal;
    private String tema = null;
    private String diaDaSemana = null;
    private String diaDoMes = null;
    private String mesDoAno = null;
    private String horario = null;
    private String local = null;
    CarregarDialog carregarDialog = new CarregarDialog(this);
    AlertDialog carregando;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout = findViewById(R.id.swipeHome);
        cvFoto = findViewById(R.id.cvFotoHome);
        ivFoto = findViewById(R.id.ivFotoHome);
        tvSaudacao = findViewById(R.id.tvNomePerfil);
        tvSaudacao.setText(Usuario.getNome() + "!");
        cvProximaReuniao = findViewById(R.id.cvProximaReuniaoHome);
        btPropriedade = findViewById(R.id.cvPropriedadeHome);
        btReuniao = findViewById(R.id.cvReunioesHome);
        btMapas = findViewById(R.id.cvMapasHome);
        btSair = findViewById(R.id.cvSairHome);

        carregando = carregarDialog.criarDialogCarregamento();

        Log.d("testeX","foto "+Usuario.getFoto());
        if(Usuario.getFoto() != null){
            ivFoto.setImageBitmap(Usuario.getFoto());
        }

        carregando.show();
        atualizarReuniao();
        carregando.dismiss();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefreshLayout.setRefreshing(true);
                atualizarReuniao();

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("testeX","iniciou task");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);


            }
        });

        cvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btPropriedade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PropriedadeActivity.class);
                startActivity(intent);
            }
        });

//        btMinhasInformacoes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),MinhasInformacoesActivity.class);
//                startActivity(intent);
//            }
//        });

        btReuniao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando.show();
                Intent intent = new Intent(HomeActivity.this, ReuniaoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btMapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando.show();
                Intent intent = new Intent(HomeActivity.this, MapasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

    }

    @Override
    protected void onRestart() {
        if(Usuario.getFoto() != null){
            ivFoto.setImageBitmap(Usuario.getFoto());
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        carregando.dismiss();
        super.onDestroy();
    }

    private void atualizarReuniao(){
        Util.esvaziarAgendamentos();
        buscarReunioes();
        if(exibirProximaReuniao()){
            tvTema = findViewById(R.id.tvTemaHome);
            tvDiaDaSemana = findViewById(R.id.tvDiaSemanaHome);
            tvDiaDoMes = findViewById(R.id.tvDiaMesHome);
            tvMesDoAno = findViewById(R.id.tvMesHome);
            tvHorario = findViewById(R.id.tvHorarioHome);
            tvLocal = findViewById(R.id.tvLocalHome);
            Log.d("testeX", "exibiuReunião");

            tvTema.setText(tema);
            tvDiaDaSemana.setText(diaDaSemana);
            tvDiaDoMes.setText(diaDoMes);
            tvMesDoAno.setText(mesDoAno);
            if(horario != null){
                tvHorario.setText(horario);
            } else {
                tvHorario.setVisibility(View.GONE);
            }
            tvLocal.setText(local);
        } else {
            cvProximaReuniao.setVisibility(View.GONE);
        }
    }

    public void buscarReunioes(){
        RotaListarReunioes rotaListarReunioes = new RotaListarReunioes();
        Future<ConexaoAPI> future = threadpool.submit(rotaListarReunioes);

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
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }

    private boolean exibirProximaReuniao(){
        boolean reuniaoAgendada = false;
        ArrayList<AgendamentoReuniao> agendamentos = new ArrayList<AgendamentoReuniao>();
        //Seleciona reuniões não registradas e com data futura
        for(AgendamentoReuniao agendamentoReuniao : Util.getAgendamentos()){
            if(!agendamentoReuniao.isRegistrada()){
                Date dataAtual = new Date();
                dataAtual.setHours(00);
                dataAtual.setMinutes(01);
                if(dataAtual.before(agendamentoReuniao.getData())){
                    agendamentos.add(agendamentoReuniao);
                }
            }
        }
        //Seleciona a reunião futura mais próxima
        if(!agendamentos.isEmpty()){
            Date data0 = agendamentos.get(0).getData();
            tema = agendamentos.get(0).getNome();
            local = agendamentos.get(0).getLocal();
            Calendar calendarMin = Calendar.getInstance();
            calendarMin.setTime(data0);

            for(AgendamentoReuniao agendamento : agendamentos){
                Date data1 = agendamento.getData();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(data1);
                if(calendar.compareTo(calendarMin) < 0){
                    calendarMin = calendar;
                    local = agendamento.getLocal();
                }
            }
            diaDaSemana = Util.calendarioIntParaStringDia(calendarMin.get(Calendar.DAY_OF_WEEK));
            diaDoMes = Util.formatarDiaHoraCalendar(calendarMin.get(Calendar.DAY_OF_MONTH));
            mesDoAno =  Util.calendarioIntParaStringMes(calendarMin.get(Calendar.MONTH));

            horario = Util.formatarDiaHoraCalendar(calendarMin.get(Calendar.HOUR_OF_DAY)) + "h" +
                    Util.formatarDiaHoraCalendar(calendarMin.get(Calendar.MINUTE));
            reuniaoAgendada = true;
        }
        return reuniaoAgendada;
    }
}