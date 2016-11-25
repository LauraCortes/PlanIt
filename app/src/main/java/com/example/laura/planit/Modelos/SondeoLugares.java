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
    public Map<String,OpcionSondeo> opciones=new HashMap<>();
    public Map<String, String> votaron=new HashMap<>();

    public SondeoLugares()
    {};

    public SondeoLugares(int cantidadVotantes, List<Sitio> opciones_Sitio) {
        this.cantidadVotantes = cantidadVotantes;
        int i=0;
        for(Sitio sitioActual: opciones_Sitio)
        {
            opciones.put("opcion"+i,new OpcionSondeo(sitioActual));
            i++;
        }
    }

    public Map<String, String> getVotaron() {
        return votaron;
    }

    public void setVotaron(Map<String, String> votaron) {
        this.votaron = votaron;
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

    public Map<String,OpcionSondeo> getOpciones() {
        return opciones;
    }

    public void setOpciones(Map<String,OpcionSondeo> opciones) {
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
        map.put("opciones",opciones);
        return  map;
    }
}
