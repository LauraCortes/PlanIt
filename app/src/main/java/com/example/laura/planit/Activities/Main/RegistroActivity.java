package com.example.laura.planit.Activities.Main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 07/11/2016.
 */

public class RegistroActivity extends AppCompatActivity {
    TextView txtCelular, txtNombre, txtNickName, txtPin, txtPin2;
    Button btnRegistro;
    private Context contexto=this;


    public final static int REGISTRO_OK = 1;
    public final static int REGISTRAR_USUARIO = 421;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setSupportActionBar(null);
        setContentView(R.layout.activity_registro);

    }

    private void bindElements() {
        txtCelular = (TextView) findViewById(R.id.txtCelularRegistro);
        txtNombre = (TextView) findViewById(R.id.txtNombreRegistro);
        txtNickName = (TextView) findViewById(R.id.txtNicknameRegistro);
        txtPin = (TextView) findViewById(R.id.txtPasswordRegistro);
        txtPin2 = (TextView) findViewById(R.id.txtConfirmacionPasswordRegistro);
        btnRegistro = (Button) findViewById(R.id.btnRegistroUsuario);
    }

    public void registrarUsuario(View v) {
        bindElements();
        String celular = String.valueOf(txtCelular.getText());
        String nombre = String.valueOf(txtNombre.getText());
        String nick = String.valueOf(txtNickName.getText());
        String pass = String.valueOf(txtPin.getText());
        if (celular.trim().length() != 10) {
            txtCelular.setError("El número debe ser válido");
            txtCelular.requestFocus();
        } else {
            txtCelular.setError(null);
            if (nombre.trim().isEmpty()) {
                txtNombre.setError("Debe completar este campo");
                txtNombre.requestFocus();
            } else {
                txtNombre.setError(null);
                if (nick.trim().isEmpty()) {
                    txtNickName.setError("Debe completar este campo");
                    txtNickName.requestFocus();
                } else {
                    txtNickName.setError(null);
                    if (pass.trim().length() != 4 || !pass.equalsIgnoreCase(String.valueOf(txtPin2.getText()))) {

                        txtPin2.setText("");
                        txtPin.setText("");
                        txtPin.setError("Los pines no coinciden o no contienen 4 dígitos");
                        txtPin.requestFocus();
                        return;
                    } else {
                        if(!hayConexionInternet())
                        {
                            MainActivity.mostrarMensaje(this,"Error de conexión","¡Ops! Parece que no tienes conexión a internet. \nRevisa e intenta nuevamente");
                        }
                        else
                        {
                            //public UsuarioFB(String celular, int latitud_actual, int longitud_actual, String nickname, String nombre, String pin, String token) {
                            final UsuarioFB nuevoUser = new UsuarioFB(celular, 0, 0, nick, nombre, UsuarioFB.cifrar_SHA_256(pass), null);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child(nuevoUser.darRutaElemento());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        txtCelular.setError("El número ya está en uso");
                                        MainActivity.mostrarMensaje(contexto,"Error de regisro","El número con el que te estás intentando registrar ya está siendo usado por otro usuario");
                                        txtCelular.requestFocus();
                                        return;
                                    } else {
                                        databaseReference.setValue(nuevoUser);
                                        setResult(REGISTRO_OK);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Error en la conexión con la DB", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
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

}
