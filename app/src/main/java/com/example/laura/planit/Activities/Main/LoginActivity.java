package com.example.laura.planit.Activities.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.UsuarioFB;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Laura on 12/09/2016.
 */
public class LoginActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private UsuarioFB usuarioFB;
    Context loginActivity;

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
        final TextView txtCelular, txtPin;
        txtCelular = (TextView)findViewById(R.id.txtCelularLogin);
        txtPin = (TextView)findViewById(R.id.txtPasswordLogin);
        String celular = String.valueOf(txtCelular.getText());
        final String pin = String.valueOf(txtPin.getText());
        loginActivity = this;

        if(celular.trim().length()==10)
        {
            txtCelular.setError(null);
            if(pin.trim().length()==4)
            {
                if(hayConexionInternet())
                {
                    //public UsuarioFB(String celular, int latitud_actual, int longitud_actual, String nickname, String nombre, String pin, String token) {
                    final UsuarioFB usuario = new UsuarioFB();
                    usuario.setCelular(celular);
                    usuario.setPin(pin);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child(usuario.darRutaElemento());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            System.out.println("Se recibió respuesta "+dataSnapshot.exists()+" - pin "+dataSnapshot.child("pin").getValue());
                            if( dataSnapshot.exists() && dataSnapshot.child("pin").getValue().equals(usuario.cifrar_SHA_256(pin)) )
                            {
                                ((Activity)loginActivity).finish();
                                Intent i = new Intent(loginActivity, MainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                                builder.setTitle("Error de autenticación");
                                builder.setMessage("El usuario o contraseña son incorrectos, revise e intente nuevamente");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK",null);
                                builder.show();
                                txtPin.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                            builder.setTitle("Error de conexión");
                            builder.setMessage("Se produjó un error en la conexión con el servidor de PlanIt:\n"+databaseError.getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK",null);
                            builder.show();
                        }
                    });
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                    builder.setTitle("Error de conexión");
                    builder.setMessage("Parece que no tienes conexión a internet. Verifica e intenta nuevamente");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",null);
                    builder.show();
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


    private boolean hayConexionInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
           return true;
        } else
        {
            return false;
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
