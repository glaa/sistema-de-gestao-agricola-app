package com.sistemadegestaoagricola.entidades;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Util {

    private static ArrayList<AgendamentoReuniao> agendamentos = new ArrayList<AgendamentoReuniao>();

    private static ArrayList<Uri> ata = new ArrayList<>();

    private static ArrayList<Uri> fotos = new ArrayList<>();

    public static ArrayList<Uri> getAta() {
        return ata;
    }

    public static void setAta(ArrayList<Uri> ata) {
        Util.ata = ata;
    }

    public static void addAta(Uri uri){
        Util.ata.add(uri);
    }

    public static ArrayList<Uri> getFotos() {
        return fotos;
    }

    public static void setFotos(ArrayList<Uri> fotos) {
        Util.fotos = fotos;
    }

    public static void addFotos(Uri uri){
        Util.fotos.add(uri);
    }

    public static ArrayList<AgendamentoReuniao> getAgendamentos() {
        return agendamentos;
    }

    public static void setAgendamentos(ArrayList<AgendamentoReuniao> agendamentos) {
        Util.agendamentos = agendamentos;
    }

    public static void addAgendamentos(AgendamentoReuniao reuniao){
        agendamentos.add(reuniao);
    }

    public static void esvaziarAgendamentos(){
        Util.agendamentos.clear();
    }

    public static Date converterStringDate(String data) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        try {
            d1 = df.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1;
    }

    public static String converterDateString(Date data){
        String d1;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        d1 = df.format(data);
        return d1;
    }

    public static String calendarioIntParaStringDia(int dia){
        String diaSemana = null;
        if(dia == 1){
            diaSemana = "Domingo";
        } else if(dia == 2){
            diaSemana = "Segunda";
        } else if(dia == 3){
            diaSemana = "Terça";
        } else if(dia == 4){
            diaSemana = "Quarta";
        } else if(dia == 5){
            diaSemana = "Quinta";
        } else if(dia == 6){
            diaSemana = "Sexta";
        } else if(dia == 7){
            diaSemana = "Sabado";
        }
        return diaSemana;
    }

    public static String calendarioIntParaStringMes(int mes){
        String mesDoAno = null;
        if(mes == 0){
            mesDoAno = "Janeiro";
        } else if(mes == 1){
            mesDoAno = "Fevereiro";
        } else if(mes == 2){
            mesDoAno = "Março";
        } else if(mes == 3){
            mesDoAno = "Abril";
        } else if(mes == 4){
            mesDoAno = "Maio";
        } else if(mes == 5){
            mesDoAno = "Junho";
        } else if(mes == 6){
            mesDoAno = "Julho";
        } else if(mes == 7){
            mesDoAno = "Agosto";
        } else if(mes == 8){
            mesDoAno = "Setembro";
        } else if(mes == 9){
            mesDoAno = "Outubro";
        } else if(mes == 10){
            mesDoAno = "Novembro";
        } else if(mes == 11){
            mesDoAno = "Dezembro";
        }
        return mesDoAno;
    }

    public static String converterBitmapParaString(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytesArray = stream.toByteArray();
        return Base64.encodeToString(bytesArray,Base64.DEFAULT);
    }

    public static byte[] converterBitmapParaByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }

    /*
     * Criar máscara para o CEP
     *@param editText Campo a ser aplicado a máscara
     *@param key Código correspondente ao caracter capturado pelo setOnKeyListener
     *@autor Gleison Alves de Araujo
     */
    public static void mascaraCepOnKeyListener(EditText editText, int key){
        String strCep = editText.getText().toString();
        if(key != 67){
            strCep = strCep.replace("-","");
            int tamanho = strCep.length();
            if(tamanho >= 5){
                strCep = strCep.substring(0,5) + "-" + strCep.substring(5,strCep.length());
                editText.setText(strCep);
                editText.setSelection(strCep.length());
            }
        } else {
            int tamanho = strCep.length();
            if(tamanho < 5){
                strCep = strCep.replace("-","");
                editText.setText(strCep);
                editText.setSelection(strCep.length());
            }
        }
    }

    /*Receber um número e devolver uma String com dois digítos*/
    public static String formatarDiaHoraCalendar(int dia){
        String d = String.valueOf(dia);

        if(dia < 10){
            d = "0"+ dia;
        }
        return d;
    }

    /*Receber um número e devolver uma String com dois digítos*/
    public static String formatarMesCalendar(int mes){
        String mf;
        mes++;
        mf = String.valueOf(mes);
        if(mes < 10){
            mf = "0"+ mes;
        }
        return mf;
    }

    public static String converterPraBase64(String texto){
        String code = null;
            code = Base64.encodeToString(texto.getBytes(),Base64.DEFAULT);
        return code;
    }

    public static String extrairHoraDeData(Date data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        String horario;
        horario = formatarDiaHoraCalendar(calendar.get(Calendar.HOUR_OF_DAY))+":"+
                formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE));
        return horario;
    }

}
