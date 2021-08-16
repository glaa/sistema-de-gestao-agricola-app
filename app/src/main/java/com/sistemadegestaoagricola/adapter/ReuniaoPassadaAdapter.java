package com.sistemadegestaoagricola.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.coordenador.ProximaReuniaoCoordenadorActivity;
import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.produtor.ProximaReuniaoProdutorActivity;
import com.sistemadegestaoagricola.produtor.ReuniaoRealizadaProdutorActivity;
import com.sistemadegestaoagricola.coordenador.ReuniaoRealizadaCoordenadorActivity;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;
import com.sistemadegestaoagricola.reuniao.ReuniaoActivity;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ReuniaoPassadaAdapter extends RecyclerView.Adapter<ReuniaoPassadaAdapter.MyViewHolder> {

    ArrayList<AgendamentoReuniao> agendamentoReuniaos = new ArrayList<AgendamentoReuniao>();
    Context context;


    public ReuniaoPassadaAdapter(Context contexto, ArrayList<AgendamentoReuniao> agendamentos) {
        this.agendamentoReuniaos = agendamentos;
        this.context = contexto;
    }

    @NonNull

    @Override
    public ReuniaoPassadaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuniao_passada_adapter,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder( ReuniaoPassadaAdapter.MyViewHolder holder, int position) {

        //Retorna somente o que exitir
        if(position < agendamentoReuniaos.size()) {
            AgendamentoReuniao agenda  = agendamentoReuniaos.get(position);
            Log.d("testeX", "" + position + " size = " + agendamentoReuniaos.size());
            //String data = agenda.getData().getDate() + "/" + (agenda.getData().getMonth()+1) + "/" + (agenda.getData().getYear()+1900);
            String data = Util.converterDateString(agenda.getData());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(agenda.getData());

            String diaDaSemana = Util.calendarioIntParaStringDia(calendar.get(Calendar.DAY_OF_WEEK));
            holder.dia.setText(diaDaSemana.substring(0,3));
            holder.data.setText(data);
            if(agenda.isRegistrada()){
                holder.status.setText("Evento realizado");
            } else {
                holder.status.setText("Pendente de registro");
                holder.status.setTextColor(context.getResources().getColorStateList(R.color.vermelho_alerta));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Usuario.getPerfil().equals("Coordenador")){
                        if(agenda.isRegistrada()){
                            Intent intent = new Intent(context, ReuniaoRealizadaCoordenadorActivity.class);
                            intent.putExtra("REUNIAO", agenda);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, ProximaReuniaoCoordenadorActivity.class);
                            intent.putExtra("REUNIAO", agenda);
                            context.startActivity(intent);
                        }

                    } else if(Usuario.getPerfil().equals("Produtor")){
                        if(agenda.isRegistrada()){
                            Intent intent = new Intent(context, ReuniaoRealizadaProdutorActivity.class);
                            intent.putExtra("REUNIAO", agenda);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, ProximaReuniaoProdutorActivity.class);
                            intent.putExtra("REUNIAO", agenda);
                            context.startActivity(intent);
                        }

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return agendamentoReuniaos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dia, data, status;

        public MyViewHolder(View itemView) {
            super(itemView);
            dia = itemView.findViewById(R.id.tvDiaReuniaoPassadaAdapter);
            data = itemView.findViewById(R.id.tvDataReuniaoPassadaAdapter);
            status = itemView.findViewById(R.id.tvStatusReuniaoPassadaAdapter);
        }
    }
}
