package com.example.laura.planit.Logica;

import java.util.List;

/**
 * Created by Usuario on 17/09/2016.
 */
public class Evento
{
    private String nombreEvento,descripcionEvento, puntoEncuentro;
    private MedioTransporte medioRegreso;
    private int horaEncuentro, minutosEncuentro;
    private List<Usuario> invitados;

    public Evento() {
    }

    public Evento(String nombreEvento, String descripcionEvento, String puntoEncuentro, MedioTransporte medioRegreso, int horaEncuentro, int minutosEncuentro, List<Usuario> invitados) {
        this.nombreEvento = nombreEvento;
        this.descripcionEvento = descripcionEvento;
        this.puntoEncuentro = puntoEncuentro;
        this.medioRegreso = medioRegreso;
        this.horaEncuentro = horaEncuentro;
        this.minutosEncuentro = minutosEncuentro;
        this.invitados = invitados;
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
        return puntoEncuentro;
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

    public int getHoraEncuentro() {
        return horaEncuentro;
    }

    public void setHoraEncuentro(int horaEncuentro) {
        this.horaEncuentro = horaEncuentro;
    }

    public int getMinutosEncuentro() {
        return minutosEncuentro;
    }

    public void setMinutosEncuentro(int minutosEncuentro) {
        this.minutosEncuentro = minutosEncuentro;
    }

    public List<Usuario> getInvitados() {
        return invitados;
    }

    public void setInvitados(List<Usuario> invitados) {
        this.invitados = invitados;
    }
}
