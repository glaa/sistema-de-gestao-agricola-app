package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;

public class ReuniaoRealizadaCoordenadorActivity extends AppCompatActivity {

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
    private TextView tvHora;
    private TextView tvMinuto;
    private TextView tvTurno;
    private TextView tvLocal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniao_realizada_coordenador);

        llVoltar = findViewById(R.id.llVoltarReuniaoRealizadaCoordenador);
        tvTema = findViewById(R.id.tvTemaReuniaoRealizadaCoordenador);
        tvDiaSemana = findViewById(R.id.tvDiaSemanaReuniaoRealizadaCoordenador);
        tvDiaMes = findViewById(R.id.tvDiaMesReuniaoRealizadaCoordenador);
        tvMes = findViewById(R.id.tvMesReuniaoRealizadaCoordenador);
        tvAno = findViewById(R.id.tvAnoReuniaoRealizadaCoordenador);
        tvHora = findViewById(R.id.tvHoraReuniaoRealizadaCoordenador);
        tvMinuto = findViewById(R.id.tvMinutoReuniaoRealizadaCoordenador);
        tvTurno = findViewById(R.id.tvTurnoReuniaoRealizadaCoordenador);
        tvLocal = findViewById(R.id.tvLocalReuniaoRealizadaCoordenador);

        tabLayout = findViewById(R.id.tlOpcoesReuniaoRealizadaCoordenador);
        viewPager = findViewById(R.id.vpOpcoesReuniaoRealizadaCoordenador);
        tiAta = findViewById(R.id.tiAtaReuniaoRealizadaCoordenador);
        tiReuniao = findViewById(R.id.tiReuniaoRealizadaCoordenador);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        //tabLayout.addTab(tabLayout.newTab().setText("Ata da reunião"));
       // tabLayout.addTab(tabLayout.newTab().setText("Reunião"));


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
                    Log.d("testeX", "fragment ATA");
                    pageAdapter.notifyDataSetChanged();
                } else if(tab.getPosition() == 1){
                    Log.d("testeX", "fragment Reuniao");
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
        tvLocal.setText(reuniao.getLocal());
    }
}