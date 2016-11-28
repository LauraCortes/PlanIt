package com.example.laura.planit.Activities.SoyContacto;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Eventos.RegresoRowViewHolder;
import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Modelos.Movimiento;
import com.example.laura.planit.Modelos.Regreso;
import com.example.laura.planit.Modelos.Usuario;
import com.example.laura.planit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Laura on 25/11/2016.
 */

public class MovimientoRecyclerViewAdapter extends RecyclerView.Adapter<MovimientoRowViewHolder> {

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private String id_evento;
    private Context context;
    private List<Movimiento> elementos;

    public void swapData(List nuevosElementos)
    {
        elementos=nuevosElementos;
    }

    public MovimientoRecyclerViewAdapter(Context context, List<Movimiento> elementos) {

        this.context=context;
        this.elementos=elementos;
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
    }

    @Override
    public int getItemCount() {
        if (elementos == null) {
            return 0;
        } else {
            return elementos.size();
        }
    }

    @Override
    public MovimientoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movimiento_row, null);
        return new MovimientoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovimientoRowViewHolder holder, final int position)
    {
        final MovimientoRowViewHolder rowViewHolder = (MovimientoRowViewHolder) holder;
        final Movimiento movimiento = (Movimiento) this.elementos.get(position);
        id_evento=movimiento.id_evento;
        rowViewHolder.lblDescripcion.setText(movimiento.getDescripcion());
        rowViewHolder.lblNombreEvento.setText(movimiento.getNombre_evento());
        rowViewHolder.lblHoraMovimiento.setText(timeFormatter.format(movimiento.getHora_movimiento()));

    }
}
