package com.sistemadegestaoagricola.produtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.PageAdapter;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;

public class ReuniaoRealizadaProdutorActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tiAta, tiReuniao;
    private PageAdapter pageAdapter;
    private LinearLayout llVoltar;
    private TextView tvTema;
    private TextView tvDiaSemana;
    private TextView tvDiaMes;
    private TextView tvMes;
    private TextView tvAno;
    private TextView tvHorario;
    private TextView tvLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_realizada_produtor);

        llVoltar = findViewById(R.id.llVoltarReuniaoRealizadaProdutor);
        tvTema = findViewById(R.id.tvTemaReuniaoRealizadaProdutor);
        tvDiaSemana = findViewById(R.id.tvDiaSemanaReuniaoRealizadaProdutor);
        tvDiaMes = findViewById(R.id.tvDiaMesReuniaoRealizadaProdutor);
        tvMes = findViewById(R.id.tvMesReuniaoRealizadaProdutor);
        tvAno = findViewById(R.id.tvAnoReuniaoRealizadaProdutor);
        tvHorario = findViewById(R.id.tvHorarioReuniaoRealizadaProdutor);
        tvLocal = findViewById(R.id.tvLocalReuniaoRealizadaProdutor);

        tabLayout = findViewById(R.id.tlOpcoesReuniaoRealizadaProdutor);
        viewPager = findViewById(R.id.vpOpcoesReuniaoRealizadaProdutor);
        tiAta = findViewById(R.id.tiAtaReuniaoRealizadaProdutor);
        tiReuniao = findViewById(R.id.tiReuniaoRealizadaProdutor);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        AgendamentoReuniao reuniao = null;
        if(getIntent().hasExtra("REUNIAO")){
            Bundle bundle = getIntent().getExtras();
            reuniao = (AgendamentoReuniao) bundle.get("REUNIAO");
            preencherCampo(reuniao);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                } else if(tab.getPosition() == 1){
                    pageAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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
        tvHorario.setText(Util.formatarDiaHoraCalendar(calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + Util.formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE)));
        tvLocal.setText(reuniao.getLocal());
    }
}