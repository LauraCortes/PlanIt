package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Usuario on 26/11/2016.
 */
@IgnoreExtraProperties
public class Movimiento {

    @Exclude
    public final static String LLEGO_CASA="Llegó a casa";
    @Exclude
    public final static String LLEGO_EVENTO="Llegó al evento";
    @Exclude
    public final static String CAMINO_EVENTO="Camino al evento";
    @Exclude
    public final static String CAMINO_CASA="Camino a casa";



    public String descripcion;
    public String id_evento;
    public String nombre_evento;
    public long hora_movimiento;

    public Movimiento() {
    }

    public Movimiento(String id_evento, String nombre_evento) {
        this.id_evento = id_evento;
        this.nombre_evento = nombre_evento;
        hora_movimiento=System.currentTimeMillis();
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

    public long getHora_movimiento() {
        return hora_movimiento;
    }

    public void setHora_movimiento(long hora_movimiento) {
        this.hora_movimiento = hora_movimiento;
    }
}
