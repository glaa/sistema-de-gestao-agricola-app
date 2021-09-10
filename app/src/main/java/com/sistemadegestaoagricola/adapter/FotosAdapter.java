package com.sistemadegestaoagricola.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sistemadegestaoagricola.ExibirFotoActivity;
import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.entidades.Util;

import java.io.File;
import java.util.ArrayList;

public class FotosAdapter extends BaseAdapter {

    private Context contexto;
    private ArrayList<String> lista;

    public FotosAdapter(Context contexto) {
        this.contexto = contexto;
        this.lista = Util.getFotos();
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
            view = layoutInflater.inflate(R.layout.item_imagem_adapter,null);
        }

        ImageView imageView = view.findViewById(R.id.ivItemImagemAdapter);
        imageView.setImageURI(Uri.fromFile(new File(lista.get(i))));
        imageView.setAdjustViewBounds(true);
        imageView.setBackground(contexto.getDrawable(R.drawable.bt_arredondado));
        imageView.setBackgroundTintList(contexto.getResources().getColorStateList(R.color.verde_escuro));

        return imageView;
    }
}
