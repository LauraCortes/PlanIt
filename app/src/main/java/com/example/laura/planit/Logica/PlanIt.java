package com.example.laura.planit.Logica;

import com.example.laura.planit.Persistencia.DBHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Usuario on 15/09/2016.
 */
public class PlanIt
{
    private static PlanIt instancia;

    private DBHandler db;

    private List<Sitio> sitios;

    private List<Evento> eventos;

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

    public boolean existeSitio(String nombre)
    {
        for(Sitio sitio:sitios)
        {
            if(sitio.getNombre().equals(nombre))
            {
                return true;
            }
        }
        return false;
    }

    public void eliminarSitio(int posicion)
    {
        sitios.remove(posicion);
    }

    public List<Sitio> darSitios()
    {
        return sitios;
    }

    public Evento agregarEvento (String nombreEvento, String descripcionEvento, String puntoEncuentro, MedioTransporte medioRegreso, int horaEncuentro, int minutosEncuentro, List<Usuario> invitados)
    {
        Evento agregado = new Evento(nombreEvento,descripcionEvento,puntoEncuentro,medioRegreso,horaEncuentro,minutosEncuentro,invitados);
        eventos.add(agregado);
        return agregado;
    }

    public Evento darEventoPos (int pos)
    {
        return eventos.get(pos);
    }

    public boolean existeEventoNombre(String nombreEvento)
    {
        for(Sitio sitio: sitios)
        {
            if(sitio.getNombre().equals(nombreEvento))
            {
                return true;
            }
        }
        return false;
    }

    public void eliminarEvento (int pos)
    {
        eventos.remove(pos);
    }

    public List<Evento> darEventos()
    {
        return eventos;
    }

}
