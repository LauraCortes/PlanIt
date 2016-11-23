package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 23/11/2016.
 */
@IgnoreExtraProperties
public class SondeoLugares implements Serializable{

    public int cantidadVotantes;
    public int votosActuales=0;
    public boolean cerrado=false;
    public Sitio lugarActual=null;
    public List<OpcionSondeo> opciones=new ArrayList<>();

    public SondeoLugares()
    {};

    public SondeoLugares(int cantidadVotantes, List<Sitio> opciones_Sitio) {
        this.cantidadVotantes = cantidadVotantes;
        for(Sitio sitioActual: opciones_Sitio)
        {
            opciones.add(new OpcionSondeo(sitioActual));
        }
    }

    public int getCantidadVotantes() {
        return cantidadVotantes;
    }

    public void setCantidadVotantes(int cantidadVotantes) {
        this.cantidadVotantes = cantidadVotantes;
    }

    public int getVotosActuales() {
        return votosActuales;
    }

    public void setVotosActuales(int votosActuales) {
        this.votosActuales = votosActuales;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

    public Sitio getLugarActual() {
        return lugarActual;
    }

    public void setLugarActual(Sitio lugarActual) {
        this.lugarActual = lugarActual;
    }

    public List<OpcionSondeo> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionSondeo> opciones) {
        this.opciones = opciones;
    }

    @Exclude
    public Map<String,Object> toMap()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("cantidadVotantes",cantidadVotantes);
        map.put("votosActuales",votosActuales);
        map.put("cerrado",cerrado);
        if(lugarActual!=null)
        {
            map.put("lugarActual",lugarActual.toMap());
        }
        map.put("lugarActual",lugarActual);
        for(int i=0; i<opciones.size();i++)
        {
            map.put("opcion"+i,opciones.get(i).toMap());
        }
        return  map;
    }
}
