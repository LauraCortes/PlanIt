package com.example.laura.planit.Activities.Sitios;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Usuario on 16/09/2016.
 */
public class SitioRecyclerViewAdapter extends RecyclerView.Adapter<SitioRowViewHolder> {
    Context context;
    List<Sitio> sitios;
    SitioRecyclerViewAdapter recycler;

    public SitioRecyclerViewAdapter(Context context, List<Sitio> sitios)
    {
        recycler=this;
        this.context = context;
        this.sitios = sitios;
    }


    @Override
    public int getItemCount() {
        if (sitios == null) {
            return 0;
        } else {
            return sitios.size();
        }
    }

    @Override
    public SitioRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_sitio, null);
        return  new SitioRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SitioRowViewHolder rowViewHolder, final int position) {
        final Sitio sitio = this.sitios.get(position);
        rowViewHolder.nombreTextView.setText(String.valueOf(sitio.getNombre()));
        rowViewHolder.barrioTextView.setText(String.valueOf(sitio.getBarrio()));
        rowViewHolder.direccionTextView.setText(String.valueOf(sitio.getDirección()));
        rowViewHolder.imagenView.setBackground(ContextCompat.getDrawable(context,R.drawable.home));
        rowViewHolder.vista.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(sitio.getNombre());
                        builder.setItems(new CharSequence[]{"editar", "eliminar"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent i = new Intent(context, AgregarSitioActivity.class);
                                    i.putExtra("editar",true);
                                    i.putExtra("titulo","Editar sitio favorito");
                                    i.putExtra("posicion", position);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }
                                else
                                {
                                    PlanIt.darInstancia().eliminarSitio(position);
                                    recycler.notifyDataSetChanged();
                                }
                            }
                        });
                        builder.show();
                        return true;
                    }
                }
        );

    }
}
