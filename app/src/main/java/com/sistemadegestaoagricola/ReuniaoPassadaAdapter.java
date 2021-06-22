package com.sistemadegestaoagricola;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReuniaoPassadaAdapter extends RecyclerView.Adapter<ReuniaoPassadaAdapter.MyViewHolder> {

    ArrayList<AgendamentoReuniao> agendamentoReuniaos = new ArrayList<AgendamentoReuniao>();

    public ReuniaoPassadaAdapter(ArrayList<AgendamentoReuniao> agendamentos) {
        this.agendamentoReuniaos = agendamentos;
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
            holder.status.setText("Evento realizado");

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
