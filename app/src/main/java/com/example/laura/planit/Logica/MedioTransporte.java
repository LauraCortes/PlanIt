package com.example.laura.planit.Logica;

import java.util.Date;

/**
 * Created by Usuario on 17/09/2016.
 */
public class MedioTransporte
{
    private String nombre;
    Date horaRegreso;
    private Sitio sitioRegreso;
    private String direccionRegreso;
    private int tiempoAproximado;
    //private boolean compartir;
    //private int cupoCompartido;


    public MedioTransporte() {
    }

    public MedioTransporte(String nombre, Date horaRegreso, String direccionRegreso, int tiempoAproximado)
    {
        this.nombre = nombre;
        this.horaRegreso = horaRegreso;
        this.direccionRegreso = direccionRegreso;
        this.tiempoAproximado = tiempoAproximado;
    }

    public void setSitioRegreso(Sitio sitioRegreso) {
        this.sitioRegreso = sitioRegreso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getHoraRegreso() {
        return horaRegreso;
    }

    public void setHoraRegreso(Date horaRegreso) {
        this.horaRegreso = horaRegreso;
    }

    public String getDireccionRegreso() {
        if(sitioRegreso==null)
        {
            return direccionRegreso;
        }
        else
        {
            return sitioRegreso.toString();
        }
    }

    public void setDireccionRegreso(String direccionRegreso) {
        this.direccionRegreso = direccionRegreso;
    }

    public int getTiempoAproximado() {
        return tiempoAproximado;
    }

    public void setTiempoAproximado(int tiempoAproximado) {
        this.tiempoAproximado = tiempoAproximado;
    }
}
