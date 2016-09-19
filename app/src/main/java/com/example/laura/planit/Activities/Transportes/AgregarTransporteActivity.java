package com.example.laura.planit.Activities.Transportes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Usuario on 18/09/2016.
 */
public class AgregarTransporteActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    RadioButton radioTaxi, radioUber, radioBus, radioCarro;
    List<RadioButton> radios;
    EditText txtSitioRegreso, txtHoraRegreso, txtFechaRegreso;
    Context contexto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto=this;
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
        txtFechaRegreso=(EditText)findViewById(R.id.txtFechaRegreso);

        getSupportActionBar().setTitle("Regreso del evento");

        txtSitioRegreso.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        builder.setTitle("Sitio de regreso");
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

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }
}
