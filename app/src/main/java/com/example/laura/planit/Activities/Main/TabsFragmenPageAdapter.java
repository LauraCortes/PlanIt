package com.example.laura.planit.Activities.Main;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.laura.planit.Activities.Contactos.ContactosTabFragment;

/**
 * Created by Usuario on 25/09/2016.
 */

public class TabsFragmenPageAdapter extends FragmentStatePagerAdapter
{
    int numTabs;

    public TabsFragmenPageAdapter(FragmentManager fragmentManager, int numOfTabs)
    {
        super(fragmentManager);
        this.numTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                ContactosTabFragment tab1 = new ContactosTabFragment();
                return tab1;
            /**
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
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
