package com.sistemadegestaoagricola.entidades;

import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sistemadegestaoagricola.R;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintSet;

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
        return builder.create();
    }

    public AlertDialog criarDialogAvisoPerfil(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Seu perfil de usuário não é permitido!");
        builder.setMessage("Entre com uma conta de Produtor ou Coordenador.");
        builder.setCancelable(true);
        return builder.create();
    }

    public AlertDialog criarDialogPermissaoCamera(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Aviso");
        builder.setMessage("Uso da câmera e armazenamento são necessários para fotograr seu mapa. Sem isso seus dados " +
                "ficaram incompletos.");
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogBuscarCep(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.progressbar_carregamento,null);
        TextView tvMensagem = view.findViewById(R.id.tvMensagemCarregamento);
        tvMensagem.setText("Buscando CEP...");
        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogCepInvalido(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("CEP inválido");
        builder.setMessage("O CEP deve possuir 8 dígitos ou não será salvo.");
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogSalvarInformacoes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.progressbar_carregamento,null);
        TextView tvMensagem = view.findViewById(R.id.tvMensagemCarregamento);
        tvMensagem.setText("Salvando...");
        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogExcluirInformacoes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.progressbar_carregamento,null);
        TextView tvMensagem = view.findViewById(R.id.tvMensagemCarregamento);
        tvMensagem.setText("Excluindo...");
        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogExcluirReuniao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Aviso");
        builder.setMessage("Deseja realmente excluir esta reunião?");
        builder.setCancelable(false);
        return builder.create();
    }

    public AlertDialog criarDialogRegistroNaoPermitido(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogAviso);
        builder.setTitle("Reunião futura");
        builder.setMessage("Esta reunião ainda não pode ser registrada.");
        builder.setCancelable(true);
        return builder.create();
    }

}
