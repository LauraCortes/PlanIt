package com.example.laura.planit.Logica;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Laura on 17/09/2016.
 */
public class Contacto implements Serializable
{
    private String numeroTelefonico;

    private String nombre;

    private boolean selected;

    public Contacto(String nombre, String numeroTelefonico, boolean selected) {
        this.nombre = nombre;
        this.numeroTelefonico = numeroTelefonico;
        this.selected=selected;
    }

    public String getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
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
