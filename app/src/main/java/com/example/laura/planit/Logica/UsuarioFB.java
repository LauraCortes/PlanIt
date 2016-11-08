package com.example.laura.planit.Logica;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * Created by Laura on 14/09/2016.
 */
@IgnoreExtraProperties
public class UsuarioFB implements Serializable
{

    @Exclude
    public String celular;

    public int latitud_actual;
    public int longitud_actual;
    public String nickname;
    public String nombre;
    public String pin;
    public String token;

    public UsuarioFB() {
        //Default constructor to firebase
    }

    public UsuarioFB(String celular, int latitud_actual, int longitud_actual, String nickname, String nombre, String pin, String token) {
        this.celular = celular;
        this.latitud_actual = latitud_actual;
        this.longitud_actual = longitud_actual;
        this.nickname = nickname;
        this.nombre = nombre;
        this.pin = pin;
        this.token = token;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getLatitud_actual() {
        return latitud_actual;
    }

    public void setLatitud_actual(int latitud_actual) {
        this.latitud_actual = latitud_actual;
    }

    public int getLongitud_actual() {
        return longitud_actual;
    }

    public void setLongitud_actual(int longitud_actual) {
        this.longitud_actual = longitud_actual;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Exclude
    public String darRutaElemento()
    {
        return "/usuarios/"+celular;
    }

    @Exclude
    public boolean existeUsuario()
    {
        return false;
    }

    @Exclude
    public static String cifrar_SHA_256(String input)
    {
        try
        {
            input="\\u00F1opil\\u00E2[[!\\u00A1?=%$#{]\\u00E0sdf"+input+"opil\\u00E";
            MessageDigest mDigest = MessageDigest.getInstance("SHA256");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
