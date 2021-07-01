package com.sistemadegestaoagricola.adapter;

import com.sistemadegestaoagricola.reuniao.AtaReuniaoFragment;
import com.sistemadegestaoagricola.reuniao.ReuniaoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int numerosTabelas;

    public PageAdapter(FragmentManager fm, int numerosTabelas) {
        super(fm);
        this.numerosTabelas = numerosTabelas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new AtaReuniaoFragment();
        } else if(position == 1){
            return new ReuniaoFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return numerosTabelas;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
