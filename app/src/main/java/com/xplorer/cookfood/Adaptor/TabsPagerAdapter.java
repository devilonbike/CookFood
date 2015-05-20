package com.xplorer.cookfood.Adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Fragment.IngredientsFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private List<IngredientsFragment> fragments = new ArrayList<IngredientsFragment>();


    public static String[] TITLES;
    public TabsPagerAdapter(FragmentManager fm,List<IngredientsFragment> list) {
        super(fm);
        fragments = list;
        this.TITLES= CookFoodApp.getInstance().IngredientsTitle;
    }

    public IngredientsFragment getFragment(int position){
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
