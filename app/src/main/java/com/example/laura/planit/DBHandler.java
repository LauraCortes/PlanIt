package com.example.laura.planit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.laura.planit.Model.Sitio;
import com.example.laura.planit.Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 14/09/2016.
 */
public class DBHandler extends SQLiteOpenHelper implements Serializable {

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
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(PHONE_NUMBER INTEGER PRIMARY KEY,NOMBRE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + "(PHONE_NUMBER INTEGER PRIMARY KEY,NOMBRE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_SITIOS_FAVORITOS + "(NOMBRE TEXT PRIMARY KEY, BARRIO TEXT, DIRECCION, TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Agregar un usuario
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PHONE_NUMBER", user.getNumeroTelefonico());
        values.put("NOMBRE", user.getNombre());// User Phone Number

        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one user
    public User getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{"PHONE_NUMBER","NOMBRE"}, "PHONE_NUMBER" + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return contact;
    }

    public void agregarSitio(Sitio nSitio) throws Exception
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nSitio.getNombre());
        values.put("BARRIO", nSitio.getBarrio());
        values.put("DIRECCION", nSitio.getDirección());
        long resultado = db.insert(TABLE_SITIOS_FAVORITOS, null, values);
        db.close();
        if(resultado<0)
        {
            throw new Exception("Hubo un error guardando los datos");
        }
    }

    public List<Sitio> darSitios()
    {
        ArrayList<Sitio> resultado = new ArrayList<Sitio>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SITIOS_FAVORITOS, new String[]{"NOMBRE", "BARRIO", "DIRECCION"}, null, null, null, null, null, null);
        if(cursor!=null)
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
        db.close();
        return resultado;
    }
}
