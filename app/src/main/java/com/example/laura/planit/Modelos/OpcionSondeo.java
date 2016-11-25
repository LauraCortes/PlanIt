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
public class OpcionSondeo implements Serializable
{
    public Sitio lugar;
    public int votosFavor=0;

    public OpcionSondeo() {
    }

    public OpcionSondeo(Sitio lugar) {
        this.lugar = lugar;
    }

    public Sitio getLugar() {
        return lugar;
    }

    public void setLugar(Sitio lugar) {
        this.lugar = lugar;
    }

    public int getVotosFavor() {
        return votosFavor;
    }

    public void setVotosFavor(int votosFavor) {
        this.votosFavor = votosFavor;
    }

    @Exclude
    public Map<String,Object> toMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("votosFavor",votosFavor);
        map.put("lugar",lugar.toMap());
        return map;
    }

    @Exclude
    public String toString()
    {
        return "("+votosFavor+")-"+lugar.toString();
    }
}