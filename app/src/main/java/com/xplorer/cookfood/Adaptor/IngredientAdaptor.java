package com.xplorer.cookfood.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Activity.NutritionActivity;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 18-03-2015.
 */
public class IngredientAdaptor extends BaseAdapter {
    private Context mContext;

    private List<Ingredients> ingredientItems;
    private String TAG = "IngredientsAdaptor";

    private int IngredientType;

    public IngredientAdaptor(Context context, List<Ingredients> list, int ingredientType) {
        ingredientItems = list;
        mContext = context;
        IngredientType = ingredientType;
        if(IngredientType!=-1) TAG += " " + CookFoodApp.getInstance().IngredientsTitle[ingredientType];

    }


    @Override
    public int getCount() {
        return ingredientItems.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_ingredient, viewGroup, false);
            holder = new ViewHolder(view, mContext);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.tv_item_ingredient_name.setText(ingredientItems.get(i).getName());
        holder.tv_item_ingredient_description.setText(ingredientItems.get(i).getNameHindi());
        final Ingredients ing = ingredientItems.get(i);
        final ViewHolder FinalHolder = holder;
        final int pos = i;

        holder.ll_item_ingredient_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG + " ll_item_ingredient_top", ingredientItems.get(pos).getName());
                setQuantity(pos);
            }
        });
        if (IngredientType == 1 || IngredientType == 2) {
            holder.iv_item_ingredient_info.setVisibility(View.VISIBLE);


            holder.iv_item_ingredient_info.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    FinalHolder.iv_item_ingredient_info.setSelected(arg1.getAction() == MotionEvent.ACTION_DOWN);
                    switch (arg1.getAction()) {

                        case MotionEvent.ACTION_UP: {
                            Intent nutIntent = new Intent(mContext, NutritionActivity.class);
                            nutIntent.putExtra("name", ing.getName());
                            nutIntent.putExtra("mineral", ing.getMinerals());
                            nutIntent.putExtra("vitamin", ing.getVitamins());
                            nutIntent.putExtra("imgurl", ing.getImageFile().getUrl());

                            mContext.startActivity(nutIntent);



                            Log.d(TAG + " iv_item_ingredient_info", ingredientItems.get(pos).getName());
                            break;
                        }
                    }

                    return true;
                }
            });
        } else {
            holder.iv_item_ingredient_info.setVisibility(View.GONE);
        }


        Picasso.with(mContext).load(ingredientItems.get(i).getImageFile().getUrl()).
                error(R.drawable.error_image).into(FinalHolder.iv_item_ingredient_actionimg, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });


        return view;


    }

    public void setQuantity(final int pos) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_quantity);

        dialog.setTitle("Set quantity of ingredient");


        final Spinner spnr_dialog_quantity = (Spinner) dialog.findViewById(R.id.spnr_dialog_quantity);
        final EditText et_dialog_quantity = (EditText) dialog.findViewById(R.id.et_dialog_quantity);
        final Button b_dialog_quantity_confirm = (Button) dialog.findViewById(R.id.b_dialog_quantity_confirm);
        final Button b_dialog_quantity_cancel = (Button) dialog.findViewById(R.id.b_dialog_quantity_cancel);

        final String[] QuantityTypes = {
                "Count",
                "Gram (g)",
                "Kilogram (Kg)",
                "Cup Count",
                "Tea spoon (tsp)",
                "Table Spoon (tbsp)",
                "Packet",
                "As needed",
                "A few"
        };
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, QuantityTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_dialog_quantity.setAdapter(dataAdapter);

        spnr_dialog_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String quantitySelected = QuantityTypes[i];

                if (quantitySelected.equalsIgnoreCase("As needed") || quantitySelected.equalsIgnoreCase("A few")) {
                    et_dialog_quantity.setVisibility(View.GONE);
                } else {
                    et_dialog_quantity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        b_dialog_quantity_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String quantitySelected = spnr_dialog_quantity.getSelectedItem().toString();
                final String quantityCount = et_dialog_quantity.getText().toString();
                Log.d(TAG + " quantitySelected", quantitySelected);

                if (quantitySelected.equalsIgnoreCase("As needed") || quantitySelected.equalsIgnoreCase("A few")) {
                    CookFoodApp.getInstance().CurrentCustomIngredient = new Product();
                    CookFoodApp.getInstance().CurrentCustomIngredient.setAction("none");
                    CookFoodApp.getInstance().CurrentCustomIngredient.setIngredient(ingredientItems.get(pos));
                    CookFoodApp.getInstance().CurrentCustomIngredient.setQuantityType(quantitySelected);
                    CookFoodApp.getInstance().CurrentCustomIngredient.setQuantityCount(quantityCount);

                    dialog.dismiss();
                    ((Activity) mContext).finish();
                } else {
                    if (quantityCount.equalsIgnoreCase("")) {
                        Toast.makeText(mContext, "Enter amount of selected quantity type", Toast.LENGTH_LONG).show();
                    } else {
                        CookFoodApp.getInstance().CurrentCustomIngredient = new Product();
                        CookFoodApp.getInstance().CurrentCustomIngredient.setAction("none");
                        CookFoodApp.getInstance().CurrentCustomIngredient.setIngredient(ingredientItems.get(pos));
                        CookFoodApp.getInstance().CurrentCustomIngredient.setQuantityType(quantitySelected);
                        CookFoodApp.getInstance().CurrentCustomIngredient.setQuantityCount(quantityCount);

                        dialog.dismiss();
                        ((Activity) mContext).finish();
                    }
                }


            }
        });

        b_dialog_quantity_cancel.setOnClickListener(new View.OnClickListener() {
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

}
