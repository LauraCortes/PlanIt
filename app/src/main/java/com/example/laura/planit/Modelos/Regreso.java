package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Laura on 22/11/2016.
 */

public class Regreso implements Serializable {

    private String numeroDueño;
    private Date horaRegreso;
    private String medioRegreso;
    private int cupos;
    private Sitio destino;
    private int tiempoEstimado;
    private String[] compartido;

    public Regreso() {

    }

    public Regreso(String numeroDueño, Date horaRegreso, String medioRegreso, int cupos, Sitio destino, int tiempoEstimado)
    {
        this.numeroDueño=numeroDueño;
        this.horaRegreso=horaRegreso;
        this.medioRegreso=medioRegreso;
        this.cupos=cupos;
        this.destino=destino;
        this.tiempoEstimado=tiempoEstimado;
        compartido=new String[cupos];

    }

    public String getNumeroDueño() {
        return numeroDueño;
    }

    public void setNumeroDueño(String numeroDueño) {
        this.numeroDueño = numeroDueño;
    }

    public Date getHoraRegreso() {
        return horaRegreso;
    }

    public void setHoraRegreso(Date horaRegreso) {
        this.horaRegreso = horaRegreso;
    }

    public String getMedioRegreso() {
        return medioRegreso;
    }

    public void setMedioRegreso(String medioRegreso) {
        this.medioRegreso = medioRegreso;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public Sitio getDestino() {
        return destino;
    }

    public void setDestino(Sitio destino) {
        this.destino = destino;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public String[] getCompartido() {
        return compartido;
    }

    public void setCompartido(String[] compartido) {
        this.compartido = compartido;
    }

    @Exclude
    public void agregarCompartido(String numero)
    {
        cupos --;
        compartido[cupos]=numero;
    }

    @Exclude
    public String darRutaElemento(String id_evento)
    {
        return "/regresos/"+id_evento+"/"+numeroDueño;
    }
}
