package com.example.laura.planit.Persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.MedioTransporte;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.Logica.Usuario;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
    private static final String TABLE_EVENTOS="TABLA_EVENTOS";
    private static final String TABLE_INVITADOS_EVENTO="INVITADOS";

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        timeFormatter = new SimpleDateFormat("hh:mm a");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Lau, habíamos dicho que íbamos a programar en español para no tener revueltos :s
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(PHONE_NUMBER TEXT PRIMARY KEY,NOMBRE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + "(PHONE_NUMBER TEXT PRIMARY KEY,NOMBRE TEXT, FAVORITO BOOLEAN)");
        db.execSQL("CREATE TABLE " + TABLE_SITIOS_FAVORITOS + "(NOMBRE TEXT PRIMARY KEY, BARRIO TEXT, DIRECCION, TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_EVENTOS + " ( NOMBRE TEXT PRIMARY KEY, DESCRIPCION TEXT, " +
                "LUGAR_ENCUENTRO TEXT, LUGAR_EVENTO TEXT," +
                " HORA_EVENTO TEXT, FECHA_EVENTO TEXT,  TIPO_TRANSPORTE TEXT," +
                " HORA_TRANSPORTE TEXT, SITIO_TRANSPORTE TEXT, TIEMPO_TRANSPORTE INTEGER, INVITADOS INTEGER) ");
        db.execSQL("CREATE TABLE " + TABLE_INVITADOS_EVENTO + " (EVENTO TEXT, PHONE_NUMBER TEXT, PRIMARY KEY (EVENTO,PHONE_NUMBER) ) ");
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
            db.close();
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
            db.close();
            new Exception("Nombre del sitio ya existe");
        }
        db.close();
    }

    public long agregarEvento(Evento evento)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", evento.getNombreEvento());
        values.put("DESCRIPCION", evento.getDescripcionEvento());
        values.put("LUGAR_ENCUENTRO", evento.getPuntoEncuentro());
        values.put("LUGAR_EVENTO",evento.getLugar());
        values.put("HORA_EVENTO", timeFormatter.format(evento.getHoraEncuentro()));
        values.put("FECHA_EVENTO", dateFormatter.format(evento.getFechaEvento()));
        values.put("INVITADOS",evento.getInvitados()!=null?evento.getInvitados().size():0);
        MedioTransporte transporte = evento.getMedioRegreso();
        if(transporte!=null)
        {
            values.put("TIPO_TRANSPORTE",transporte.getNombre());
            values.put("HORA_TRANPORTE",timeFormatter.format(transporte.getHoraRegreso()));
            values.put("SITIO_TRANSPORTE", transporte.getDireccionRegreso());
            values.put("TIEMPO_TRANSPORTE", transporte.getTiempoAproximado());
        }
        evento=null;
        long result = db.insert(TABLE_EVENTOS, null, values);
        db.close();
        return result;
    }

    public int editarEvento(Evento evento, String nombreEvento)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOMBRE", evento.getNombreEvento());
        values.put("DESCRIPCION", evento.getDescripcionEvento());
        values.put("LUGAR_ENCUENTRO", evento.getPuntoEncuentro());
        values.put("LUGAR_EVENTO",evento.getLugar());
        values.put("HORA_EVENTO", timeFormatter.format(evento.getHoraEncuentro()));
        values.put("FECHA_EVENTO", dateFormatter.format(evento.getFechaEvento()));
        values.put("INVITADOS",evento.getInvitados()!=null?evento.getInvitados().size():0);
        MedioTransporte transporte = evento.getMedioRegreso();
        if(transporte!=null)
        {
            values.put("TIPO_TRANSPORTE",transporte.getNombre());
            values.put("HORA_TRANPORTE",timeFormatter.format(transporte.getHoraRegreso()));
            values.put("SITIO_TRANSPORTE", transporte.getDireccionRegreso());
            values.put("TIEMPO_TRANSPORTE", transporte.getTiempoAproximado());
        }
        int modificaciones= db.update(TABLE_EVENTOS, values, "NOMBRE='"+nombreEvento+"'",null);
        evento=null;
        db.close();
        return modificaciones;
    }

    public int agregarTransporteAEvento(MedioTransporte transporte, String nombreEvento)
    {
        int modificaciones=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIPO_TRANSPORTE",transporte.getNombre());
        values.put("HORA_TRANSPORTE", (transporte.getHoraRegreso().toString()));
        values.put("SITIO_TRANSPORTE", transporte.getDireccionRegreso());
        values.put("TIEMPO_TRANSPORTE", transporte.getTiempoAproximado());
        modificaciones= db.update(TABLE_EVENTOS, values, "NOMBRE='"+nombreEvento+"'",null);
        db.close();
        return modificaciones;
    }
    public int eliminarEvento(String nombreEvento)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result= db.delete(TABLE_EVENTOS, "NOMBRE='"+nombreEvento+"'",null);
        db.close();
        return result;
    }

    public List<Evento> darEventos()
    {
        ArrayList<Evento> resultado = new ArrayList<Evento>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTOS, new String[]{"NOMBRE", "DESCRIPCION", "LUGAR_ENCUENTRO", "LUGAR_EVENTO",
                "HORA_EVENTO","TIPO_TRANSPORTE","HORA_TRANSPORTE","TIEMPO_TRANSPORTE","SITIO_TRANSPORTE", "FECHA_EVENTO", "INVITADOS" }, null, null, null, null, null, null);
        if(cursor!=null)
        {
            if(cursor.getCount()>0)
            {
                cursor.moveToFirst();
                do
                {
                    try
                    {
                        Evento actual= new Evento();
                        actual.setNombreEvento(cursor.getString(0));
                        actual.setDescripcionEvento(cursor.getString(1));
                        actual.setPuntoEncuentro(cursor.getString(2));
                        actual.setLugar(cursor.getString(3));
                        System.out.println(cursor.getString(4));
                        actual.setHoraEncuentro(timeFormatter.parse(cursor.getString(4)));
                        actual.setFechaEvento(dateFormatter.parse(cursor.getString(9)));
                        MedioTransporte transporte= new MedioTransporte();
                        transporte.setNombre(cursor.getString(5));
                        String horaRegreso= cursor.getString(6);
                        if(horaRegreso!=null)
                        {
                            transporte.setHoraRegreso(new Date(java.util.Date.parse(horaRegreso)));
                        }
                        transporte.setTiempoAproximado(cursor.getInt(7));
                        transporte.setDireccionRegreso(cursor.getString(8));
                        actual.setMedioRegreso(transporte);
                        int invitados=cursor.getInt(9);
                        if(invitados>0)
                        {
                            ArrayList<Contacto> inv = new ArrayList<Contacto>();
                            for(int i=0;i<invitados;i++){inv.add(null);}
                            actual.setInvitados(inv);
                        }
                        resultado.add(actual);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
        db.close();
        return resultado;
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
            db.close();
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
        SQLiteDatabase db = this.getReadableDatabase();
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
        values.put("FAVORITO", contacto.isFavorito());
        try
        {
            long resultado = db.insert(TABLE_EMERGENCY_CONTACTS, null, values);
        }
        catch(Exception e)
        {
            db.close();
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
                    actual.setFavorito(Integer.valueOf(cursor.getString(2)));
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
