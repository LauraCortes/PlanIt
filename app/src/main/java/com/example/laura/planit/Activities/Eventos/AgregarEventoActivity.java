package com.example.laura.planit.Activities.Eventos;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.MensajesService;
import com.example.laura.planit.Services.PersitenciaService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Atributos interfaz
    EditText txtNombre, txtDescripcion, txtLugar, txtPuntoEncuentro;
    EditText txtFechaEncuentro, txtHoraEncuentro;

    int INVITAR_AMIGOS=1;

    Context contexto;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    //Atributos para soporte a edición-----
    boolean editar;
    int pos;
    String nombre;
    boolean horaEncuentro;
    boolean horaRegreso;
    int sitioEventoPos;
    int puntoEncuentroPos;

    private List<Contacto> invitados;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        sitioEventoPos = -1;
        puntoEncuentroPos = -1;

        txtNombre = (EditText) findViewById(R.id.txtNombreEvento);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcionEvento);
        txtLugar = (EditText) findViewById(R.id.txtLugarEvento);
        txtPuntoEncuentro = (EditText) findViewById(R.id.txtPuntoEncuentroEvento);
        txtFechaEncuentro = (EditText) findViewById(R.id.txtFechaEncuentro);
        txtHoraEncuentro = (EditText) findViewById(R.id.txtHoraEncuentro);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");

        horaEncuentro = false;
        horaRegreso = false;
        contexto = this;

        Intent intent = getIntent();
        editar = intent.getExtras().getBoolean("editar");
        if (editar) {
            pos = intent.getIntExtra("posicion", -1);
            if (pos != -1) {
                Evento eventoEditado = PlanIt.darInstancia().darEventoPos(pos);
                txtNombre.setText(eventoEditado.getNombreEvento());
                //......
                eventoEditado = null;
            }

        }
        //getSupportActionBar().setTitle(intent.getStringExtra("titulo"));
        intent = null;


        txtLugar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        builder.setTitle("Sitio del evento");
                        List<Sitio> sitios = PlanIt.darInstancia().darSitios();
                        final CharSequence[] opciones = new CharSequence[sitios.size() + 1];
                        opciones[0] = "Otro";
                        for (int i = 0; i < opciones.length - 1; i++) {
                            Sitio sitio = sitios.get(i);
                            opciones[i + 1] = sitio.toString();
                        }
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    txtLugar.setText("");
                                    sitioEventoPos = -1;
                                } else {
                                    txtLugar.setText(opciones[which]);
                                    sitioEventoPos = which - 1;
                                }
                            }
                        });
                        builder.show();


                    }
                }
        );

        txtPuntoEncuentro.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        builder.setTitle("Punto de encuentro");
                        List<Sitio> sitios = PlanIt.darInstancia().darSitios();
                        final CharSequence[] opciones = new CharSequence[sitios.size() + 1];
                        opciones[0] = "Otro";
                        for (int i = 0; i < opciones.length - 1; i++) {
                            Sitio sitio = sitios.get(i);
                            opciones[i + 1] = sitio.toString();
                        }
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    txtPuntoEncuentro.setText("");
                                    puntoEncuentroPos = -1;
                                } else {
                                    txtPuntoEncuentro.setText(opciones[which]);
                                    puntoEncuentroPos = which - 1;
                                }
                            }
                        });
                        builder.show();


                    }
                }
        );

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.agregarInvitados);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarInvitados(v);
            }
        });

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

    public void definirHoraRegreso(View view) {
        horaRegreso = true;
    }

    public void agregarEvento(View view) {
        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        String sitioEvento = txtLugar.getText().toString().trim();
        String fechaString = txtFechaEncuentro.getText().toString().trim();
        String puntoEncuentro = txtPuntoEncuentro.getText().toString().trim();
        String horaEncuentroString = txtHoraEncuentro.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || sitioEvento.isEmpty() || fechaString.isEmpty() || puntoEncuentro.isEmpty() || horaEncuentroString.isEmpty())
        {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (PlanIt.darInstancia().existeEventoNombre(nombre)) {
            Toast.makeText(this, "Ya existe otro evento con ese nombre", Toast.LENGTH_SHORT).show();
        } else
        {
            if (editar) {
                /**
                 Sitio agregado = PlanIt.darInstancia().editarSitio(pos,nombre,barrio,direccion);
                 Toast.makeText(this, "Tu sitio se editó", Toast.LENGTH_SHORT).show();
                 finish();
                 Intent service = new Intent(this, PersitenciaService.class);
                 service.putExtra("Requerimiento","EditarSitio");
                 service.putExtra("Sitio", agregado);
                 service.putExtra("Nombre",this.nombre);
                 startService(service);
                 agregado = null;
                 this.nombre=null;
                 **/
            }
            else
            {
                Evento agregado = null;
                try {
                    agregado = PlanIt.darInstancia().agregarEvento(nombre, descripcion, sitioEvento, puntoEncuentro, null, timeFormatter.parse(horaEncuentroString), dateFormatter.parse(fechaString), null);
                    if (puntoEncuentroPos != -1) {
                        agregado.setPuntoEncuentroObjeto(PlanIt.darInstancia().darSitios().get(puntoEncuentroPos));
                    }
                    if (sitioEventoPos != -1) {
                        agregado.setLugarEventoObjeto(PlanIt.darInstancia().darSitios().get(sitioEventoPos));
                    }
                    agregado.setInvitados(invitados);
                    Intent intent = new Intent(this, PersitenciaService.class);
                    intent.putExtra("Requerimiento", "AgregarEvento");
                    intent.putExtra("Evento", agregado);
                    startService(intent);
                    Intent intent2= new Intent(this, MensajesService.class);
                    intent2.putExtra("Requerimiento","EnviarALista");
                    intent2.putExtra("Contactos",(Serializable)invitados);
                    intent2.putExtra("Msj","Te estoy invitando al siguiente evento\n "+agregado.toStringSMS()+"\nDescarga PlanIt y accede a la info completa de este envento. Podrás crear los tuyos y maximizar tu seguridad");
                    startService(intent2);
                    Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT).show();
                    finish();

                    agregado = null;
                } catch (ParseException e) {
                    Toast.makeText(contexto, "Debe seleccionar una fecha y hora correcta", Toast.LENGTH_LONG);
                }

            }
            finish();

        }

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        txtFechaEncuentro.setText(dateFormatter.format(newDate.getTime()));

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(0, 0, 0, hourOfDay, minute);
        txtHoraEncuentro.setText(timeFormatter.format(newDate.getTime()));
    }

    public void agregarInvitados(View view) {
        Intent intent = new Intent(this, AgregarInvitadosActivity.class);
        intent.putExtra("Invitados", (Serializable) invitados);
        startActivityForResult(intent,INVITAR_AMIGOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == INVITAR_AMIGOS) {
            // Make sure the request was successful
            if (resultCode != 0) {
                invitados=(List<Contacto>) data.getExtras().get("Invitados");
                Toast.makeText(this,"Invitados: "+invitados.size(),Toast.LENGTH_LONG);
            }
            else
            {
                Toast.makeText(this,"Debes seleccionar algunos invitados",Toast.LENGTH_LONG);
            }
        }
    }
}
