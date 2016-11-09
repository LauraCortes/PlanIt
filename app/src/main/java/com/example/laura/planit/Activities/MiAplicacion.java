package com.example.laura.planit.Activities;

import android.app.Application;

import com.example.laura.planit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Asistente on 12/09/2016.
 */
public class MiAplicacion extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        setTheme(R.style.AppTheme);

        //Garantiza que cuando se lanza la aplicación se carga la info


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DarwinRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}
