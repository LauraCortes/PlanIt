package com.example.laura.planit.Activities.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laura.planit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 07/11/2016.
 */

public class RegistroActivity extends AppCompatActivity
{
    TextView txtCelular, txtNombre, txtNickName, txtPin, txtPin2;
    Button btnRegistro;

    public final static int REGISTRO_OK=1;
    public final static int REGISTRAR_USUARIO=421;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setSupportActionBar(null);
        setContentView(R.layout.activity_registro);

    }

    private void bindElements()
    {
        txtCelular=(TextView)findViewById(R.id.txtCelularRegistro);
        txtNombre=(TextView)findViewById(R.id.txtNombreRegistro);
        txtNickName=(TextView)findViewById(R.id.txtNicknameRegistro);
        txtPin=(TextView)findViewById(R.id.txtPasswordRegistro);
        txtPin2=(TextView)findViewById(R.id.txtConfirmacionPasswordRegistro);
        btnRegistro=(Button)findViewById(R.id.btnRegistroUsuario);
    }

    public void registrarUsuario(View v)
    {
        bindElements();
        //TODO
        String celular = String.valueOf(txtCelular.getText());
        String nombre = String.valueOf(txtNombre.getText());
        String nick = String.valueOf(txtNickName.getText());
        String pass = String.valueOf(txtPin.getText());
        if(celular.trim().length()!=10)
        {
            txtCelular.setError("El número debe ser válido");
            txtCelular.requestFocus();
            return;
        }
        else
        {
            txtCelular.setError(null);
        }
        //Validar si el número no está registrado
        if( false )
        {
            txtCelular.setError("El número ya está en uso");
            txtCelular.requestFocus();
            return;
        }
        else
        {
            txtCelular.setError(null);
        }
        if(nombre.trim().isEmpty())
        {
            txtNombre.setError("Debe completar este campo");
            txtNombre.requestFocus();
            return;
        }
        else
        {
            txtNombre.setError(null);
        }
        if(nick.trim().isEmpty())
        {
            txtNickName.setError("Debe completar este campo");
            txtNickName.requestFocus();
            return;
        }
        else
        {
            txtNickName.setError(null);
        }
        if(pass.trim().length()!=4 || !pass.equalsIgnoreCase(String.valueOf(txtPin2.getText())))
        {

            txtPin2.setText("");
            txtPin.setText("");
            txtPin.setError("Los pines no coinciden o no contienen 4 dígitos");
            txtPin.requestFocus();
            return;
        }
        else
        {
            //TODO todo está OK, registrar en la DB
            setResult(REGISTRO_OK);
            finish();
        }
    }
}
