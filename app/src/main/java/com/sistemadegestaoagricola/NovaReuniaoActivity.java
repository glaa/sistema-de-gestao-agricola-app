package com.sistemadegestaoagricola;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.sistemadegestaoagricola.entidades.Util;

import java.util.Calendar;

public class NovaReuniaoActivity extends AppCompatActivity {

    private LinearLayout llVoltar;
    private EditText etTema;
    private EditText etData;
    private EditText etHora;
    private EditText etLocal;
    private Button btSalvar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_reuniao);

        llVoltar = findViewById(R.id.llVoltarNovaReuniao);
        etTema = findViewById(R.id.etTemaNovaReuniao);
        etData = findViewById(R.id.etDataNovaReuniao);
        etHora = findViewById(R.id.etHoraNovaReuniao);
        etLocal = findViewById(R.id.etLocalNovaReuniao);
        btSalvar = findViewById(R.id.btSalvarNovaReuniao);

        llVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        etData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exirDatePicker();
                return false;
            }
        });

        etHora.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exibirTimePicker();
                return false;
            }
        });
    }

    private void exirDatePicker(){
        if(datePickerDialog != null && datePickerDialog.isShowing()){
            datePickerDialog.dismiss();
        }
        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(NovaReuniaoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                String diaString = Util.formatarDiaHoraCalendar(dia);
                String mesString = Util.formatarMesCalendar(mes);
                etData.setText(diaString+"/"+mesString+"/"+ano);
            }
        },ano,mes,dia);
        datePickerDialog.show();
    }

    private void exibirTimePicker(){
        if(timePickerDialog != null && timePickerDialog.isShowing()){
            timePickerDialog.dismiss();
        }
        Calendar calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener  timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {
                String horaString = Util.formatarDiaHoraCalendar(hora);
                String minutoString = Util.formatarDiaHoraCalendar(minuto);
                etHora.setText(horaString+ ":" +minutoString);
            }
        };
        timePickerDialog = new TimePickerDialog(NovaReuniaoActivity.this, timeSetListener,hora, minuto, true);
        timePickerDialog.show();
    }
}