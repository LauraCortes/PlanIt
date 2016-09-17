package com.example.laura.planit.Logica;

/**
 * Created by Usuario on 17/09/2016.
 */
public class MedioTransporte
{
    private String nombre;
    private int horaRegreso;
    private int minRegreso;
    private Sitio sitioRegreso;
    private String direccionRegreso;
    private boolean compartir;
    private int cupoCompartido;


    public MedioTransporte() {
    }

    public MedioTransporte(String nombre, int horaRegreso, int minRegreso, Sitio sitioRegreso, String direccionRegreso, boolean compartir, int cupoCompartido) {
        this.nombre = nombre;
        this.horaRegreso = horaRegreso;
        this.minRegreso = minRegreso;
        this.sitioRegreso = sitioRegreso;
        this.direccionRegreso = direccionRegreso;
        this.compartir = compartir;
        this.cupoCompartido = cupoCompartido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getHoraRegreso() {
        return horaRegreso;
    }

    public void setHoraRegreso(int horaRegreso) {
        this.horaRegreso = horaRegreso;
    }

    public int getMinRegreso() {
        return minRegreso;
    }

    public void setMinRegreso(int minRegreso) {
        this.minRegreso = minRegreso;
    }

    public Sitio getSitioRegreso() {
        return sitioRegreso;
    }

    public void setSitioRegreso(Sitio sitioRegreso) {
        this.sitioRegreso = sitioRegreso;
    }

    public String getDireccionRegreso() {
        return direccionRegreso;
    }

    public void setDireccionRegreso(String direccionRegreso) {
        this.direccionRegreso = direccionRegreso;
    }

    public boolean isCompartir() {
        return compartir;
    }

    public void setCompartir(boolean compartir) {
        this.compartir = compartir;
    }

    public int getCupoCompartido() {
        return cupoCompartido;
    }

    public void setCupoCompartido(int cupoCompartido) {
        this.cupoCompartido = cupoCompartido;
    }
}
