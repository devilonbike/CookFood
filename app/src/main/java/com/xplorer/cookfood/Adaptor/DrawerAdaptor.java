package com.xplorer.cookfood.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.R;

/**
 * Created by Raghavendra on 25-03-2015.
 */
public class DrawerAdaptor  extends BaseAdapter {
    private Context mContext;
    String[] drawerCandidate = {};
    int[] drawerCandidateImg = {};

    public DrawerAdaptor(Context mContext) {
        this.mContext = mContext;
        this.drawerCandidate = CookFoodApp.getInstance().DrawerItems;
        this.drawerCandidateImg = CookFoodApp.getInstance().DrawerItemsImages;
    }


    @Override
    public int getCount() {
        return drawerCandidate.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerCandidate[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_drawer, parent, false);
        }

        TextView itemTxt = (TextView) convertView.findViewById(R.id.tv_item_drawer_element);
        ImageView imgView = (ImageView) convertView.findViewById(R.id.iv_item_drawer_img);
        LinearLayout ll_item_drawer = (LinearLayout) convertView.findViewById(R.id.ll_item_drawer);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ll_item_drawer.setBackgroundResource(R.drawable.item_selector);
        }


        itemTxt.setText(drawerCandidate[position].toString());
        Picasso.with(mContext).load(drawerCandidateImg[position]).error(R.drawable.error_image).into(imgView);

        return convertView;
    }
}
