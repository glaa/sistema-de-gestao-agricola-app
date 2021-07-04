package com.sistemadegestaoagricola.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.sistemadegestaoagricola.AtaReuniaoActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.Util;

import java.util.ArrayList;

public class AtaAdapter extends BaseAdapter {

    private Context contexto;
    private ArrayList<Uri> lista;

    public AtaAdapter(Context contexto) {
        this.contexto = contexto;
        this.lista = Util.getAta();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.ata_adapter,null);
        }

        ImageView imageView = view.findViewById(R.id.ivFotoAtaAdapter);
        imageView.setImageURI(lista.get(i));
        imageView.setAdjustViewBounds(true);
        imageView.setBackground(contexto.getDrawable(R.drawable.bt_arredondado));
        imageView.setBackgroundTintList(contexto.getResources().getColorStateList(R.color.verde_escuro));

        return imageView;
    }
}
