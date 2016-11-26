package com.example.laura.planit.Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 23/11/2016.
 */
@IgnoreExtraProperties
public class ParticipanteEvento implements Serializable
{
    public boolean camino_casa=false;
    public boolean camino_evento=false;
    public boolean cancelo=false;
    public long hora_llegada_propia=-1;
    public boolean llego_casa=false;
    public boolean llego_evento=false;
    public int tiempo_llegada=60;
    public String nombre;
    public String celular;
    //En kilometros
    public int distancia=30;

    public ParticipanteEvento() {
    }

    public ParticipanteEvento(String nNombre)
    {
        nombre = nNombre;
    }

    public boolean isCamino_casa() {
        return camino_casa;
    }

    public void setCamino_casa(boolean camino_casa) {
        this.camino_casa = camino_casa;
    }

    public boolean isCamino_evento() {
        return camino_evento;
    }

    public void setCamino_evento(boolean camino_evento) {
        this.camino_evento = camino_evento;
    }

    public boolean isCancelo() {
        return cancelo;
    }

    public void setCancelo(boolean cancelo) {
        this.cancelo = cancelo;
    }

    public long getHora_llegada_propia() {
        return hora_llegada_propia;
    }

    public void setHora_llegada_propia(long hora_llegada_propia) {
        this.hora_llegada_propia = hora_llegada_propia;
    }

    public boolean isLlego_casa() {
        return llego_casa;
    }

    public void setLlego_casa(boolean llego_casa) {
        this.llego_casa = llego_casa;
    }

    public boolean isLlego_evento() {
        return llego_evento;
    }

    public void setLlego_evento(boolean llego_evento) {
        this.llego_evento = llego_evento;
    }

    public int getTiempo_llegada() {
        return tiempo_llegada;
    }

    public void setTiempo_llegada(int tiempo_llegada) {
        this.tiempo_llegada = tiempo_llegada;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Exclude
    public Map<String,Object> toMap()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("camino_casa",camino_casa);
        result.put("camino_evento",camino_evento);
        result.put("cancelo",cancelo);
        result.put("hora_llegada_propia",hora_llegada_propia);
        result.put("llego_casa",llego_casa);
        result.put("llego_evento",llego_evento);
        result.put("tiempo_llegada",tiempo_llegada);
        result.put("distancia",distancia);
        result.put("nombre",nombre);
        result.put("celular",celular);
        return result;
    }

    @Exclude
    public String toString()
    {
        String result="";
        result+=nombre+" - ";
        if(llego_casa)
        {
            result+="En casa";
        }
        else if (camino_casa)
        {
            result+="Camino a casa";
        }
        else if(llego_evento)
        {
            result+="Ya llegué al evento";
        }
        else if(camino_evento)
        {
            result+="Camino al evento ("+tiempo_llegada+" minutos, "+distancia+" km )";
        }
        else
        {
            result+="Aún no voy en camino";
        }

        return result;
    }
}
