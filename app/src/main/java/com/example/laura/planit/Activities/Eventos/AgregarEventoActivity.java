package com.example.laura.planit.Activities.Eventos;


import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Activities.Sitios.SitiosActivity;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarEventoActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    //Atributos interfaz
    EditText txtNombre, txtDescripcion , txtLugar, txtPuntoEncuentro;
    TextView lblFechaEncuentro;

    private DatePickerDialog fechaEncuentroDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    final String TAG_FECHA_ENCUENTRO="FechaEnc";

    //Atributos para soporte a edición-----
    boolean editar;
    int pos;
    String nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);

        txtNombre=(EditText)findViewById(R.id.txtNombreEvento);
        txtDescripcion=(EditText)findViewById(R.id.txtDescripcionEvento);
        txtLugar=(EditText)findViewById(R.id.txtLugarEvento);
        txtPuntoEncuentro=(EditText)findViewById(R.id.txtPuntoEncuentroEvento);
        lblFechaEncuentro=(TextView)findViewById(R.id.lblFechaEncuentro);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

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

        fechaEncuentroDatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        fechaEncuentroDatePickerDialog.show(getFragmentManager(), TAG_FECHA_ENCUENTRO);
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
            lblFechaEncuentro.setText(dateFormatter.format(newDate.getTime()));
        }

    }
}
