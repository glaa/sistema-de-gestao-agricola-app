package com.sistemadegestaoagricola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.Propriedade;

public class CadastroCidadeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtCidade;
    private Spinner spinnerEstado;
    private Button btProximo;
    private String estado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cidade);

        edtCidade = findViewById(R.id.edtCidadeCadastroCidade);
        btProximo = findViewById(R.id.btProximoCadastroCidade);

        spinnerEstado = findViewById(R.id.spinnerCadastroCidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.estados, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setOnItemSelectedListener(this);

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade = edtCidade.getText().toString();
                if(!cidade.isEmpty() || !estado.isEmpty()){
                    CarregarDialog dialog = new CarregarDialog(CadastroCidadeActivity.this);
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
                            salvarDados(cidade,estado);
                            Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        estado = adapterView.getItemAtPosition(i).toString();
        Log.d("testeX",adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("testeX","nao selecionado");

    }

    private void salvarDados(String cidade, String estado){
        Propriedade.setCidade(cidade);
        Propriedade.setEstado(estado);

        Log.d("testeX",""+Propriedade.getBairro());

    }
}
