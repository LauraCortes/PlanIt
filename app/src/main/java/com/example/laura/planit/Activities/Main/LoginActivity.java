package com.example.laura.planit.Activities.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Logica.Usuario;
import com.example.laura.planit.R;

import java.security.MessageDigest;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Laura on 12/09/2016.
 */
public class LoginActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private Usuario usuario;

    private int confirmNumb;

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
        setContentView(R.layout.activity_login);
        EditText txtPass = (EditText)findViewById(R.id.txtPasswordLogin);
    }

    public void lanzarActivityRegistro(View v)
    {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivityForResult(i,RegistroActivity.REGISTRAR_USUARIO);
    }

    public void login(View v)
    {
        TextView txtCelular, txtPin;
        txtCelular = (TextView)findViewById(R.id.txtCelularLogin);
        txtPin = (TextView)findViewById(R.id.txtPasswordLogin);
        String celular = String.valueOf(txtCelular.getText());
        String pin = String.valueOf(txtPin.getText());

        if(celular.trim().length()==10)
        {
            txtCelular.setError(null);
            if(pin.trim().length()==4)
            {
                //TODO intentar hacer logueo
                if(false)
                {
                    Toast.makeText(this,"La autenticación falló",Toast.LENGTH_SHORT);
                    txtPin.setText("");
                }
                else
                {
                    String password = cifrar_SHA_256(pin);
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
            }
            else
            {
                txtPin.requestFocus();
                txtPin.setError("Pin de 4 dígitos");
            }

        }
        else
        {
            txtCelular.requestFocus();
            txtCelular.setError("Verifique que sea válido");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==RegistroActivity.REGISTRAR_USUARIO && resultCode==RegistroActivity.REGISTRO_OK)
        {
            super.onActivityResult(requestCode, resultCode, data);
            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

    }

    public static String cifrar_SHA_256(String input)
    {
        try
        {
            input="\\u00F1opil\\u00E2[[!\\u00A1?=%$#{]\\u00E0sdf"+input+"opil\\u00E";
            MessageDigest mDigest = MessageDigest.getInstance("SHA256");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**

    public void registrar(View view)
    {
        confirmNumb=(int)Math.random();
        EditText mEdit   = (EditText)findViewById(R.id.txtCelularLogin);
        EditText mEdit2   = (EditText)findViewById(R.id.txtPasswordLogin);

        usuario = new Usuario(mEdit.getText().toString(),mEdit2.getText().toString() );

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                showSnackBar();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }
        else
        {
            /*
            EditText mEdit3 = (EditText) findViewById(R.id.editTextConfirmation);
            mEdit3.setHint(String.valueOf(confirmNumb));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(usuario.getNumeroTelefonico(), null, "Número de confirmación: "+confirmNumb, null, null);

        }
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(R.id.relativeLayout),"Activar envío sms",Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override public void onClick(View view){
                        openSettings();
                    }}).show();
    }

    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   /* EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
                    mEdit.setHint(confirmNumb);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(usuario.getNumeroTelefonico(), null, "Número de confirmación: "+confirmNumb, null, null);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showSnackBar();
                   // finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void confirmar(View view) {
       /* EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
        if (mEdit.getText().toString().equals(String.valueOf(confirmNumb))) {
            Intent intent = new Intent(this, PersitenciaService.class);
            intent.putExtra("Requerimiento","Registrar");
            intent.putExtra("Usuario", usuario);
            startService(intent);
            usuario = null;
            finish();
        } else {
            mEdit.setHint("Número equivocado");
        }
    }

    */
}
