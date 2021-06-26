package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;

public class ProximaReuniaoCoordenadorActivity extends AppCompatActivity {

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

    }

    private void preencherCampo(AgendamentoReuniao reuniao){
        tvTema.setText(reuniao.getNome());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reuniao.getData());
        tvDiaSemana.setText(Util.calendarioIntParaStringDia(calendar.get(Calendar.DAY_OF_WEEK)).
                substring(0,3));
        tvDiaMes.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        tvMes.setText(Util.calendarioIntParaStringMes(calendar.get(Calendar.MONTH)));
        tvAno.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvLocal.setText(reuniao.getLocal());
    }
}