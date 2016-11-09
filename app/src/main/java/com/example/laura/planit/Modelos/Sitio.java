package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Usuario on 15/09/2016.
 */
@IgnoreExtraProperties
public class Sitio implements Serializable
{
    public int latitud;
    public int longitud;
    public String nombre;
    public String dirección;

    public Sitio() {
    }

    public Sitio(int latitud, int longitud, String nombre, String dirección) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.dirección = dirección;
    }

    public int getLatitud() {
        return latitud;
    }

    public void setLatitud(int latitud) {
        this.latitud = latitud;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    @Exclude
    public String darRutaElemento(String celular)
    {
        return "/lugares_favoritos/"+celular;
    }
}
