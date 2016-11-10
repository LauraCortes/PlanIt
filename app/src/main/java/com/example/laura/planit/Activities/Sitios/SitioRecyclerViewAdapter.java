package com.example.laura.planit.Activities.Sitios;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRecyclerViewAdapter extends ElementRecyclerViewAdapter
{


    public SitioRecyclerViewAdapter(Context context, List sitios, TabFragment tabFragment)
    {
        super(context,sitios,tabFragment);
        sitios.add(new Sitio(0,0,"prueba1","prueba1"));
        notifyDataSetChanged();
    }

    public void swapData(List sitios)
    {
        elementos=sitios;
    }



    @Override
    public SitioRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_sitios_row_sitio, null);
        return  new SitioRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementoRowViewHolder holder, final int position)
    {
        SitioRowViewHolder rowViewHolder = (SitioRowViewHolder) holder;
        final Sitio sitio = (Sitio) this.elementos.get(position);
        rowViewHolder.nombreTextView.setText(String.valueOf(sitio.getNombre()));
        rowViewHolder.direccionTextView.setText(String.valueOf(sitio.getDirección()));
        rowViewHolder.btn_editar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, AgregarSitioActivity.class);
                        i.putExtra("editar",true);
                        i.putExtra("titulo","Editar sitio favorito");
                        i.putExtra("sitio", sitio);
                        context.startActivity(i);
                    }
                }
        );
    }
}
