package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Laura on 26/11/2016.
 */

public class Movimiento implements Serializable {
    private Date fecha;
    private String descripcion;
    private String id_evento;
    private String nombre_evento;

    public Movimiento(Date fecha, String descripcion, String id_evento, String nombre_evento) {

        this.fecha = fecha;
        this.descripcion = descripcion;
        this.id_evento = id_evento;
        this.nombre_evento = nombre_evento;
    }

    public Movimiento(){}

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    @Exclude
    public String darRutaElemento(String celular)
    {
        return "/movimiento/"+celular+"/"+fecha.getTime();
    }
}
