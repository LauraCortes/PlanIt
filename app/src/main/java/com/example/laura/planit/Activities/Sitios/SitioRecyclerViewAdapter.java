package com.example.laura.planit.Activities.Sitios;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRecyclerViewAdapter extends RecyclerView.Adapter<SitioRowViewHolder> {
    Context context;
    List<Sitio> sitios;

    public SitioRecyclerViewAdapter(Context context, List<Sitio> sitios)
    {
        this.context = context;
        this.sitios = sitios;
    }


    @Override
    public int getItemCount() {
        if (sitios == null) {
            return 0;
        } else {
            return sitios.size();
        }
    }

    @Override
    public SitioRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_sitio, null);
        return  new SitioRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SitioRowViewHolder rowViewHolder, int position) {
        Sitio sitio = this.sitios.get(position);
        System.out.println(sitio==null);
        rowViewHolder.nombreTextView.setText(String.valueOf(sitio.getNombre()));
        rowViewHolder.barrioTextView.setText(String.valueOf(sitio.getBarrio()));
        rowViewHolder.direccionTextView.setText(String.valueOf(sitio.getDirección()));
        rowViewHolder.imagenView.setBackground(ContextCompat.getDrawable(context,R.drawable.home));
    }
}
