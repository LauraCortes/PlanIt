package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laura on 17/09/2016.
 */
public class Contacto implements Serializable
{
    private String numeroTelefonico;

    private String nombre;

    //0= false, 1=true
    private boolean favorito;

    public Contacto()
    {}

    public Contacto(String nombre, String numeroTelefonico) {
        this.nombre = nombre;
        this.numeroTelefonico = numeroTelefonico;
        this.favorito =false;
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

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Exclude
    public String darRutaElemento(String celular)
    {
        return "/contacto_emergencia/"+celular+"/"+numeroTelefonico;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("favorito", favorito);
        result.put("numeroTelefonico", numeroTelefonico);
        result.put("nombre", nombre);

        return result;
    }

}
