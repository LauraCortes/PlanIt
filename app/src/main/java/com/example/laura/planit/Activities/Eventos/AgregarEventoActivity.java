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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //Atributos interfaz
    EditText txtNombre, txtDescripcion , txtLugar, txtPuntoEncuentro;
    EditText txtFechaEncuentro, txtHoraEncuentro;


    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    final String TAG_FECHA_ENCUENTRO="FechaEnc";
    final String TAG_HORA_ENCUENTRO="HoraEnc";

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
        fechaEncuentroDatePickerDialog.show(getFragmentManager(), TAG_FECHA_ENCUENTRO);
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
        horaEncuentroTimePickerDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        horaEncuentro=false;
                    }
                }
        );
        horaEncuentroTimePickerDialog.show(getFragmentManager(), TAG_HORA_ENCUENTRO);
        horaEncuentro=true;
    }

    public void definirHoraRegreso(View view)
    {
        horaRegreso=true;
    }

    public void agregarEvento(View view)
    {
        /**
        String nombre,barrio,direccion;
        nombre=txtNombre.getText().toString().trim();
        barrio=txtBarrio.getText().toString().trim();
        direccion=txtDireccion.getText().toString().trim();
        if(nombre.isEmpty()||barrio.isEmpty()||direccion.isEmpty())
        {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        else if (PlanIt.darInstancia().existeSitio(nombre))
        {
            Toast.makeText(this, "Ya existe otro sitio con ese nombre", Toast.LENGTH_SHORT).show();
        }
        else
        {

            if(editar)
            {
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
            }
            else
            {
                Sitio agregado = PlanIt.darInstancia().agregarSitio(nombre,barrio,direccion);
                Toast.makeText(this, "Tu sitio se agregó", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","AgregarSitio");
                intent.putExtra("Sitio", agregado);
                startService(intent);
                agregado = null;
            }
            Intent i = new Intent(this, SitiosActivity.class);
            startActivity(i);

        }
         **/

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String tag = view.getTag();
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        if(tag.equals(TAG_FECHA_ENCUENTRO))
        {
            txtFechaEncuentro.setText(dateFormatter.format(newDate.getTime()));
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
    {
        Calendar newDate = Calendar.getInstance();
        newDate.set(0,0,0,hourOfDay,minute);
        if(horaEncuentro)
        {
            horaEncuentro=false;
            txtHoraEncuentro.setText(timeFormatter.format(newDate.getTime()));
        }
        else if(horaRegreso)
        {
            horaRegreso=false;
        }
    }
}
