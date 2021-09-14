package com.sistemadegestaoagricola.reuniao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemadegestaoagricola.principal.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.ReuniaoProximaAdapter;
import com.sistemadegestaoagricola.adapter.ReuniaoPassadaAdapter;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ReuniaoActivity extends AppCompatActivity {

    private RecyclerView rvProximaReuniao, rvPassadaReuniao;
    private LinearLayout llVoltar;
    private TextView tvProximaReuniao, tvReuniaoPassada;
    private ArrayList<AgendamentoReuniao> agendamentos = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> proximas = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> passadas = new ArrayList<>();
    private Button btAdicionar;
    CarregarDialog carregarDialog = new CarregarDialog(ReuniaoActivity.this);
    AlertDialog carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao);

        carregando = carregarDialog.criarDialogCarregamento();

        btAdicionar = findViewById(R.id.btAdicionarReuniaoCoodernador);
        tvProximaReuniao = findViewById(R.id.tvProximaReuniaoCoordenador);
        tvReuniaoPassada = findViewById(R.id.tvReuniaoPassadaCoordenador);

        llVoltar = findViewById(R.id.llVoltarReuniaoCoordenador);
        rvProximaReuniao = findViewById(R.id.rvProximaReuniaoCoordenador);
        rvPassadaReuniao = findViewById(R.id.rvPassadaReuniaoCoordenador);

        //Verifica o perfil do usuário, caso seja produtor, o botão adicionar será ocultado
        verificarPerfil();

        carregarReunioes();

        //Colocar rótulos no plural, caso necessário
        if(proximas.size() > 1){
            tvProximaReuniao.setText("Próximas reuniões");
        } else {
            tvProximaReuniao.setText("Próxima reunião");
        }

        if(passadas.size() > 1){
            tvReuniaoPassada.setText("Reuniões passadas");
        } else {
            tvReuniaoPassada.setText("Reunião passada");
        }

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregando = carregarDialog.criarDialogCarregamento();
                carregando.show();
                Intent intent = new Intent(ReuniaoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReuniaoActivity.this, NovaReuniaoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void carregarReunioes(){
        classificarAgendamentos();

        ReuniaoProximaAdapter reuniaoProximaAdapter = new ReuniaoProximaAdapter(this,proximas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvProximaReuniao.setLayoutManager(layoutManager);
        rvProximaReuniao.setAdapter(reuniaoProximaAdapter);

        ReuniaoPassadaAdapter reuniaoPassadaAdapter = new ReuniaoPassadaAdapter(this,passadas,carregando);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        rvPassadaReuniao.setLayoutManager(layoutManager2);
        rvPassadaReuniao.setAdapter(reuniaoPassadaAdapter);
    }

    private void classificarAgendamentos(){
        agendamentos.clear();
        passadas.clear();
        proximas.clear();

        for(AgendamentoReuniao agenda : Util.getAgendamentos()){
            agendamentos.add(agenda);
        }

        for(AgendamentoReuniao agenda : agendamentos){
            if(agenda.isRegistrada()){
                passadas.add(agenda);
            } else {
                if(reuniaoPendente(agenda)){
                    passadas.add(agenda);
                } else {
                    proximas.add(agenda);
                }
            }
        }

        //inverterOrdemArray(passadas);
        ordenarArray(passadas);
        inverterOrdemArray(passadas);
        ordenarArray(proximas);
    }

    private boolean reuniaoPendente(AgendamentoReuniao agenda){
        Date dataAtual = new Date();
        dataAtual.setHours(00);
        dataAtual.setMinutes(01);
        return dataAtual.after(agenda.getData());
    }

    private void ordenarArray(ArrayList agendametos){
        Collections.sort(agendametos);
    }

    private void inverterOrdemArray(ArrayList agendametos){
        Collections.reverse(agendametos);
    }

    private void verificarPerfil(){
        if(Usuario.getPerfil().equals("Produtor")){
            btAdicionar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        carregarReunioes();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        carregando.dismiss();
    }
}