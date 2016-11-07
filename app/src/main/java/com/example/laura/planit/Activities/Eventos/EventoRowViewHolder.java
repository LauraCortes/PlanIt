package com.example.laura.planit.Activities.Eventos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 16/09/2016.
 */
public class EventoRowViewHolder extends ElementoRowViewHolder
{

    TextView lblnombre;
    TextView lblDescripcion;
    TextView lblFechaEvento;
    TextView lblLugarEvento;
    TextView lblLugarEncuentro;
    TextView lblHoraEncuentro;
    TextView lblmedioTransporte;
    TextView lblhoraIda;
    TextView lbltiempoAproximado;
    TextView lbldestinoRegreso;
    LinearLayout medioTransporteSeleccionado;
    LinearLayout medioNOSeleccionado;
    TextView lblInvitados;
    ImageButton btnTimer;


    public EventoRowViewHolder(View view) {
        super(view);
        lblnombre =(TextView)view.findViewById(R.id.nombreEvento);
        lblDescripcion =(TextView)view.findViewById(R.id.descripcionEvento);
        lblFechaEvento =(TextView)view.findViewById(R.id.fechaEvento);
        lblLugarEvento =(TextView)view.findViewById(R.id.lugarEvento);
        lblLugarEncuentro =(TextView)view.findViewById(R.id.lugarEncuentro);
        lblHoraEncuentro =(TextView)view.findViewById(R.id.horaEncuentro);
        lblmedioTransporte=(TextView)view.findViewById(R.id.lblmedioRegreso);
        lblhoraIda=(TextView)view.findViewById(R.id.lblHoraRegreso);
        lbltiempoAproximado=(TextView)view.findViewById(R.id.lblTiempoAproximadoRegreso);
        lbldestinoRegreso=(TextView)view.findViewById(R.id.lblDestinoRegreso);
        medioTransporteSeleccionado=(LinearLayout)view.findViewById(R.id.medioTransporteSeleccionado);
        medioNOSeleccionado=(LinearLayout)view.findViewById(R.id.medioTransporteNoSeleccionado);
        lblInvitados=(TextView)view.findViewById(R.id.lblInvitados);
        btnTimer=(ImageButton)view.findViewById(R.id.btnTimerRegreso);
    }
}
