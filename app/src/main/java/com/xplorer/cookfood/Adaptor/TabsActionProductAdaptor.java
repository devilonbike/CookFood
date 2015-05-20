package com.xplorer.cookfood.Adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xplorer.cookfood.Fragment.ActionProductFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghavendra on 06-05-2015.
 */
public class TabsActionProductAdaptor extends FragmentPagerAdapter {
    private List<ActionProductFragment> fragments = new ArrayList<ActionProductFragment>();


    public static String[] TITLES = {
            "Actions",
            "Products"
    };
    public TabsActionProductAdaptor(FragmentManager fm,List<ActionProductFragment> list) {
        super(fm);
        fragments = list;
    }

    public ActionProductFragment getFragment(int position){
        return fragments.get(position);
    }

    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
