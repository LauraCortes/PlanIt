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

    //0= false, 1=true
    private int selected;

    public Contacto(String nombre, String numeroTelefonico) {
        this.nombre = nombre;
        this.numeroTelefonico = numeroTelefonico;
        this.selected=0;
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

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
