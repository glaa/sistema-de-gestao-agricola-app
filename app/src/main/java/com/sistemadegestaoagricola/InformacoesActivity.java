package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sistemadegestaoagricola.entidades.Propriedade;

import java.util.ArrayList;

public class InformacoesActivity extends AppCompatActivity {

    private LinearLayout llVoltar;
    private EditText edtTamanho;
    private Button btAtualizar;
    private Button btPoco;
    private Button btCisterna;
    private Button btCompesa;
    private Button btOutro;
    private boolean pocoMarcado = false;
    private boolean cisternaMarcado = false;
    private boolean compesaMarcado = false;
    private boolean outroMarcado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        llVoltar = findViewById(R.id.llVoltarInformacoes);
        edtTamanho = findViewById(R.id.edtTamanhoInformacoes);
        btAtualizar = findViewById(R.id.btAtualizarInformacoes);
        btPoco = findViewById(R.id.btPocoInformacoes);
        btCisterna = findViewById(R.id.btCisternaInformacoes);
        btCompesa = findViewById(R.id.btCompesaInformacoes);
        btOutro = findViewById(R.id.btOutroInformacoes);

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { voltar(); }
        });

        preencherInformacoes();

        btPoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pocoMarcado){
                    marcar(btPoco);
                    pocoMarcado = true;
                } else {
                    desmarcar(btPoco);
                    pocoMarcado = false;
                }
            }
        });

        btCisterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cisternaMarcado){
                    marcar(btCisterna);
                    cisternaMarcado = true;
                } else {
                    desmarcar(btCisterna);
                    cisternaMarcado = false;
                }
            }
        });

        btCompesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!compesaMarcado){
                    marcar(btCompesa);
                    compesaMarcado = true;
                } else {
                    desmarcar(btCompesa);
                    compesaMarcado = false;
                }
            }
        });

        btOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!outroMarcado){
                    marcar(btOutro);
                    outroMarcado = true;
                } else {
                    desmarcar(btOutro);
                    outroMarcado = false;
                }
            }
        });

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void voltar(){
        onBackPressed();
        finish();
    }

    private void preencherInformacoes(){
        //Preencher Tamanho
        edtTamanho.setText(String.valueOf(Propriedade.getTamanho()));
        //Preencher Fonte
        String[] fontes = Propriedade.getFonteAgua().split(",");
        for(int i=0; i < fontes.length; i++){
            if(fontes[i].equals("Poço")){
                marcar(btPoco);
                pocoMarcado = true;
            } else if(fontes[i].equals("Cisterna")){
                marcar(btCisterna);
                cisternaMarcado = true;
            } else if(fontes[i].equals("Compesa")){
                marcar(btCompesa);
                compesaMarcado = true;
            } else if(fontes[i].equals("Outro")){
                marcar(btOutro);
                outroMarcado = true;
            }
        }
    }

    private void marcar(Button botao){
        botao.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
        botao.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.verde_escuro));
        botao.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_check_24,0);
    }

    private void desmarcar(Button botao){
        botao.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.verde_escuro));
        botao.setTextColor(getApplicationContext().getResources().getColorStateList(R.color.white));
        botao.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }
}