package com.xplorer.cookfood.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.xplorer.cookfood.Adaptor.TabsPagerAdapter;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Fragment.IngredientsFragment;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class IngredientsActivity extends ActionBarActivity {
    Context _context;
    private TabsPagerAdapter pagerAdapter;

//-------------Search-------------------//

    MenuItem searchItem;
    SearchView searchView;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip pts_titleBar;
    @InjectView(R.id.pager)
    ViewPager vp_pager;


    public int _actionBarColor;
    public int _statusBarColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);


        _context = this;
        ButterKnife.inject(this);

        InitActionBar();
        Initpager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(searchItem!=null)
            MenuItemCompat.collapseActionView(searchItem);


    }
    public void InitActionBar(){
        _actionBarColor = ContextCompat.getColor(_context,R.color.DodgerBlue);
        _statusBarColor = CookFoodApp.getInstance().getDarkColor(_actionBarColor);

        getSupportActionBar().setTitle("Choose Ingredient");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(_actionBarColor));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(_statusBarColor);

        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void Initpager(){
        List<IngredientsFragment> FragmentList = new ArrayList<IngredientsFragment>();
        for (int i = 0; i < CookFoodApp.getInstance().IngredientsTitle.length; i++) {
            FragmentList.add(new IngredientsFragment().newInstance(i));
        }

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),FragmentList);

        vp_pager.setAdapter(pagerAdapter);
        vp_pager.setOffscreenPageLimit(1);
        vp_pager.setCurrentItem(0);

        pts_titleBar.setViewPager(vp_pager);


        pts_titleBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // pagerAdapter.getFragment(position).checkIfFilterApplied();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredients, menu);
        searchItem = menu.findItem(R.id.action_ingredient_menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        ComponentName cn = new ComponentName(this, SearchIngredientActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));


        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            CookFoodApp.getInstance().CurrentCustomIngredient = null;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
