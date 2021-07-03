package com.sistemadegestaoagricola.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sistemadegestaoagricola.R;

public class AtaReuniaoAdapter extends BaseAdapter {

    private Context contexto;
    private int[] lista;

    public AtaReuniaoAdapter(Context contexto, int[] lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int i) {
        return lista[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.fragment_ata_reuniao,null);
        }
        ImageView imageView = new ImageView(contexto);
        imageView.setImageResource(lista[i]);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
