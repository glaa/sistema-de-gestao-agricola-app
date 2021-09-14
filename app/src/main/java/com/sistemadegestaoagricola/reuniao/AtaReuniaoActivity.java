package com.sistemadegestaoagricola.reuniao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.AtaAdapter;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;

public class AtaReuniaoActivity extends AppCompatActivity{

    private GridView gridView;
    private ArrayList<String> lista = new ArrayList<>();
    private Button btConfirmar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ata_reuniao);

        gridView = findViewById(R.id.gvAtaReuniao);
        btConfirmar = findViewById(R.id.btConfirmarAtaReuniao);

        AtaAdapter adapter = new AtaAdapter(this);
        gridView.setAdapter(adapter);

        //Aviso ao iniciar a activity
        Toast.makeText(AtaReuniaoActivity.this, "Mantenha um item pressionado para excluir", Toast.LENGTH_SHORT).show();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //lista.clear();
                lista = Util.getAta();
                lista.remove(i);

                gridView.setAdapter(adapter);
                Toast.makeText(AtaReuniaoActivity.this, "Item excluído", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

    }
}