package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 15/09/2016.
 */
@IgnoreExtraProperties
public class Sitio implements Serializable
{
    public double latitud;
    public double longitud;
    public String nombre;
    public String direccion;

    public Sitio() {
    }

    public Sitio(double latitud, double longitud, String nombre, String direccion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion()
    {
        if(direccion!=null && !direccion.trim().isEmpty())
            return direccion;
        else
        {
            return getCoordenadas();
        }
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Exclude
    public String darRutaElemento(String celular)
    {
        return "/lugares_favoritos/"+celular+"/"+nombre;
    }

    @Exclude
    public String getCoordenadas()
    {
        return latitud+", "+longitud;
    }

    @Exclude
    public String toString()
    {
        return nombre+": "+getDireccion();
    }

    public Map<String, Object> toMap() {

        HashMap<String,Object> result = new HashMap<>();
        result.put("latitud",latitud);
        result.put("longitud",longitud);
        result.put("nombre",nombre);
        result.put("direccion",direccion);
        return result;
    }
}
