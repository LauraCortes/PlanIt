package com.example.laura.planit.Activities.Eventos;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Atributos interfaz
    EditText txtNombre, txtDescripcion;
    EditText txtFechaEvento, txtHoraEvento;
    TextView lblSitio;
    CheckBox cbxVotarSitio;
    Button btnSitio;
    Button btnContinuar;
    int INVITAR_AMIGOS = 1;

    Context contexto;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private Calendar fechaEvento;
    private Sitio sitioEvento;
    private String celular;
    private String nombreOrganizador;

    private String key_evento;

    //Atributos para soporte a edición-----
    boolean editar;
    int pos;
    String nombre;
    boolean horaEncuentro;
    boolean horaRegreso;

    private List<Contacto> invitados;
    private List<Sitio> sitios;

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
        lblSitio =(TextView)findViewById(R.id.lbl_sitio_evento);

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
                        if (isChecked) {
                            layout.setVisibility(View.GONE);
                            btnContinuar.setText("Elegir sitios de votación");
                        } else {
                            layout.setVisibility(View.VISIBLE);
                            btnContinuar.setText("Agregar invitados");
                        }
                    }
                }
        );


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_LUGARES_FAVORITOS + celular);
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, Sitio>> t = new GenericTypeIndicator<HashMap<String, Sitio>>() {
                        };
                        HashMap<String, Sitio> map = dataSnapshot.getValue(t);
                        if (map != null) {
                            ArrayList<Sitio> nuevos = new ArrayList(map.values());
                            sitios = nuevos;
                        } else {
                            sitios = new ArrayList<Sitio>();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        DatabaseReference databaseReferenceInfoUsuario=database.getReferenceFromUrl(Constants.FIREBASE_URL).child((Constants.URL_USUARIOS +celular+"nombre"));
        databaseReferenceInfoUsuario.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            nombreOrganizador=(String)dataSnapshot.getValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );




//        Intent intent = getIntent();
//        editar = intent.getExtras().getBoolean("editar");
//        if (editar) {
//            Evento eventoEditado = (Evento)intent.getSerializableExtra(Constants.EVENTO);
//            if (eventoEditado!=null)
//            {
//                txtNombre.setText(eventoEditado.getNombreEvento());
//                //......
//                eventoEditado = null;
//            }
//        }
        //getSupportActionBar().setTitle(intent.getStringExtra("titulo"));
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
        String[] items = new String[sitios.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = sitios.get(i).toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Selecciona el lugar");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancelar", null);
        builder.setItems(items,
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
        } else
        {
            Evento nuevoEvento = new Evento();
            nuevoEvento.setNombre(nombre);
            nuevoEvento.setDescripcion(descripcion);
            nuevoEvento.setFecha(fechaEvento.getTimeInMillis());
            nuevoEvento.setLugar(sitioEvento);
            nuevoEvento.setCelular_organizador(celular);
            nuevoEvento.setNombre_organizador(nombreOrganizador);
            boolean lugarFijo = !cbxVotarSitio.isChecked();
            nuevoEvento.setLugar_fijo(lugarFijo);
            nuevoEvento.setLugar_definitivo(lugarFijo);





            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_EVENTOS);
            key_evento = databaseReference.push().getKey();


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

    public void agregarInvitados(View view) {
        Intent intent = new Intent(this, AgregarInvitadosActivity.class);
        intent.putExtra("Invitados", (Serializable) invitados);
        startActivityForResult(intent, INVITAR_AMIGOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == INVITAR_AMIGOS) {
            // Make sure the request was successful
            if (resultCode != 0) {
                invitados = (List<Contacto>) data.getExtras().get("Invitados");
                Toast.makeText(this, "Invitados: " + invitados.size(), Toast.LENGTH_LONG);
            } else {
                Toast.makeText(this, "Debes seleccionar algunos invitados", Toast.LENGTH_LONG);
            }
        }
    }


}
