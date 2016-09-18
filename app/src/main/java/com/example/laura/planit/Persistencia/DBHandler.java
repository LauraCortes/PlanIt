package com.example.laura.planit.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.Logica.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 14/09/2016.
 */
public class DBHandler extends SQLiteOpenHelper implements Serializable{

    //Database Version
    private static final int DATABASE_VERSION = 3;

    //Database Name
    private static final String DATABASE_NAME = "PlanItDB";

    //Contacts table name
    private static final String TABLE_USERS = "USUARIOS";
    private static final String TABLE_EMERGENCY_CONTACTS = "CONTACTOS_EMERGENCIA";
    private static final String TABLE_SITIOS_FAVORITOS ="SITIOS_FAVORITOS";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Lau, habíamos dicho que íbamos a programar en español para no tener revueltos :s
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(PHONE_NUMBER TEXT PRIMARY KEY,NOMBRE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + "(PHONE_NUMBER TEXT PRIMARY KEY,NOMBRE TEXT, FAVORITO BOOLEAN)");
        db.execSQL("CREATE TABLE " + TABLE_SITIOS_FAVORITOS + "(NOMBRE TEXT PRIMARY KEY, BARRIO TEXT, DIRECCION, TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Agregar un usuario
    public void addUser(Usuario usuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PHONE_NUMBER", usuario.getNumeroTelefonico());
        values.put("NOMBRE", usuario.getNombre());// User Phone Number
        try {
            db.insert(TABLE_USERS, null, values);
            db.close(); // Closing database connection
        }
        catch (Exception e)
        {
            new Exception("El usuario ya existe");
        }
    }

    // Getting one user
    public Usuario getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{"PHONE_NUMBER","NOMBRE"}, "PHONE_NUMBER" + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Usuario contact = new Usuario(cursor.getString(0), cursor.getString(1));

        return contact;
    }

    public void agregarSitio(Sitio nSitio) throws Exception
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nSitio.getNombre());
        values.put("BARRIO", nSitio.getBarrio());
        values.put("DIRECCION", nSitio.getDirección());
        try
        {
            long resultado = db.insert(TABLE_SITIOS_FAVORITOS, null, values);
        }
        catch(Exception e)
        {
            new Exception("Nombre del sitio ya existe");
        }
        db.close();
    }

    public int editarSitio(String nombreSitio, Sitio nSitio)
    {
        int modificaciones=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nSitio.getNombre());
        values.put("BARRIO", nSitio.getBarrio());
        values.put("DIRECCION", nSitio.getDirección());
        try
        {
            modificaciones= db.update(TABLE_SITIOS_FAVORITOS, values, "NOMBRE='"+nombreSitio+"'",null);

        }
        catch(Exception e)
        {
            new Exception("Error persistiendo");
        }
        db.close();
        return modificaciones;
    }

    public int eliminarSitio(String nombreSitio)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SITIOS_FAVORITOS, "NOMBRE='"+nombreSitio+"'",null);
    }

    public List<Sitio> darSitios()
    {
        ArrayList<Sitio> resultado = new ArrayList<Sitio>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SITIOS_FAVORITOS, new String[]{"NOMBRE", "BARRIO", "DIRECCION"}, null, null, null, null, null, null);
        if(cursor!=null)
        {
            if(cursor.getCount()>0)
            {
                cursor.moveToFirst();
                do
                {
                    Sitio actual= new Sitio(cursor.getString(0), cursor.getString(1),  cursor.getString(2));
                    resultado.add(actual);
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
        db.close();
        return resultado;
    }

    public void agregarContacto(Contacto contacto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("PHONE_NUMBER", contacto.getNumeroTelefonico());
        values.put("NOMBRE", contacto.getNombre());
        values.put("FAVORITO", contacto.isSelected());
        try
        {
            long resultado = db.insert(TABLE_EMERGENCY_CONTACTS, null, values);
        }
        catch(Exception e)
        {
            new Exception("Contacto ya existe");
        }
        db.close();
    }

    public List<Contacto> darContactos()
    {
        ArrayList<Contacto> resultado = new ArrayList<Contacto>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_EMERGENCY_CONTACTS, new String[]{"PHONE_NUMBER", "NOMBRE", "FAVORITO"}, null, null, null, null, null, null);
        if(cursor!=null)
        {
            if(cursor.getCount()>0)
            {
                cursor.moveToFirst();
                do
                {
                    Contacto actual= new Contacto(cursor.getString(1),cursor.getString(0));
                    actual.setSelected(Integer.valueOf(cursor.getString(2)));
                    resultado.add(actual);
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
        db.close();
        return resultado;
    }

    public int eliminarContacto(String telefono)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EMERGENCY_CONTACTS, "PHONE_NUMBER='"+telefono+"'",null);
    }

    public void marcarContacto(Contacto contacto, int seleccionado)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PHONE_NUMBER", contacto.getNumeroTelefonico());
        values.put("NOMBRE", contacto.getNombre());
        values.put("FAVORITO", seleccionado);
        try
        {
            db.update(TABLE_EMERGENCY_CONTACTS, values, "PHONE_NUMBER='"+contacto.getNumeroTelefonico()+"'",null);
        }
        catch(Exception e)
        {
            new Exception("Error persistiendo");
        }
        db.close();
    }
}
