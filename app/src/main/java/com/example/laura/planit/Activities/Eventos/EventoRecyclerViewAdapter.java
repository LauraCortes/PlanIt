package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.MedioTransporte;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;

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
        rowViewHolder.lblnombre.setText(evento.getNombreEvento());
        rowViewHolder.lblDescripcion.setText(evento.getDescripcionEvento());
        rowViewHolder.lblFechaEvento.setText(dateFormatter.format(evento.getFechaEvento()));
        rowViewHolder.lblLugarEvento.setText(evento.getLugar());
        rowViewHolder.lblLugarEncuentro.setText(evento.getPuntoEncuentro());
        rowViewHolder.lblHoraEncuentro.setText(timeFormatter.format(evento.getHoraEncuentro()));
        if(evento.getMedioRegreso()==null)
        {
            rowViewHolder.medioNOSeleccionado.setVisibility(View.VISIBLE);
            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.GONE);
        }
        else
        {
            MedioTransporte medio = evento.getMedioRegreso();
            rowViewHolder.lblhoraIda.setText(timeFormatter.format(medio.getHoraRegreso()));
            rowViewHolder.lbltiempoAproximado.setText(medio.getTiempoAproximado());
            rowViewHolder.lblmedioTransporte.setText(medio.getNombre());
            rowViewHolder.medioNOSeleccionado.setVisibility(View.GONE);
            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.VISIBLE);
            Sitio sitioRegreso = evento.getMedioRegreso().getSitioRegreso();
            if(sitioRegreso==null)
            {
                rowViewHolder.lbldestinoRegreso.setText(medio.getDireccionRegreso());
            }
            else
            {
                rowViewHolder.lbldestinoRegreso.setText(sitioRegreso.toString());
            }
        }
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