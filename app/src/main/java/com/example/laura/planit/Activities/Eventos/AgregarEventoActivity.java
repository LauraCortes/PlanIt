package com.example.laura.planit.Activities.Eventos;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.ParticipanteEvento;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.Modelos.SondeoLugares;
import com.example.laura.planit.R;
import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Services.MensajesService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Atributos interfaz
    EditText txtNombre, txtDescripcion;
    EditText txtFechaEvento, txtHoraEvento;
    TextView lblSitio;
    TextView lblVotacion;
    CheckBox cbxVotarSitio;
    Button btnSitio;
    Button btnContinuar;

    public static int INVITAR_AMIGOS = 1;
    int CREAR_ENCUESTA_LUGARES = 23;
    int CREAR_LUGAR = 12;

    Context contexto;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private Calendar fechaEvento;
    private Sitio sitioEvento;
    private String celular;
    private String nombreOrganizador;

    private String key_evento;
    private boolean encuesta_creada;

    //Atributos para soporte a edición-----
    boolean editar;
    int pos;
    String nombre;
    boolean horaEncuentro;
    boolean horaRegreso;

    private List<Contacto> invitados;
    private List<Sitio> sitios;
    private List<Sitio> sitiosEncuesta;
    private String[] itemsSeleccionarSitio;

    private List<Contacto> invitados_no_registrados = new ArrayList<>();
    private int cantidad_invitados_no_registrados = 0;

    private Evento nuevoEvento;

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
        setContentView(R.layout.activity_agregar_evento);
        encuesta_creada = false;

        //ToolBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_agregar_evento));
        getSupportActionBar().setTitle("Agregar Evento");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        txtNombre = (EditText) findViewById(R.id.txtNombreEvento);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = (EditText) findViewById(R.id.txtFechaEvento);
        txtHoraEvento = (EditText) findViewById(R.id.txtHoraEvento);
        cbxVotarSitio = (CheckBox) findViewById(R.id.cbxVotarSitio);
        btnContinuar = (Button) findViewById(R.id.btn_continuar_Agregar_Evento);
        btnSitio = (Button) findViewById(R.id.btn_seleccionar_sitio_evento);
        lblSitio = (TextView) findViewById(R.id.lbl_sitio_evento);
        lblVotacion = (TextView) findViewById(R.id.lblVotacion);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");

        horaEncuentro = false;
        horaRegreso = false;
        contexto = this;

        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            celular = properties.getString(getString(R.string.usuario), "desconocido");
        }


        txtHoraEvento.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            definirHoraEncuentro(v);
                        }
                        return true;
                    }
                }
        );

        txtFechaEvento.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            definirFechaEncuentro(v);
                        }
                        return true;
                    }

                }
        );

        cbxVotarSitio.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        LinearLayout layout = (LinearLayout) ((View) (buttonView.getParent().getParent())).findViewById(R.id.layout_votar_sitio);
                        int colorLetra;
                        if (isChecked) {
                            layout.setVisibility(View.GONE);
                            btnContinuar.setText("Elegir sitios de votación");
                            colorLetra = ContextCompat.getColor(contexto, R.color.colorAccent);

                        } else {
                            layout.setVisibility(View.VISIBLE);
                            btnContinuar.setText("Agregar invitados");
                            colorLetra = ContextCompat.getColor(contexto, R.color.colorSecondaryText);
                        }
                        lblVotacion.setTextColor(colorLetra);
                    }
                }
        );


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_LUGARES_FAVORITOS + celular);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, Sitio>> t = new GenericTypeIndicator<HashMap<String, Sitio>>() {
                        };
                        HashMap<String, Sitio> map = dataSnapshot.getValue(t);
                        if (map != null) {
                            ArrayList<Sitio> nuevos = new ArrayList(map.values());
                            sitios = nuevos;
                            itemsSeleccionarSitio = new String[sitios.size()];
                            for (int i = 0; i < itemsSeleccionarSitio.length; i++) {
                                itemsSeleccionarSitio[i] = sitios.get(i).toString();
                            }
                        } else {
                            sitios = new ArrayList<Sitio>();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        DatabaseReference databaseReferenceInfoUsuario = database.getReferenceFromUrl(Constants.FIREBASE_URL).child((Constants.URL_USUARIOS + celular + "/nombre"));
        databaseReferenceInfoUsuario.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            nombreOrganizador = (String) dataSnapshot.getValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void definirFechaEncuentro(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog fechaEncuentroDatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        fechaEncuentroDatePickerDialog.setTitle("Fecha del evento");
        fechaEncuentroDatePickerDialog.show(getFragmentManager(), "tagDatePicker");
        horaEncuentro = true;

    }


    public void definirSitioEvento(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Selecciona el lugar");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Nuevo sitio", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentAgregarSitio = new Intent(contexto, AgregarSitioActivity.class);
                startActivityForResult(intentAgregarSitio, CREAR_LUGAR);
            }
        });
        builder.setItems(itemsSeleccionarSitio,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sitioEvento = sitios.get(which);
                        if (sitioEvento != null) {
                            btnSitio.setText("Cambiar");
                            lblSitio.setText(sitioEvento.toString());
                        }
                    }
                });
        builder.show();
    }


    public void definirHoraEncuentro(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog horaEncuentroTimePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        horaEncuentroTimePickerDialog.setTitle("Hora de encuentro");
        horaEncuentroTimePickerDialog.show(getFragmentManager(), "tagTimePicker");
        horaEncuentro = true;
    }


    public void continuar(View view) {
        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        String fechaString = txtFechaEvento.getText().toString().trim();
        String horaEncuentroString = txtHoraEvento.getText().toString().trim();

        if (!cbxVotarSitio.isChecked() && sitioEvento == null) {
            Toast.makeText(contexto, "Debe seleccionar un sitio", Toast.LENGTH_SHORT).show();
        } else if (nombre.isEmpty() || descripcion.isEmpty() || fechaString.isEmpty() || horaEncuentroString.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (fechaEvento.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(this, "La fecha debe ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
        } else {
            nuevoEvento = new Evento();
            nuevoEvento.setNombre(nombre);
            nuevoEvento.setDescripcion(descripcion);
            nuevoEvento.setFecha(fechaEvento.getTimeInMillis());
            nuevoEvento.setCelular_organizador(celular);
            nuevoEvento.setNombre_organizador(nombreOrganizador);
            boolean lugarFijo = !cbxVotarSitio.isChecked();
            nuevoEvento.setLugar_fijo(lugarFijo);
            nuevoEvento.setLugar_definitivo(lugarFijo);

            if (cbxVotarSitio.isChecked() && !encuesta_creada) {
                Intent intent = new Intent(this, AgregarEventoEncuestaLugaresActivity.class);
                startActivityForResult(intent, CREAR_ENCUESTA_LUGARES);
            } else {
                nuevoEvento.setLugar(sitioEvento);
                Intent intent = new Intent(this, AgregarEventoInvitadosActivity.class);
                startActivityForResult(intent, INVITAR_AMIGOS);
            }
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (fechaEvento == null) {
            fechaEvento = Calendar.getInstance();
        }
        fechaEvento.set(year, monthOfYear, dayOfMonth);
        txtFechaEvento.setText(dateFormatter.format(fechaEvento.getTime()));

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (fechaEvento == null) {
            fechaEvento = Calendar.getInstance();
        }
        fechaEvento.set(fechaEvento.get(Calendar.YEAR), fechaEvento.get(Calendar.MONTH),
                fechaEvento.get(Calendar.DATE), hourOfDay, minute);
        txtHoraEvento.setText(timeFormatter.format(fechaEvento.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CREAR_LUGAR) {
                //TODO
                //Lanzar popUp o seleccionar sitio
            } else {
                if (requestCode == INVITAR_AMIGOS) {
                    invitados = (List<Contacto>) data.getExtras().get("Invitados");
                } else if (requestCode == CREAR_ENCUESTA_LUGARES) {
                    sitiosEncuesta = (List<Sitio>) data.getSerializableExtra(Constants.EXTRA_SITIOS_EVENTO);
                    invitados = (List<Contacto>) data.getSerializableExtra(Constants.INVITADOS_EVENTO);
                }
                nuevoEvento.setCantidad_invitados(invitados.size() + 1);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbReference = database.getReferenceFromUrl(Constants.FIREBASE_URL);
                key_evento = dbReference.child(Constants.URL_EVENTOS).push().getKey();

                Map<String, Object> childUpdates = new HashMap<>();
                //Agrega el evento en la tabla principal
                childUpdates.put(Constants.URL_EVENTOS + key_evento, nuevoEvento.toMap());

                ResumenEvento resumen = new ResumenEvento(nuevoEvento.getNombre(), nombreOrganizador, fechaEvento.getTimeInMillis(), key_evento);
                Map resumenMap = resumen.toMap();
                //Agrega un resumen en mis Eventos
                childUpdates.put(Constants.URL_MIS_EVENTOS + celular + "/" + key_evento, resumenMap);

                //Agrega al administrador como participante
                ParticipanteEvento nuevoParticipante = new ParticipanteEvento(nombreOrganizador);
                nuevoParticipante.setCelular(celular);
                childUpdates.put(Constants.URL_PARTICIPANTES_EVENTO + key_evento + "/" + celular, nuevoParticipante.toMap());

                for (Contacto invitadoActual : invitados) {
                    nuevoParticipante = new ParticipanteEvento(invitadoActual.getNombre());
                    nuevoParticipante.setCelular(invitadoActual.getNumeroTelefonico());
                    //Agrega cada participante al evento
                    childUpdates.put(Constants.URL_PARTICIPANTES_EVENTO + key_evento + "/" + invitadoActual.getNumeroTelefonico(), nuevoParticipante.toMap());
                    //Agrega un resumen en invitación a cada contacto
                    childUpdates.put(Constants.URL_INVITACIONES_EVENTO + invitadoActual.getNumeroTelefonico() + "/" + key_evento, resumenMap);

                }

                if (sitiosEncuesta != null) {

                    //Crea la encuesta
                    SondeoLugares sondeo = new SondeoLugares(invitados.size() + 1, sitiosEncuesta);
                    childUpdates.put(Constants.URL_SONDEOS + key_evento, sondeo.toMap());
                }

                dbReference.updateChildren(childUpdates);
                verificarRegistrados();
                btnContinuar.setVisibility(View.GONE);
            }

        } else {
            Toast.makeText(this, "Algo salió mal al seleccionar los invitados", Toast.LENGTH_LONG);
        }


    }

    public void enviarInvitacionesSMS() {
        final String msj = nombreOrganizador + " te ha invitado al evento: " + nuevoEvento.getNombre() + ". Descarga planit y mira toda la info. " + Constants.LINK_DESCARGA;
        if (cantidad_invitados_no_registrados > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle("Invitaciones");
            builder.setMessage(cantidad_invitados_no_registrados + " amigos no están registrados en PlanIt. Desea invitarlos por medio de mensajes de texto?");
            builder.setCancelable(false);
            builder.setOnDismissListener(
                    new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }
            );
            builder.setOnKeyListener(
                    new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            finish();
                            return true;
                        }
                    }
            );
            builder.setNegativeButton("OMITIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setPositiveButton("ENVIAR SMS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentoSMS = new Intent(contexto, MensajesService.class);
                    intentoSMS.putExtra(Constants.EXTRA_SMS, msj);
                    intentoSMS.putExtra(Constants.EXTRA_CONTACTOS_SMS, (Serializable) invitados_no_registrados);
                    contexto.startService(intentoSMS);
                    finish();
                }
            });
            builder.setItems(itemsSeleccionarSitio,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sitioEvento = sitios.get(which);
                            if (sitioEvento != null) {
                                btnSitio.setText("Cambiar");
                                lblSitio.setText(sitioEvento.toString());
                            }
                        }
                    });
            builder.show();
        }
        else
        {
            finish();
        }

    }

    private void verificarRegistrados()
    {
        int i = 0;
        for (final Contacto invitadoActual : invitados) {
            i++;
            DatabaseReference refParticipanteActual = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL + Constants.URL_USUARIOS)
                    .child(invitadoActual.getNumeroTelefonico()).child("nombre");
            final int finalI = i;
            refParticipanteActual.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println(finalI);
                            if (dataSnapshot.exists()) {
                                //Ponerle el nombre
                                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL + Constants.URL_PARTICIPANTES_EVENTO)
                                        .child(key_evento).child(invitadoActual.getNumeroTelefonico()).child("nombre").setValue(dataSnapshot.getValue());
                            } else {
                                //Invitar sms
                                invitados_no_registrados.add(invitadoActual);
                                cantidad_invitados_no_registrados++;
                                System.out.println("Contacto no registrado->" + invitadoActual.getNombre());
                            }
                            if (finalI == invitados.size()) {
                                enviarInvitacionesSMS();
                                System.out.println("El dialogo debería estar mostrado");

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
    }

}
