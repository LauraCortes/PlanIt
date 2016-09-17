package com.example.laura.planit.Logica;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/09/2016.
 */
public class PlanIt
{
    private static PlanIt instancia;

    private List<Sitio> sitios;

    public static PlanIt darInstancia()
    {
        if(instancia==null)
        {
            instancia=new PlanIt();
        }
        return instancia;
    }

    private PlanIt()
    {
        sitios = new ArrayList<Sitio>();
    }

    public void agregarSitio(String nNombre, String nBarrio, String nDireccion)
    {
        sitios.add(new Sitio(nNombre,nBarrio,nDireccion));
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
