package com.example.laura.planit.Activities.Sitios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarSitioActivity extends AppCompatActivity{

    private EditText txtNombre, txtBarrio, txtDireccion;
    private boolean editar;
    private int pos;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_sitios);
        txtNombre=(EditText)findViewById(R.id.txtNombreSitioFavorito);
        txtBarrio=(EditText)findViewById(R.id.txtBarrioFavorito);
        txtDireccion=(EditText)findViewById(R.id.txtDireccionFavorito);
        Intent intent = getIntent();
        editar = intent.getExtras().getBoolean("editar");
        if(editar)
        {
            pos=intent.getIntExtra("posicion",-1);
            if(pos!=-1)
            {
                Sitio sitioEditado = PlanIt.darInstancia().darSitios().get(pos);
                nombre=sitioEditado.getNombre();
                txtNombre.setText(nombre);
                txtBarrio.setText(sitioEditado.getBarrio());
                txtDireccion.setText(sitioEditado.getDirección());
                sitioEditado=null;
            }

        }
        //getSupportActionBar().setTitle(intent.getStringExtra("titulo"));
        intent=null;
    }



    public void agregarSitio(View view)
    {
        String nombre,barrio,direccion;
        nombre=txtNombre.getText().toString().trim();
        barrio=txtBarrio.getText().toString().trim();
        direccion=txtDireccion.getText().toString().trim();
        if(nombre.isEmpty()||barrio.isEmpty()||direccion.isEmpty())
        {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        else if (!editar && PlanIt.darInstancia().existeSitio(nombre))
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
            finish();

        }

    }
}
