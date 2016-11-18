package com.example.laura.planit.Activities.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laura.planit.Modelos.Usuario;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Laura on 12/09/2016.
 */
public class LoginActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private Usuario usuario;
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
        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if(properties.getBoolean(getString(R.string.logueado),false))
        {
            finish();
            System.out.println("Usuario logueado");
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        super.onCreate(savedInstanceState);
        setSupportActionBar(null);
        setContentView(R.layout.activity_login);
        EditText txtPass = (EditText)findViewById(R.id.txtPasswordLogin);
        loginActivity=this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!hayConexionInternet())
        {
            MainActivity.mostrarMensaje(this,"No hay conexión",
                    "Parece que no tienes conexión a internet, así que "+
                            "trabajaremos en modo offline. Es posible que " +
                            "algunos datos no se encuentren actualizados. Al recuperar conexión los" +
                            " actualizaremos");
        }
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
        final String celular = String.valueOf(txtCelular.getText());
        final String pin = String.valueOf(txtPin.getText());

        if(celular.trim().length()==10)
        {
            txtCelular.setError(null);
            if(pin.trim().length()==4)
            {
                if(true)
                {
                    //public UsuarioFB(String celular, int latitud_actual, int longitud_actual, String nickname, String nombre, String pin, String token) {
                    final Usuario usuario = new Usuario();
                    usuario.setCelular(celular);
                    usuario.setPin(pin);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(usuario.darRutaElemento());
                    databaseReference.keepSynced(true);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            System.out.println("Se recibió respuesta "+dataSnapshot.exists()+" - pin "+dataSnapshot.child("pin").getValue());
                            if( dataSnapshot.exists() && dataSnapshot.child("pin").getValue().equals(usuario.cifrar_SHA_256(pin)) )
                            {
                                SharedPreferences properties = loginActivity.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = properties.edit();
                                editor.putBoolean(getString(R.string.logueado),true);
                                editor.putString(getString(R.string.usuario),celular);
                                editor.commit();
                                ((Activity)loginActivity).finish();
                                Intent i = new Intent(loginActivity, MainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                txtPin.setText("");
                                String msj="El usuario o contraseña son incorrectos, revisa e intenta nuevamente.\n";
                                msj+=((!hayConexionInternet())?"Como estás en modo offline es probable" +
                                    " que no se haya actualizado tu contraseña. Si cambiaste tu contraseña recientemente" +
                                    " intenta ingresar con tu anterior contraseña. \nPero no te preocupes, en cuanto recuperemos la conexión ésta se actualizará":"");
                                MainActivity.mostrarMensaje(loginActivity,"Error de autenticación",msj);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                            MainActivity.mostrarMensaje(loginActivity, "Error de conexión",
                                    "Se produjó un error en la conexión con el servidor de PlanIt:\n" +
                                    databaseError.getMessage() );
                            System.out.println(databaseError.toString());
                        }
                    });
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

    public boolean hayConexionInternet()
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
