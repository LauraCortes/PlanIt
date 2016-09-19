package com.example.laura.planit.Activities.Contactos;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.laura.planit.Activities.Sitios.SitiosActivity;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoActivity extends ListActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private List<Contacto> contactos;
    private  ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        listView = (ListView) findViewById(android.R.id.list);
        contactos= new ArrayList<Contacto>();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                showSnackBar();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
        else {
            Cursor mCursor = getContentResolver().query(
                    Data.CONTENT_URI,
                    new String[]{Data.DISPLAY_NAME, CommonDataKinds.Phone.NUMBER},
                    Data.MIMETYPE + "='" + CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                            + CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                    Data.DISPLAY_NAME + " ASC");
            startManagingCursor(mCursor);
            Contacto contact;
            if(mCursor!=null) {
                if (mCursor.getCount() > 0) {
                    mCursor.moveToFirst();
                    do {
                        contact = new Contacto(mCursor.getString(0), mCursor.getString(1));
                        contactos.add(contact);
                    }
                    while (mCursor.moveToNext());
                    mCursor.close();
                }
            }contact = null;
                listView.setAdapter(new AddContactAdapter(this, contactos));
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
                            Data.CONTENT_URI,
                            new String[] { Data._ID, Data.DISPLAY_NAME, CommonDataKinds.Phone.NUMBER,
                                    CommonDataKinds.Phone.TYPE },
                            Data.MIMETYPE + "='" + CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                                    + CommonDataKinds.Phone.NUMBER + " IS NOT NULL", null,
                            Data.DISPLAY_NAME + " ASC");
                    startManagingCursor(mCursor);
                    Contacto contact;
                    if(mCursor!=null) {
                        if (mCursor.getCount() > 0) {
                            mCursor.moveToFirst();
                            do {
                                contact = new Contacto(mCursor.getString(0), mCursor.getString(1));
                                contactos.add(contact);
                            }
                            while (mCursor.moveToNext());
                            mCursor.close();
                        }
                    }
                    contact = null;
                    listView.setAdapter(new AddContactAdapter(this,contactos));

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

    public void agregar(View view)
    {
        for (int i = 0; i <listView.getAdapter().getCount() ; i++)
        {
            ViewGroup row = (ViewGroup) listView.getChildAt(i);
            if(row!=null) {
                CheckBox tvTest = (CheckBox) row.findViewById(R.id.checkBox);
                //  Get your controls from this ViewGroup and perform your task on them =)
                if (tvTest.isChecked()) {
                    // DO SOMETHING
                    TextView t = (TextView) row.findViewById(R.id.textViewNombreAgregar);
                    TextView tN = (TextView) row.findViewById(R.id.textViewTelefonoAgregar);
                    PlanIt.darInstancia().agregarContacto(t.getText().toString(), tN.getText().toString());
                    Intent intent = new Intent(this, PersitenciaService.class);
                    intent.putExtra("Requerimiento", "AgregarContacto");
                    intent.putExtra("Contacto", new Contacto(t.getText().toString(), tN.getText().toString()));
                    startService(intent);

                }
            }
        }
        finish();
        Intent i = new Intent(this, ContactosActivity.class);
        startActivity(i);

    }
}
