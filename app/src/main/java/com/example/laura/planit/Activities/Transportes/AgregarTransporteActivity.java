package com.example.laura.planit.Activities.Transportes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.Regreso;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 18/09/2016.
 */
public class AgregarTransporteActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private RadioButton radioTaxi, radioUber, radioBus, radioCarro;
    private List<RadioButton> radios;
    private EditText txtSitioRegreso, txtHoraRegreso, txtFechaRegreso, txtTiempoRegreso, txtCupos;
    private Context contexto;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    int pos;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto=this;
        pos=(int)getIntent().getExtras().get("pos");
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
        setContentView(R.layout.activity_agregar_transporte);
        radioBus=(RadioButton) findViewById(R.id.radioTransportePublico);
        radioBus.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            desactivarRadios(radioBus);
                                        }
                                    }
        );
        radioTaxi=(RadioButton) findViewById(R.id.radioTaxi);
        radioTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarRadios(radioTaxi);
            }
        });
        radioUber=(RadioButton) findViewById(R.id.radioUber);
        radioUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarRadios(radioUber);
            }
        });
        radioCarro=(RadioButton) findViewById(R.id.radioCarro);
        radioCarro.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                        desactivarRadios(radioCarro);
                }
            }
        );
        radios=new ArrayList<RadioButton>();
        radios.add(radioBus);
        radios.add(radioTaxi);
        radios.add(radioUber);
        radios.add(radioCarro);

        txtSitioRegreso=(EditText)findViewById(R.id.txtSitioRegreso);
        txtHoraRegreso=(EditText)findViewById(R.id.txtHoraRegreso);
        txtCupos=(EditText)findViewById(R.id.txtCupos);
        txtFechaRegreso=(EditText)findViewById(R.id.txtFechaRegreso);
        txtTiempoRegreso=(EditText)findViewById(R.id.txtTiempoRegreso);
        txtFechaRegreso.setText(dateFormatter.format(System.currentTimeMillis()));

        getSupportActionBar().setTitle("Regreso del evento");

        txtSitioRegreso.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        builder.setTitle("Sitio de regreso");
                        //TODO traer sitios
                        List<Sitio> sitios = new ArrayList<Sitio>();
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
                                    txtSitioRegreso.setText("");
                                } else {
                                    txtSitioRegreso.setText(opciones[which]);
                                }
                            }
                        });
                        builder.show();


                    }
                }
        );
    }


    public void desactivarRadios(View originador)
    {
        for(RadioButton radio: radios)
        {
            if(radio!=originador)
            {
                radio.setChecked(false);
            }
            else
            {
                radio.setChecked(true);
            }
        }

    }

    public void definirFechaRegreso(View view) {
        Calendar now = Calendar.getInstance();

        DatePickerDialog fechaEncuentroDatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        fechaEncuentroDatePickerDialog.setTitle("Fecha del evento");
        fechaEncuentroDatePickerDialog.show(getFragmentManager(), "tagDatePicker");
    }

    public void definirHoraRegreso(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog horaEncuentroTimePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        horaEncuentroTimePickerDialog.setTitle("Hora de regreso");
        horaEncuentroTimePickerDialog.show(getFragmentManager(), "tagTimePicker");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        txtFechaRegreso.setText(dateFormatter.format(newDate.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(0, 0, 0, hourOfDay, minute);
        txtHoraRegreso.setText(timeFormatter.format(newDate.getTime()));
    }

    public void agregarRegreso(View view)
    {
        String nombre="";
        String hora=txtHoraRegreso.getText().toString().trim();
        String fecha=txtFechaRegreso.getText().toString().trim();
        String tiempo=txtTiempoRegreso.getText().toString().trim();
        String lugar=txtSitioRegreso.getText().toString().trim();
        String cupos= txtCupos.getText().toString().trim();
        boolean continuar=true;
        if(radioCarro.isChecked())
        {
            nombre="Carro propio";
        }
        else if( radioBus.isChecked())
        {
            nombre="Otro medio trans. público";
        }
        else if(radioUber.isChecked())
        {
            nombre="Uber";
        }
        else if(radioTaxi.isChecked())
        {
            nombre="Taxi";
        }
        else
        {
            continuar=false;
            Toast.makeText(this,"Debes seleccionar un medio de transporte",Toast.LENGTH_SHORT).show();
        }
        if(continuar)
        {
            if(lugar.isEmpty()||hora.isEmpty()||fecha.isEmpty()||tiempo.isEmpty())
            {
                Toast.makeText(this,"Debes llenar todos los campos",Toast.LENGTH_SHORT).show();
            }
            else
            {
                try{
                    Date fechaRegreso =(Date) dateFormatter.parse(fecha);
                    Date horaRegreso=(Date)timeFormatter.parse(hora);
                    horaRegreso.setMonth(fechaRegreso.getMonth());
                    horaRegreso.setYear(fechaRegreso.getYear());
                    horaRegreso.setDate(fechaRegreso.getDate());
                    SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
                    if (properties.getBoolean(getString(R.string.logueado), false))
                    {
                        final String celular = properties.getString(getString(R.string.usuario), "desconocido");
                        //TODO Sitio!!!
                        final Regreso regreso = new Regreso(celular,horaRegreso,nombre, Integer.valueOf(cupos),null, Integer.valueOf(tiempo));
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //TODO ID_EVENTO
                        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(regreso.darRutaElemento(""));
                        databaseReference.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.exists()) {
                                            MainActivity.mostrarMensaje(contexto, "Retorno existente", "Ya existe" +
                                                    " un retorno con este numero");
                                        } else
                                        {
                                            databaseReference.setValue(regreso);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        MainActivity.mostrarMensaje(contexto, "Error", databaseError.getMessage());
                                        databaseError.toException().printStackTrace();
                                    }
                                }
                        );
                        finish();
                    } else {
                        MainActivity.mostrarMensaje(this, "Error", "Parece que no has iniciado sesi?n. Intenta cerrar sesi?n e ingresar de nuevo");
                    }
                    Toast.makeText(this,"Regreso configurado",Toast.LENGTH_SHORT).show();

                    finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this,"Los datos deben respetar el formato indicado",Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}
