package com.xplorer.cookfood.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Adaptor.ProductAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActionActivity extends ActionBarActivity {

    public int index =0;
    public String ActionName ="";
    public int _actionBarColor;
    public int _statusBarColor;
    public List<Product> ProdList =new ArrayList<Product>();

    Context _context;
    private static final String TAG = "ActionActivity";
    ProductAdaptor prodAdaptr;

    @InjectView(R.id.iv_action_icon)
    ImageView iv_action_icon;

    @InjectView(R.id.iv_action_add)
    ImageView iv_action_add;

    @InjectView(R.id.iv_action_addbg)
    ImageView iv_action_addbg;

    @InjectView(R.id.tv_aa_desc)
    TextView tv_aa_desc;

    @InjectView(R.id.lv_action_activity_prod)
    ListView lv_action_activity_prod;


    @InjectView(R.id.ll_action_activity_info)
    LinearLayout ll_action_activity_info;


    @InjectView(R.id.ll_action_activity_title)
    LinearLayout ll_action_activity_title;

    @InjectView(R.id.tv_action_activity_ingredient)
    TextView tv_action_activity_ingredient;

    @InjectView(R.id.b_dialog_quantity_confirm)
    Button b_dialog_quantity_confirm;

    boolean justCreated =true;
    boolean IsProductNew =true;
    int PositionOfProductInList =0;

    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);



        if(CookFoodApp.getInstance().EditProduct==null){
            p = new Product();
            p.setTemperature("40");
            p.setTime("60");
            p.setStopCriteria("Use Timer (In Minutes)");
            p.setStopReason("");
            ProdList =new ArrayList<Product>();
            IsProductNew=true;
        }else{
            p = CookFoodApp.getInstance().EditProduct;
            ProdList =CookFoodApp.getInstance().EditProduct.getProductList();
            PositionOfProductInList=getIdOfProduct(p);
            IsProductNew=false;
        }
        justCreated=true;
        _context = this;
        ButterKnife.inject(this);
    }

    public int getIdOfProduct(Product p){
        for(int i=0; i< CookFoodApp.getInstance().CustomProductList.size(); i++){
            if(CookFoodApp.getInstance().CustomProductList.get(i).equals(p)){
                return i;
            }
        }
        return -1;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(justCreated==false) {
            if (CookFoodApp.getInstance().CurrentCustomIngredient != null) {
                ProdList.add(CookFoodApp.getInstance().CurrentCustomIngredient);
                tv_action_activity_ingredient.setVisibility(View.GONE);
                lv_action_activity_prod.setVisibility(View.VISIBLE);
                lv_action_activity_prod.setAdapter(prodAdaptr);
                prodAdaptr.notifyDataSetChanged();
                CookFoodApp.getInstance().CurrentCustomIngredient = null;
            }

        }else{
            justCreated=false;
        }


    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);

        InitHeader();
        InitActionBar();
        InitMainScreen();
        return true;

    }
    public void InitHeader() {
        index = CookFoodApp.getInstance().currentAction;
        ActionName = CookFoodApp.getInstance().Actions[index];
        tv_aa_desc.setText(CookFoodApp.getInstance().ActionDescription.get(ActionName));
        Picasso.with(_context).load(CookFoodApp.getInstance().ActionImg.get(ActionName)).error(R.drawable.ic_launcher).into(iv_action_icon);

    }

    public void InitActionBar(){

        getSupportActionBar().setTitle(ActionName);


        if(CookFoodApp.getInstance().palletColor.containsKey(ActionName)) {
            _actionBarColor = CookFoodApp.getInstance().palletColor.get(ActionName);
        }else{
            Random rnd = new Random();
            _actionBarColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }


        _statusBarColor = CookFoodApp.getInstance().getDarkColor(_actionBarColor);

        int c = R.color.Red;
        Log.d("c", c+"");
        Log.d("_actionBarColor", _actionBarColor+"");
        Log.d("_statusBarColor", _statusBarColor+"");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(_actionBarColor));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.setStatusBarColor(_statusBarColor);

        }


    }


    public void InitMainScreen(){
        iv_action_add.setColorFilter(_actionBarColor);
        iv_action_addbg.setColorFilter(_statusBarColor);

        ll_action_activity_info.setBackgroundColor(_actionBarColor);
        ll_action_activity_title.setBackgroundColor(_statusBarColor);
        b_dialog_quantity_confirm.setBackgroundColor(_actionBarColor);

        iv_action_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                switch (arg1.getAction()) {

                    case MotionEvent.ACTION_UP: {
                        iv_action_add.setColorFilter(_actionBarColor);
                        iv_action_addbg.setColorFilter(_statusBarColor);
                        CookFoodApp.getInstance().CurrentCustomIngredient=null;
                        startActivity(new Intent(ActionActivity.this, IngredientsActivity.class));
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        iv_action_add.setColorFilter(_statusBarColor);
                        iv_action_addbg.setColorFilter(_actionBarColor);

                        break;
                    }
                    case MotionEvent.ACTION_OUTSIDE: {
                        iv_action_add.setColorFilter(_actionBarColor);
                        iv_action_addbg.setColorFilter(_statusBarColor);

                        break;
                    }
                }

                return true;
            }
        });

        if(CookFoodApp.getInstance().ActionTemp.get(ActionName) || CookFoodApp.getInstance().ActionStopCriteria.get(ActionName)){
            ll_action_activity_info.setVisibility(View.VISIBLE);
        }else{
            ll_action_activity_info.setVisibility(View.GONE);
        }
        b_dialog_quantity_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActionSpecific();
            }
        });
        prodAdaptr = new ProductAdaptor(_context, ProdList, "ActionActivity");

        if(ProdList.size()==0){
            tv_action_activity_ingredient.setVisibility(View.VISIBLE);
            lv_action_activity_prod.setVisibility(View.GONE);
        }else{
            tv_action_activity_ingredient.setVisibility(View.GONE);
            lv_action_activity_prod.setVisibility(View.VISIBLE);
            lv_action_activity_prod.setAdapter(prodAdaptr);
        }




    }

    public void setActionSpecific() {
        //if(CookFoodApp.getInstance().ActionTemp.get(ActionName) || CookFoodApp.getInstance().ActionStopCriteria.get(ActionName))

        final Dialog dialog = new Dialog(_context);
        dialog.setContentView(R.layout.dialog_action_specific);

        dialog.setTitle("Set "+ActionName+" specific options.");

        final SeekBar sb_action_specific_temperature = (SeekBar) dialog.findViewById(R.id.sb_action_specific_temperature);
        final EditText et_dialog_action_specific_tempVal = (EditText) dialog.findViewById(R.id.et_dialog_action_specific_tempVal);

        final LinearLayout ll_action_specific_temp = (LinearLayout) dialog.findViewById(R.id.ll_action_specific_temp);
        final LinearLayout ll_action_specific_stop_criteria = (LinearLayout) dialog.findViewById(R.id.ll_action_specific_stop_criteria);

        ll_action_specific_temp.setVisibility(View.GONE);
        ll_action_specific_stop_criteria.setVisibility(View.GONE);

        if(CookFoodApp.getInstance().ActionTemp.get(ActionName)){
            ll_action_specific_temp.setVisibility(View.VISIBLE);
        }
        if(CookFoodApp.getInstance().ActionStopCriteria.get(ActionName)){
            ll_action_specific_stop_criteria.setVisibility(View.VISIBLE);
        }


        et_dialog_action_specific_tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = et_dialog_action_specific_tempVal.getText()+"";
                if(!val.equalsIgnoreCase("") && !val.equalsIgnoreCase(".")) {
                    double d = Double.parseDouble(val);
                    if ((d == Math.floor(d)) && !Double.isInfinite(d)) {
                        int i = (int) d;
                        sb_action_specific_temperature.setProgress(i);
                    }
                }
            }
        });
        sb_action_specific_temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                et_dialog_action_specific_tempVal.setText(progress + "");
                et_dialog_action_specific_tempVal.setSelection(et_dialog_action_specific_tempVal.getText().length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        et_dialog_action_specific_tempVal.setText("40");

        final SeekBar sb_action_specific_time = (SeekBar) dialog.findViewById(R.id.sb_action_specific_time);
        final EditText et_dialog_action_specific_timeVal = (EditText) dialog.findViewById(R.id.et_dialog_action_specific_timeVal);
        et_dialog_action_specific_timeVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = et_dialog_action_specific_timeVal.getText()+"";
                if(!val.equalsIgnoreCase("") && !val.equalsIgnoreCase(".")) {
                    double d = Double.parseDouble(val);

                    if ((d == Math.floor(d)) && !Double.isInfinite(d)) {
                        int i = (int) d;
                        sb_action_specific_time.setProgress(i);
                        // int
                    }
                }

            }
        });

        sb_action_specific_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                et_dialog_action_specific_timeVal.setText(progress+"");
                et_dialog_action_specific_timeVal.setSelection(et_dialog_action_specific_timeVal.getText().length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        et_dialog_action_specific_timeVal.setText("60");




        final Spinner spnr_dialog_criteria = (Spinner) dialog.findViewById(R.id.sr_action_specific_stop_criteria);
        final EditText et_action_specific_stop_desc = (EditText) dialog.findViewById(R.id.et_action_specific_stop_desc);

        final Button b_dialog_action_specific_confirm = (Button) dialog.findViewById(R.id.b_dialog_action_specific_confirm);
        final Button b_dialog_action_specific_cancel = (Button) dialog.findViewById(R.id.b_dialog_action_specific_cancel);
        final LinearLayout ll_action_specific_time = (LinearLayout) dialog.findViewById(R.id.ll_action_specific_time);

        final String[] CriteriaTypes = {
                "Use Timer (In Minutes)",
                "Provide Description Criteria",
        };
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, CriteriaTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_dialog_criteria.setAdapter(dataAdapter);

        spnr_dialog_criteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String criteriaSelected = CriteriaTypes[i];

                if (criteriaSelected.equalsIgnoreCase("Use Timer (In Minutes)")) {
                    ll_action_specific_time.setVisibility(View.VISIBLE);
                    et_action_specific_stop_desc.setVisibility(View.GONE);
                } else if(criteriaSelected.equalsIgnoreCase("Provide Description Criteria")) {
                    et_action_specific_stop_desc.setVisibility(View.VISIBLE);
                    ll_action_specific_time.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(p.getTemperature()!=null){
            et_dialog_action_specific_tempVal.setText(p.getTemperature());
        }
        if(p.getStopCriteria()!=null){
            int index = Arrays.asList(CriteriaTypes).indexOf(p.getStopCriteria());
            spnr_dialog_criteria.setSelection(index);
            if(index==0){

                et_dialog_action_specific_timeVal.setText(p.getTime());
            }else{
                et_action_specific_stop_desc.setText(p.getStopReason());
            }

        }



            et_action_specific_stop_desc.setHint(ActionName+" until...");

        b_dialog_action_specific_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String criteriaSelected = spnr_dialog_criteria.getSelectedItem().toString();
                Log.d(TAG + " criteriaSelected", criteriaSelected);

                p.setStopCriteria(criteriaSelected);

                final String tempVal = et_dialog_action_specific_tempVal.getText().toString();
                p.setTemperature(tempVal);

                if (criteriaSelected.equalsIgnoreCase("Use Timer (In Minutes)")) {
                    final String desc = et_action_specific_stop_desc.getText().toString();
                    final String timeVal = et_dialog_action_specific_timeVal.getText().toString();

                    if(timeVal.equalsIgnoreCase("")){
                        Toast.makeText(_context, "Timer cannot be empty",Toast.LENGTH_LONG ).show();
                        return;
                    }
                    p.setTime(timeVal);
                    p.setStopReason(desc);
                }else{
                    final String desc = et_action_specific_stop_desc.getText().toString();
                    final String timeVal = et_dialog_action_specific_timeVal.getText().toString();

                    if(desc.equalsIgnoreCase("")){
                        Toast.makeText(_context, "Stop description cannot be empty",Toast.LENGTH_LONG ).show();
                        return;
                    }
                    p.setTime(timeVal);
                    p.setStopReason(desc);
                }

                dialog.dismiss();

            }
        });

        b_dialog_action_specific_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }


        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(R.color.DodgerBlue);

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
        }else if(id == R.id.action_add){
            ProdList = prodAdaptr.getList();
            Log.d("ProdList", ProdList.toString());
            if(ProdList.size()==0){
                Toast.makeText(_context, "Cannot save empty action, add some ingredients", Toast.LENGTH_SHORT).show();
                return true;
            }
            p.setAction(ActionName);
            p.setProductList(ProdList);
            if(IsProductNew){
                p.setOrder(CookFoodApp.getInstance().OrderId);
                CookFoodApp.getInstance().OrderId++;
                CookFoodApp.getInstance().CustomProductList.add(p);
            }else{
                if(PositionOfProductInList!=-1) CookFoodApp.getInstance().CustomProductList.set(PositionOfProductInList, p);

            }


            Toast.makeText(_context, "Saved custom product successfully.", Toast.LENGTH_SHORT).show();


            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
