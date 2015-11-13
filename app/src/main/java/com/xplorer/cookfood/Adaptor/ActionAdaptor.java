package com.xplorer.cookfood.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
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

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 16-03-2015.
 */
public class ActionAdaptor extends BaseAdapter {
    private Context _context;

    public ActionAdaptor(Context context){
        _context = context;


    }
    @Override
    public int getCount() {
        return CookFoodApp.getInstance().Actions.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_action, viewGroup, false);
            holder = new ViewHolder(view, _context);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        String name = CookFoodApp.getInstance().Actions[i];
        holder.tv_item_action_name.setText(name);
        holder.tv_item_action_description.setText(CookFoodApp.getInstance().ActionDescription.get(name));
        final ViewHolder FinalHolder = holder;



        Picasso.with(_context).load(CookFoodApp.getInstance().ActionImg.get(name)).
                error(R.drawable.error_image).into(FinalHolder.iv_item_action_actionimg, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                String name = (String) FinalHolder.tv_item_action_name.getText();
                if(CookFoodApp.getInstance().palletColor.containsKey(name)) {
                    FinalHolder.iv_item_action_actionimg.setBackgroundColor(CookFoodApp.getInstance().palletColor.get(name));
                }else {
                    Bitmap bitmap = ((BitmapDrawable) FinalHolder.iv_item_action_actionimg.getDrawable()).getBitmap();
                    Palette palette = Palette.generate(bitmap);
                    int vibrant = palette.getVibrantColor(0x000000);

                    if(vibrant==0){
                        Random rnd = new Random();
                        vibrant = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    }
                    CookFoodApp.getInstance().palletColor.put(name, vibrant);

                    FinalHolder.iv_item_action_actionimg.setBackgroundColor(CookFoodApp.getInstance().palletColor.get(name));

                }
            }

            @Override
            public void onError() {

            }
        });




        return view;
    }

    public static class ViewHolder {
        @InjectView(R.id.iv_item_action_actionimg)ImageView iv_item_action_actionimg;
        @InjectView(R.id.tv_item_action_name)TextView tv_item_action_name;
        @InjectView(R.id.tv_item_action_description)TextView tv_item_action_description;
        @InjectView(R.id.ll_item_action_top)LinearLayout ll_item_action_top;



        public ViewHolder(View view, final Context context ){
            ButterKnife.inject(this, view);
        }

    }
}
