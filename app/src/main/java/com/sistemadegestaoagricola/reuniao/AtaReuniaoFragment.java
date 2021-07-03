package com.sistemadegestaoagricola.reuniao;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.sistemadegestaoagricola.R;
import com.sistemadegestaoagricola.adapter.AtaReuniaoAdapter;

public class AtaReuniaoFragment extends Fragment {



    public AtaReuniaoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int[] lista = new int[]{R.drawable.ic_baseline_camera_alt_24,R.drawable.ic_baseline_camera_alt_24,
                R.drawable.ic_baseline_camera_alt_24,R.drawable.ic_baseline_camera_alt_24,R.drawable.ic_baseline_camera_alt_24,
                R.drawable.ic_baseline_camera_alt_24,};
        View view = inflater.inflate(R.layout.fragment_ata_reuniao, container, false);

        GridView gridView = view.findViewById(R.id.gvAtaReuniaoFragment);
        //TextView t = view.findViewById(R.id.tvTextoAtaReuniaoFragment);
        //t.setText("mudeir");
        gridView.setAdapter(new AtaReuniaoAdapter(getActivity(),lista));

        Log.d("testeX","lista Ata");
        //return inflater.inflate(R.layout.fragment_ata_reuniao, container, false);
        return view;
    }
}