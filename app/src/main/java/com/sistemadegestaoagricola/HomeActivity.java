package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.ConexaoAPIVelha;
import com.sistemadegestaoagricola.conexao.RotaCadastrarPropriedade;
import com.sistemadegestaoagricola.conexao.RotaListarReunioes;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class HomeActivity extends AppCompatActivity {

    private ConexaoAPIVelha conexao;
    private int status;
    private String nome;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvSaudacao;
    private CardView cvProximaReuniao;
    private CardView btMinhaPropriedade;
    private CardView btMinhasInformacoes;
    private CardView btReuniao;
    private CardView btSair;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    //Para o card reunião
    private TextView tvDiaDaSemana;
    private TextView tvDiaDoMes;
    private TextView tvMesDoAno;
    private TextView tvHorario;
    private String diaDaSemana = null;
    private String diaDoMes = null;
    private String mesDoAno = null;
    private String horario = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout = findViewById(R.id.swipeHome);
        tvSaudacao = findViewById(R.id.tvNomeHome);
        tvSaudacao.setText(Usuario.getNome() + "!");

        cvProximaReuniao = findViewById(R.id.cvProximaReuniaoHome);
        btMinhaPropriedade = findViewById(R.id.cvPropriedadeHome);
        //btMinhasInformacoes = findViewById(R.id.btMinhasInformacoesHome);
        btReuniao = findViewById(R.id.cvReunioesHome);
        btSair = findViewById(R.id.cvSairHome);

        CarregarDialog carregarDialog = new CarregarDialog(this);
        AlertDialog carregando = carregarDialog.criarDialogCarregamento();

        carregando.show();
        Util.esvaziarAgendamentos();
        buscarReunioes();
        if(exibirProximaReuniao()){
            tvDiaDaSemana = findViewById(R.id.tvDiaSemanaHome);
            tvDiaDoMes = findViewById(R.id.tvDiaMesHome);
            tvMesDoAno = findViewById(R.id.tvMesHome);
            tvHorario = findViewById(R.id.tvHorarioHome);
            Log.d("testeX", "exibiuReunião");

            tvDiaDaSemana.setText(diaDaSemana);
            tvDiaDoMes.setText(diaDoMes);
            tvMesDoAno.setText(mesDoAno);
            if(horario != null){
                tvHorario.setText(horario);
            } else {
                tvHorario.setVisibility(View.GONE);
            }
        } else {
            cvProximaReuniao.setVisibility(View.GONE);
        }
        carregando.dismiss();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                finish();
                startActivity(getIntent());
            }
        });

        btMinhaPropriedade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MinhaPropriedadeActivity.class);
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
                Intent intent = new Intent(getApplicationContext(),ReuniaoActivity.class);
                startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
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
                    Toast.makeText(this, "Busca de reuniões realizada", Toast.LENGTH_SHORT).show();
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

        for(AgendamentoReuniao agendamentoReuniao : Util.getAgendamentos()){
            if(!agendamentoReuniao.isRegistrada()){
                agendamentos.add(agendamentoReuniao);
            }
        }

        if(!agendamentos.isEmpty()){
            Date data0 = agendamentos.get(0).getData();
            Calendar calendarMin = Calendar.getInstance();
            calendarMin.setTime(data0);

            for(AgendamentoReuniao agendamento : agendamentos){
                Date data1 = agendamento.getData();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(data1);
                if(calendar.compareTo(calendarMin) < 0){
                    calendarMin = calendar;
                }
            }
            diaDaSemana = Util.calendarioIntParaStringDia(calendarMin.get(Calendar.DAY_OF_WEEK));
            diaDoMes = String.valueOf(calendarMin.get(Calendar.DAY_OF_MONTH));
            mesDoAno =  Util.calendarioIntParaStringMes(calendarMin.get(Calendar.MONTH));
            reuniaoAgendada = true;
        }
        return reuniaoAgendada;
    }
}