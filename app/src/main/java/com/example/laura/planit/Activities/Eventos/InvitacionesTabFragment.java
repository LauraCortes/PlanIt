package com.example.laura.planit.Activities.Eventos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.LoginActivity;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.R;
import com.example.laura.planit.Activities.Main.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Usuario on 02/11/2016.
 */

public class InvitacionesTabFragment extends TabFragment
{
    public final static int NOTIFICACION_INVITACION=923;

    public InvitacionesTabFragment()
    {
        super();
    }

    private int invitacionesIniciales;
    private int invitacionActual;
    private boolean cargueInicial=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        SharedPreferences properties = getContext().getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            celular = properties.getString(getString(R.string.usuario), "desconocido");
        } else {
            MainActivity.mostrarMensaje(getContext(), "Inicia sesión", "Tu sesión caducó, por favor vuelve a iniciar sesión");
            getActivity().finish();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
        }

        elementosSeleccionados = new ArrayList();
        super.onCreate(savedInstanceState);
        elementos= new ArrayList();
        adapter=new InvitacionRecyclerViewAdapter(getActivity(), (List<ResumenEvento>)elementos,this);

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.tab_invitaciones, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewInvitacionesEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabEliminarInvitacion);
        btnFAB.setVisibility(View.GONE);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionFAB(v);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_INVITACIONES_EVENTO + celular);
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("fecha");
        invitacionActual=0;

        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        GenericTypeIndicator<HashMap<String,ResumenEvento>> t = new GenericTypeIndicator<HashMap<String, ResumenEvento>>(){};
                        HashMap<String, ResumenEvento> map =dataSnapshot.getValue(t);
                        if(map!=null)
                        {
                            ArrayList<ResumenEvento> nuevos = new ArrayList(map.values());
                            elementos=nuevos;
                            ((ElementRecyclerViewAdapter)adapter).swapData(nuevos);
                            adapter.notifyDataSetChanged();
                            cargueInicial=true;
                            invitacionesIniciales=nuevos.size();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(cargueInicial)
                        {
                            GenericTypeIndicator<HashMap<String,ResumenEvento>> t = new GenericTypeIndicator<HashMap<String, ResumenEvento>>(){};
                            HashMap<String, ResumenEvento> map =dataSnapshot.getValue(t);
                            if(map!=null)
                            {
                                ArrayList<ResumenEvento> nuevos = new ArrayList(map.values());
                                elementos=nuevos;
                                ((ElementRecyclerViewAdapter)adapter).swapData(nuevos);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        //Crea los callbacks una vez ya se ha leído toda la información

        databaseReference.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        invitacionActual++;
                        if(invitacionActual>invitacionesIniciales)
                        {
                            ResumenEvento evento = dataSnapshot.getValue(ResumenEvento.class);
                            Intent resultIntent = new Intent(getActivity(), DetallesEventoActivity.class);
                            resultIntent.putExtra(Constants.EXTRA_ID_EVENTO, evento.getId_evento());
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
// Adds the back stack for the Intent (but not the Intent itself)
                            stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            Notification notificacion = new NotificationCompat.Builder(getContext())
                                    .setSmallIcon(R.drawable.logo_planit)
                                    .setContentTitle(evento.getNombre())
                                    .setContentText(evento.getOrganizador()+" te ha invitado")
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .setAutoCancel(true)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setContentIntent(resultPendingIntent)
                                    .build();

                            NotificationManager mNotifyMgr=(NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
                            //Tono de notificación
                            notificacion.sound= Uri.parse("android.resource://"+ getContext().getPackageName() + "/" + R.raw.notificacion_invitacion);
                            //Vibración
                            long[] vibrate = { 0, 200, 50, 200,100,200 };
                            notificacion.vibrate=vibrate;
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(NOTIFICACION_INVITACION, notificacion);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
                }
        );


        return view;
    }

    @Override
    protected void lanzarActivityAgregarElemento(View view)
    {
        //Esta acción nunca se lanzará
    }

    public void cambiarIconoFAB()
    {
        super.cambiarIconoFAB();
        if(elementosSeleccionados.size()!=0)
        {
            btnFAB.setVisibility(View.VISIBLE);
        }
        else
        {
            btnFAB.setVisibility(View.GONE);
        }

    }



}
