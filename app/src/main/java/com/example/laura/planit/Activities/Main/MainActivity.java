package com.example.laura.planit.Activities.Main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.laura.planit.Activities.Contactos.AgregarContactoActivity;
import com.example.laura.planit.Activities.Contactos.ContactosTabFragment;
import com.example.laura.planit.Activities.Eventos.MisEventosTabFragment;
import com.example.laura.planit.Activities.RegistroActivity;
import com.example.laura.planit.Activities.Sitios.SitiosTabFragment;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Fragments.TabsFragmenPageAdapter;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Persistencia.DBHandler;
import com.example.laura.planit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{

    //Base de datos
    private DBHandler db ;
    private TabsFragmenPageAdapter tabContactos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crear el toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Preparar las pestañas
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("EVENTOS").setIcon(R.drawable.calendar_tab));
        //tabs.addTab(tabs.newTab().setText("MOVIMIENTOS").setIcon(R.drawable.movimientos_tab));

        tabs.addTab(tabs.newTab().setText("SITIOS").setIcon(R.drawable.sitios_favoritos_tab));
        tabs.addTab(tabs.newTab().setText("AMIGOS").setIcon(R.drawable.amigos_favoritos_tab));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        // Setear adaptador al viewpager.
        final ViewPager viewPagerTabs = (ViewPager) findViewById(R.id.pager);
        final TabsFragmenPageAdapter adapter = new TabsFragmenPageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPagerTabs.setAdapter(adapter);
        viewPagerTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTabs.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        db = new DBHandler(this);
        PlanIt mundo = PlanIt.darInstancia();
        mundo.setDB(db);
        mundo.inicializar();
        mundo=null;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void agregarContactos(View view)
    {
        Intent i = new Intent(this, AgregarContactoActivity.class);
        startActivity(i);
    }

    public void Registrar(View view) {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }

    public void lanzarSitios(View view)
    {
        Intent i = new Intent(this, SitiosTabFragment.class);
        startActivity(i);
    }

    public void contactosEmergencia(View view)
    {
        Intent i = new Intent(this, ContactosTabFragment.class);
        startActivity(i);
    }





    @Override
    public void onBackPressed()
    {
        boolean ejecutado =false;
        for(android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments())
        {
            TabFragment actual = (TabFragment)fragment;
            if(actual!= null && actual.isVisible() && actual.hayItemsSeleccionados())
            {
                actual.deseleccionar();
                ejecutado=true;
                break;
            }
        }
        if(!ejecutado)
        {
            super.onBackPressed();
        }
    }
}
