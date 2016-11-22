package com.example.laura.planit.Modelos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 17/09/2016.
 */
@IgnoreExtraProperties
public class Evento implements Serializable
{
    public String celular_organizador;
    public String descripcion;
    public long fecha;
    public Sitio lugar;
    public boolean lugar_definitivo;
    public boolean lugar_fijo;
    public String nombre;
    public String nombre_organizador;
    public int cantidad_invitados;

    public Evento() {
    }

    public String getCelular_organizador() {
        return celular_organizador;
    }

    public void setCelular_organizador(String celular_organizador) {
        this.celular_organizador = celular_organizador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public Sitio getLugar() {
        return lugar;
    }

    public void setLugar(Sitio lugar) {
        this.lugar = lugar;
    }

    public boolean isLugar_definitivo() {
        return lugar_definitivo;
    }

    public void setLugar_definitivo(boolean lugar_definitivo) {
        this.lugar_definitivo = lugar_definitivo;
    }

    public boolean isLugar_fijo() {
        return lugar_fijo;
    }

    public void setLugar_fijo(boolean lugar_fijo) {
        this.lugar_fijo = lugar_fijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_organizador() {
        return nombre_organizador;
    }

    public void setNombre_organizador(String nombre_organizador) {
        this.nombre_organizador = nombre_organizador;
    }

    public int getCantidad_invitados() {
        return cantidad_invitados;
    }

    public void setCantidad_invitados(int cantidad_invitados) {
        this.cantidad_invitados = cantidad_invitados;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String,Object> result = new HashMap<>();
        result.put("celular_organizador",celular_organizador);
        result.put("descripcion",descripcion);
        result.put("fecha",fecha);
        result.put("lugar_definitivo",lugar_definitivo);
        result.put("lugar_fijo",lugar_fijo);
        result.put("nombre",nombre);
        result.put("nombre_organizador",nombre_organizador);
        result.put("cantidad_invitados",cantidad_invitados);
        if(lugar!=null)
        {
            result.put("lugar",lugar.toMap());
        }
        return result;
    }
}
