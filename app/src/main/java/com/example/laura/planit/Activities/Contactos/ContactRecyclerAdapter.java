package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.LoginActivity;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Usuario on 06/11/2016.
 */

public class ContactRecyclerAdapter extends ElementRecyclerViewAdapter
{
    private Context context;
    public ContactRecyclerAdapter(Context context, List<Contacto> elementos, TabFragment tabFragment)
    {
        super(context, elementos, tabFragment);
        this.context=context;
    }
    public void swapData(List contactos)
    {
        elementos=contactos;
    }
    @Override
    public ElementoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_contactos_row_contacto, null);
        return  new ContactoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ElementoRowViewHolder elementoRowViewHolder, final int position)
    {
        final Contacto contacto = (Contacto)elementos.get(position);
        String nombre = contacto.getNombre();
        final ContactoRowViewHolder contactoRowViewHolder = (ContactoRowViewHolder)elementoRowViewHolder;
        contactoRowViewHolder.textViewTitle.setText(nombre);
        contactoRowViewHolder.textViewSubtitle.setText(contacto.getNumeroTelefonico());
        Drawable iniciales =darCirculoIniciales(nombre,position);
        contactoRowViewHolder.imgCirculo=iniciales;
        contactoRowViewHolder.circuloIniciales.setImageDrawable(iniciales);
        contactoRowViewHolder.decorarFavorito(contacto.isFavorito()==1);
        contactoRowViewHolder.decorarSeleccionado(tabFragment.isItemSelected(position));
        final ContactoRowViewHolder finalContactoRowViewHolder = contactoRowViewHolder;

        //Marcar contacto como Favorito
        contactoRowViewHolder.favorito.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Contacto contactoFavorito = (Contacto)elementos.get(position);
                SharedPreferences properties = context.getSharedPreferences(context.getResources().getString(R.string.properties), Context.MODE_PRIVATE);
                String celular = properties.getString(context.getResources().getString(R.string.usuario), "desconocido");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(contacto.darRutaElemento(celular));
                databaseReference.keepSynced(true);
                if(contactoFavorito.isFavorito()==0)
                {
                    finalContactoRowViewHolder.decorarFavorito(true);
                    contactoFavorito.setFavorito(1);
                    databaseReference.child("favorito").setValue(1);
                }
                else
                {
                    finalContactoRowViewHolder.decorarFavorito(false);
                    contactoFavorito.setFavorito(0);
                    databaseReference.child("favorito").setValue(0);
                }
            }
        });

        contactoRowViewHolder.vista.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(tabFragment.hayItemsSeleccionados())
                {
                    seleccionarItem(position,contactoRowViewHolder);
                }
            }
        });

        contactoRowViewHolder.vista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                seleccionarItem(position,contactoRowViewHolder);
                //Retorna que sí lo usó->No invoca el clickListener
                return true;
            }
        });

    }
}
