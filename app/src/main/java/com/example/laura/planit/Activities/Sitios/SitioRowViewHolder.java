package com.example.laura.planit.Activities.Sitios;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRowViewHolder extends ElementoRowViewHolder
{
    TextView nombreTextView;
    TextView direccionTextView;
    ImageButton btn_editar;
    ImageButton btn_localizar;

    public SitioRowViewHolder(View view) {
        super(view);
        this.nombreTextView = (TextView) view.findViewById(R.id.nombreSitioFavorito);
        this.direccionTextView = (TextView) view.findViewById(R.id.direccionSitioFavorito);
        this.btn_editar=(ImageButton)view.findViewById(R.id.btn_editar_row_sitio);
        this.btn_localizar=(ImageButton)view.findViewById(R.id.btn_gps_row_sitio);
    }
}
