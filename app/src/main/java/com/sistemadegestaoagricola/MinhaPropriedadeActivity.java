package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;


public class MinhaPropriedadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_propriedade);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Voltar");
       // toolbar.onTouchEvent();



    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("testeX","voltar");
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }

//    @Override
//    public void onBackPressed(){ //Botão BACK padrão do android
//        Log.d("testeX","voltar");
//        Intent intent = new Intent(this,HomeActivity.class);
//        startActivity(intent);
//        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
//        return;
//    }

}