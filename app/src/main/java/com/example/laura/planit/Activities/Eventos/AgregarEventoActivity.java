package com.example.laura.planit.Activities.Eventos;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //Atributos interfaz
    EditText txtNombre, txtDescripcion , txtLugar, txtPuntoEncuentro;
    EditText txtFechaEncuentro, txtHoraEncuentro;


    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    //Atributos para soporte a edición-----
    boolean editar;
    int pos;
    String nombre;
    boolean horaEncuentro;
    boolean horaRegreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        txtNombre=(EditText)findViewById(R.id.txtNombreEvento);
        txtDescripcion=(EditText)findViewById(R.id.txtDescripcionEvento);
        txtLugar=(EditText)findViewById(R.id.txtLugarEvento);
        txtPuntoEncuentro=(EditText)findViewById(R.id.txtPuntoEncuentroEvento);
        txtFechaEncuentro =(EditText) findViewById(R.id.txtFechaEncuentro);
        txtHoraEncuentro=(EditText) findViewById(R.id.txtHoraEncuentro);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");

        horaEncuentro=false;
        horaRegreso=false;

        Intent intent = getIntent();
        editar = intent.getExtras().getBoolean("editar");
        if(editar)
        {
            pos=intent.getIntExtra("posicion",-1);
            if(pos!=-1)
            {
                Evento eventoEditado = PlanIt.darInstancia().darEventoPos(pos);
                txtNombre.setText(eventoEditado.getNombreEvento());
                //......
                eventoEditado=null;
            }

        }
        getSupportActionBar().setTitle(intent.getStringExtra("titulo"));
        intent=null;
    }

    public void definirFechaEncuentro(View view)
    {
        Calendar now = Calendar.getInstance();

        DatePickerDialog fechaEncuentroDatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        fechaEncuentroDatePickerDialog.setTitle("Fecha del evento");
        fechaEncuentroDatePickerDialog.show(getFragmentManager(), "tagDatePicker");
        horaEncuentro=true;

    }

    public void definirHoraEncuentro(View view)
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog horaEncuentroTimePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        horaEncuentroTimePickerDialog.setTitle("Hora de encuentro");
        horaEncuentroTimePickerDialog.show(getFragmentManager(), "tagTimePicker");
        horaEncuentro=true;
    }

    public void definirHoraRegreso(View view)
    {
        horaRegreso=true;
    }

    public void agregarEvento(View view)
    {
        String nombre=txtNombre.getText().toString().trim();
        String descripcion=txtDescripcion.getText().toString().trim();
        String sitioEvento = txtLugar.getText().toString().trim();
        String fechaString = txtFechaEncuentro.getText().toString().trim();
        String puntoEncuentro = txtPuntoEncuentro.getText().toString().trim();
        String horaEncuentroString = txtHoraEncuentro.getText().toString().trim();

        if(nombre.isEmpty()||descripcion.isEmpty()||sitioEvento.isEmpty()||fechaString.isEmpty()||puntoEncuentro.isEmpty()||horaEncuentroString.isEmpty())
        {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        else if (PlanIt.darInstancia().existeEventoNombre(nombre))
        {
            Toast.makeText(this, "Ya existe otro evento con ese nombre", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(editar)
            {
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
            } else
            {
                Evento agregado = null;
                try
                {
                    agregado = PlanIt.darInstancia().agregarEvento(nombre, descripcion,sitioEvento, puntoEncuentro, null, timeFormatter.parse(horaEncuentroString), dateFormatter.parse(fechaString),null);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT).show();
                finish();
                //TODO
                /**
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","AgregarEvento");
                intent.putExtra("Evento", agregado);
                startService(intent);
                 **/
                agregado = null;
            }
            Intent i = new Intent(this, MisEventosActivity.class);
            startActivity(i);

        }

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String tag = view.getTag();
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        txtFechaEncuentro.setText(dateFormatter.format(newDate.getTime()));

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
    {
        Calendar newDate = Calendar.getInstance();
        newDate.set(0,0,0,hourOfDay,minute);
        txtHoraEncuentro.setText(timeFormatter.format(newDate.getTime()));
    }
}
