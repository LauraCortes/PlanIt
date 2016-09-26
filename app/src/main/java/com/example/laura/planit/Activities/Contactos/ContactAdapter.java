package com.example.laura.planit.Activities.Contactos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.util.List;

/**
 * Created by Laura on 18/09/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contacto> {
    private LayoutInflater layoutInflater;

    private Context context;
    public ContactAdapter(Context context, List<Contacto> contactos) {
        super(context, 0, contactos);
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // holder pattern
        Holder holder = null;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.row_contacto, null);
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.textViewNombre));
            holder.setTextViewSubtitle((TextView) convertView.findViewById(R.id.textViewTelefono));
            holder.setFavorito((ImageButton) convertView.findViewById(R.id.imageContactoFavorito));
            holder.circuloIniciales=(ImageView)convertView.findViewById(R.id.circulo_iniciales);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Contacto contacto = getItem(position);
        String nombre = contacto.getNombre();
        holder.getTextViewTitle().setText(nombre);
        holder.getTextViewSubtitle().setText(contacto.getNumeroTelefonico());
        String[] separaciones = nombre.trim().split(" ");
        String iniciales="";
        for(int i=0; i<separaciones.length && i<2;i++)
        {
            if(separaciones[i].trim().length()>0)
            {
                iniciales+=separaciones[i].trim().charAt(0);
            }
        }
        holder.circuloIniciales.setImageDrawable(
                TextDrawable.builder().buildRound(iniciales.toUpperCase(), holder.colores[position%5]));

        holder.decorarFavorito(contacto.isSelected()==1);
        final Holder finalHolder = holder;
        holder.getFavorito().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(PlanIt.darInstancia().darContacto(position).isSelected()==0)
                {
                    finalHolder.decorarFavorito(true);
                    PlanIt.darInstancia().marcarFavorito(position, 1);
                    Intent intent = new Intent(context, PersitenciaService.class);
                    intent.putExtra("Requerimiento","MarcarContacto");
                    intent.putExtra("Contacto",PlanIt.darInstancia().darContacto(position));
                    intent.putExtra("Favoritos",1);
                    context.startService(intent);
                }
                else
                {
                    finalHolder.decorarFavorito(false);
                    PlanIt.darInstancia().marcarFavorito(position, 0);
                    Intent intent = new Intent(context, PersitenciaService.class);
                    intent.putExtra("Requerimiento","MarcarContacto");
                    intent.putExtra("Contacto",PlanIt.darInstancia().darContacto(position));
                    intent.putExtra("Favoritos",0);
                    context.startService(intent);
                }
            }
        });

        return convertView;
    }


    static class Holder
    {
        TextView textViewTitle;
        TextView textViewSubtitle;
        ImageButton favorito;
        ImageView circuloIniciales;
        int[] colores = new int[]{Color.parseColor("#26532B"),Color.parseColor("#6A0136"),
                Color.parseColor("#D72638"),Color.parseColor("#95C623"),Color.parseColor("#080708")};


        public TextView getTextViewTitle()
        {
            return textViewTitle;
        }

        public void setTextViewTitle(TextView textViewTitle)
        {
            this.textViewTitle = textViewTitle;
        }

        public TextView getTextViewSubtitle()
        {
            return textViewSubtitle;
        }

        public void setTextViewSubtitle(TextView textViewSubtitle)
        {
            this.textViewSubtitle = textViewSubtitle;
        }

        public ImageButton getFavorito() {
            return favorito;
        }

        public void setFavorito(ImageButton favorito) {
            this.favorito = favorito;
        }

        public void decorarFavorito(boolean isFavorito)
        {
            if(isFavorito)
            {
                favorito.setBackgroundResource(R.drawable.estrella_verde);
                favorito.setAlpha((float) 1.0);
            }
            else
            {
                favorito.setBackgroundResource(R.drawable.estrella_gris);
                favorito.setAlpha((float) 0.5);
            }
        }
    }
}
