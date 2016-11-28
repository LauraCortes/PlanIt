package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.Constants;
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

public class RegresoRecyclerViewAdapter extends RecyclerView.Adapter<RegresoRowViewHolder> {

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private String id_evento;
    private Context context;
    private List<Regreso> elementos;

    public void swapData(List nuevosElementos)
    {
        elementos=nuevosElementos;
    }

    public RegresoRecyclerViewAdapter(Context context, List<Regreso> elementos) {

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
    public RegresoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.regreso_row, null);
        return new RegresoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RegresoRowViewHolder holder, final int position)
    {
        final RegresoRowViewHolder rowViewHolder = (RegresoRowViewHolder) holder;
        final Regreso regreso = (Regreso) this.elementos.get(position);
        id_evento=regreso.id_evento;
        rowViewHolder.lblmedioTransporte.setText(regreso.getMedioRegreso());
        rowViewHolder.lblCupos.setText(String.valueOf(regreso.getCupos()));
        rowViewHolder.lblHoraRegreso.setText(timeFormatter.format(regreso.getHoraRegreso()));
        rowViewHolder.lblDestino.setText(regreso.getDestino().toString());
        rowViewHolder.lblTiempo.setText(String.valueOf(regreso.getTiempoEstimado()));
        rowViewHolder.lblCelularDuenio.setText(regreso.getNumeroDueño());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("usuarios/"+regreso.getNumeroDueño());
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        if(usuario!=null)
                            rowViewHolder.lblNombreDuenio.setText(usuario.getNombre());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        rowViewHolder.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regreso!=null) {
                    SharedPreferences properties = context.getSharedPreferences(context.getString(R.string.properties), Context.MODE_PRIVATE);
                    String celular;
                    if (properties.getBoolean(context.getString(R.string.logueado), false))
                    {
                        celular = properties.getString(context.getString(R.string.usuario), "desconocido");
                        regreso.agregarCompartido(celular);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(regreso.darRutaElemento(id_evento));
                        database.getReferenceFromUrl(Constants.FIREBASE_URL+Constants.URL_PARTICIPANTES_EVENTO).child(id_evento).child(celular).child("regreso").setValue(celular);
                        databaseReference.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        databaseReference.setValue(regreso);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
                    }

                }
            }});
        DatabaseReference databaseReference2 = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("regresos/"+id_evento);
        databaseReference2.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Regreso actual = dataSnapshot.getValue(Regreso.class);
                        List<String> agregados=actual.getCompartido();
                        if(agregados!=null){
                            for(int i = 0;i<agregados.size();i++)
                            {
                                SharedPreferences properties = context.getSharedPreferences(context.getString(R.string.properties), Context.MODE_PRIVATE);
                                if (properties.getBoolean(context.getString(R.string.logueado), false))
                                {
                                    String celular = properties.getString(context.getString(R.string.usuario), "desconocido");
                                    if(agregados.get(i).equals(celular))
                                    {
                                        rowViewHolder.btnRegistrar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        if(actual.getCupos()==0)
                            rowViewHolder.btnRegistrar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s)
                    {
                        Regreso actual = dataSnapshot.getValue(Regreso.class);
                        List<String> agregados=actual.getCompartido();
                        if(agregados!=null){
                            for(int i = 0;i<agregados.size();i++)
                            {
                                SharedPreferences properties = context.getSharedPreferences(context.getString(R.string.properties), Context.MODE_PRIVATE);
                                if (properties.getBoolean(context.getString(R.string.logueado), false))
                                {
                                    String celular = properties.getString(context.getString(R.string.usuario), "desconocido");
                                    if(agregados.get(i).equals(celular))
                                    {
                                        rowViewHolder.btnRegistrar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        if(actual.getCupos()==0)
                            rowViewHolder.btnRegistrar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
