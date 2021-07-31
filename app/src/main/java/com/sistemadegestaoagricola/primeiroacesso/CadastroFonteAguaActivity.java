package com.sistemadegestaoagricola.primeiroacesso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.Propriedade;

import java.util.ArrayList;

public class CadastroFonteAguaActivity extends AppCompatActivity {

    private Button btPoco;
    private Button btCisterna;
    private Button btCompesa;
    private Button btOutro;
    private Button btProximo;
    private boolean pocoMarcado = false;
    private boolean cisternaMarcado = false;
    private boolean compesaMarcado = false;
    private boolean outroMarcado = false;
    private ArrayList<String> fonte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fonte_agua);

        fonte = new ArrayList<String>();

        btPoco = findViewById(R.id.btPocoCadastroFonteAgua);
        btCisterna = findViewById(R.id.btCisternaCadastroFonteAgua);
        btCompesa = findViewById(R.id.btCompesaCadastroFonteAgua);
        btOutro = findViewById(R.id.btOutroCadastroFonteAgua);
        btProximo = findViewById(R.id.btProximoCadastroFonteAgua);

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

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pocoMarcado){
                    fonte.add("Poço");
                }
                if(cisternaMarcado){
                    fonte.add("Cisterna");
                }
                if(compesaMarcado){
                    fonte.add("Compesa");
                }
                if(outroMarcado){
                    fonte.add("Outro");
                }
                if(fonte.isEmpty()){
                    Toast.makeText(CadastroFonteAguaActivity.this, "Selecione algum item", Toast.LENGTH_SHORT).show();
                } else {
                    salvarDados();
                    Intent intent = new Intent(CadastroFonteAguaActivity.this,CadastroCepActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
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

    private void salvarDados(){
        String fonteAgua = "";
        for(String f: fonte){
            fonteAgua += f + ",";
        }
        fonteAgua = fonteAgua.substring(0,fonteAgua.length()-1);

        Propriedade.setFonteAgua(fonteAgua);
    }
}