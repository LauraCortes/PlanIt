package com.example.laura.planit.Modelos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 23/11/2016.
 */
@IgnoreExtraProperties
public class ResumenEvento implements Serializable
{
    public String nombre;
    public String organizador;
    public long fecha;
    public String id_evento;

    public ResumenEvento() {
    }

    public ResumenEvento(String nombre, String organizador, long fecha, String id) {
        this.nombre = nombre;
        this.organizador = organizador;
        this.fecha = fecha;
        id_evento =id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public Map<String,Object> toMap()
    {
        Map<String,Object> result = new HashMap<>();
        result.put("nombre",nombre);
        result.put("organizador",organizador);
        result.put("fecha",fecha);
        result.put("id_evento",id_evento);
        return result;
    }
}
