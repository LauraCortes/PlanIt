package com.example.laura.planit.Activities.Contactos;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.widget.ListView;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoActivity extends ListActivity {

    List<Contacto> contactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        ListView listView = (ListView) findViewById(R.id.listViewAddContacts);
        contactos= new ArrayList<Contacto>();

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
            int telefono=Integer.valueOf(mCursor.getString(2));
            contact=new Contacto(nombre,telefono,false);
            contactos.add(contact);
        }
        contact=null;

        listView.setAdapter(new AddContactAdapter(this,contactos));
    }

}
