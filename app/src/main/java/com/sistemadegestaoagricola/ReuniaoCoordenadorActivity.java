package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;

public class ReuniaoCoordenadorActivity extends AppCompatActivity {

    private RecyclerView rvProximaReuniao, rvPassadaReuniao;
    private LinearLayout llVoltar;
    private ArrayList<AgendamentoReuniao> agendametos = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> proximas = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> passadas = new ArrayList<>();
    private Button btAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_coordenador);

//        Toolbar toolbar = findViewById(R.id.toolbarReuniao);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Voltar");
        btAdicionar = findViewById(R.id.btAdicionarReuniaoCoodernador);

        llVoltar = findViewById(R.id.llVoltarReuniaoCoordenador);
        rvProximaReuniao = findViewById(R.id.rvProximaReuniaoCoordenador);
        rvPassadaReuniao = findViewById(R.id.rvPassadaReuniaoCoordenador);
        Log.d("testeX", "reunioies passada = " + passadas.size() + " total = " + Util.getAgendamentos().size());
        classificarAgendamentos();

        ReuniaoAdapter reuniaoAdapter = new ReuniaoAdapter(proximas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProximaReuniao.setLayoutManager(layoutManager);
        rvProximaReuniao.setAdapter(reuniaoAdapter);


        ReuniaoPassadaAdapter reuniaoPassadaAdapter = new ReuniaoPassadaAdapter(ReuniaoCoordenadorActivity.this,passadas);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        rvPassadaReuniao.setLayoutManager(layoutManager2);
        rvPassadaReuniao.setAdapter(reuniaoPassadaAdapter);


        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NovaReuniaoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void classificarAgendamentos(){
        agendametos = Util.getAgendamentos();
        for(AgendamentoReuniao agenda : agendametos){
            if(agenda.isRegistrada()){
                passadas.add(agenda);
            } else {
                proximas.add(agenda);
            }
        }
    }

//    class ObterReuniaoPassada implements ReuniaoPassadaAdapter.ItemClicked{
//
//        @Override
//        public void onItemClicked(int index) {
//            Log.d("testeX", "reuniao passada = " + index);
//        }
//    }
}