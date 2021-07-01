package com.sistemadegestaoagricola.produtor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;

public class ProximaReuniaoProdutorActivity extends AppCompatActivity {

    private TextView tvTema;
    private TextView tvDiaSemana;
    private TextView tvDiaMes;
    private TextView tvMes;
    private TextView tvAno;
    private TextView tvHorario;
    private TextView tvLocal;
    private LinearLayout llVoltar;
    AgendamentoReuniao reuniao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxima_reuniao_produtor);

        tvTema = findViewById(R.id.tvTemaProximaReuniaoProdutor);
        tvDiaSemana = findViewById(R.id.tvDiaSemanaProximaReuniaoProdutor);
        tvDiaMes = findViewById(R.id.tvDiaMesProximaReuniaoProdutor);
        tvMes = findViewById(R.id.tvMesProximaReuniaoProdutor);
        tvAno = findViewById(R.id.tvAnoProximaReuniaoProdutor);
        tvHorario = findViewById(R.id.tvHorarioProximaReuniaoProdutor);
        tvLocal = findViewById(R.id.tvLocalProximaReuniaoProdutor);
        llVoltar = findViewById(R.id.llVoltarProximaReuniaoProdutor);

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
        tvHorario.setText(Util.formatarDiaHoraCalendar(calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + Util.formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE)));
        tvAno.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvLocal.setText(reuniao.getLocal());
    }
}