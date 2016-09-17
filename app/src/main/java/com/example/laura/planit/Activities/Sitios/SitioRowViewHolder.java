package com.example.laura.planit.Activities.Sitios;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.R;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRowViewHolder extends RecyclerView.ViewHolder {


    ImageView imagenView;
    TextView nombreTextView;
    TextView barrioTextView;
    TextView direccionTextView;
    View vista;

    public SitioRowViewHolder(View view) {
        super(view);
        vista=view;
        this.nombreTextView = (TextView) view.findViewById(R.id.nombreSitioFavorito);
        this.imagenView = (ImageView) view.findViewById(R.id.imagenSitioFavorito);
        this.barrioTextView = (TextView) view.findViewById(R.id.barrioSitioFavorito);
        this.direccionTextView = (TextView) view.findViewById(R.id.direccionSitioFavorito);
    }
}
