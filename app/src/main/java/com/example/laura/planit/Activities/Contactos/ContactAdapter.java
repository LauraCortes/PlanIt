package com.example.laura.planit.Activities.Contactos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Contacto row = getItem(position);
        holder.getTextViewTitle().setText(row.getNombre());
        holder.getTextViewSubtitle().setText(row.getNumeroTelefonico());

        if(row.isSelected()==1)
        {
            holder.getFavorito().setBackgroundResource(android.R.drawable.btn_star_big_on);
        }
        holder.getFavorito().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PlanIt.darInstancia().darContacto(position).isSelected()==0) {
                    view.setBackgroundResource(android.R.drawable.btn_star_big_on);
                    PlanIt.darInstancia().marcarFavorito(position, 1);
                    Intent intent = new Intent(context, PersitenciaService.class);
                    intent.putExtra("Requerimiento","MarcarContacto");
                    intent.putExtra("Contacto",PlanIt.darInstancia().darContacto(position));
                    intent.putExtra("Favoritos",1);
                    context.startService(intent);
                }
                else
                {
                    view.setBackgroundResource(android.R.drawable.btn_star_big_off);
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
    }
}
