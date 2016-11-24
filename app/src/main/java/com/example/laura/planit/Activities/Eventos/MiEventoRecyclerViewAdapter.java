package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Activities.Transportes.AgregarTransporteActivity;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class MiEventoRecyclerViewAdapter extends ElementRecyclerViewAdapter {

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    public MiEventoRecyclerViewAdapter(Context context, List<ResumenEvento> eventos, TabFragment fragment) {
        super(context, eventos, fragment);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
    }


    @Override
    public MiEventoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_eventos_row_evento, null);
        return new MiEventoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ElementoRowViewHolder holder, final int position) {
        MiEventoRowViewHolder rowViewHolder = (MiEventoRowViewHolder) holder;
        final ResumenEvento evento = (ResumenEvento) this.elementos.get(position);
        rowViewHolder.lblnombre.setText(evento.getNombre());
        rowViewHolder.lblFechaEvento.setText(dateFormatter.format(new Date(evento.getFecha())));
        rowViewHolder.lblHoraEncuentro.setText(timeFormatter.format(new Date(evento.getFecha())));

        boolean seleccionado = tabFragment.isItemSelected(evento);
        holder.decorarSeleccionado(seleccionado);

        holder.vista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tabFragment.hayItemsSeleccionados())
                        {
                            seleccionarItem(evento,holder);
                        }
                        else
                        {
                            Intent i = new Intent(context, DetallesEventoActivity.class);
                            i.putExtra(Constants.EXTRA_ID_EVENTO,evento.getId_evento());
                            context.startActivity(i);
                        }
                    }
                }
        );

        holder.vista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                seleccionarItem(evento,holder);
                return true;
            }
        });

    }
}
