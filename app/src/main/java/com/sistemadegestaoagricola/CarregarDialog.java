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

    public AlertDialog criarDialogCarregamento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar_carregamento,null));
        builder.setCancelable(false);
        Log.d("testeX","exibir");
        return builder.create();
    }

    public AlertDialog criarDialogAvisoPerfil(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Seu perfil de usuário não é permitido!");
        builder.setMessage("Entre com uma conta de Produtor ou Coordenador.");
        builder.setCancelable(true);

        Log.d("testeX","exibir");
        return builder.create();
    }

    public AlertDialog criarDialogPermissaoCamera(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Aviso");
        builder.setMessage("Uso da câmera é necessário para fotograr seu mapa. Sem isso seus dados " +
                "ficaram incompletos.");
        builder.setCancelable(false);

        Log.d("testeX","exibir");
        return builder.create();
    }

}
