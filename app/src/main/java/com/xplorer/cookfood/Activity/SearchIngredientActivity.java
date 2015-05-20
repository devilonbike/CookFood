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
import com.xplorer.cookfood.Adaptor.IngredientAdaptor;
import com.xplorer.cookfood.Adaptor.MultiItemRowListAdapter;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchIngredientActivity extends ActionBarActivity {
    String SearchString;
    Context mContext;
    private static final String TAG = "SearchIngredientActivity";

    @InjectView(R.id.lv_search_ingredient)
    ListView lv_search_ingredient;

    @InjectView(R.id.tv_search_ingredient_info)
    TextView tv_search_ingredient_info;

    public int ActionBarColor;
    public int StatusBarColor;

    IngredientAdaptor IngAdapr;
    public List<Ingredients> ingredientItems;
    MenuItem searchItem;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredient);

        mContext = this;
        ButterKnife.inject(this);
        SearchString="";

        InitActionBar();
        SetUpMainContent();
        handleIntent(getIntent());

    }

    public void InitActionBar() {

        ActionBarColor = getResources().getColor(R.color.DodgerBlue);
        StatusBarColor = CookFoodApp.getInstance().getDarkColor(ActionBarColor);


        getSupportActionBar().setTitle("Search Recipe");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ActionBarColor));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(StatusBarColor);

        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void SetUpMainContent() {

        ingredientItems = new ArrayList<Ingredients>();
        IngAdapr = new IngredientAdaptor(mContext,ingredientItems, -1 );
        MultiItemRowListAdapter twoRowAdapter = new MultiItemRowListAdapter(mContext, IngAdapr, 3, 5 );


        lv_search_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String RecipeDataObjectId =ingredientItems.get(position).getObjectId();
                startActivity(new Intent(mContext, CreateRecipeActivity.class).putExtra("command","play").putExtra("RecipeDataObjectId",RecipeDataObjectId));


            }
        });
        lv_search_ingredient.setAdapter(twoRowAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_ingredient, menu);
        searchItem = menu.findItem(R.id.action_search_ingredient_menu_search);
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
                searchView.setQuery(SearchString, false);
                return true;  // Return true to expand action view
            }
        });

        return true;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d( TAG+" onNewIntent", "CAll");
        handleIntent(intent);
        if(searchItem!=null){
            Log.d(TAG+" onNewIntent","searchItem!=null");
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


        List<ParseQuery<Ingredients>> queries = new ArrayList<ParseQuery<Ingredients>>();

        //---search by lower case name
        ParseQuery<Ingredients> q1 = ParseQuery.getQuery("Ingredients");
        q1.whereContains("Name", SearchString.toLowerCase());

        //---search by uppercase case name

        ParseQuery<Ingredients> q3 = ParseQuery.getQuery("Ingredients");
        q3.whereContains("Name", CookFoodApp.getInstance().UpperCase(SearchString.toLowerCase()));


        //---search by name
        ParseQuery<Ingredients> q2 = ParseQuery.getQuery("Ingredients");
        q2.whereContains("NameHindi", SearchString.toLowerCase());

        //---search by uppercase case name

        ParseQuery<Ingredients> q4 = ParseQuery.getQuery("Ingredients");
        q4.whereContains("NameHindi", CookFoodApp.getInstance().UpperCase(SearchString.toLowerCase()));


        queries.add(q1);
        queries.add(q2);
        queries.add(q3);
        queries.add(q4);

        ParseQuery<Ingredients> query  = ParseQuery.or(queries);

        query.orderByDescending("Name");

        CookFoodApp.getInstance().RunPreLoader(mContext);
        query.findInBackground(new FindCallback<Ingredients>() {
            @Override
            public void done(List<Ingredients> productListTemp, ParseException e) {
                CookFoodApp.getInstance().pd.dismiss();
                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_LONG).show();
                    return;
                }
                ingredientItems.clear();
                ingredientItems.addAll(productListTemp);
                Log.d(TAG+" productListTemp", productListTemp.size()+"");
                Log.d(TAG+" ingredientItems", ingredientItems.size()+"");

                IngAdapr.notifyDataSetChanged();

                if(ingredientItems.size()==0){
                    tv_search_ingredient_info.setVisibility(View.VISIBLE);
                    lv_search_ingredient.setVisibility(View.GONE);

                }else{
                    tv_search_ingredient_info.setVisibility(View.GONE);
                    lv_search_ingredient.setVisibility(View.VISIBLE);
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

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
