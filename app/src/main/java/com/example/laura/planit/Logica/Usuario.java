package com.example.laura.planit.Logica;

import java.io.Serializable;

/**
 * Created by Laura on 14/09/2016.
 */
public class Usuario implements Serializable
{
    private String numeroTelefonico;

    private String nombre;

    public Usuario(String numeroTelefonico, String nombre)
    {
        this.numeroTelefonico=numeroTelefonico;
        this.nombre=nombre;
    }

    public void setNumeroTelefonico(String numeroTelefonico)
    {
        this.numeroTelefonico=numeroTelefonico;
    }

    public String getNumeroTelefonico()
    {
        return numeroTelefonico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
