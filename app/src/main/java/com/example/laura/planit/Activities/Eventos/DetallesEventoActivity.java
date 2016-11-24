package com.example.laura.planit.Activities.Eventos;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Usuario on 23/11/2016.
 */
public class DetallesEventoActivity extends AppCompatActivity
{
    //Atributos de la interfaz
    private TextView lblNombre, lblDetalles, lblInvitados, lblHora, lblFecha, lblLugar;

    //Atributos de regreso;
    private TextView lblRegresoNoDefinido,lblRegresoLugar, lblRegresoHora, lblRegresoTiempo, lblRegresoMedio, lblDuenioRegreso, lblCelularDuenio;
    private LinearLayout layoutDetallesRegreso, layoutBtnCompartir;
    private Button btnAceptar, btnVerInvitados, btnSeleccionarRegreso, btnCrearRegreso;
}
