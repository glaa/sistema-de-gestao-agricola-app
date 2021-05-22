package com.sistemadegestaoagricola;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;

import com.sistemadegestaoagricola.R;

import androidx.appcompat.app.AlertDialog;

public class CarregarDialog {

    private Activity activity;
    //private AlertDialog dialog;

    public CarregarDialog(Activity minhaActivity){
        activity = minhaActivity;
    }

    public AlertDialog cirarDialogCarregamento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar_carregamento,null));
        builder.setCancelable(false);
//        dialog = builder.create();
//        dialog.show();
        Log.d("testeX","exibir");
        return builder.create();
    }

//    public void encerrarDialog(){
//        dialog.dismiss();
//        Log.d("testeX","encerrar");
//
//    }
//
//    public void ocultar(){
//        dialog.hide();
//    }
//
//    public void exibir(){
//        dialog.show();
//    }
}
