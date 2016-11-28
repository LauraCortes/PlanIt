package com.example.laura.planit.Activities.SoyContacto;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 06/11/2016.
 */

public class SoyContactoRowViewHolder extends ElementoRowViewHolder
{
    TextView textViewTitle;
    TextView textViewSubtitle;
    ImageView circuloIniciales;
    Drawable imgCirculo;

    public SoyContactoRowViewHolder(View itemView) {
        super(itemView);
        textViewTitle=((TextView) itemView.findViewById(R.id.textViewNombreContacto));
        textViewSubtitle=((TextView) itemView.findViewById(R.id.textViewTelefonoContacto));
        circuloIniciales=(ImageView)itemView.findViewById(R.id.circulo_iniciales_contacto);
    }

}
