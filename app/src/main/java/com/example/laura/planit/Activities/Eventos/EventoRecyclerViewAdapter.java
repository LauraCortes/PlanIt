package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Transportes.AgregarTransporteActivity;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.MedioTransporte;
import com.example.laura.planit.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class EventoRecyclerViewAdapter extends ElementRecyclerViewAdapter
{

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    public EventoRecyclerViewAdapter(Context context, List<Evento> eventos, TabFragment fragment)
    {
        super(context,eventos,fragment);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
    }


    @Override
    public EventoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_eventos_row_evento, null);
        return  new EventoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementoRowViewHolder viewHolder, final int position)
    {
        EventoRowViewHolder rowViewHolder = (EventoRowViewHolder)viewHolder;
        final Evento evento =(Evento) this.elementos.get(position);
        rowViewHolder.lblnombre.setText(evento.getNombre());
        rowViewHolder.lblDescripcion.setText(evento.getDescripcion());
        rowViewHolder.lblFechaEvento.setText(dateFormatter.format(new Date(evento.getFecha())));
        rowViewHolder.lblLugarEvento.setText(""+evento.getLugar());
        rowViewHolder.lblHoraEncuentro.setText(timeFormatter.format(new Date(evento.getFecha())));

        int invitados = evento.getCantidad_invitados();
        if(invitados==0)
        {
            rowViewHolder.lblInvitados.setText("   Aún no has invitado a nadie a tu evento");
        }
        else
        {
            rowViewHolder.lblInvitados.setText("  "+invitados+" amigos en el evento");
        }
//        if(evento.getMedioRegreso()==null||evento.getMedioRegreso().getNombre()==null)
//        {
//            rowViewHolder.medioNOSeleccionado.setVisibility(View.VISIBLE);
//            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.GONE);
//            rowViewHolder.btnTimer.setVisibility(View.GONE);
//        }
//        else
//        {
//            MedioTransporte medio = evento.getMedioRegreso();
//            rowViewHolder.lblhoraIda.setText(timeFormatter.format(medio.getHoraRegreso()));
//            rowViewHolder.lbltiempoAproximado.setText(""+medio.getTiempoAproximado());
//            rowViewHolder.lblmedioTransporte.setText(medio.getNombre());
//            rowViewHolder.medioNOSeleccionado.setVisibility(View.GONE);
//            rowViewHolder.medioTransporteSeleccionado.setVisibility(View.VISIBLE);
//            rowViewHolder.btnTimer.setVisibility(View.VISIBLE);
//            rowViewHolder.lbldestinoRegreso.setText(medio.getDireccionRegreso());
//        }

        rowViewHolder.vista.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(evento.getNombre());
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
                                    //TODO eliminar evento
                                    //PlanIt.darInstancia().eliminarEvento(position);
                                    notifyDataSetChanged();

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
                        //TODO arreglar que sea automático
                        int tiempo= 0;//PlanIt.darInstancia().darEventoPos(position).getMedioRegreso().getTiempoAproximado();
                        Intent intent= new Intent(context, TimerActivity.class);
                        intent.putExtra("Tiempo",tiempo);
                        context.startActivity(intent);
                    }
                }
        );
    }
}
