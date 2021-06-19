package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;

public class ReuniaoCoordenadorActivity extends AppCompatActivity {

    private RecyclerView rvProximaReuniao, rvPassadaReuniao;
    private ImageView ivVoltar;
    private ArrayList<AgendamentoReuniao> agendametos = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> proximas = new ArrayList<>();
    private ArrayList<AgendamentoReuniao> passadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_coordenador);

//        Toolbar toolbar = findViewById(R.id.toolbarReuniao);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Voltar");

        ivVoltar = findViewById(R.id.ivVoltarReuniaoCoordenador);
        rvProximaReuniao = findViewById(R.id.rvProximaReuniaoCoordenador);
        rvPassadaReuniao = findViewById(R.id.rvPassadaReuniaoCoordenador);
        Log.d("testeX", "reunioies passada = " + passadas.size() + " total = " + Util.getAgendamentos().size());
        classificarAgendamentos();

        ReuniaoAdapter reuniaoAdapter = new ReuniaoAdapter(proximas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProximaReuniao.setLayoutManager(layoutManager);
        rvProximaReuniao.setAdapter(reuniaoAdapter);


        ReuniaoAdapter reuniaoAdapter2 = new ReuniaoAdapter(passadas);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        rvPassadaReuniao.setLayoutManager(layoutManager2);
        rvPassadaReuniao.setAdapter(reuniaoAdapter2);

        ivVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        finish();
//        return super.onSupportNavigateUp();
//    }

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
}