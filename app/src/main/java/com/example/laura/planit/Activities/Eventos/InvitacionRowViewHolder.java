package com.example.laura.planit.Activities.Eventos;

import android.view.View;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 16/09/2016.
 */
public class InvitacionRowViewHolder extends ElementoRowViewHolder
{

    TextView lblnombre;
    TextView lblFechaEvento;
    TextView lblHoraEncuentro;
    TextView lblOrganizador;

    public InvitacionRowViewHolder(View view) {
        super(view);
        lblnombre =(TextView)view.findViewById(R.id.nombreEvento);
        lblHoraEncuentro =(TextView)view.findViewById(R.id.horaEncuentro);
        lblFechaEvento =(TextView)view.findViewById(R.id.fechaEvento);
        lblOrganizador =(TextView)view.findViewById(R.id.lblOrganizador);
    }
}
