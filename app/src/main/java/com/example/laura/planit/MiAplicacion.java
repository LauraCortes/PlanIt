package com.example.laura.planit;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Asistente on 12/09/2016.
 */
public class MiAplicacion extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DarwinRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
