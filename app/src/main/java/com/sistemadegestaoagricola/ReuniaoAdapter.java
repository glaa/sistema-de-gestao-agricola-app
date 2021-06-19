package com.sistemadegestaoagricola;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistemadegestaoagricola.entidades.AgendamentoReuniao;
import com.sistemadegestaoagricola.entidades.Propriedade;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReuniaoAdapter extends RecyclerView.Adapter<ReuniaoAdapter.MyViewHolder> {

    ArrayList<AgendamentoReuniao> agendamentoReuniaos = new ArrayList<AgendamentoReuniao>();

    public ReuniaoAdapter(ArrayList<AgendamentoReuniao> agendamentos) {
        this.agendamentoReuniaos = agendamentos;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuniao_adapter,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder( ReuniaoAdapter.MyViewHolder holder, int position) {
        //Retorna somente o que exitir
        if(position < agendamentoReuniaos.size()) {
            AgendamentoReuniao agenda  = agendamentoReuniaos.get(position);
            Log.d("testeX", "" + position + " size = " + agendamentoReuniaos.size());
            //String data = agenda.getData().getDate() + "/" + (agenda.getData().getMonth()+1) + "/" + (agenda.getData().getYear()+1900);
            String data = Util.converterDateString(agenda.getData());
            holder.data.setText(data);
            if (agenda.isRegistrada()) {
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("Realizada");
            } else {
                holder.status.setVisibility(View.GONE);
                holder.status.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return agendamentoReuniaos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView data, status;

        public MyViewHolder(View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.tvDataReuniaoAdapter);
            status = itemView.findViewById(R.id.tvStatusReuniaoAdapter);

        }
    }


}
