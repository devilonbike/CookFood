package com.xplorer.cookfood.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.xplorer.cookfood.Adaptor.RecipeAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchRecipeActivity extends ActionBarActivity {

    String SearchString;
    Context mContext;
    private static final String TAG = "SearchRecipeActivity";

    @InjectView(R.id.lv_search_recipe)
    ListView lv_search_recipe;

    @InjectView(R.id.tv_search_recipe_info)
    TextView tv_search_recipe_info;

    public int ActionBarColor;
    public int StatusBarColor;

    RecipeAdaptor recpAdapt;
    public List<RecipeData> RecipeList;
    MenuItem searchItem;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        mContext = this;
        ButterKnife.inject(this);
        SearchString="";

        InitActionBar();
        SetUpMainContent();
        handleIntent(getIntent());

    }

    public void InitActionBar() {

        ActionBarColor = getResources().getColor(R.color.LightGold);
        StatusBarColor = CookFoodApp.getInstance().getDarkColor(ActionBarColor);


        getSupportActionBar().setTitle("Search Recipe");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ActionBarColor));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(StatusBarColor);

        }
    }

    public void SetUpMainContent() {

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        RecipeList = new ArrayList<RecipeData>();
        recpAdapt = new RecipeAdaptor(mContext, RecipeList);


        lv_search_recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String RecipeDataObjectId =RecipeList.get(position).getObjectId();
                startActivity(new Intent(mContext, CreateRecipeActivity.class).putExtra("command","play").putExtra("RecipeDataObjectId",RecipeDataObjectId));


            }
        });
        lv_search_recipe.setAdapter(recpAdapt);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_recipe, menu);
        searchItem = menu.findItem(R.id.action_search_recipe_menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        ComponentName cn = new ComponentName(this, SearchRecipeActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));


        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.setQuery(SearchString, false);
                return true;  // Return true to expand action view
            }
        });

        return true;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("onNewIntent","CAll");
        handleIntent(intent);
        if(searchItem!=null){
            Log.d("onNewIntent","searchItem!=null");
            MenuItemCompat.collapseActionView(searchItem);
        }
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            SearchString = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG+ " handleIntent", SearchString);
            getSupportActionBar().setTitle(SearchString);


            FetchRecipeWithThisTitle();
        }
    }

    public void FetchRecipeWithThisTitle(){
        List<ParseQuery<RecipeData>> queries = new ArrayList<ParseQuery<RecipeData>>();

        //---search by Title
        ParseQuery<RecipeData> q1 = ParseQuery.getQuery("RecipeData");
        q1.whereContains("Title", SearchString.toLowerCase());

        //---search by Description
        ParseQuery<RecipeData> q2 = ParseQuery.getQuery("RecipeData");
        q2.whereContains("Description", SearchString.toLowerCase());

        //---search by Category
        ParseQuery<RecipeData> q3 = ParseQuery.getQuery("RecipeData");
        q3.whereContains("Category", SearchString.toLowerCase());


        queries.add(q1);
        queries.add(q2);
        queries.add(q3);

        ParseQuery<RecipeData> query  = ParseQuery.or(queries);

        query.orderByDescending("createdAt");


        CookFoodApp.getInstance().RunPreLoader(mContext);
        query.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(List<RecipeData> productListTemp, ParseException e) {
                CookFoodApp.getInstance().pd.dismiss();

                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_LONG).show();
                    return;
                }
                RecipeList.clear();
                RecipeList.addAll(productListTemp);
                Log.d(TAG+" productListTemp", productListTemp.size()+"");
                Log.d(TAG+" RecipeList", RecipeList.size()+"");

                recpAdapt.notifyDataSetChanged();

                if(RecipeList.size()==0){
                    tv_search_recipe_info.setVisibility(View.VISIBLE);
                    lv_search_recipe.setVisibility(View.GONE);

                }else{
                    tv_search_recipe_info.setVisibility(View.GONE);
                    lv_search_recipe.setVisibility(View.VISIBLE);
                }

            };


        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
