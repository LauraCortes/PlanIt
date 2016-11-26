package com.example.laura.planit.Activities.Eventos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Laura on 25/11/2016.
 */

public class RegresoRowViewHolder extends RecyclerView.ViewHolder {
    public TextView lblmedioTransporte;
    public TextView lblCupos;
    public TextView lblHoraRegreso;
    public TextView lblDestino;
    public TextView lblTiempo;
    public TextView lblNombreDuenio;
    public TextView lblCelularDuenio;
    public Button btnRegistrar;

    public RegresoRowViewHolder(View view)
    {
        super(view);
        lblmedioTransporte =(TextView)view.findViewById(R.id.lbl_medio_regreso);
        lblCupos =(TextView)view.findViewById(R.id.lbl_cupos_regreso);
        lblHoraRegreso =(TextView)view.findViewById(R.id.lbl_hora_regreso);
        lblDestino =(TextView)view.findViewById(R.id.lbl_destino_regreso);
        lblTiempo =(TextView)view.findViewById(R.id.lbl_tiempo_regreso);
        lblNombreDuenio = (TextView)view.findViewById(R.id.lbl_duenio_regreso);
        lblCelularDuenio=(TextView)view.findViewById(R.id.lbl_celular_duenio_regreso);
        btnRegistrar =(Button)view.findViewById(R.id.btn_unir_regreso);
    }

}
