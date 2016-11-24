package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 23/11/2016.
 */
public class DetallesEventoActivity extends AppCompatActivity
{
    private Evento evento;
    private String id_evento;
    private String celular;

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
                celular = properties.getString(getString(R.string.usuario), "desconocido");
                if(!celular.equals(Constants.DESCONOCIDO))
                {
                    leerEventoDB();
                }
                else
                {
                    MainActivity.mostrarMensaje(contexto,"Error","No se pudo tener acceso a sus datos." +
                            " Intente cerrar sesión e ingresar nuevamente");
                }
            }

        }
    }


    private void leerEventoDB()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_EVENTOS + id_evento);
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        evento = dataSnapshot.getValue(Evento.class);
                        bind();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void bind()
    {
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

}
