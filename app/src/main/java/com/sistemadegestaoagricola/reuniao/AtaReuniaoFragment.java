package com.sistemadegestaoagricola.reuniao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sistemadegestaoagricola.ExibirFotoActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.AtaFotosReuniaoAdapter;
import com.sistemadegestaoagricola.entidades.Reuniao;

public class AtaReuniaoFragment extends Fragment {

    public AtaReuniaoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bitmap[] lista = Reuniao.getAtasBitmap().toArray(new Bitmap[Reuniao.getAtasBitmap().size()]);
        View view = inflater.inflate(R.layout.fragment_ata_reuniao, container, false);

        GridView gridView = view.findViewById(R.id.gvAtaReuniaoFragment);
        gridView.setAdapter(new AtaFotosReuniaoAdapter(getActivity(),lista));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("testeX", "clicou p"+i);
                Intent intent = new Intent(getContext(), ExibirFotoActivity.class);
                intent.putExtra("ATA",i);
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}