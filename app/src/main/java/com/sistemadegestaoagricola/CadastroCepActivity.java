package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sistemadegestaoagricola.conexao.CepAPI;
import com.sistemadegestaoagricola.entidades.Propriedade;

public class CadastroCepActivity extends AppCompatActivity implements Runnable{

    private EditText edtCep;
    private Button btPular;
    private Button btBuscar;
    private String cep = "";
    private boolean bloquearBotao = true;
    private AlertDialog buscando;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cep);

        edtCep = findViewById(R.id.edtCepCadastroCep);
        btPular = findViewById(R.id.btPularCadastroCep);
        btBuscar = findViewById(R.id.btBuscarCadastroCep);

        CarregarDialog carregarDialog = new CarregarDialog(CadastroCepActivity.this);
        buscando = carregarDialog.criarDialogBuscarCep();

        edtCep.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String strCep = edtCep.getText().toString();

                if(i == 66){
                    if(!strCep.isEmpty()){
                        if(strCep.length() == 8){
                            bloquearBotao = false;
                            btBuscar.setClickable(true);
                            btBuscar.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));

                        } else {
                            bloquearBotao = true;
                            btBuscar.setClickable(false);
                            btBuscar.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.cinza_escuro));
                            Toast.makeText(CadastroCepActivity.this, "O CEP deve possui 8 números", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        bloquearBotao = true;
                        btBuscar.setClickable(false);
                        btBuscar.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.cinza_escuro));
                        Toast.makeText(CadastroCepActivity.this, "Digite o CEP", Toast.LENGTH_LONG).show();
                    }
                }

                return false;
            }
        });

        btPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), CadastroLocalizacaoActivity.class);
                startActivity(intent);
            }
        });

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bloquearBotao){
                    cep = edtCep.getText().toString();
                    if(cep.length() == 8){
                        buscando.show();
                        thread = new Thread(CadastroCepActivity.this);
                        thread.start();
                    }
                }
            }
        });
    }

    private void imprimirPropriedade(){
        System.out.println(Propriedade.getCep());
        System.out.println(Propriedade.getLogradouro());
        System.out.println(Propriedade.getBairro());
        System.out.println(Propriedade.getCidade());
        System.out.println(Propriedade.getEstado());
    }

    @Override
    public void run() {
        boolean sucesso = new CepAPI(cep).buscar();
        if(sucesso){
            CarregarDialog dialog = new CarregarDialog(CadastroCepActivity.this);
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   AlertDialog alertDialog = dialog.criarDialogContinuarCadastroLocalizacao();
                   alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Intent intent  = new Intent(getApplicationContext(), CadastroLocalizacaoActivity.class);
                           startActivity(intent);
                       }
                   });
                   alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                           startActivity(intent);
                       }
                   });
                   alertDialog.show();
                   imprimirPropriedade();
               }
           });
        } else {
            Intent intent  = new Intent(getApplicationContext(), CadastroLocalizacaoActivity.class);
            startActivity(intent);
        }
        buscando.dismiss();
    }
}