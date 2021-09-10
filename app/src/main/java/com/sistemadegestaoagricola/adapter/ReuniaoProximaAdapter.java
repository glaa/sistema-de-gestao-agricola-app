package com.sistemadegestaoagricola.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.CarregarDialog;
import com.sistemadegestaoagricola.produtor.ProximaReuniaoProdutorActivity;
import com.sistemadegestaoagricola.coordenador.ProximaReuniaoCoordenadorActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Usuario;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ReuniaoProximaAdapter extends RecyclerView.Adapter<ReuniaoProximaAdapter.MyViewHolder> {

    ArrayList<AgendamentoReuniao> agendamentoReuniaos = new ArrayList<AgendamentoReuniao>();
    Context contexto;

    public ReuniaoProximaAdapter(Context context, ArrayList<AgendamentoReuniao> agendamentos) {
        this.agendamentoReuniaos = agendamentos;
        this.contexto = context;

    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuniao_adapter,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(ReuniaoProximaAdapter.MyViewHolder holder, int position) {
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
            String horario = Util.formatarDiaHoraCalendar(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    Util.formatarDiaHoraCalendar(calendar.get(Calendar.MINUTE));
            holder.hora.setText(horario);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Usuario.getPerfil().equals("Coordenador")){
                        Intent intent = new Intent(contexto, ProximaReuniaoCoordenadorActivity.class);
                        intent.putExtra("REUNIAO",agenda);
                        contexto.startActivity(intent);
                    } else if(Usuario.getPerfil().equals("Produtor")){
                        Intent intent = new Intent(contexto, ProximaReuniaoProdutorActivity.class);
                        intent.putExtra("REUNIAO",agenda);
                        contexto.startActivity(intent);
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
        TextView dia, data, hora;

        public MyViewHolder(View itemView) {
            super(itemView);
            dia = itemView.findViewById(R.id.tvDiaReuniaoAdapter);
            data = itemView.findViewById(R.id.tvDataReuniaoAdapter);
            hora = itemView.findViewById(R.id.tvHoraReuniaoAdapter);
        }
    }


}
