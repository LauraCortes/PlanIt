package com.example.laura.planit.Logica;

import com.example.laura.planit.Persistencia.DBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/09/2016.
 */
public class PlanIt
{
    private static PlanIt instancia;

    private DBHandler db;

    private List<Sitio> sitios;

    public static PlanIt darInstancia()
    {
        if(instancia==null)
        {
            instancia=new PlanIt();
        }
        return instancia;
    }

    public void setDB(DBHandler db)
    {
        this.db=db;
    }

    public DBHandler getDb() {
        return db;
    }

    private PlanIt()
    {
    }

    public void inicializar()
    {
        sitios=db.darSitios();
    }

    public Sitio agregarSitio(String nNombre, String nBarrio, String nDireccion)
    {
        Sitio agregado = new Sitio(nNombre,nBarrio,nDireccion);
        sitios.add(agregado);
        return agregado;
    }

    public Sitio editarSitio (int pos, String nNombre, String nBarrio, String nDireccion)
    {
        Sitio agregado = new Sitio(nNombre,nBarrio,nDireccion);
        sitios.set(pos,agregado);
        return agregado;
    }

    public void eliminarSitio(int posicion)
    {
        sitios.remove(posicion);
    }

    public List<Sitio> darSitios()
    {
        return sitios;
    }

}
