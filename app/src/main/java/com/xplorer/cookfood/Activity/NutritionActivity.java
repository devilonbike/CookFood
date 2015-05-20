package com.xplorer.cookfood.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NutritionActivity extends ActionBarActivity {
    String SearchString;
    Context mContext;
    private static final String TAG = "SearchRecipeActivity";

    @InjectView(R.id.tv_nutrition_mineral)
    TextView tv_nutrition_mineral;

    @InjectView(R.id.tv_nutrition_vitamin)
    TextView tv_nutrition_vitamin;

    @InjectView(R.id.iv_nutrition_ing)
    ImageView iv_nutrition_ing;



    public int ActionBarColor;
    public int StatusBarColor;

    String name;
    String vitamin;
    String mineral;
    String imgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);


    }

    public void InitActionBar() {

        ActionBarColor = getResources().getColor(R.color.CornflowerBlue);
        StatusBarColor = CookFoodApp.getInstance().getDarkColor(ActionBarColor);


        getSupportActionBar().setTitle("Nutrition ("+name+")");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ActionBarColor));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(StatusBarColor);

        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nutrition, menu);
        name= getIntent().getStringExtra("name");
        vitamin= getIntent().getStringExtra("vitamin");
        mineral= getIntent().getStringExtra("mineral");
        imgurl= getIntent().getStringExtra("imgurl");

        mContext = this;
        ButterKnife.inject(this);

        InitActionBar();

        tv_nutrition_mineral.setText(mineral);
        tv_nutrition_vitamin.setText(vitamin);
        Picasso.with(mContext).load(imgurl).error(R.drawable.recepi).into(iv_nutrition_ing);


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
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
