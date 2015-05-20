package com.xplorer.cookfood.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 22-03-2015.
 */
public class ProductAdaptor extends BaseAdapter {
    private Context mContext;
    String TAG = "ProductAdaptor";
    public List<Product> ProductList;
    public String from = "";
    public int ProductSizeToDelete;
    public int DoneProductDeleted;

    public ProductAdaptor(Context context, List<Product> productListArg, String fromstr) {
        mContext = context;
        ProductList = productListArg;
        this.from = fromstr;
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

    public String getDesc(Product p){
        String ret="";
        if(p.getAction().equalsIgnoreCase("none")){
            ret = p.getIngredient().getName()+" | ";
        }else{
            ret = p.getAction()+" [...] | ";
        }
        return ret;
    }

    public void setList(List<Product> productListArg){
        ProductList = productListArg;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_product, viewGroup, false);
            holder = new ViewHolder(view, mContext);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ViewHolder FinalHolder = holder;
        final int pos = i;


        String NameAction = ProductList.get(i).getAction();
        int Numbering =i+1;
        if (NameAction.equalsIgnoreCase("none")) {
            Ingredients ing = ProductList.get(i).getIngredient();
            Picasso.with(mContext).load(ing.getImageFile().getUrl()).error(R.drawable.error_image).into(FinalHolder.iv_item_product_action);
            holder.tv_item_product_action.setText(Numbering+". "+ing.getName());
            String Desc = ing.getNameHindi()+"\n"+ProductList.get(i).getQuantityCount()+" "+ProductList.get(i).getQuantityType();
            holder.tv_item_product_desc.setText(Desc);
            holder.ll_item_product_info.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext).load(CookFoodApp.getInstance().ActionImg.get(NameAction)).error(R.drawable.error_image).into(FinalHolder.iv_item_product_action);
            holder.tv_item_product_action.setText(Numbering+". "+NameAction);
            String Desc = "";


            for (int j = 0; (ProductList.get(i).getProductList()!=null) && (j < ProductList.get(i).getProductList().size()) && (j < 5); j++) {
                Desc += getDesc(ProductList.get(i).getProductList().get(j));
            }

            holder.tv_item_product_desc.setText(Desc);

            holder.ll_item_product_info_temp.setVisibility(View.GONE);
            holder.ll_item_product_info_stop.setVisibility(View.GONE);

            if(CookFoodApp.getInstance().ActionStopCriteria.get(NameAction)){
                holder.ll_item_product_info_stop.setVisibility(View.VISIBLE);
                if(ProductList.get(i).getStopCriteria()!=null && ProductList.get(i).getStopCriteria().equalsIgnoreCase("Provide Description Criteria")){
                    holder.tv_item_product_time.setText(ProductList.get(i).getStopReason());
                }else{
                    holder.tv_item_product_time.setText("Timer goes off");
                }
            }
            if(CookFoodApp.getInstance().ActionTemp.get(NameAction)){
                holder.ll_item_product_info_temp.setVisibility(View.VISIBLE);
                holder.tv_item_product_temp.setText(ProductList.get(i).getTemperature());
            }

            //holder.ll_item_product_info.setVisibility(View.GONE);
        }

        if(from.equalsIgnoreCase("Ingredients")){
            holder.iv_item_product_del.setVisibility(View.GONE);
        }else if(from.equalsIgnoreCase("ActionProduct")){
            holder.iv_item_product_del.setVisibility(View.VISIBLE);
            holder.iv_item_product_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AskToDelete(pos);
                }
            });
        }else if(from.equalsIgnoreCase("ActionActivity")){
            holder.iv_item_product_del.setVisibility(View.VISIBLE);
            holder.iv_item_product_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductList.remove(pos);

                    notifyDataSetChanged();
                }
            });
        }


        return view;
    }
    public void AskToDelete(final int pos){

        AlertDialog.Builder adb = new AlertDialog.Builder(mContext)
                .setTitle("Delete Product")
                .setMessage("All other associated products with this will also be deleted, Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(pos);
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

    }

    public void delete(int pos){
        CookFoodApp.getInstance().RunPreLoader(mContext);

        DeleteProductList= new HashMap();

        Product p_to_delete = ProductList.get(pos);

        DeleteProductList.put(p_to_delete, true);
        GetDeleteProductList(p_to_delete);


        ProductSizeToDelete = DeleteProductList.size();
        DoneProductDeleted =0;
        Log.d(TAG+" DeleteProductList", " Size:"+DeleteProductList.size());

        Iterator it = DeleteProductList.entrySet().iterator() ;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Product CurrentProductInDel = (Product) pair.getKey();    // + " = " + pair.getValue());
            CurrentProductInDel.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Log.d(TAG, e.toString());
                    }
                    DoneProductDeleted++;
                    if(ProductSizeToDelete==DoneProductDeleted){
                        CookFoodApp.getInstance().pd.dismiss();
                    }
                }
            });

            ProductList.remove(CurrentProductInDel);
        }

        if(DeleteProductList.size()==0){
            CookFoodApp.getInstance().pd.dismiss();
        }

        notifyDataSetChanged();

        CookFoodApp.getInstance().CustomProductList = ProductList;


       // CookFoodApp.getInstance().pd.dismiss();
    }

    public static HashMap<Product, Boolean> DeleteProductList = new HashMap();

    public void GetDeleteProductList(Product p_to_delete){

        for(int i=0; i< CookFoodApp.getInstance().CustomProductList.size(); i++){
            Product p = CookFoodApp.getInstance().CustomProductList.get(i);
            String NameAction = p.getAction();

            if(p!=p_to_delete && !NameAction.equalsIgnoreCase("none")) {
                for (int j = 0; j < p.getProductList().size(); j++) {
                    Product p_l = p.getProductList().get(j);
                    if (p_to_delete.equals(p_l)) {   // Product i contains p_to_delete in its product list

                        if(DeleteProductList.get(p)!=null && DeleteProductList.get(p)){
                            Log.d(TAG + " indexToDel(Exist)", j+"");
                        }else{
                            Log.d(TAG + " indexToDel", j+"");
                            DeleteProductList.put(p, true);
                            GetDeleteProductList(p);
                        }

                    }

                }
            }
        }
    }

    public List<Product> getList() {
        return ProductList;
    }

    public static class ViewHolder {
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
        @InjectView(R.id.iv_item_product_del)
        ImageView iv_item_product_del;
        @InjectView(R.id.ll_item_product_info_stop)
        LinearLayout ll_item_product_info_stop;

        @InjectView(R.id.ll_item_product_info_temp)
        LinearLayout ll_item_product_info_temp;

        public ViewHolder(View view, final Context context) {
            ButterKnife.inject(this, view);
        }

    }
}
