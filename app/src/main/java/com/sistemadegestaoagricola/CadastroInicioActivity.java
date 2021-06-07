package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Usuario;

public class CadastroInicioActivity extends AppCompatActivity {

    private TextView tvNome;
    private Button btComecar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_inicial_novo);

        tvNome = findViewById(R.id.tvNomeCadastroInicialNovo);
        btComecar = findViewById(R.id.btComecarCadastroInicialNovo);

        tvNome.setText(Usuario.getNome() + "!");

        /* Zerando os atributos da Classe Propriedade */
        Propriedade.zerarAtributos();

        btComecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CadastroTamanhoPropriedadeActivity.class);
                startActivity(intent);
            }
        });
    }
}