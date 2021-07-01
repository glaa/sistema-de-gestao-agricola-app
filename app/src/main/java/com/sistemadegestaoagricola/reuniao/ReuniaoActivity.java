package com.sistemadegestaoagricola.reuniao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemadegestaoagricola.HomeActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.ReuniaoProximaAdapter;
import com.sistemadegestaoagricola.adapter.ReuniaoPassadaAdapter;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ReuniaoActivity extends AppCompatActivity {

    private RecyclerView rvProximaReuniao, rvPassadaReuniao;
    private LinearLayout llVoltar;
    private TextView tvProximaReuniao, tvReuniaoPassada;
    private ArrayList<AgendamentoReuniao> agendametos = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> proximas = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> passadas = new ArrayList<>();
    private Button btAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao);

//        Toolbar toolbar = findViewById(R.id.toolbarReuniao);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Voltar");
        btAdicionar = findViewById(R.id.btAdicionarReuniaoCoodernador);
        tvProximaReuniao = findViewById(R.id.tvProximaReuniaoCoordenador);
        tvReuniaoPassada = findViewById(R.id.tvReuniaoPassadaCoordenador);

        llVoltar = findViewById(R.id.llVoltarReuniaoCoordenador);
        rvProximaReuniao = findViewById(R.id.rvProximaReuniaoCoordenador);
        rvPassadaReuniao = findViewById(R.id.rvPassadaReuniaoCoordenador);
        Log.d("testeX", "reunioies passada = " + passadas.size() + " total = " + Util.getAgendamentos().size());
        classificarAgendamentos();

        ReuniaoProximaAdapter reuniaoProximaAdapter = new ReuniaoProximaAdapter(this,proximas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProximaReuniao.setLayoutManager(layoutManager);
        rvProximaReuniao.setAdapter(reuniaoProximaAdapter);


        ReuniaoPassadaAdapter reuniaoPassadaAdapter = new ReuniaoPassadaAdapter(ReuniaoActivity.this,passadas);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        rvPassadaReuniao.setLayoutManager(layoutManager2);
        rvPassadaReuniao.setAdapter(reuniaoPassadaAdapter);

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
                Intent intent = new Intent(ReuniaoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NovaReuniaoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void classificarAgendamentos(){
        agendametos.clear();

        for(AgendamentoReuniao agenda : Util.getAgendamentos()){
            agendametos.add(agenda);
        }

        for(AgendamentoReuniao agenda : agendametos){
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

        ordenarArray(passadas);
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
}