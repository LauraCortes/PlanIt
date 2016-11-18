package com.example.laura.planit.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Usuario on 10/11/2016.
 */

public class ObtenerDireccionesIntentService extends IntentService {

    public ObtenerDireccionesIntentService()
    {
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ObtenerDireccionesIntentService(String name) {
        super(name);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        System.out.println("Solicitud de servicio para geocodificación");
        final Sitio sitio = (Sitio) intent.getSerializableExtra(Constants.SITIO);
        if (sitio != null && hayConexionInternet()) {
            System.out.println("Llegó sitio");
            // Get the location passed to this service through an extra.
            Location location = new Location("");
            location.setLongitude(sitio.getLongitud());
            location.setLatitude(sitio.getLatitud());

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String errorMessage = "";

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = "Servicio no disponible";
                System.out.println(errorMessage);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = "Latitud o longitud inválida";
                System.out.println(errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude());
            }
            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "Dirección no encontrada";
                    System.out.println(errorMessage);
                }
            } else {

                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }

                final String direccionObtenida = TextUtils.join(", ", addressFragments);
                System.out.println("Dirección obtenida por el service -> " + direccionObtenida);

                SharedPreferences properties = getSharedPreferences(getString(R.string.properties), MODE_PRIVATE);
                String celular = properties.getString(getString(R.string.usuario), "not found");

                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference ref = db.getReferenceFromUrl(Constants.FIREBASE_URL).child(sitio.darRutaElemento(celular));
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    sitio.setDireccion(direccionObtenida);
                                    ref.setValue(sitio);
                                    System.out.println("SITIO ACTUALIZADO");
                                } else {
                                    System.out.println(ref.toString());
                                    System.out.println("SITIO no existe");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }
        } else {
            System.out.println("NO SE RECIBIÓ SITIO");
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

