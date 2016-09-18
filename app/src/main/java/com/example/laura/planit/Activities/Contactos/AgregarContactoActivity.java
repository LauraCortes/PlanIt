package com.example.laura.planit.Activities.Contactos;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
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

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

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
                    for (int i=0;i<mCursor.getCount();i++)
                    {
                        String nombre= mCursor.getString(1);
                        String telefono=mCursor.getString(2);
                        contact=new Contacto(nombre,telefono,false);
                        contactos.add(contact);
                    }
                    contact=null;

                    listView.setAdapter(new AddContactAdapter(this,contactos));

                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void agregar(View view)
    {
        for (int i = 0; i <listView.getAdapter().getCount() ; i++)
        {
            ViewGroup row = (ViewGroup) listView.getChildAt(i);
            CheckBox tvTest = (CheckBox) row.findViewById(R.id.checkBox);
            //  Get your controls from this ViewGroup and perform your task on them =)
            if (tvTest.isChecked())
            {
                // DO SOMETHING
                PlanIt.darInstancia().agregarContacto(row.findViewById(R.id.textViewTelefono).toString(),row.findViewById(R.id.textViewNombre).toString());
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","AgregarContacto");
                intent.putExtra("Contacto", new Contacto(row.findViewById(R.id.textViewNombre).toString(),row.findViewById(R.id.textViewTelefono).toString(),true));
                startService(intent);
            }
        }
    }
}
