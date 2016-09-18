package com.example.laura.planit.Activities.Eventos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.R;

/**
 * Created by Usuario on 16/09/2016.
 */
public class EventoRowViewHolder extends RecyclerView.ViewHolder {

    TextView nombreTextView;
    TextView descripcionTextView;
    TextView fechaTextView;
    TextView lugarEventoTextView;
    TextView lugarEncuentroTextView;
    TextView horaEncuentroTextView;
    TextView medioTransporte;
    TextView horaIda;
    TextView tiempoAproximado;

    View vista;

    public EventoRowViewHolder(View view) {
        super(view);
        vista=view;
        nombreTextView=(TextView)view.findViewById(R.id.nombreEvento);
        descripcionTextView=(TextView)view.findViewById(R.id.descripcionEvento);
        fechaTextView=(TextView)view.findViewById(R.id.fechaEvento);
        lugarEventoTextView=(TextView)view.findViewById(R.id.lugarEvento);
        lugarEncuentroTextView=(TextView)view.findViewById(R.id.lugarEncuentro);
        horaEncuentroTextView=(TextView)view.findViewById(R.id.horaEncuentro);
    }
}
