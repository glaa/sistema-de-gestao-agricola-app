package com.sistemadegestaoagricola.reuniao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sistemadegestaoagricola.auxiliar.ExibirFotoActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.AtaFotosReuniaoAdapter;
import com.sistemadegestaoagricola.entidades.Reuniao;

import androidx.fragment.app.Fragment;

public class FotosReuniaoFragment extends Fragment {

    public FotosReuniaoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bitmap[] lista = Reuniao.getFotosBitmap().toArray(new Bitmap[Reuniao.getFotosBitmap().size()]);
        View view = inflater.inflate(R.layout.fragment_fotos_reuniao, container, false);

        GridView gridView = view.findViewById(R.id.gvFotosReuniaoFragment);
        gridView.setAdapter(new AtaFotosReuniaoAdapter(getActivity(),lista));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ExibirFotoActivity.class);
                intent.putExtra("REUNIAO",i);
                startActivity(intent);
            }
        });
        return view;
    }
}
