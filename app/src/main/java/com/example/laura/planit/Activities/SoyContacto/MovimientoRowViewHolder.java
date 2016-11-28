package com.example.laura.planit.Activities.SoyContacto;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laura.planit.R;

/**
 * Created by Laura on 25/11/2016.
 */

public class MovimientoRowViewHolder extends RecyclerView.ViewHolder {
    public TextView lblDescripcion;
    public TextView lblHoraMovimiento;
    public TextView lblNombreEvento;

    public MovimientoRowViewHolder(View view)
    {
        super(view);
        lblDescripcion =(TextView)view.findViewById(R.id.lbl_descripcion);
        lblHoraMovimiento =(TextView)view.findViewById(R.id.lbl_hora_movimiento);
        lblNombreEvento =(TextView)view.findViewById(R.id.lbl_nombre_evento);

    }

}
