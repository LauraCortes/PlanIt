package com.example.laura.planit.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.laura.planit.Activities.Contactos.AgregarContactoAdapter;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 06/11/2016.
 */

public abstract class AgregarSuper extends AppCompatActivity
{
    protected static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    protected List<Contacto> contactos;
    protected HashMap<Integer,Contacto> contactosSeleccionados;
    protected ListView listView;
    protected FloatingActionButton btnFAB;
    protected String titulo;

    protected AgregarContactoAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contactos = new ArrayList<>();
        adapter= new AgregarContactoAdapter(this,contactos);
    }

    public void seleccionarItem(int pos)
    {
        if(contactosSeleccionados.containsKey(pos))
        {
            contactosSeleccionados.remove(pos);
        }
        else
        {
            contactosSeleccionados.put(Integer.valueOf(pos),contactos.get(pos));
        }
        cambiarIconoFAB();
    }

    public boolean estaSeleccionado(int pos)
    {
        return contactosSeleccionados.containsKey(pos);
    }

    public void cambiarIconoFAB()
    {
        if(contactosSeleccionados.size()!=0)
        {
            btnFAB.setVisibility(View.VISIBLE);
        }
        else
        {
            btnFAB.setVisibility(View.GONE);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Cursor mCursor = getContentResolver().query(
                            ContactsContract.Data.CONTENT_URI,
                            new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE },
                            ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                                    + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                            ContactsContract.Data.DISPLAY_NAME + " ASC");
                    startManagingCursor(mCursor);
                    Contacto contact;
                    if(mCursor!=null) {
                        if (mCursor.getCount() > 0) {
                            mCursor.moveToFirst();
                            do
                            {
                                String numero =  mCursor.getString(1).trim();
                                numero=numero.replaceAll(" ","");
                                if(!contactoExistente(numero))
                                {
                                    contact = new Contacto(mCursor.getString(0), numero);
                                    contactos.add(contact);
                                }
                            }
                            while (mCursor.moveToNext());
                            mCursor.close();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    contact = null;
                    listView.setAdapter(new AgregarContactoAdapter(this,contactos));

                } else {
                    showSnackBar();
                    //finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    protected boolean contactoExistente(String numero)
    {
        for(Contacto actual: contactos)
        {
            if(actual.getNumeroTelefonico().trim().equalsIgnoreCase(numero))
            {
                System.out.println("Contacto existente "+actual.getNombre()+actual.getNumeroTelefonico());
                return true;
            }
        }
        return false;
    }

    public abstract void agregar(View view);

    protected void showSnackBar() {
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

    protected void leerContactos()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                showSnackBar();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Cursor mCursor = getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                            + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                    ContactsContract.Data.DISPLAY_NAME + " ASC");
            startManagingCursor(mCursor);
            Contacto contact;
            if (mCursor != null) {
                if (mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    do {
                        String numero =  mCursor.getString(1).trim();
                        numero=numero.replaceAll(" ","");

                        if(!contactoExistente(numero))
                        {
                            contact = new Contacto(mCursor.getString(0), numero);
                            contactos.add(contact);
                            System.out.println("Agregado "+contact.getNombre()+" - "+contact.getNumeroTelefonico());
                        }
                    }
                    while (mCursor.moveToNext());
                    adapter.notifyDataSetChanged();
                    mCursor.close();


                }
            }
            contact = null;
        }
    }
}

