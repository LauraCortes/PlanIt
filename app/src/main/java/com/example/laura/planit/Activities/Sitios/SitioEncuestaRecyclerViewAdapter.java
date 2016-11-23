package com.example.laura.planit.Activities.Sitios;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.laura.planit.Activities.Eventos.CrearEncuestaLugaresActivity;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioEncuestaRecyclerViewAdapter extends RecyclerView.Adapter<ElementoRowViewHolder>
{
    private List<Sitio> elementos;
    protected Context context;
    protected CrearEncuestaLugaresActivity activity;

    public void swapData(List sitios)
    {
        elementos=sitios;
    }

    public SitioEncuestaRecyclerViewAdapter(List<Sitio> elementos, Context context, CrearEncuestaLugaresActivity activity) {
        this.elementos = elementos;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public int getItemCount() {
        if (elementos == null) {
            return 0;
        } else {
            return elementos.size();
        }
    }


    protected void seleccionarItem(Sitio elemento, ElementoRowViewHolder elementoRowViewHolder)
    {
        boolean seleccionado = activity.seleccionarItem(elemento);
        elementoRowViewHolder.decorarSeleccionado(seleccionado);
    }

    @Override
    public SitioRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_sitios_row_sitio, null);
        return  new SitioRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ElementoRowViewHolder holder, final int position)
    {
        SitioRowViewHolder rowViewHolder = (SitioRowViewHolder) holder;
        final Sitio sitio = this.elementos.get(position);
        rowViewHolder.nombreTextView.setText(sitio.getNombre());
        rowViewHolder.coordenadasTextView.setText(sitio.getCoordenadas());
        String direccion = (sitio.getDireccion());
        LinearLayout layoutDireccion = (LinearLayout)rowViewHolder.vista.findViewById(R.id.layout_direccion_sitios);
        LinearLayout layoutGPS = (LinearLayout)rowViewHolder.vista.findViewById(R.id.layout_gps_sitios);
        if(direccion!= null && !direccion.trim().isEmpty())
        {
            rowViewHolder.direccionTextView.setText(direccion);
            layoutDireccion.setVisibility(View.VISIBLE);
            layoutGPS.setVisibility(View.GONE);
        }
        else
        {
            layoutDireccion.setVisibility(View.GONE);
            layoutGPS.setVisibility(View.VISIBLE);
        }
        boolean seleccionado = activity.estaSeleccionado(sitio.getNombre());
        holder.decorarSeleccionado(seleccionado);
        holder.vista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seleccionarItem(sitio,holder);
                    }
                }
        );
    }
}
