package com.xplorer.cookfood.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.cookfood.Activity.CreateRecipeActivity;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.Object.UserInfo;
import com.xplorer.cookfood.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 26-03-2015.
 */
public class RecipeEditAdaptor extends BaseAdapter {

    Context _context;
    public List<RecipeData> ProductList;
    private static final String TAG = "RecipeEditAdaptor";
    int RecordCounter;
    int FoundCounter;

    public RecipeEditAdaptor(Context context, List<RecipeData> productList) {
        _context = context;
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
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_recipe_edit, parent, false);
            holder = new ViewHolder(view, _context);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ViewHolder FinalHolder = holder;
        final int pos = i;


        final RecipeData p = ProductList.get(i);

        if (p.getImageFile() != null)
            Picasso.with(_context).load(p.getImageFile().getUrl()).error(R.drawable.recepi).into(FinalHolder.iv_item_recipe_food);
        holder.tv_item_recipe_title.setText(CookFoodApp.getInstance().UpperCaseEachWord(p.getTitle()));
        if (p.getDescription().equalsIgnoreCase("")) {
            holder.tv_item_recipe_desc.setVisibility(View.GONE);
        } else {
            holder.tv_item_recipe_desc.setVisibility(View.VISIBLE);
            holder.tv_item_recipe_desc.setText(CookFoodApp.getInstance().UpperCase(p.getDescription()));
        }

        if (p.getCategory() == null) {
            holder.tv_item_recipe_categoryname.setVisibility(View.GONE);
        } else {
            holder.tv_item_recipe_categoryname.setVisibility(View.VISIBLE);
            holder.tv_item_recipe_categoryname.setText("#" + CookFoodApp.getInstance().UpperCaseEachWord(p.getCategory()));
        }

        holder.b_item_recipe_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RecipeDataObjectId = p.getObjectId();
                _context.startActivity(new Intent(_context, CreateRecipeActivity.class).putExtra("command", "edit").putExtra("RecipeDataObjectId", RecipeDataObjectId));

            }
        });
        holder.b_item_recipe_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RecipeDataObjectId = p.getObjectId();
                AskToDelete(RecipeDataObjectId, pos);
            }
        });
        Boolean isPublished = p.getPublished();
        if (isPublished) {
            FinalHolder.b_item_recipe_publish.setText("UNPUBLISH");
        } else {
            FinalHolder.b_item_recipe_publish.setText("PUBLISH");
        }

        holder.b_item_recipe_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isPublished = p.getPublished();
                p.setPublished(!isPublished);
                p.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(_context, "Saved change successfully", Toast.LENGTH_LONG).show();
                    }
                });

                if (isPublished) {
                    FinalHolder.b_item_recipe_publish.setText("PUBLISH");
                } else {
                    FinalHolder.b_item_recipe_publish.setText("UNPUBLISH");
                }
            }
        });


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", p.getOwner());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e != null) {
                    Log.d("ParseQuery<ParseUser>", e.toString());
                    return;
                }
                List<UserInfo> users_ = (List<UserInfo>) (List<?>) parseUsers;
                UserInfo usr = users_.get(0);
                FinalHolder.tv_item_recipe_ownername.setText(usr.getName());
                Picasso.with(_context).load(usr.getImageFile().getUrl()).error(R.drawable.profile).into(FinalHolder.iv_item_recipe_owner);

            }
        });


        return view;
    }

    public void DeleteProductRecipe(final String RecipeObjectId, final int pos) {
        Log.d(TAG, "DeleteProductRecipe");
        ParseQuery<Product> query = ParseQuery.getQuery("Product");
        query.whereEqualTo("RecipeDataId", RecipeObjectId);
        CookFoodApp.getInstance().RunPreLoader(_context);

        query.findInBackground(new FindCallback<Product>() {
                                   @Override
                                   public void done(List<Product> productListTemp, ParseException e) {

                                       if (e != null) {
                                           Log.d(TAG, e.toString());
                                           Toast.makeText(_context, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                                           CookFoodApp.getInstance().pd.dismiss();
                                           return;
                                       }

                                       RecordCounter = productListTemp.size();
                                       FoundCounter = 0;
                                       for (int i = 0; i < productListTemp.size(); i++) {

                                           productListTemp.get(i).deleteInBackground(new DeleteCallback() {
                                               @Override
                                               public void done(ParseException e) {
                                                   if (e != null) {
                                                       Log.d(TAG, e.toString());
                                                   }
                                                   FoundCounter++;
                                                   if (FoundCounter == RecordCounter) {
                                                       DeleteRecipe(RecipeObjectId, pos);
                                                   }

                                               }
                                           });

                                       }
                                       Log.d(TAG, "DeleteProductRecipe Size:" + productListTemp.size());

                                       if (productListTemp.size() == 0) {
                                           DeleteRecipe(RecipeObjectId, pos);
                                       }

                                   }
                               }

        );
    }

    public void DeleteRecipe(String RecipeObjectId, final int pos) {
        Log.d(TAG, "DeleteRecipe");
        ParseQuery<RecipeData> queryTemp = ParseQuery.getQuery("RecipeData");
        queryTemp.whereEqualTo("objectId", RecipeObjectId);

        queryTemp.findInBackground(new FindCallback<RecipeData>() {
            @Override
            public void done(List<RecipeData> RecipeDataList, ParseException e) {

                if (e != null || RecipeDataList.size() == 0) {
                    if (e != null) Log.d(TAG, e.toString());
                    Toast.makeText(_context, "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    CookFoodApp.getInstance().pd.dismiss();
                    return;
                }

                RecipeData temp = RecipeDataList.get(0);

                temp.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) Log.d(TAG, e.toString());
                        CookFoodApp.getInstance().pd.dismiss();
                        ProductList.remove(pos);
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    public void AskToDelete(final String RecipeObjectId, final int pos) {
        AlertDialog.Builder adb = new AlertDialog.Builder(_context)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteProductRecipe(RecipeObjectId, pos);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog ad = adb.create();
        ad.show();

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

        @InjectView(R.id.b_item_recipe_edit)
        Button b_item_recipe_edit;

        @InjectView(R.id.b_item_recipe_delete)
        Button b_item_recipe_delete;

        @InjectView(R.id.b_item_recipe_publish)
        Button b_item_recipe_publish;


        public ViewHolder(View view, final Context context) {
            ButterKnife.inject(this, view);
        }

    }
}
