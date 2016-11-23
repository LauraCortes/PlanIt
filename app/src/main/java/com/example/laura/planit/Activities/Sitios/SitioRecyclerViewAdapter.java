package com.example.laura.planit.Activities.Sitios;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
import com.example.laura.planit.Services.ObtenerDireccionesIntentService;

import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRecyclerViewAdapter extends ElementRecyclerViewAdapter
{


    public SitioRecyclerViewAdapter(Context context, List sitios, TabFragment tabFragment)
    {
        super(context,sitios,tabFragment);
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
    public void onBindViewHolder(final ElementoRowViewHolder holder, final int position)
    {
        SitioRowViewHolder rowViewHolder = (SitioRowViewHolder) holder;
        final Sitio sitio = (Sitio) this.elementos.get(position);
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
            System.out.println("Service lanzado para obtener direccion");
            //Lanzar servicio para obtener direccion
            Intent intent = new Intent(context, ObtenerDireccionesIntentService.class);
            intent.putExtra(Constants.SITIO, sitio);
            context.startService(intent);
        }
        boolean seleccionado = tabFragment.isItemSelected(sitio);
        holder.decorarSeleccionado(seleccionado);

        holder.vista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tabFragment.hayItemsSeleccionados())
                        {
                            seleccionarItem(sitio,holder);
                        }
                        else
                        {
                            Intent i = new Intent(context, AgregarSitioActivity.class);
                            i.putExtra("editar",true);
                            i.putExtra("sitio", sitio);
                            context.startActivity(i);
                        }
                    }
                }
        );

        holder.vista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                seleccionarItem(sitio,holder);
                //Retorna que sí lo usó->No invoca el clickListener
                return true;
            }
        });
    }
}
