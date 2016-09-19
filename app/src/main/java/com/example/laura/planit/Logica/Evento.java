package com.example.laura.planit.Logica;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 17/09/2016.
 */
public class Evento implements Serializable
{
    private String nombreEvento,descripcionEvento, puntoEncuentro, lugar;
    private MedioTransporte medioRegreso;
    Date horaEncuentro, fechaEvento;
    private List<Contacto> invitados;
    private Sitio puntoEncuentroObjeto;
    private Sitio lugarEventoObjeto;
    public Evento() {
    }

    public Evento(String nombreEvento, String descripcionEvento, String lugar, String puntoEncuentro, MedioTransporte medioRegreso, Date horaEncuentro, Date fechaEvento , List<Contacto> invitados) {
        this.nombreEvento = nombreEvento;
        this.descripcionEvento = descripcionEvento;
        this.puntoEncuentro = puntoEncuentro;
        this.medioRegreso = medioRegreso;
        this.horaEncuentro = horaEncuentro;
        this.fechaEvento =fechaEvento;
        this.invitados = invitados;
        this.lugar=lugar;
        puntoEncuentro=null;
        lugarEventoObjeto=null;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getPuntoEncuentro() {

        if(puntoEncuentroObjeto==null)
        {
            return puntoEncuentro;
        }
        else
        {
            return puntoEncuentroObjeto.toString();
        }
    }

    public void setPuntoEncuentro(String puntoEncuentro) {
        this.puntoEncuentro = puntoEncuentro;
    }

    public MedioTransporte getMedioRegreso() {
        return medioRegreso;
    }

    public void setMedioRegreso(MedioTransporte medioRegreso) {
        this.medioRegreso = medioRegreso;
    }

    public List<Contacto> getInvitados() {
        return invitados;
    }

    public void setInvitados(List<Contacto> invitados) {
        this.invitados = invitados;
    }

    public Date getHoraEncuentro() {
        return horaEncuentro;
    }

    public void setHoraEncuentro(Date horaEncuentro) {
        this.horaEncuentro = horaEncuentro;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getLugar() {

        if(lugarEventoObjeto==null)
        {
            return lugar;
        }
        else
        {
            return lugarEventoObjeto.toString();
        }
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setPuntoEncuentroObjeto(Sitio puntoEncuentroObjeto) {
        this.puntoEncuentroObjeto = puntoEncuentroObjeto;
    }

    public void setLugarEventoObjeto(Sitio lugarEventoObjeto) {
        this.lugarEventoObjeto = lugarEventoObjeto;
    }

    public Sitio getPuntoEncuentroObjeto() {
        return puntoEncuentroObjeto;
    }

    public Sitio getLugarEventoObjeto() {
        return lugarEventoObjeto;
    }


    public String toStringSMS()
    {
        return nombreEvento+": "+descripcionEvento+"\n el día "+fechaEvento.toString()+" a las "+horaEncuentro+". Nos vemos en "+puntoEncuentro;
    }
}
