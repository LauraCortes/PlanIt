package com.example.laura.planit.Activities.Eventos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Activities.Sitios.SitioRowViewHolder;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class EventoRecyclerViewAdapter extends RecyclerView.Adapter<EventoRowViewHolder>
{
    Context context;
    List<Evento> eventos;
    EventoRecyclerViewAdapter recycler;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    public EventoRecyclerViewAdapter(Context context, List<Evento> eventos)
    {
        recycler=this;
        this.context = context;
        this.eventos = eventos;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
    }


    @Override
    public int getItemCount() {
        if (eventos == null) {
            return 0;
        } else {
            return eventos.size();
        }
    }

    @Override
    public EventoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_evento, null);
        return  new EventoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventoRowViewHolder rowViewHolder, int position)
    {
        final Evento evento = this.eventos.get(position);
        rowViewHolder.nombreTextView.setText(evento.getNombreEvento());
        rowViewHolder.descripcionTextView.setText(evento.getDescripcionEvento());
        rowViewHolder.fechaTextView.setText(dateFormatter.format(evento.getFechaEvento()));
        rowViewHolder.lugarEventoTextView.setText(evento.getLugar());
        rowViewHolder.lugarEncuentroTextView.setText(evento.getPuntoEncuentro());
        rowViewHolder.horaEncuentroTextView.setText(timeFormatter.format(evento.getHoraEncuentro()));
        /**
        rowViewHolder.vista.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(evento.getNombre());
                        builder.setItems(new CharSequence[]{"editar", "eliminar"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent i = new Intent(context, AgregarSitioActivity.class);
                                    i.putExtra("editar",true);
                                    i.putExtra("titulo","Editar sitio favorito");
                                    i.putExtra("posicion", position);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }
                                else
                                {
                                    recycler.notifyDataSetChanged();
                                    Intent service = new Intent(context, PersitenciaService.class);
                                    service.putExtra("Requerimiento","EliminarSitio");
                                    service.putExtra("Nombre",PlanIt.darInstancia().darSitios().get(position).getNombre());
                                    context.startService(service);
                                    PlanIt.darInstancia().eliminarSitio(position);
                                    Toast.makeText(context, "Sitio eliminado de favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                        return true;
                    }
                }
        );
    */
    }
}
