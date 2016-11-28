package com.example.laura.planit.Fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.laura.planit.Activities.Contactos.ContactosTabFragment;
import com.example.laura.planit.Activities.Eventos.InvitacionesTabFragment;
import com.example.laura.planit.Activities.Eventos.MisEventosTabFragment;
import com.example.laura.planit.Activities.Sitios.SitiosTabFragment;
import com.example.laura.planit.Activities.SoyContacto.SoyContactoTabFragment;

/**
 * Created by Usuario on 25/09/2016.
 */

public class TabsFragmenPageAdapter extends FragmentStatePagerAdapter
{
    int numTabs;

    public final static int FRAGMENTO_MIS_EVENTOS=0;
    public final static int FRAGMENTO_INVITACIONES=1;
    public final static int FRAMGENTO_CONTACTOS_EMERGENCIA=3;
    public final static int FRAMGENTO_SITIOS=2;
    public final static int FRAGMENTO_SOY_CONTACTO=4;

    public TabsFragmenPageAdapter(FragmentManager fragmentManager, int numOfTabs)
    {
        super(fragmentManager);
        this.numTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case FRAMGENTO_CONTACTOS_EMERGENCIA:
                return new ContactosTabFragment();
            case FRAGMENTO_MIS_EVENTOS:
                return new MisEventosTabFragment();
            case FRAMGENTO_SITIOS:
                return new SitiosTabFragment();
            case FRAGMENTO_INVITACIONES :
                return new InvitacionesTabFragment();
            case FRAGMENTO_SOY_CONTACTO:
                return new SoyContactoTabFragment();
            /**
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
             */
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
