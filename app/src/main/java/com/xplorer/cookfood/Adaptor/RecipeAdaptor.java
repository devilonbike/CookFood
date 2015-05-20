package com.xplorer.cookfood.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.Object.UserInfo;
import com.xplorer.cookfood.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 26-03-2015.
 */
public class RecipeAdaptor extends BaseAdapter {

    Context mContext;
    public List<RecipeData> ProductList;

    public RecipeAdaptor(Context context, List<RecipeData> productList) {
        mContext = context;
        ProductList = productList;
    }

    @Override
    public int getCount() {
        return ProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return ProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_recipe, parent, false);
            holder = new ViewHolder(view, mContext);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ViewHolder FinalHolder = holder;
        final int pos = i;


        RecipeData p = ProductList.get(i);

        Picasso.with(mContext).load(p.getImageFile().getUrl()).error(R.drawable.recepi).into(FinalHolder.iv_item_recipe_food);
        holder.tv_item_recipe_title.setText(CookFoodApp.getInstance().UpperCaseEachWord(p.getTitle()));
        if(p.getDescription().equalsIgnoreCase("")){
            holder.tv_item_recipe_desc.setVisibility(View.GONE);
        }else{
            holder.tv_item_recipe_desc.setVisibility(View.VISIBLE);
            holder.tv_item_recipe_desc.setText(CookFoodApp.getInstance().UpperCase(p.getDescription()));
        }

        if(p.getCategory()==null){
            holder.tv_item_recipe_categoryname.setVisibility(View.GONE);
        }else{
            holder.tv_item_recipe_categoryname.setVisibility(View.VISIBLE);
            holder.tv_item_recipe_categoryname.setText("#"+CookFoodApp.getInstance().UpperCaseEachWord(p.getCategory()));
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", p.getOwner());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e!=null){
                    Log.d("ParseQuery<ParseUser>", e.toString());
                    return;
                }
                List<UserInfo> users_ = (List<UserInfo>) (List<?>) parseUsers;
                UserInfo usr = users_.get(0);
                FinalHolder.tv_item_recipe_ownername.setText(usr.getName());
                Picasso.with(mContext).load(usr.getImageFile().getUrl()).error(R.drawable.profile).into(FinalHolder.iv_item_recipe_owner);

            }
        });


        return view;
    }



    public static class ViewHolder {
        @InjectView(R.id.iv_item_recipe_food)
        ImageView iv_item_recipe_food;
        @InjectView(R.id.iv_item_recipe_owner)
        ImageView iv_item_recipe_owner;

        @InjectView(R.id.tv_item_recipe_title)
        TextView tv_item_recipe_title;
        @InjectView(R.id.tv_item_recipe_desc)
        TextView tv_item_recipe_desc;

        @InjectView(R.id.tv_item_recipe_ownername)
        TextView tv_item_recipe_ownername;

        @InjectView(R.id.tv_item_recipe_categoryname)
        TextView tv_item_recipe_categoryname;


        public ViewHolder(View view, final Context context) {
            ButterKnife.inject(this, view);
        }

    }
}
