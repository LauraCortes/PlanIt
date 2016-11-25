package com.example.laura.planit.Activities.Eventos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laura.planit.Activities.Contactos.AgregarContactoActivity;
import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Activities.Transportes.AgregarTransporteActivity;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.OpcionSondeo;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.Modelos.SondeoLugares;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 23/11/2016.
 */
public class DetallesEventoActivity extends AppCompatActivity
{
    private Evento evento;
    private String id_evento;
    private String celular;

    private List<OpcionSondeo> opcionesVotacion=new ArrayList<>();
    private OpcionSondeo votada;

    Context contexto;

    //Atributos de la interfaz
    private TextView lblNombre, lblDescripcion, lblInvitados, lblHora, lblFecha, lblLugar;

    //Atributos de regreso;
    private TextView lblRegresoNoDefinido,lblRegresoLugar, lblRegresoHora, lblRegresoTiempo,
            lblRegresoMedio, lblDuenioRegreso, lblCelularDuenio;
    private LinearLayout layoutDetallesRegreso;
    private Button btnAceptar, btnCamino, btnVerInvitados, btnSeleccionarRegreso, btnCrearRegreso, bnComparten, btnVotar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto = this;
        setContentView(R.layout.activity_detalles_evento);

        //ToolBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_detalles_evento));
        getSupportActionBar().setTitle("Detalles del evento");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(contexto, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        lblNombre = (TextView)findViewById(R.id.detalles_nombre_evento);
        lblDescripcion= (TextView)findViewById(R.id.detalles_descripcion_evento);
        lblInvitados = (TextView)findViewById(R.id.detalles_cantidad_invitados);
        lblHora= (TextView)findViewById(R.id.detalles_hora_evento);
        lblFecha= (TextView)findViewById(R.id.detalles_fecha_evento);
        lblLugar= (TextView)findViewById(R.id.detalles_lugar_evento);

        layoutDetallesRegreso = (LinearLayout)findViewById(R.id.detalles_detalles_regreso);

        lblRegresoNoDefinido= (TextView)findViewById(R.id.lbl_detalles_reg_no_def);
        lblRegresoLugar= (TextView)findViewById(R.id.lbl_detalles_destino_regreso);
        lblRegresoHora=(TextView)findViewById(R.id.lbl_detalles_hora_regreso);
        lblRegresoTiempo= (TextView)findViewById(R.id.lbl_detalles_tiempo_regreso);
        lblRegresoMedio= (TextView)findViewById(R.id.lbl_detalles_medio_regreso);
        lblDuenioRegreso= (TextView)findViewById(R.id.lbl_detalles_duenio_regreso);
        lblCelularDuenio= (TextView)findViewById(R.id.lbl_detalles_celular_duenio_regreso);

        btnAceptar=(Button)findViewById(R.id.btn_detalles_aceptar);
        btnVerInvitados=(Button)findViewById(R.id.btn_detalles_ver_invitados);
        btnSeleccionarRegreso=(Button)findViewById(R.id.btn_detalles_seleccionar_regreso);
        btnCrearRegreso=(Button)findViewById(R.id.btn_detalles_crear_regreso);
        btnCrearRegreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActivityAgregarRegreso(v);
            }
        });
        bnComparten=(Button)findViewById(R.id.btn_detalles_compartido);
        btnVotar =(Button)findViewById(R.id.btn_votar);
        btnCamino=(Button)findViewById(R.id.btn_detalles_camino);

        Intent intent = getIntent();
        id_evento = intent.getExtras().getString(Constants.EXTRA_ID_EVENTO);
        if(id_evento==null)
        {
            finish();
            //No debería suceder
        }
        else
        {
            SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
            if (properties.getBoolean(getString(R.string.logueado), false))
            {
                celular = properties.getString(getString(R.string.usuario), Constants.DESCONOCIDO);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!celular.equals(Constants.DESCONOCIDO))
        {
            leerEventoDB();
            prepararVotacion();
        }
        else
        {
            MainActivity.mostrarMensaje(contexto,"Error","No se pudo tener acceso a sus datos." +
                    " Intente cerrar sesión e ingresar nuevamente");
        }
    }

    private void leerEventoDB()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_EVENTOS + id_evento);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            Evento eventoRecibido = dataSnapshot.getValue(Evento.class);
                            if((evento!=null && ( (evento.lugar==null && eventoRecibido.lugar!=null) || (evento.lugar!=null && !evento.lugar.toString().equals(eventoRecibido.lugar.toString())))))
                            {
                                //TODO Notificación cambio lugar
                                Intent resultIntent = new Intent(contexto, DetallesEventoActivity.class);
                                resultIntent.putExtra(Constants.EXTRA_ID_EVENTO, id_evento);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(contexto);
// Adds the back stack for the Intent (but not the Intent itself)
                                stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                                0,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );

                                Notification notificacion = new NotificationCompat.Builder(contexto)
                                        .setSmallIcon(R.drawable.logo_planit)
                                        .setContentTitle(evento.getNombre())
                                        .setContentText("El lugar del evento ha cambiado")
                                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                        .setAutoCancel(true)
                                        .setPriority(Notification.PRIORITY_MAX)
                                        .setContentIntent(resultPendingIntent)
                                        .build();

                                NotificationManager mNotifyMgr=(NotificationManager)contexto.getSystemService(NOTIFICATION_SERVICE);
                                //Tono de notificación
                                notificacion.sound= Uri.parse("android.resource://"+ contexto.getPackageName() + "/" + R.raw.notificacion_invitacion);
                                //Vibración
                                long[] vibrate = { 0, 200, 50, 200,100,200 };
                                notificacion.vibrate=vibrate;
                                // Builds the notification and issues it.
                                int id_not_sitio=193;
                                mNotifyMgr.notify(id_not_sitio, notificacion);
                            }
                            evento = eventoRecibido;
                            bind();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void bind()
    {
        FrameLayout cargando = (FrameLayout)findViewById(R.id.imgn_cargando);
        cargando.setVisibility(View.GONE);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

        lblNombre.setText(evento.getNombre());
        lblDescripcion.setText(evento.getDescripcion());
        lblInvitados.setText(""+evento.getCantidad_invitados());
        lblHora.setText(timeFormatter.format(evento.getFecha()));
        lblFecha.setText(dateFormatter.format(evento.getFecha()));
        Sitio sitioActual = evento.getLugar();
        if(sitioActual!=null)
        {
            lblLugar.setText(sitioActual.toString());
        }
        else
        {
            lblLugar.setText("( No definido aún )");
        }
        if(evento.isLugar_fijo()||evento.isLugar_definitivo())
        {
            btnVotar.setVisibility(View.GONE);
        }
        else
        {
            btnVotar.setVisibility(View.VISIBLE);
        }
        if(evento.getCantidad_invitados()>2)
        {
            btnVerInvitados.setVisibility(View.VISIBLE);
        }
        else {
            btnVerInvitados.setVisibility(View.GONE);
        }
    }


    public void regresar(View v)
    {
        onBackPressed();
    }

    protected void lanzarActivityAgregarRegreso(View view)
    {
        Intent intent = new Intent(contexto, AgregarTransporteActivity.class);
        startActivity(intent);
    }

    private void prepararVotacion()
    {
        final DatabaseReference refVotacion = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_SONDEOS+id_evento);
        refVotacion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    SondeoLugares sondeo = dataSnapshot.getValue(SondeoLugares.class);
                    if(sondeo!=null)
                    {
                        Collection<OpcionSondeo> opcionesCollection = sondeo.getOpciones().values();
                        opcionesVotacion=new ArrayList<OpcionSondeo>();
                        int i=0;
                        for(OpcionSondeo opcion : opcionesCollection)
                        {
                            opcionesVotacion.add(opcion);
                            i++;
                        }

                        if(sondeo.isCerrado() ||  (sondeo.getVotaron()!=null && sondeo.getVotaron().containsValue(celular)))
                        {
                            btnVotar.setVisibility(View.GONE);
                        }
                        else
                        {
                            btnVotar.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        btnVotar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void votar(View v)
    {
        String[] opciones = new String[opcionesVotacion.size()];
        int i=0;
        for(OpcionSondeo opcionActual:opcionesVotacion)
        {
            opciones[i]=opcionActual.toString();
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Vota por el lugar del evento");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancelar", null);
        builder.setItems(opciones,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        votada = opcionesVotacion.get(which);
                        ejecutarVoto();
                        System.out.println("Votada -> "+votada.getLugar().toString());
                        //btnVotar.setVisibility(View.GONE);
                        //Debería sobrar pues se actualiza con el otro listener
                    }
                });
        builder.show();
    }

    private void ejecutarVoto()
    {
        final DatabaseReference refVotacion = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_SONDEOS+id_evento);
        refVotacion.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                SondeoLugares sondeo = mutableData.getValue(SondeoLugares.class);
                if (sondeo == null) {
                    return Transaction.success(mutableData);
                }
                else
                {
                    sondeo.votosActuales++;
                    System.out.println("******cuántas opciones en ejecutar voto"+    sondeo.getOpciones().size());
                    Collection<OpcionSondeo> preOpciones = sondeo.getOpciones().values();
                    for(OpcionSondeo opcionDB : preOpciones)
                    {
                        System.out.println(opcionDB.getLugar().toString());
                    }
                    System.out.println("----------FIN CICLO PRE");

                    for(OpcionSondeo opcionDB : sondeo.getOpciones().values())
                    {
                        System.out.println("**--Actual->"+opcionDB.lugar.toString());
                        if(opcionDB.lugar.toString().equals(votada.getLugar().toString()))
                        {
                            System.out.println("------------------------ENCONTRÓ UNA OPCIÓN IGUAL");
                            opcionDB.votosFavor++;
                            boolean ganadora=true;
                            for(OpcionSondeo otraOpcion : sondeo.getOpciones().values())
                            {
                                //Determina si es la que gana
                                if(otraOpcion!=opcionDB && !(opcionDB.votosFavor>=(otraOpcion.votosFavor+(sondeo.cantidadVotantes-sondeo.votosActuales))))
                                {
                                    ganadora=false;
                                    break;
                                }
                            }
                            sondeo.cerrado=ganadora;

                            DatabaseReference refEvento =  FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL)
                                    .child(Constants.URL_EVENTOS+id_evento);
                            //Si es la ganadora pone el sitio en el evento
                            if(ganadora)
                            {
                                refEvento.child("lugar_definitivo").setValue(true);
                                refEvento.child("lugar").setValue(votada.getLugar());
                            }
                            else
                            {
                                //Pone el de mayor votación
                                OpcionSondeo mayorVotacion=null;
                                for(OpcionSondeo actual:sondeo.getOpciones().values())
                                {
                                    if(mayorVotacion==null)
                                    {
                                        mayorVotacion=actual;
                                    }
                                    else if(actual.votosFavor>actual.votosFavor)
                                    {
                                        mayorVotacion=actual;
                                    }
                                }
                                refEvento.child("lugar").setValue(mayorVotacion.lugar);
                            }
                            sondeo.votaron.put(celular,celular);
                            break;
                        }
                    }
                }
                mutableData.setValue(sondeo);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(databaseError!=null)
                {
                    databaseError.toException().printStackTrace();
                }
            }
        });
    }



}
