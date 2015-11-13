package com.xplorer.cookfood.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Adaptor.DrawerAdaptor;
import com.xplorer.cookfood.Adaptor.RecipeAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.Object.UserInfo;
import com.xplorer.cookfood.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    Context _context;
    private static final String TAG = "MainActivity";

    public int _actionBarColor;
    public int _statusBarColor;
    Dialog dialog;
    ImageView iv_dialog_sign_log;
    UserInfo user;
    byte[] dataImage = null;

    //-------------Search-------------------//

    MenuItem searchItem;
    SearchView searchView;

    //-------------Drawer-------------------//
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public LinearLayout ll_main_drawerparent;
    private DrawerAdaptor drawerAdptr;

    public List<RecipeData> RecipeList;

    @InjectView(R.id.lv_main)
    ListView lv_main;

    @InjectView(R.id.tv_activity_main_info)
    TextView tv_activity_main_info;



    boolean login = true;

    View footerView;

    boolean loadingMore = false;
    boolean GotAllData = false;
    RecipeAdaptor recpAdapt;

    boolean FirstTime =true;
    boolean ReloadList =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _context = this;
        ButterKnife.inject(this);
        dialog = new Dialog(_context);
        InitActionBar();
        LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        footerView = mInflater.inflate(R.layout.footer_loadmore, null, false);
        GotAllData=false;
        loadingMore=false;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        user = (UserInfo) ParseUser.getCurrentUser();
        if(user==null){
            dialogSignUpLogIn();
        }else{
            CookFoodApp.getInstance().user= user;
            AfterUserLoggedIn();
        }



    }

    public void AfterUserLoggedIn(){
        setUpDrawer();
        setUpList();
    }

    public void InitActionBar() {

        _actionBarColor = ContextCompat.getColor(_context,R.color.LimeGreen);
        _statusBarColor = CookFoodApp.getInstance().getDarkColor(_actionBarColor);


        getSupportActionBar().setTitle("Cook Food");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(_actionBarColor));
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.setStatusBarColor(_statusBarColor);

        }
    }

    public void dialogSignUpLogIn() {

        dialog.setContentView(R.layout.dialog_sign_up_log_in);

        dialog.setTitle(Html.fromHtml("<font color='#FFFFFF' >Enter your information</font>"));

        final LinearLayout ll_dialog_sign_top = (LinearLayout) dialog.findViewById(R.id.ll_dialog_sign_top);
        ll_dialog_sign_top.setVisibility(View.VISIBLE);


        final EditText et_dialog_name = (EditText) dialog.findViewById(R.id.et_sign_log_name);
        final EditText et_dialog_email = (EditText) dialog.findViewById(R.id.et_sign_log_email);
        final EditText et_dialog_pwd = (EditText) dialog.findViewById(R.id.et_sign_log_pwd);
        iv_dialog_sign_log = (ImageView) dialog.findViewById(R.id.iv_dialog_sign_log);

        et_dialog_name.setText("");
        String emailAddr = getEmail();
        if(emailAddr!=null) et_dialog_email.setText(emailAddr);

        final Button b_sign_log_confirm = (Button) dialog.findViewById(R.id.b_sign_log_confirm);
        final Button b_sign_up = (Button) dialog.findViewById(R.id.b_sign_up);
        final Button b_log_in = (Button) dialog.findViewById(R.id.b_log_in);


        b_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable staticBg = getResources().getDrawable(R.color.gray_light_8);
                Drawable btnBg = getResources().getDrawable(R.drawable.list_selector);


                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    b_log_in.setBackgroundDrawable(btnBg);
                    b_sign_up.setBackgroundDrawable(staticBg);
                } else {
                    b_log_in.setBackground(btnBg);
                    b_sign_up.setBackground(staticBg);
                }
                et_dialog_name.setVisibility(View.VISIBLE);
                iv_dialog_sign_log.setVisibility(View.VISIBLE);
                login=false;
            }
        });
        b_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable staticBg = getResources().getDrawable(R.color.gray_light_8);
                Drawable btnBg = getResources().getDrawable(R.drawable.list_selector);

                int sdk = android.os.Build.VERSION.SDK_INT;
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    b_log_in.setBackgroundDrawable(staticBg);
                    b_sign_up.setBackgroundDrawable(btnBg);
                } else {
                    b_log_in.setBackground(staticBg);
                    b_sign_up.setBackground(btnBg);
                }
                et_dialog_name.setVisibility(View.GONE);
                iv_dialog_sign_log.setVisibility(View.GONE);
                login=true;
            }
        });

        iv_dialog_sign_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        b_sign_log_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_dialog_name.getText().toString();
                final String email = et_dialog_email.getText().toString();
                String pswd = et_dialog_pwd.getText().toString();
                if(name.equalsIgnoreCase("") && !login){
                    Toast.makeText(_context, "Please Enter Name.", Toast.LENGTH_SHORT).show();
                }else if(email.equalsIgnoreCase("")){
                    Toast.makeText(_context, "Please Enter Email Address.", Toast.LENGTH_SHORT).show();
                }else if(pswd.equalsIgnoreCase("")){
                    Toast.makeText(_context, "Please Enter Password.", Toast.LENGTH_SHORT).show();
                }else{
                    CookFoodApp.getInstance().RunPreLoader(_context);
                    user = new UserInfo();
                    user.setName(name);
                    user.setUsername(email);
                    user.setPassword(pswd);

                    if(login){
                        ParseUser.logInInBackground(email, pswd, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if(e!=null){
                                    Log.e(TAG, e.toString());
                                    Toast.makeText(_context, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                                    CookFoodApp.getInstance().pd.dismiss();
                                    return;
                                }else if(parseUser!=null){
                                    CookFoodApp.getInstance().pd.dismiss();
                                    CookFoodApp.getInstance().user = (UserInfo) parseUser;
                                    Log.d(TAG,"Log in "+CookFoodApp.getInstance().user.getName());
                                    Toast.makeText(_context, "User Logged in successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    AfterUserLoggedIn();
                                }
                            }
                        });

                    }else{
                        Bitmap bitmap = ((BitmapDrawable)iv_dialog_sign_log.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        dataImage = stream.toByteArray();

                        final ParseFile file = new ParseFile("img.jpg", dataImage);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    Log.e(TAG, e.toString());

                                    Toast.makeText(_context, "Please check your Internet connection.", Toast.LENGTH_SHORT).show();

                                    CookFoodApp.getInstance().pd.dismiss();
                                    dialog.dismiss();
                                    return;
                                }
                                user.setImageFile(file);

                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e!=null){
                                            Log.e(TAG, e.toString());

                                            if(e.toString().equalsIgnoreCase("com.parse.ParseException: username raghav.bazari@gmail.com already taken")){
                                                Toast.makeText(_context, "Email address, \""+email+"\" already taken.", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(_context, "Please check your Internet connection.", Toast.LENGTH_SHORT).show();

                                            }
                                            CookFoodApp.getInstance().pd.dismiss();
                                            return;
                                        }

                                        CookFoodApp.getInstance().pd.dismiss();
                                        CookFoodApp.getInstance().user = user;
                                        Toast.makeText(_context, "You Signed up Successfully", Toast.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                        AfterUserLoggedIn();
                                    }
                                });
                            }
                        });
                    }


                }

            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(_statusBarColor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(colorDrawable);

    }

    public void dialogProfile() {

        dialog.setContentView(R.layout.dialog_sign_up_log_in);

        dialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>User Profile</font>"));

        final LinearLayout ll_dialog_sign_top = (LinearLayout) dialog.findViewById(R.id.ll_dialog_sign_top);
        ll_dialog_sign_top.setVisibility(View.GONE);

        final EditText et_dialog_name = (EditText) dialog.findViewById(R.id.et_sign_log_name);
        final EditText et_dialog_email = (EditText) dialog.findViewById(R.id.et_sign_log_email);
        final EditText et_dialog_pwd = (EditText) dialog.findViewById(R.id.et_sign_log_pwd);
        iv_dialog_sign_log = (ImageView) dialog.findViewById(R.id.iv_dialog_sign_log);

        if(CookFoodApp.getInstance().user==null){
            Toast.makeText(_context, "Please Sign up first", Toast.LENGTH_SHORT).show();
            return;
        }

        et_dialog_name.setText(CookFoodApp.getInstance().user.getName());
        et_dialog_email.setText(CookFoodApp.getInstance().user.getUsername());
        et_dialog_pwd.setText("********");
        et_dialog_name.setVisibility(View.VISIBLE);
        iv_dialog_sign_log.setVisibility(View.VISIBLE);
        Picasso.with(_context).load(CookFoodApp.getInstance().user.getImageFile().getUrl()).error(R.drawable.error_image).into(iv_dialog_sign_log);

        final Button b_sign_log_confirm = (Button) dialog.findViewById(R.id.b_sign_log_confirm);

        iv_dialog_sign_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        b_sign_log_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = et_dialog_name.getText().toString();
                final String email = et_dialog_email.getText().toString();
                final String pswd = et_dialog_pwd.getText().toString();
                if(name.equalsIgnoreCase("")){
                    Toast.makeText(_context, "Please Enter Name.", Toast.LENGTH_SHORT).show();
                }else if(email.equalsIgnoreCase("")){
                    Toast.makeText(_context, "Please Enter Email Address.", Toast.LENGTH_SHORT).show();
                }else if(pswd.equalsIgnoreCase("")){
                    Toast.makeText(_context, "Please Enter Password.", Toast.LENGTH_SHORT).show();
                }else {
                    CookFoodApp.getInstance().RunPreLoader(_context);


                    Bitmap bitmap = ((BitmapDrawable) iv_dialog_sign_log.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    dataImage = stream.toByteArray();

                    final ParseFile file = new ParseFile("img.jpg", dataImage);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG + " (while uploading)", e.toString());
                                CookFoodApp.getInstance().pd.dismiss();
                                dialog.dismiss();
                                Toast.makeText(_context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            CookFoodApp.getInstance().user.setName(name);
                            CookFoodApp.getInstance().user.setUsername(email);
                            if(!pswd.equalsIgnoreCase("********")) CookFoodApp.getInstance().user.setPassword(pswd);
                            CookFoodApp.getInstance().user.setImageFile(file);
                            CookFoodApp.getInstance().user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, e.toString());
                                        CookFoodApp.getInstance().pd.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(_context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    CookFoodApp.getInstance().pd.dismiss();
                                    Toast.makeText(_context, "Profile saved successfully.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });


                        }
                    });
                }

            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(_statusBarColor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(colorDrawable);

    }

    private void setUpDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawerlayout);
        mDrawerList = (ListView) findViewById(R.id.lv_main_drawer);
        ll_main_drawerparent = (LinearLayout) findViewById(R.id.ll_main_drawerparent);


        // Setting drawer width 80%
        Double d = getResources().getDisplayMetrics().widthPixels*.85;
        int width = d.intValue();
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) ll_main_drawerparent.getLayoutParams();
        params.width = width;
        ll_main_drawerparent.setLayoutParams(params);



        drawerAdptr = new DrawerAdaptor(_context);

        LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View headerView = mInflater.inflate(R.layout.item_header_profile, null, false);

        TextView tv_header_profile_name = (TextView) headerView.findViewById(R.id.tv_header_profile_name);
        TextView tv_header_profile_email = (TextView) headerView.findViewById(R.id.tv_header_profile_email);
        ImageView iv_header_profile_image= (ImageView) headerView.findViewById(R.id.iv_header_profile_image);

        if(CookFoodApp.getInstance().user!=null) {
            tv_header_profile_name.setText(CookFoodApp.getInstance().user.getName());
            tv_header_profile_email.setText(CookFoodApp.getInstance().user.getUsername());
            Picasso.with(_context).load(CookFoodApp.getInstance().user.getImageFile().getUrl()).error(R.drawable.error_image).into(iv_header_profile_image);
        }else{
            Log.d(TAG, "Drawer Error, user is null");
        }

        mDrawerList.addHeaderView(headerView);
        mDrawerList.setAdapter(drawerAdptr);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "mDrawerList:"+i);
                if(i == 0){
                    dialogProfile();
                }else if (i == 1) {   //create recipe
                    ReloadList=true;
                    startActivity(new Intent(_context, CreateRecipeActivity.class).putExtra("command","create").putExtra("RecipeDataObjectId", "none"));

                } else if(i==2){    //My Recipe
                    startActivity(new Intent(_context, RecipeActivity.class).putExtra("TITLE","My Recipes"));

                }else if(i==3){ //Saved Recipe
                    startActivity(new Intent(_context, RecipeActivity.class).putExtra("TITLE","Saved Recipes"));

                }
                mDrawerLayout.closeDrawers();


            }
        });

        ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.app_icon, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                //invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void setUpList(){
        RecipeList = new ArrayList<RecipeData>();
        recpAdapt = new RecipeAdaptor(_context,RecipeList);

        GotAllData=false;
        loadingMore=false;
        lv_main.addFooterView(footerView, null, false);

        lv_main.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                //is the bottom item visible & not loading more already ? Load more !
                if ((lastInScreen == totalItemCount) && !(loadingMore) && !GotAllData) {
                    Log.d(TAG, "onScroll");
                    lv_main.setVisibility(View.VISIBLE);
                    tv_activity_main_info.setVisibility(View.GONE);
                    footerView.setVisibility(View.VISIBLE);
                    loadingMore = true;
                    FetchRecipe();
                }
            }
        });
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            RecipeData r = RecipeList.get(position);
            int views = r.getViews();
            views++;
            r.setViews(views);
            r.saveInBackground();

            String RecipeDataObjectId =r.getObjectId();
            Log.d(TAG+" Click", RecipeDataObjectId);
            startActivity(new Intent(_context, CreateRecipeActivity.class).putExtra("command","play").putExtra("RecipeDataObjectId",RecipeDataObjectId));

            }
        });
        lv_main.setAdapter(recpAdapt);

        tv_activity_main_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_main.setVisibility(View.VISIBLE);
                tv_activity_main_info.setVisibility(View.GONE);

                ReloadList=false;
                RecipeList.clear();
                loadingMore = false;
                GotAllData = false;
                recpAdapt.notifyDataSetChanged();
                Log.d("tv_activity_main_info","Click");

            }
        });
    }

    public void FetchRecipe(){
        ParseQuery<RecipeData> query = ParseQuery.getQuery("RecipeData");
        query.whereEqualTo("Published", true);
        query.orderByDescending("createdAt");

        query.setLimit(5);

        query.setSkip(RecipeList.size());

        query.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(List<RecipeData> productListTemp, ParseException e) {
                Log.d(TAG, "done");
                footerView.setVisibility(View.GONE);

                if (e != null) {
                    Log.d(TAG+" done(error)", e.toString());
                    ReloadList=true;

                    lv_main.setVisibility(View.GONE);
                    tv_activity_main_info.setVisibility(View.VISIBLE);

                    return;
                }
                lv_main.setVisibility(View.VISIBLE);
                tv_activity_main_info.setVisibility(View.GONE);
                if(productListTemp.size()==0){
                    GotAllData=true;
                }

                RecipeList.addAll(productListTemp);

                recpAdapt.notifyDataSetChanged();
                loadingMore = false;


            };


        });

    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
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
                    int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);


                    Bitmap bmImg = getResizedBitmap(rotatedBitmap);
                    iv_dialog_sign_log.setImageBitmap(bmImg);


                    f.delete();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap bm = getResizedBitmap(thumbnail);
                iv_dialog_sign_log.setImageBitmap(bm);



            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = 200;
            height = (int) (width / bitmapRatio);
        } else {
            height = 240;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getEmail() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            if(email!=null)
                return email;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(searchItem!=null)
            MenuItemCompat.collapseActionView(searchItem);

        if(FirstTime){  //Do Nothing
            FirstTime=false;
        }else if(ReloadList==true){
            ReloadList=false;
            RecipeList.clear();
            loadingMore = false;
            GotAllData = false;
            recpAdapt.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_main_menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        ComponentName cn = new ComponentName(this, SearchRecipeActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

//setting search color to while
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView searchText = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        searchText.setHintTextColor(ContextCompat.getColor(_context,R.color.white));
        searchText.setTextColor(ContextCompat.getColor(_context,R.color.white));

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
