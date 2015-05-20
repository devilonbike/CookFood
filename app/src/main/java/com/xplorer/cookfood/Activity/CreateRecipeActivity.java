package com.xplorer.cookfood.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Adaptor.TabsActionProductAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Fragment.ActionProductFragment;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CreateRecipeActivity extends ActionBarActivity implements TextToSpeech.OnUtteranceCompletedListener, SensorEventListener {

    Context mContext;
    TextToSpeech ttobj;
    private float lastX;
    boolean isPlaying = false;
    boolean isTimePlaying = false;
    String timeMin = "60";
    private static final String TAG = "CreateRecipeActivity";
    public int CP = 0;
    private long lastPause = 0;
    List<Product> productList;
    Dialog dialog;
    Dialog TimerDialog;
    String RecipeObjectId;
    String command;


    public int ActionBarColor;
    public int StatusBarColor;


    Menu MenuInstance;
    MenuItem SaveBtn;

    TabsActionProductAdaptor ActionProductTabsAdaptor;

    @InjectView(R.id.tabs_action_product)
    PagerSlidingTabStrip tabs_action_product;

    @InjectView(R.id.pager_action_product)
    ViewPager pager_action_product;

    @InjectView(R.id.sl_create_recipe_globle)
    SlidingUpPanelLayout sl_create_recipe_globle;



    @InjectView(R.id.ll_create_recipe_header)
    LinearLayout ll_create_recipe_header;

    @InjectView(R.id.iv_create_recipe_actionicon)
    ImageView iv_create_recipe_actionicon;

    @InjectView(R.id.tv_create_recipe_ht)
    TextView tv_create_recipe_ht;

    @InjectView(R.id.tv_create_recipe_hd)
    TextView tv_create_recipe_hd;

    @InjectView(R.id.iv_create_recipe_playpause)
    ImageView iv_create_recipe_playpause;

    @InjectView(R.id.iv_create_recipe_stop)
    ImageView iv_create_recipe_stop;

    @InjectView(R.id.iv_create_recipe_fwd)
    ImageView iv_create_recipe_fwd;

    @InjectView(R.id.iv_create_recipe_bwd)
    ImageView iv_create_recipe_bwd;

    @InjectView(R.id.tv_create_recipe_speak)
    TextView tv_create_recipe_speak;

    @InjectView(R.id.ll_item_product_main)
    LinearLayout ll_item_product_main;
    @InjectView(R.id.iv_item_product_action)
    ImageView iv_item_product_action;
    @InjectView(R.id.tv_item_product_action)
    TextView tv_item_product_action;
    @InjectView(R.id.tv_item_product_desc)
    TextView tv_item_product_desc;
    @InjectView(R.id.ll_item_product_info)
    LinearLayout ll_item_product_info;
    @InjectView(R.id.tv_item_product_temp)
    TextView tv_item_product_temp;
    @InjectView(R.id.tv_item_product_time)
    TextView tv_item_product_time;

    @InjectView(R.id.rl_create_activity_Anim_iv)
    RelativeLayout rl_create_activity_Anim_iv;


    @InjectView(R.id.rl_create_activity_Anim_info)
    RelativeLayout rl_create_activity_Anim_info;


    @InjectView(R.id.vf_create_recepie_ingredient)
    ViewFlipper vf_create_recepie_ingredient;

    @InjectView(R.id.iv_create_recipe_timerplay)
    ImageView iv_create_recipe_timerplay;

    @InjectView(R.id.iv_create_recipe_timerreplay)
    ImageView iv_create_recipe_timerreplay;

    @InjectView(R.id.cm_create_recipe_speak)
    Chronometer cm_create_recipe_speak;

    @InjectView(R.id.ll_create_recipe_like)
    LinearLayout ll_create_recipe_like;

    @InjectView(R.id.ll_create_recipe_download)
    LinearLayout ll_create_recipe_download;

    @InjectView(R.id.tv_create_recipe_views)
    TextView tv_create_recipe_views;

    @InjectView(R.id.tv_create_recipe_proximity)
    TextView tv_create_recipe_proximity;

    @InjectView(R.id.iv_create_recipe_download)
    ImageView iv_create_recipe_download;

    @InjectView(R.id.iv_create_recipe_view)
    ImageView iv_create_recipe_view;

    @InjectView(R.id.iv_create_recipe_proximity)
    ImageView iv_create_recipe_proximity;
    @InjectView(R.id.ll_create_recipe_proximity)
    LinearLayout ll_create_recipe_proximity;

    @InjectView(R.id.ll_item_product_info_stop)
    LinearLayout ll_item_product_info_stop;

    @InjectView(R.id.ll_item_product_info_temp)
    LinearLayout ll_item_product_info_temp;



    public int RecordCounter = 0;
    public int FoundCounter = 0;
    ImageView iv_dialog_recipe;
    EditText et_dialog_desc;
    EditText et_dialog_name;
    Button b_sign_log_confirm;
    Spinner spnr_dialog_category;
    EditText et_dialog_timer_name;
    RadioGroup rg_dialog_recipe;
    RadioButton rb_dialog_recipe_publish;
    RadioButton rb_dialog_recipe_unpublish;


    private SensorManager mSensorManager;
    private Sensor mSensor;

    String recipeTag = CookFoodApp.getInstance().RecipeTag;
    String productTag = CookFoodApp.getInstance().ProductTag;
    String ingredientTag = CookFoodApp.getInstance().IngredientTag;

    List<ActionProductFragment> FragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(command!=null) {
            if (command.equalsIgnoreCase("play")) {  //TURN PROXIMITY SENSOR ONLY IF COMMAND IS play
                if (!CookFoodApp.getSPString(CookFoodApp.PROXIMITY_ON).equalsIgnoreCase("true")) {
                    Log.d(TAG, "play Proximity turned off");
                    if (mSensorManager != null)
                        mSensorManager.unregisterListener((SensorEventListener) mContext);
                    tv_create_recipe_proximity.setText("Sensor Off");
                    CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "false");
                } else {
                    Log.d(TAG, "play Proximity turned on");
                    mSensorManager.registerListener((SensorEventListener) mContext, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    tv_create_recipe_proximity.setText("Sensor On");
                    CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "true");
                }
            } else {
                Log.d(TAG, "!play Proximity turned off");
                if (mSensorManager != null)
                    mSensorManager.unregisterListener((SensorEventListener) mContext);
            }
        }

    }

    public void InitActionBar() {

        ActionBarColor = getResources().getColor(R.color.Red);
        StatusBarColor = CookFoodApp.getInstance().getDarkColor(ActionBarColor);

        getSupportActionBar().setTitle("Create your Recipe");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ActionBarColor));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(StatusBarColor);

        }

        int iconColor = getResources().getColor(R.color.gray6);

        iv_create_recipe_download.setColorFilter(iconColor);
        iv_create_recipe_view.setColorFilter(iconColor);
        iv_create_recipe_proximity.setColorFilter(iconColor);
        //setColorFilter(ActionBarColor);
    }

    public void InitSliderPanel() {
        sl_create_recipe_globle.setDragView(ll_create_recipe_header);
        //sl_create_recipe_globle.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        sl_create_recipe_globle.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset > .82 && getSupportActionBar().isShowing()) {
                    getSupportActionBar().hide();

                    iv_create_recipe_actionicon.setColorFilter(mContext.getResources().getColor(R.color.white));
                    tv_create_recipe_ht.setTextColor(mContext.getResources().getColor(R.color.white));
                    tv_create_recipe_hd.setTextColor(mContext.getResources().getColor(R.color.white));

                    ll_create_recipe_header.setBackgroundColor(mContext.getResources().getColor(R.color.Red));
                } else if (slideOffset < .82 && !getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();

                    iv_create_recipe_actionicon.setColorFilter(mContext.getResources().getColor(R.color.Black));
                    tv_create_recipe_ht.setTextColor(mContext.getResources().getColor(R.color.Black));
                    tv_create_recipe_hd.setTextColor(mContext.getResources().getColor(R.color.Black));

                    ll_create_recipe_header.setBackgroundColor(mContext.getResources().getColor(R.color.white));


                }
            }

            @Override
            public void onPanelExpanded(View panel) {
                //Log.i(TAG, "onPanelExpanded");
                ProximityRun = false;


            }

            @Override
            public void onPanelCollapsed(View panel) {
                //Log.i(TAG, "onPanelCollapsed");
                ProximityRun = true;

            }

            @Override
            public void onPanelAnchored(View panel) {
                //Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                //Log.i(TAG, "onPanelHidden");
                ProximityRun = true;
            }
        });
    }


    public void initCabinetInit() {
        FragmentList = new ArrayList<ActionProductFragment>();

        FragmentList.add(new ActionProductFragment().newInstance(0));
        FragmentList.add(new ActionProductFragment().newInstance(1));


        ActionProductTabsAdaptor = new TabsActionProductAdaptor(getSupportFragmentManager(),FragmentList);

        pager_action_product.setAdapter(ActionProductTabsAdaptor);
        pager_action_product.setOffscreenPageLimit(0);
        pager_action_product.setCurrentItem(0);

        tabs_action_product.setViewPager(pager_action_product);
        tabs_action_product.setDividerColor(getResources().getColor(R.color.black));
        //tabs_action_product.setTextColor(getResources().getColor(R.color.white));
        //tabs_action_product.setIndicatorColor(getResources().getColor(R.color.white));

        tabs_action_product.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                FragmentList.get(position).refreshProdList();
                // pagerAdapter.getFragment(position).checkIfFilterApplied();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    public void InitDialog() {
        dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.dialog_recipe);
        dialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Recipe Information</font>"));

        et_dialog_name = (EditText) dialog.findViewById(R.id.et_dialog_recipe_title);
        et_dialog_desc = (EditText) dialog.findViewById(R.id.et_dialog_recipe_desc);
        iv_dialog_recipe = (ImageView) dialog.findViewById(R.id.iv_dialog_recipe);
        b_sign_log_confirm = (Button) dialog.findViewById(R.id.b_dialog_recipe_save);
        spnr_dialog_category = (Spinner) dialog.findViewById(R.id.spnr_dialog_recipe);
        rg_dialog_recipe = (RadioGroup) dialog.findViewById(R.id.rg_dialog_recipe);
        rb_dialog_recipe_publish = (RadioButton) dialog.findViewById(R.id.rb_dialog_recipe_publish);
        rb_dialog_recipe_unpublish = (RadioButton) dialog.findViewById(R.id.rb_dialog_recipe_unpublish);

        iv_dialog_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, CookFoodApp.getInstance().CategoryFood);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_dialog_category.setAdapter(dataAdapter);

        spnr_dialog_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String quantitySelected = CookFoodApp.getInstance().CategoryFood[i];
                Log.d("Spinner", spnr_dialog_category.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        b_sign_log_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_dialog_name.getText().toString();
                String desc = et_dialog_desc.getText().toString();

                if (title.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please Enter Title.", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadRecipe();

            }
        });
    }

    public void InitTimerDialog() {

        TimerDialog = new Dialog(mContext);

        TimerDialog.setContentView(R.layout.dialog_timer);
        TimerDialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Set Timer</font>"));

        et_dialog_timer_name = (EditText) TimerDialog.findViewById(R.id.et_dialog_timer);
        Button b_timer_confirm = (Button) TimerDialog.findViewById(R.id.b_dialog_timer_confirm);
        Button b_timer_cancel = (Button) TimerDialog.findViewById(R.id.b_dialog_timer_cancel);


        b_timer_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timer = et_dialog_timer_name.getText().toString();

                if (timer.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Please Enter Some Time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                timeMin = timer;
                cm_create_recipe_speak.setBase(SystemClock.elapsedRealtime());
                cm_create_recipe_speak.start();
                isTimePlaying = true;
                TimerDialog.dismiss();
            }
        });

        b_timer_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialog.dismiss();
            }
        });
        TimerDialog.show();

        ColorDrawable colorDrawable = new ColorDrawable(StatusBarColor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(TimerDialog.getWindow().getAttributes());
        lp.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
        TimerDialog.getWindow().setAttributes(lp);
        TimerDialog.getWindow().setBackgroundDrawable(colorDrawable);
    }

    public void FetchAndDownloadRecipe() {


        if (command.equalsIgnoreCase("create") || command.equalsIgnoreCase("unsaved")) {
            Toast.makeText(mContext, "Save your recipe first.", Toast.LENGTH_LONG).show();
            return;
        }

        recipeTag = CookFoodApp.getInstance().RecipeTag;
        productTag = CookFoodApp.getInstance().ProductTag;
        ingredientTag = CookFoodApp.getInstance().IngredientTag;


        ParseQuery<RecipeData> query = ParseQuery.getQuery("RecipeData");

        query.fromPin(recipeTag);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(final List<RecipeData> ListOfRecipes, ParseException e) {

                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                ParseObject.unpinAllInBackground(recipeTag, ListOfRecipes, new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {    // There was some error.
                            Log.d(TAG, e.toString());
                            Toast.makeText(mContext, "Error in deleting cache.", Toast.LENGTH_LONG).show();

                        }
                        Log.d("fromPin Recipe (" + ListOfRecipes.size() + ")", ListOfRecipes.toString());

                        if (CookFoodApp.getInstance().RecipeObject != null) ListOfRecipes.add(CookFoodApp.getInstance().RecipeObject);
                        ParseObject.pinAllInBackground(recipeTag, ListOfRecipes);

                        // Now PINNING of Recipe is done go for Product
                        FetchAndDownloadProduct();

                    }
                });
            }

        });


    }

    public void FetchAndDownloadProduct() {

        for (int i = 0; i < productList.size(); i++) {
            productList.get(i).setRecipeDataId(RecipeObjectId);
            Product p = productList.get(i);
            if(p!=null && p.getProductList()!=null) {
                for (int j = 0; j < p.getProductList().size(); j++) {
                    Product pl = p.getProductList().get(j);
                    pl.setRecipeDataId(RecipeObjectId);
                }
            }
        }
        ParseQuery<Product> query = ParseQuery.getQuery("Product");

        query.orderByAscending("createdAt");
        query.fromPin(productTag);

        query.findInBackground(new FindCallback<Product>() {
               @Override
               public void done(final List<Product> productListTemp, ParseException e) {

               if (e != null) {
                   Log.d(TAG, e.toString());
                   Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                   return;
               }
               ParseObject.unpinAllInBackground(productTag, productListTemp, new DeleteCallback() {
                   public void done(ParseException e) {
                       if (e != null) {    // There was some error.
                           Log.d(TAG, e.toString());
                           Toast.makeText(mContext, "Error in deleting cache.", Toast.LENGTH_LONG).show();

                       }
                       Log.d("fromPin product (" + productListTemp.size() + ")", productListTemp.toString());
                       productListTemp.addAll(productList);
                       ParseObject.pinAllInBackground(productTag, productListTemp);

                       // Now PINNING of Product is done go for ingredient, but before make list all ingredients to be added

                       List<Ingredients> addThisIngList = new ArrayList<Ingredients>();
                       for (int i = 0; i < productList.size(); i++) {
                           if (!productList.get(i).getAction().equalsIgnoreCase("none")) {

                               Product p = productList.get(i);
                               Log.d("Action:", p.getAction());
                               for (int j = 0; j < p.getProductList().size(); j++) {

                                   Product pl = p.getProductList().get(j);
                                   Log.d("    Sub Action:", pl.getAction());
                                   if (pl.getAction().equalsIgnoreCase("none")) {
                                       addThisIngList.add(pl.getIngredient());
                                   }
                               }

                           }
                       }

                       //Now we have All ingredients used in this product, hence pin them

                       FetchAndDownloadIngredient(addThisIngList);
                   }
               });


           }
       }

        );
    }

    public void FetchAndDownloadIngredient(final List<Ingredients> addThisIngList) {

        ParseQuery<Ingredients> queryIng = ParseQuery.getQuery("Ingredients");

        queryIng.fromPin(ingredientTag);

        queryIng.findInBackground(new FindCallback<Ingredients>() {
            @Override
            public void done(final List<Ingredients> IngredientsList, ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseObject.unpinAllInBackground(ingredientTag, IngredientsList, new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {    // There was some error.
                            Log.d(TAG, e.toString());
                            Toast.makeText(mContext, "Error in deleting cache.", Toast.LENGTH_LONG).show();

                        }
                        Log.d("fromPin Ingredient (" + IngredientsList.size() + ")", IngredientsList.toString());
                        IngredientsList.addAll(addThisIngList);
                        Log.d("New IngredientsList (" + IngredientsList.size() + ")", IngredientsList.toString());
                        ParseObject.pinAllInBackground(ingredientTag, IngredientsList);

                        Toast.makeText(mContext, "Recipe has been saved offline successfully.", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }

    public void setMainContent() {


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ll_create_recipe_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchAndDownloadRecipe();
            }
        });

        ttobj = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobj.setLanguage(Locale.US);
                        }
                    }
                });


        iv_create_recipe_playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    ttobj.stop();
                    isPlaying = false;
                    Picasso.with(mContext).load(R.drawable.play_recipe).error(R.drawable.error_image).into(iv_create_recipe_playpause);
                    stopTimer();
                } else {
                    if (CP >= 0 && CP < CookFoodApp.getInstance().CustomProductList.size()) {
                        speakThisProduct();
                        rl_create_activity_Anim_iv.setVisibility(View.VISIBLE);
                        rl_create_activity_Anim_info.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(mContext, "Create some recipe first.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        iv_create_recipe_fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveForward();
            }
        });

        iv_create_recipe_bwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CP > 0) {
                    CP--;
                    speakThisProduct();
                    rl_create_activity_Anim_iv.setVisibility(View.VISIBLE);
                    rl_create_activity_Anim_info.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mContext, "Recipe Starts", Toast.LENGTH_SHORT).show();


                }
            }
        });

        iv_create_recipe_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_create_activity_Anim_iv.setVisibility(View.GONE);
                rl_create_activity_Anim_info.setVisibility(View.VISIBLE);
                ttobj.stop();
                isPlaying = false;
                Picasso.with(mContext).load(R.drawable.play_recipe).error(R.drawable.error_image).into(iv_create_recipe_playpause);

                stopTimer();

                CP = 0;
                lastPause = 0;
                cm_create_recipe_speak.setBase(SystemClock.elapsedRealtime());
            }
        });


        vf_create_recepie_ingredient.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_left));
        vf_create_recepie_ingredient.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_right));


        rl_create_activity_Anim_iv.setVisibility(View.GONE);
        rl_create_activity_Anim_info.setVisibility(View.VISIBLE);

        cm_create_recipe_speak.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equalsIgnoreCase(getFormatedTime(timeMin))) {

                    stopTimer();

                    HashMap<String, String> myHashAlarm = new HashMap<String, String>();
                    myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));

                    if (CP < CookFoodApp.getInstance().CustomProductList.size() - 1) {
                        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MOVE NEXT");
                        ttobj.speak("Time's up, moving to next step.", TextToSpeech.QUEUE_FLUSH, myHashAlarm);
                        CP++;
                        rl_create_activity_Anim_iv.setVisibility(View.VISIBLE);
                        rl_create_activity_Anim_info.setVisibility(View.GONE);
                    } else {

                        ttobj.speak("Recipe ended", TextToSpeech.QUEUE_FLUSH, myHashAlarm);

                        Toast.makeText(mContext, "Recipe Ended", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cm_create_recipe_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitTimerDialog();
            }
        });

        iv_create_recipe_timerplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimePlaying) {
                    stopTimer();
                } else {
                    Picasso.with(mContext).load(R.drawable.pause_recipe).error(R.drawable.error_image).into(iv_create_recipe_timerplay);
                    cm_create_recipe_speak.setBase(cm_create_recipe_speak.getBase() + SystemClock.elapsedRealtime() - lastPause);
                    cm_create_recipe_speak.start();
                    isTimePlaying = true;
                }
            }
        });

        iv_create_recipe_timerreplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cm_create_recipe_speak.setBase(SystemClock.elapsedRealtime());
                cm_create_recipe_speak.start();
                isTimePlaying = true;
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (command.equalsIgnoreCase("play")){  //TURN PROXIMITY SENSOR ONLY IF COMMAND IS play
            if (!CookFoodApp.getSPString(CookFoodApp.PROXIMITY_ON).equalsIgnoreCase("true")) {
                Log.d(TAG, "play Proximity turned off");
                if (mSensorManager != null)
                    mSensorManager.unregisterListener((SensorEventListener) mContext);
                tv_create_recipe_proximity.setText("Sensor Off");
                CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "false");
            } else {
                Log.d(TAG, "play Proximity turned on");
                mSensorManager.registerListener((SensorEventListener) mContext, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                tv_create_recipe_proximity.setText("Sensor On");
                CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "true");
            }
        }else{
            Log.d(TAG, "!play Proximity turned off");
            if (mSensorManager != null)
                mSensorManager.unregisterListener((SensorEventListener) mContext);
        }

        ll_create_recipe_proximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CookFoodApp.getSPString(CookFoodApp.PROXIMITY_ON).equalsIgnoreCase("false")){
                    mSensorManager.registerListener((SensorEventListener) mContext, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    tv_create_recipe_proximity.setText("Sensor On");
                    CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "true");
                }else{
                    if (mSensorManager != null)
                        mSensorManager.unregisterListener((SensorEventListener) mContext);
                    tv_create_recipe_proximity.setText("Sensor Off");
                    CookFoodApp.setSPString(CookFoodApp.PROXIMITY_ON, "false");
                }
            }
        });

    }

    public void moveForward(){
        if (CP < CookFoodApp.getInstance().CustomProductList.size() - 1) {
            if(CP==0 && rl_create_activity_Anim_info.getVisibility()==View.VISIBLE){
                speakThisProduct();
            }else{
                CP++;
                speakThisProduct();
            }


            rl_create_activity_Anim_iv.setVisibility(View.VISIBLE);
            rl_create_activity_Anim_info.setVisibility(View.GONE);
        } else {
            HashMap<String, String> myHashAlarm = new HashMap<String, String>();
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));

            ttobj.speak("Recipe ended", TextToSpeech.QUEUE_FLUSH, myHashAlarm);
            Toast.makeText(mContext, "Recipe Ended", Toast.LENGTH_SHORT).show();
        }


    }

    public void stopTimer() {
        Picasso.with(mContext).load(R.drawable.play_recipe).error(R.drawable.error_image).into(iv_create_recipe_timerplay);

        isTimePlaying = false;
        lastPause = SystemClock.elapsedRealtime();
        cm_create_recipe_speak.stop();
    }



    public void uploadRecipe() {

        Bitmap bitmap = ((BitmapDrawable) iv_dialog_recipe.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] dataImage = stream.toByteArray();

        final ParseFile file = new ParseFile("img.jpg", dataImage);
        CookFoodApp.getInstance().RunPreLoader(mContext);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                    CookFoodApp.getInstance().pd.dismiss();
                    dialog.dismiss();
                    Toast.makeText(mContext, "Server Error (while uploading Recipe Image)", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = et_dialog_name.getText().toString();
                String desc = et_dialog_desc.getText().toString();
                String owner = CookFoodApp.getInstance().user.getObjectId();
                String cat = spnr_dialog_category.getSelectedItem().toString().toLowerCase();

                if (CookFoodApp.getInstance().RecipeObject == null) {
                    CookFoodApp.getInstance().RecipeObject = new RecipeData();
                }

                CookFoodApp.getInstance().RecipeObject.setTitle(title);
                CookFoodApp.getInstance().RecipeObject.setDescription(desc);
                CookFoodApp.getInstance().RecipeObject.setOwner(owner);
                CookFoodApp.getInstance().RecipeObject.setImageFile(file);
                CookFoodApp.getInstance().RecipeObject.setCategory(cat);
                if(rg_dialog_recipe.getCheckedRadioButtonId()==R.id.rb_dialog_recipe_publish){
                    CookFoodApp.getInstance().RecipeObject.setPublished(true);
                }else{
                    CookFoodApp.getInstance().RecipeObject.setPublished(false);
                }


                CookFoodApp.getInstance().RecipeObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e != null) {
                            Log.d("saveInBackground RecipeObject", e.toString());
                        }
                        RecipeObjectId = CookFoodApp.getInstance().RecipeObject.getObjectId();
                        Log.d("RecipeObjectId", RecipeObjectId);
                        RecordCounter = 0;
                        for (int i = 0; i < CookFoodApp.getInstance().CustomProductList.size(); i++) {
                            CookFoodApp.getInstance().CustomProductList.get(i).setRecipeDataId(RecipeObjectId);
                            Product p = CookFoodApp.getInstance().CustomProductList.get(i);
                            for (int j = 0; j < p.getProductList().size(); j++) {
                                Product pl = p.getProductList().get(j);
                                pl.setRecipeDataId(RecipeObjectId);
                            }
                        }
                        uploadedCount=0;
                        uploadInChunks();

                    }
                });


            }
        });

    }

    public int uploadedCount;
    public int GoingToUpload = 5 ;

    public void uploadInChunks(){
        Log.d(TAG,"uploadInChunks" );
        //IF ALL THE RECIPE HAS BEEN UPLOADED
        if(CookFoodApp.getInstance().CustomProductList.size()== uploadedCount ){
            Log.d(TAG, "FoundCounter return");
            // UPLOAD COMPLETE
            CookFoodApp.getInstance().pd.dismiss();
            Toast.makeText(mContext, "Saved Recipe Successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
            return;
        }

        //SET GoingToUpload
        GoingToUpload = 5 ;
        if(CookFoodApp.getInstance().CustomProductList.size()- uploadedCount < GoingToUpload){
            GoingToUpload = CookFoodApp.getInstance().CustomProductList.size()- uploadedCount;
        }
        FoundCounter =0;
        for (int i = uploadedCount; i < uploadedCount+GoingToUpload; i++) {

            CookFoodApp.getInstance().CustomProductList.get(i).saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("saveInBackground CookFoodApp.getInstance().CustomProductList.get(i)", e.toString());
                        Toast.makeText(mContext, "Error while saving Recipe", Toast.LENGTH_SHORT).show();
                    }

                    uploadedCount++;
                    FoundCounter++;
                    Log.d("ProductSaving: ["+FoundCounter+"/"+GoingToUpload+"]", uploadedCount+"/"+CookFoodApp.getInstance().CustomProductList.size());
                    if (FoundCounter == GoingToUpload) {
                        Log.d("ProductSaving","Calling Upload Again");
                        uploadInChunks();

                    }


                }
            });

        }




    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRecipeActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath(), bounds);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

                    Bitmap bmImg = getResizedBitmap(rotatedBitmap);
                    iv_dialog_recipe.setImageBitmap(bmImg);


                    f.delete();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap bm = getResizedBitmap(thumbnail);
                iv_dialog_recipe.setImageBitmap(bm);


            }
        }


    }

    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = 600;
            height = (int) (width / bitmapRatio);
        } else {
            height = 400;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void getCustomProductList(final boolean fromPin) {
        Log.d("getCustomProductList", fromPin + "");
        ParseQuery<RecipeData> queryTemp = ParseQuery.getQuery("RecipeData");
        queryTemp.whereEqualTo("objectId", RecipeObjectId);
        if (fromPin) queryTemp.fromPin(recipeTag);


        if(CookFoodApp.getInstance().pd==null){
            CookFoodApp.getInstance().RunPreLoader(mContext);
        }else if(!CookFoodApp.getInstance().pd.isShowing()){
            CookFoodApp.getInstance().RunPreLoader(mContext);
        }


        queryTemp.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(List<RecipeData> RecipeDataList, ParseException e) {

                if (e != null) {
                    if (e != null) Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    CookFoodApp.getInstance().pd.dismiss();
                    return;
                }
                if (RecipeDataList.size() == 0 && fromPin) {
                    getCustomProductList(false);
                    return;
                }

                RecipeData temp = RecipeDataList.get(0);
                CookFoodApp.getInstance().RecipeObject = temp;
                int views = CookFoodApp.getInstance().RecipeObject.getViews();
                tv_create_recipe_views.setText(views + " Views");

                if (temp.getTitle() != null) {
                    getSupportActionBar().setTitle(CookFoodApp.getInstance().UpperCaseEachWord(temp.getTitle()));
                    et_dialog_name.setText(temp.getTitle());
                }
                if (temp.getDescription() != null)
                    et_dialog_desc.setText(temp.getDescription());
                if (temp.getImageFile() != null)
                    Picasso.with(mContext).load(temp.getImageFile().getUrl()).error(R.drawable.error_image).into(iv_dialog_recipe);

                getProductData(fromPin);

            }

        });


    }

    public void getProductData(final boolean fromPin) {

        Log.d("getProductData", fromPin + "");
        ParseQuery<Product> query = ParseQuery.getQuery("Product");
        query.whereEqualTo("RecipeDataId", RecipeObjectId);
        query.orderByAscending("Order");
        query.addAscendingOrder("createdAt");
        if (fromPin) query.fromPin(productTag);
        query.findInBackground(new FindCallback<Product>() {
                                   @Override
                                   public void done(List<Product> productListTemp, ParseException e) {

                                       if (e != null) {
                                           Log.d(TAG, e.toString());
                                           Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                                           CookFoodApp.getInstance().pd.dismiss();
                                           return;
                                       }

                                       if (productListTemp.size() == 0 && fromPin) {

                                           getProductData(false);
                                           return;
                                       }
                                       productList = productListTemp;

                                       CookFoodApp.getInstance().OrderId = productList.size();
                                       RecordCounter = 0;
                                       FoundCounter = 0;
                                       for (int i = 0; i < productList.size(); i++) {
                                           if (!productList.get(i).getAction().equalsIgnoreCase("none")) {

                                               final int i_final = i;
                                               Product p = productList.get(i);

                                               if(CookFoodApp.getInstance().OrderId < p.getOrder()){
                                                   CookFoodApp.getInstance().OrderId = p.getOrder();
                                               }
                                               Log.d("Action:", p.getAction());
                                               for (int j = 0; j < p.getProductList().size(); j++) {
                                                   final int j_final = j;
                                                   Product pl = p.getProductList().get(j);
                                                   Log.d("    Sub Action:", pl.getAction());
                                                   if (pl.getAction().equalsIgnoreCase("none")) {
                                                       RecordCounter++;
                                                       getIngredientData(fromPin, pl, i_final, j_final);
                                                   }
                                               }
                                               CookFoodApp.getInstance().CustomProductList.add(productList.get(i));

                                           }
                                       }
                                       if(productList.size()==0){
                                           AfterDownloadRecipeComplete();
                                       }

                                   }
                               }

        );
    }


    public void getIngredientData(final boolean fromPin, final Product pl, final int i_final, final int j_final) {
        Log.d("getIngredientData", fromPin + "");
        ParseQuery<Ingredients> queryIng = ParseQuery.getQuery("Ingredients");
        queryIng.whereEqualTo("objectId", pl.getIngredient().getObjectId());
        if (fromPin) queryIng.fromPin(ingredientTag);
        Log.d("RecordCounter", RecordCounter + "");
        queryIng.findInBackground(new FindCallback<Ingredients>() {
            @Override
            public void done(List<Ingredients> IngredientsList, ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    CookFoodApp.getInstance().pd.dismiss();
                    return;
                }

                if (IngredientsList.size() == 0 && fromPin) {
                    getIngredientData(false, pl, i_final, j_final);
                    return;
                }

                FoundCounter++;

                Log.d("FoundCounter", FoundCounter + "");

                Ingredients ing = IngredientsList.get(0);
                productList.get(i_final).getProductList().get(j_final).setIngredient(ing);
                if (FoundCounter == RecordCounter) {
                    AfterDownloadRecipeComplete();
                }
            }

        });
    }


    public void AfterDownloadRecipeComplete(){
        CookFoodApp.getInstance().pd.dismiss();
        FragmentList.get(1).refreshProdList();
    }


    public int getIndexOfProduct(Product p) {
        int index = -1;
        for (int i = 0; i < CookFoodApp.getInstance().CustomProductList.size(); i++) {
            if (CookFoodApp.getInstance().CustomProductList.get(i).equals(p)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getIndexOfPCategory(String cat) {
        int index = -1;
        for (int i = 0; i < CookFoodApp.getInstance().CategoryFood.length; i++) {
            if (CookFoodApp.getInstance().CategoryFood[i].equalsIgnoreCase(cat)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public String getQuantity(String qunt, String cnt) {
        String str = "";

        if (qunt.equalsIgnoreCase("Count")) {
            return cnt;
        } else if (qunt.equalsIgnoreCase("Gram (g)")) {
            return cnt + " gram of";
        } else if (qunt.equalsIgnoreCase("Kilogram (Kg)")) {
            return cnt + " kilogram of";
        } else if (qunt.equalsIgnoreCase("Cup Count")) {
            return cnt + " cups of";
        } else if (qunt.equalsIgnoreCase("Tea spoon (tsp)")) {
            return cnt + " tea spoon of";
        } else if (qunt.equalsIgnoreCase("Table Spoon (tbsp)")) {
            return cnt + " table spoon of";
        } else if (qunt.equalsIgnoreCase("Packet")) {
            return cnt + " packets of";
        } else if (qunt.equalsIgnoreCase("As needed")) {
            return "required amount of";
        } else if (qunt.equalsIgnoreCase("A few")) {
            return "a few of";
        }
        return str;
    }

    public void speakThisProduct() {
        int step = CP + 1;
        String speak = "Step " + step + ". \n";
        Product pMain = CookFoodApp.getInstance().CustomProductList.get(CP);
        String action = pMain.getAction();
        if (action.equalsIgnoreCase("none")) {
            Log.d(TAG, "Action came none");
        }
        speak += action + " ";
        vf_create_recepie_ingredient.removeAllViews();
        for (int i = 0; i < pMain.getProductList().size(); i++) {
            boolean last = (i == pMain.getProductList().size() - 1) ? true : false;
            Product p = pMain.getProductList().get(i);
            if (p.getAction().equalsIgnoreCase("none")) {
                vf_create_recepie_ingredient.addView(getFlipView(p, 0));
                speak += getQuantity(p.getQuantityType(), p.getQuantityCount()) + " " + p.getIngredient().getName();
            } else {
                int stepTemp = getIndexOfProduct(p) + 1;
                String acnStr = p.getAction();
                vf_create_recepie_ingredient.addView(getFlipView(p, stepTemp));
                speak += CookFoodApp.getInstance().Actionverb.get(p.getAction()) + " from step " + stepTemp;
            }
            if (!last) speak += " and ";
        }
        stopTimer();
        cm_create_recipe_speak.setBase(SystemClock.elapsedRealtime());


        ll_item_product_info_temp.setVisibility(View.GONE);
        ll_item_product_info_stop.setVisibility(View.GONE);

        if(CookFoodApp.getInstance().ActionStopCriteria.get(action)){
            ll_item_product_info_stop.setVisibility(View.VISIBLE);
            if(pMain.getStopCriteria()!=null && pMain.getStopCriteria().equalsIgnoreCase("Provide Description Criteria")){
                speak += " until " + pMain.getStopReason();
                tv_item_product_time.setText(pMain.getStopReason());
            }else{
                speak += " for about " + pMain.getTime() + " minutes";//
                tv_item_product_time.setText("Timer goes off");
                timeMin = pMain.getTime();
                cm_create_recipe_speak.start();
                Picasso.with(mContext).load(R.drawable.pause_recipe).error(R.drawable.error_image).into(iv_create_recipe_timerplay);

                isTimePlaying = true;
            }
        }
        if(CookFoodApp.getInstance().ActionTemp.get(action)){
            ll_item_product_info_temp.setVisibility(View.VISIBLE);
            tv_item_product_temp.setText(pMain.getTemperature());
            speak +=" at " + pMain.getTemperature() + " °C";
        }


        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SPEECH DONE");


        ttobj.speak(speak, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
        ttobj.setOnUtteranceCompletedListener(this);

        tv_create_recipe_speak.setText(speak);
        Picasso.with(mContext).load(CookFoodApp.getInstance().ActionImg.get(action)).error(R.drawable.error_image).into(iv_item_product_action);
        tv_item_product_action.setText(action);
        tv_item_product_desc.setText(CookFoodApp.getInstance().ActionDescription.get(action));

        if (pMain.getProductList().size() > 1) {
            vf_create_recepie_ingredient.setAutoStart(true);
            vf_create_recepie_ingredient.setFlipInterval(4000);
            vf_create_recepie_ingredient.startFlipping();
        } else {
            vf_create_recepie_ingredient.stopFlipping();
        }

        Picasso.with(mContext).load(R.drawable.pause_recipe).error(R.drawable.error_image).into(iv_create_recipe_playpause);

        isPlaying = true;

    }

    public String getFormatedTime(String min) {
        final double t = Double.parseDouble(min);
        int hours = (int) t / 60;
        int minutes = (int) t % 60;
        BigDecimal secondsPrecision = new BigDecimal((t - Math.floor(t)) * 100).setScale(2, RoundingMode.HALF_UP);
        int seconds = secondsPrecision.intValue();

        boolean nextDay = false;

        if (seconds > 59) {
            minutes++; //increment minutes by one
            seconds = seconds - 60; //recalculate seconds
        }

        if (minutes > 59) {
            hours++;
            minutes = minutes - 60;
        }

        //next day
        if (hours > 23) {
            hours = hours - 24;
            nextDay = true;
        }

        //if seconds >=10 use the same format as before else pad one zero before the seconds
        String time;
        if (hours == 0) {
            String myFormat = seconds >= 10 ? "%02d:%d" : "%02d:0%d";
            time = String.format(myFormat, minutes, seconds);
        } else {
            String myFormat = seconds >= 10 ? "%d:%02d:%d" : "%d:%02d:0%d";
            time = String.format(myFormat, hours, minutes, seconds);
        }

        return time;
    }

    public View getFlipView(Product p, final int step) {

        ViewHolder holder = null;
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_ingredient, null, false);
        holder = new ViewHolder(view, mContext);
        view.setTag(holder);
        holder.iv_item_ingredient_info.setVisibility(View.GONE);
        Log.d("getFlipView iv_item_ingredient_actionimg", p.getAction());

        if (p.getAction().equalsIgnoreCase("none")) {
            Ingredients ing = p.getIngredient();
            holder.tv_item_ingredient_name.setText(ing.getName());
            holder.tv_item_ingredient_description.setText(ing.getNameHindi());
            Picasso.with(mContext).load(ing.getImageFile().getUrl()).error(R.drawable.error_image).into(holder.iv_item_ingredient_actionimg);

        } else {
            holder.tv_item_ingredient_name.setText(p.getAction());
            holder.tv_item_ingredient_description.setText("Step " + step);

            Picasso.with(mContext).load(CookFoodApp.getInstance().ActionImg.get(p.getAction())).error(R.drawable.error_image).into(holder.iv_item_ingredient_actionimg);

        }
        holder.ll_item_ingredient_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "ll_item_ingredient_top");

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentX = event.getX();

                        if (lastX < currentX) {

                            if (vf_create_recepie_ingredient.getDisplayedChild() == 0)
                                break;

                            vf_create_recepie_ingredient.setInAnimation(mContext, R.anim.slide_in_from_left);
                            vf_create_recepie_ingredient.setOutAnimation(mContext, R.anim.slide_out_to_right);
                            vf_create_recepie_ingredient.showNext();
                        }

                        if (lastX > currentX) {

                            if (vf_create_recepie_ingredient.getDisplayedChild() == 1)
                                break;

                            vf_create_recepie_ingredient.setInAnimation(mContext, R.anim.slide_in_from_right);
                            vf_create_recepie_ingredient.setOutAnimation(mContext, R.anim.slide_out_to_left);
                            vf_create_recepie_ingredient.showPrevious();
                        }
                        break;
                }
                return true;
            }
        });

        return view;


    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.d(TAG, "onUtteranceCompleted:" + utteranceId);
        Handler mainHandler = new Handler(mContext.getMainLooper());
        if (utteranceId.equalsIgnoreCase("SPEECH DONE")) {
            isPlaying = false;

            mainHandler.post(runOnMainThreadPictureChange);
        } else if (utteranceId.equalsIgnoreCase("MOVE NEXT")) {

            mainHandler.post(runOnMainThreadSpeakProduct);
        }
    }

    Runnable runOnMainThreadPictureChange = new Runnable() {
        @Override
        public void run() {
            Picasso.with(mContext).load(R.drawable.play_recipe).error(R.drawable.error_image).into(iv_create_recipe_playpause);
        }
    };

    Runnable runOnMainThreadSpeakProduct = new Runnable() {
        @Override
        public void run() {

            speakThisProduct();
        }
    };

    public boolean ProximityRun = true;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0) { //Near
            if (ProximityRun) {
                moveForward();
            }
        } else {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static class ViewHolder {
        @InjectView(R.id.iv_item_ingredient_actionimg)
        ImageView iv_item_ingredient_actionimg;
        @InjectView(R.id.iv_item_ingredient_info)
        ImageView iv_item_ingredient_info;
        @InjectView(R.id.tv_item_ingredient_name)
        TextView tv_item_ingredient_name;
        @InjectView(R.id.tv_item_ingredient_description)
        TextView tv_item_ingredient_description;
        @InjectView(R.id.ll_item_ingredient_top)
        LinearLayout ll_item_ingredient_top;


        public ViewHolder(View view, final Context context) {
            ButterKnife.inject(this, view);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_recipe, menu);


        MenuInstance = menu;
        SaveBtn = MenuInstance.findItem(R.id.action_menu_create_recipe_save);

        command = getIntent().getStringExtra("command");
        RecipeObjectId = getIntent().getStringExtra("RecipeDataObjectId");
        CookFoodApp.getInstance().RecipeObject = null;

        mContext = this;
        ButterKnife.inject(this);
        CP = 0;
        lastPause = 0;
        cm_create_recipe_speak.setBase(SystemClock.elapsedRealtime());
        productList = null;
        CookFoodApp.getInstance().CustomProductList = new ArrayList<Product>();

        InitActionBar();

        InitSliderPanel();

        initCabinetInit();

        InitDialog();

        setMainContent();


        CookFoodApp.getInstance().OrderId = 0;
        if (command.equalsIgnoreCase("create")) {   //CREATE RECIPE
            CookFoodApp.getInstance().RecipeObject = new RecipeData();
            CookFoodApp.getInstance().RecipeObject.setTitle("Unsaved Recipe");
            CookFoodApp.getInstance().RecipeObject.setDescription("");
            CookFoodApp.getInstance().RecipeObject.setCategory("Appetizer");
            CookFoodApp.getInstance().RecipeObject.setPublished(false);

            SaveBtn.setVisible(true);
            getSupportActionBar().setTitle("Create your Recipe");
            tv_create_recipe_views.setText("0 Views");
            FragmentList.get(1).refreshProdList();

        } else if (command.equalsIgnoreCase("play")) {   //PLAY RECIPE
            SaveBtn.setVisible(false);
            sl_create_recipe_globle.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            recipeTag = CookFoodApp.getInstance().RecipeTag;
            productTag = CookFoodApp.getInstance().ProductTag;
            ingredientTag = CookFoodApp.getInstance().IngredientTag;

            getCustomProductList(true);
        } else if (command.equalsIgnoreCase("edit")) {                          //EDIT RECIPE
            recipeTag = CookFoodApp.getInstance().RecipeTag;
            productTag = CookFoodApp.getInstance().ProductTag;
            ingredientTag = CookFoodApp.getInstance().IngredientTag;

            SaveBtn.setVisible(true);
            getCustomProductList(true);
        }

        return true;

    }

    public void OpenDialog() {
        if (CookFoodApp.getInstance().RecipeObject != null) {
            et_dialog_name.setText(CookFoodApp.getInstance().RecipeObject.getTitle());
            et_dialog_desc.setText(CookFoodApp.getInstance().RecipeObject.getDescription());
            if(CookFoodApp.getInstance().RecipeObject.getImageFile()!=null) Picasso.with(mContext).load(CookFoodApp.getInstance().RecipeObject.getImageFile().getUrl()).error(R.drawable.recepi).into(iv_dialog_recipe);
            else Picasso.with(mContext).load(R.drawable.recepi).error(R.drawable.recepi).into(iv_dialog_recipe);

            if(CookFoodApp.getInstance().RecipeObject.getPublished()) rb_dialog_recipe_publish.setChecked(true);
            else rb_dialog_recipe_unpublish.setChecked(true);

            String cat = CookFoodApp.getInstance().UpperCaseEachWord(CookFoodApp.getInstance().RecipeObject.getCategory());
            spnr_dialog_category.setSelection(getIndexOfPCategory(cat));
        }
        dialog.show();
        ColorDrawable colorDrawable = new ColorDrawable(StatusBarColor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(colorDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            AskToSave();

            return true;
        } else if (id == R.id.action_menu_create_recipe_save) {
            OpenDialog();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (sl_create_recipe_globle != null &&
                (sl_create_recipe_globle.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sl_create_recipe_globle.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            sl_create_recipe_globle.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            AskToSave();
        }
    }


    public void AskToSave(){
        if (!command.equalsIgnoreCase("play")){ // ASK USER TO SAVE IF COMMAND IS create || edit
            AlertDialog.Builder adb = new AlertDialog.Builder(this)
                    .setTitle("Leave kitchen")
                    .setMessage("Are you sure you want to leave? (You can access this recipe later from 'My Recipe' section)")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            goAwayFromCreateThisActivity();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog ad =adb.create();
            ad.show();
        }else{
            goAwayFromCreateThisActivity();
        }
    }

    public void goAwayFromCreateThisActivity() {
        Log.d(TAG, "Unregistring service");

        ttobj.stop();
        if (mSensorManager != null)
            mSensorManager.unregisterListener((SensorEventListener) mContext);

        super.onBackPressed();
    }

}
