package com.sistemadegestaoagricola.versaoantiga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.sistemadegestaoagricola.R;

public class MinhaPropriedadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_propriedade);

        Toolbar toolbar = findViewById(R.id.toolbarMinhaPropriedade);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Voltar");

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("testeX","voltar");
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }
}