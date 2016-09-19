package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laura.planit.Activities.Transportes.AgregarTransporteActivity;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.MedioTransporte;
import com.example.laura.planit.Logica.PlanIt;
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
    public void onBindViewHolder(EventoRowViewHolder rowViewHolder, final int position)
    {
        final Evento evento = this.eventos.get(position);
        rowViewHolder.lblnombre.setText(evento.getNombreEvento());
        rowViewHolder.lblDescripcion.setText(evento.getDescripcionEvento());
        rowViewHolder.lblFechaEvento.setText(dateFormatter.format(evento.getFechaEvento()));
        rowViewHolder.lblLugarEvento.setText(evento.getLugar());
        rowViewHolder.lblLugarEncuentro.setText(evento.getPuntoEncuentro());
        rowViewHolder.lblHoraEncuentro.setText(timeFormatter.format(evento.getHoraEncuentro()));

        List invitados = evento.getInvitados();
        if(invitados==null || invitados.size()==0)
        {
            rowViewHolder.lblInvitados.setText("   Aún no has invitado a nadie a tu evento");
        }
        else
        {
            rowViewHolder.lblInvitados.setText("  "+invitados.size()+" amigos en el evento");
        }
        if(evento.getMedioRegreso()==null||evento.getMedioRegreso().getNombre()==null)
        {
            rowViewHolder.medioNOSeleccionado.setVisibility(View.VISIBLE);
            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.GONE);
        }
        else
        {
            MedioTransporte medio = evento.getMedioRegreso();
            rowViewHolder.lblhoraIda.setText(timeFormatter.format(medio.getHoraRegreso()));
            rowViewHolder.lbltiempoAproximado.setText(""+medio.getTiempoAproximado());
            rowViewHolder.lblmedioTransporte.setText(medio.getNombre());
            rowViewHolder.medioNOSeleccionado.setVisibility(View.GONE);
            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.VISIBLE);
            rowViewHolder.lbldestinoRegreso.setText(medio.getDireccionRegreso());
        }

        rowViewHolder.vista.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(evento.getNombreEvento());
                        builder.setItems(new CharSequence[]{"Configurar regreso","Eliminar"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent agregarTransporte = new Intent(context, AgregarTransporteActivity.class);
                                    agregarTransporte.putExtra("pos",position);
                                    context.startActivity(agregarTransporte);
                                }
                                else if(which==1)
                                {
                                    PlanIt.darInstancia().eliminarEvento(position);
                                    recycler.notifyDataSetChanged();

                                    /**
                                    Intent service = new Intent(context, PersitenciaService.class);
                                    service.putExtra("Requerimiento","EliminarSitio");
                                    service.putExtra("Nombre",PlanIt.darInstancia().darSitios().get(position).getNombre());
                                    context.startService(service);
                                    PlanIt.darInstancia().eliminarSitio(position);
                                    Toast.makeText(context, "Sitio eliminado de favoritos", Toast.LENGTH_SHORT).show();
                                     */
                                }
                            }
                        });
                        builder.show();
                        return true;
                    }
                }
        );

        rowViewHolder.btnTimer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Aquí poner el método que necesitas del mundo.
                        int tiempo= PlanIt.darInstancia().darEventoPos(position).getMedioRegreso().getTiempoAproximado();
                        Intent intent= new Intent(context, TimerActivity.class);
                        intent.putExtra("Tiempo",tiempo);
                        context.startActivity(intent);
                    }
                }
        );

    }
}
