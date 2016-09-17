package com.example.laura.planit.Logica;

import java.io.Serializable;

/**
 * Created by Usuario on 15/09/2016.
 */
public class Sitio implements Serializable
{
    private String nombre;
    private String barrio;
    private String dirección;

    public Sitio(String nombre, String barrio, String dirección) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.dirección = dirección;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
