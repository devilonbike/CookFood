package com.xplorer.cookfood.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.xplorer.cookfood.Adaptor.RecipeAdaptor;
import com.xplorer.cookfood.Adaptor.RecipeEditAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.Object.UserInfo;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeActivity extends ActionBarActivity {

    Context mContext;
    private static final String TAG = "RecipeActivity";

    public int ActionBarColor;
    public int StatusBarColor;

    @InjectView(R.id.lv_recipe)
    ListView lv_recipe;

    @InjectView(R.id.tv_activity_recipe_info)
    TextView tv_activity_recipe_info;



    View footerView;

    boolean loadingMore = false;
    boolean GotAllData = false;
    RecipeAdaptor recpAdapt;
    RecipeEditAdaptor recpEditAdapt;
    public List<RecipeData> RecipeList;
    String TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TITLE!=null) {
            if (TITLE.equalsIgnoreCase("My Recipes")) {
                Log.d(TAG, "onResume My Recipes");
                SetUpMainContent();
            } else if (TITLE.equalsIgnoreCase("Saved Recipes")) {
                Log.d(TAG, "onResume Saved Recipes");

            }
        }
    }

    public void InitActionBar() {

        if (TITLE.equalsIgnoreCase("My Recipes")) {
            ActionBarColor = getResources().getColor(R.color.LightGold);
        }else if (TITLE.equalsIgnoreCase("Saved Recipes")) {
            ActionBarColor = getResources().getColor(R.color.LightGold);
        }
        StatusBarColor = CookFoodApp.getInstance().getDarkColor(ActionBarColor);


        getSupportActionBar().setTitle(TITLE);
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

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        footerView = mInflater.inflate(R.layout.footer_loadmore, null, false);

        RecipeList = new ArrayList<RecipeData>();
        recpAdapt = new RecipeAdaptor(mContext, RecipeList);
        recpEditAdapt= new RecipeEditAdaptor(mContext, RecipeList);

        GotAllData = false;
        loadingMore = false;
        CookFoodApp.getInstance().RunPreLoader(mContext);
        lv_recipe.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                //is the bottom item visible & not loading more already ? Load more !
                if ((lastInScreen == totalItemCount) && !(loadingMore) && !GotAllData) {
                    loadingMore = true;
                    lv_recipe.addFooterView(footerView, null, false);
                    FetchRecipe();
                }
            }
        });


        if(TITLE.equalsIgnoreCase("My Recipes")){
            lv_recipe.setAdapter(recpEditAdapt);
        }else{
            lv_recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (TITLE.equalsIgnoreCase("My Recipes")) {

                    }else{
                        String RecipeDataObjectId =RecipeList.get(position).getObjectId();
                        startActivity(new Intent(mContext, CreateRecipeActivity.class).putExtra("command","play").putExtra("RecipeDataObjectId",RecipeDataObjectId));

                    }
                }
            });
            lv_recipe.setAdapter(recpAdapt);

        }

    }

    public void FetchRecipe() {
        ParseQuery<RecipeData> query = ParseQuery.getQuery("RecipeData");

        if(CookFoodApp.getInstance().user == null){
            Log.e(TAG, "CookFoodApp.getInstance().user is null!!");
            UserInfo user = (UserInfo) ParseUser.getCurrentUser();
            if(user==null){
                Log.e(TAG,"Current user is null!!");
                return;
            }else{
                CookFoodApp.getInstance().user=user;
            }
        }
        String OwnerId= CookFoodApp.getInstance().user.getObjectId();

        if (TITLE.equalsIgnoreCase("My Recipes")){
            query.whereEqualTo("Owner",OwnerId);
            query.orderByDescending("createdAt");
            query.setLimit(5);
            query.setSkip(RecipeList.size());
        }else{
            query.fromPin(TITLE);
        }
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(List<RecipeData> productListTemp, ParseException e) {
                CookFoodApp.getInstance().pd.dismiss();
                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_LONG).show();
                    return;
                }

                if (productListTemp != null) {

                    RecipeList.addAll(productListTemp);
                    if (lv_recipe.getFooterViewsCount() != 0) {
                        try {
                            lv_recipe.removeFooterView(footerView);
                        } catch (Exception e2) {
                            Log.d(TAG + " lv_recipe exception", e2.toString());
                        }
                    }
                    if(TITLE.equalsIgnoreCase("My Recipes")){
                        recpEditAdapt.notifyDataSetChanged();
                    }else {
                        recpAdapt.notifyDataSetChanged();
                    }
                    loadingMore = false;

                }

                if(productListTemp==null || productListTemp.size()==0) {
                    GotAllData = true;
                }
                if(RecipeList.size()==0){
                    tv_activity_recipe_info.setVisibility(View.VISIBLE);
                    lv_recipe.setVisibility(View.GONE);
                    if (TITLE.equalsIgnoreCase("My Recipes")) {
                        tv_activity_recipe_info.setText("You don't have created any recipe yet.\nStart by creating one from drawer.");
                    } else {
                        tv_activity_recipe_info.setText("You don't have any saved recipe.");
                    }
                }else{
                    tv_activity_recipe_info.setVisibility(View.GONE);
                    lv_recipe.setVisibility(View.VISIBLE);
                }

                if(TITLE.equalsIgnoreCase("Saved Recipes")) {
                    ParseObject.unpinAllInBackground(TITLE, RecipeList, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {    // There was some error.
                                Log.d(TAG, e.toString());
                                Toast.makeText(mContext, "Error in deleting cache.", Toast.LENGTH_LONG).show();

                            }

                            ParseObject.pinAllInBackground(TITLE, RecipeList);
                        }
                    });
                }
            }



        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);

        mContext = this;
        ButterKnife.inject(this);

        TITLE = getIntent().getStringExtra("TITLE");

        InitActionBar();

        SetUpMainContent();


        return true;
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
