package com.example.laura.planit.Logica;

/**
 * Created by Laura on 14/09/2016.
 */
public class User
{
    private int numeroTelefonico;

    private String nombre;

    public User(int numeroTelefonico, String nombre)
    {
        this.numeroTelefonico=numeroTelefonico;
        this.nombre=nombre;
    }

    public void setNumeroTelefonico(int numeroTelefonico)
    {
        this.numeroTelefonico=numeroTelefonico;
    }

    public int getNumeroTelefonico()
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
