package com.sistemadegestaoagricola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AberturaActivity extends AppCompatActivity implements Runnable{

    Thread thread;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            Thread.sleep(2000);
        } catch (Exception e){

        }
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

}