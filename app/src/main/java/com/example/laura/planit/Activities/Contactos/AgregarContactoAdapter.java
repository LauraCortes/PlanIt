package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.Activities.Eventos.AgregarSuper;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoAdapter extends ArrayAdapter<Contacto> {

    private LayoutInflater layoutInflater;
    AgregarSuper activity;


    public AgregarContactoAdapter(Context context, List contactos)
    {
        super(context, 0, contactos);
        activity = (AgregarSuper) context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // holder pattern
        HolderContactoAgregar holderContactoAgregar = null;
        if (convertView == null)
        {
            holderContactoAgregar = new HolderContactoAgregar();
            convertView = layoutInflater.inflate(R.layout.activity_agregar_contacto_row, null);
            holderContactoAgregar.setTextNombre((TextView) convertView.findViewById(R.id.textViewNombreAgregar));
            holderContactoAgregar.setTextTelefono((TextView) convertView.findViewById(R.id.textViewTelefonoAgregar));
            holderContactoAgregar.setIniciales((ImageView)convertView.findViewById(R.id.iniciales));
            convertView.setTag(holderContactoAgregar);
        }
        else
        {
            holderContactoAgregar = (HolderContactoAgregar) convertView.getTag();
        }

        final Contacto contacto = getItem(position);
        holderContactoAgregar.getTextNombre().setText(contacto.getNombre());
        holderContactoAgregar.getTextTelefono().setText(contacto.getNumeroTelefonico());
        int colorFondo = activity.estaSeleccionado(position)?getContext().getResources().getColor(R.color.colorSelectedRow):Color.TRANSPARENT;
        Drawable imgn = activity.estaSeleccionado(position)?getContext().getResources().getDrawable(R.drawable.check_border_48):ElementRecyclerViewAdapter.darCirculoIniciales(contacto.getNombre(),position);
        holderContactoAgregar.getIniciales().setImageDrawable(imgn);
        convertView.setBackgroundColor(colorFondo);
        convertView.setClickable(true);
        final HolderContactoAgregar finalHolderContactoAgregar = holderContactoAgregar;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.seleccionarItem(position);
                int colorFondo = activity.estaSeleccionado(position)?ContextCompat.getColor(getContext(),R.color.colorSelectedRow):Color.TRANSPARENT;
                Drawable imgn = activity.estaSeleccionado(position)? ContextCompat.getDrawable(v.getContext(),R.drawable.check_border_48):ElementRecyclerViewAdapter.darCirculoIniciales(contacto.getNombre(),position);
                finalHolderContactoAgregar.getIniciales().setImageDrawable(imgn);
                v.setBackgroundColor(colorFondo);
            }
        });

        return convertView;
    }

    static class HolderContactoAgregar
    {
        TextView textNombre;
        TextView textTelefono;
        ImageView iniciales;

        public TextView getTextNombre()
        {
            return textNombre;
        }

        public void setTextNombre(TextView textNombre)
        {
            this.textNombre = textNombre;
        }

        public TextView getTextTelefono()
        {
            return textTelefono;
        }

        public void setTextTelefono(TextView textTelefono)
        {
            this.textTelefono = textTelefono;
        }

        public ImageView getIniciales() {
            return iniciales;
        }

        public void setIniciales(ImageView iniciales) {
            this.iniciales = iniciales;
        }
    }
}
