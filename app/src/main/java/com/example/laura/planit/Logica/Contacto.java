package com.example.laura.planit.Logica;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Laura on 17/09/2016.
 */
public class Contacto
{
    private int numeroTelefonico;

    private String nombre;

    private boolean selected;

    public Contacto(String nombre, int numeroTelefonico, boolean selected) {
        this.nombre = nombre;
        this.numeroTelefonico = numeroTelefonico;
        this.selected=selected;
    }

    public int getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(int numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
