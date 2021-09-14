package com.sistemadegestaoagricola.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemadegestaoagricola.auxiliar.ErroActivity;
import com.sistemadegestaoagricola.auxiliar.ExibirFotoActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.conexao.ConexaoAPI;
import com.sistemadegestaoagricola.conexao.RotaGetMapa;
import com.sistemadegestaoagricola.entidades.Mapa;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class MapasAdapter extends RecyclerView.Adapter<MapasAdapter.MyViewHolder>{
    ArrayList<Mapa> mapas = new ArrayList<Mapa>();
    Context contexto;
    final ExecutorService threadpool = Executors.newFixedThreadPool(3);
    String[] mensagensExceptions;
    int status;
    AlertDialog carregando;

    public MapasAdapter(Context contexto, ArrayList<Mapa> mapas, AlertDialog carregando) {
        this.mapas = mapas;
        this.contexto = contexto;
        this.carregando = carregando;
    }

    @NonNull

    @Override
    public MapasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.mapas_adapter,parent,false);

        return new MapasAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder( MapasAdapter.MyViewHolder holder, int position) {

        //Retorna somente o que exitir
        if(position < mapas.size()) {
            Date data  = mapas.get(position).getData();
            String dataFormatada = Util.converterDateString(data);

            holder.tvData.setText(dataFormatada);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mapas.get(position).getFoto() == null){
                        carregando.show();
                        //Faz a busca da imagem do mapa pela classe RotaGetMapa e a salva no objeto
                        Log.d("testeX","clicou mapa");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                buscarMapa(mapas.get(position).getId());
                            }
                        });
                        thread.start();
                        try {
                            thread.join(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    Intent intent = new Intent(contexto, ExibirFotoActivity.class);
                    intent.putExtra("MAPA",position);
                    contexto.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvData;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvDataMapasAdapter);
        }
    }

    private void buscarMapa(int id){
        RotaGetMapa rotaGetMapa = new RotaGetMapa(id);
        Future<ConexaoAPI> future = threadpool.submit(rotaGetMapa);

        while(!future.isDone()){
            //Aguardando
        }

        ConexaoAPI conexao = null;
        try{
            conexao = future.get();

            mensagensExceptions = conexao.getMensagensExceptions();

            if(mensagensExceptions == null){
                //Sem erro de conexão
                status = conexao.getCodigoStatus();
                if(status != 200){
                    //Erro ao salvar
                    Toast.makeText(contexto, "Erro na busca do mapa", Toast.LENGTH_SHORT).show();

                }
            } else {
                Erro(mensagensExceptions[0],mensagensExceptions[1]);
            }

        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Erro("Falha na conexão","Tente novamente em alguns minutos");
        } finally {
            if(conexao != null){
                conexao.fechar();
            }
        }
    }

    private void Erro(String titulo, String subtitulo){
        Intent intent = new Intent(contexto, ErroActivity.class);
        intent.putExtra("TITULO", titulo);
        intent.putExtra("SUBTITULO", subtitulo);
        intent.putExtra("ACTIVITY","MainActivity");
        contexto.startActivity(intent);
    }
}
