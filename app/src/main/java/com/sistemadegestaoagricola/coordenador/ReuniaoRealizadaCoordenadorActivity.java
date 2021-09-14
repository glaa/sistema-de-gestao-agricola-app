package com.sistemadegestaoagricola.coordenador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.PageAdapter;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetExibirReuniao;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.entidades.Reuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private TextView tvHorario;
    private TextView tvLocal;
    private GridView gridView;
    AgendamentoReuniao reuniao = null;
    private Thread thread;
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    private String[] mensagensExceptions;
    private int status;
    CarregarDialog carregarDialog = new CarregarDialog(ReuniaoRealizadaCoordenadorActivity.this);
    AlertDialog carregando;


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
        tvHorario = findViewById(R.id.tvHorarioReuniaoRealizadaCoordenador);
        tvLocal = findViewById(R.id.tvLocalReuniaoRealizadaCoordenador);

        tabLayout = findViewById(R.id.tlOpcoesReuniaoRealizadaCoordenador);
        viewPager = findViewById(R.id.vpOpcoesReuniaoRealizadaCoordenador);
        tiAta = findViewById(R.id.tiAtaReuniaoRealizadaCoordenador);
        tiReuniao = findViewById(R.id.tiReuniaoRealizadaCoordenador);
        //gridView = findViewById(R.id.gvAtaReuniaoFragment);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        Log.d("testeX","Abriu Activity");
        if(getIntent().hasExtra("REUNIAO")){
            Bundle bundle = getIntent().getExtras();
            reuniao = (AgendamentoReuniao) bundle.get("REUNIAO");
            preencherCampo(reuniao);
        }
        carregando = carregarDialog.criarDialogCarregamento();
        buscarReuniao();
        converterStringParaBitmap();

        //tabLayout.addTab(tabLayout.newTab().setText("Ata da reunião"));
       // tabLayout.addTab(tabLayout.newTab().setText("Reunião"));

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
                //carregando.show();
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

    private void buscarReuniao(){
        if(reuniao != null){
            RotaGetExibirReuniao rotaGetExibirReuniao = new RotaGetExibirReuniao(reuniao.getId());
            Future<ConexaoAPI> future = threadpool.submit(rotaGetExibirReuniao);

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
                    if(status != 200){
                        //Erro ao salvar
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ReuniaoRealizadaCoordenadorActivity.this, "Erro na busca de reuniões", Toast.LENGTH_SHORT).show();
                            }
                        });

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
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(this, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        this.startActivity(intent);
    }

    private void converterStringParaBitmap(){
        if(!Reuniao.getAtasBase64().isEmpty()){
            ArrayList<Bitmap> atas = new ArrayList<>();
            for (String strAta: Reuniao.getAtasBase64()) {
                atas.add(Util.converterStringParaBitmap(strAta));
            }
            Reuniao.setAtasBitmap(atas);
        }
        if(!Reuniao.getFotosBase64().isEmpty()){
            ArrayList<Bitmap> fotos = new ArrayList<>();
            for (String strFotos: Reuniao.getFotosBase64()) {
                fotos.add(Util.converterStringParaBitmap(strFotos));
            }
            Reuniao.setFotosBitmap(fotos);
        }
    }
}