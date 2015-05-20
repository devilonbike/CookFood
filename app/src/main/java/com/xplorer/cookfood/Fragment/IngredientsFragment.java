package com.xplorer.cookfood.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.xplorer.cookfood.Adaptor.IngredientAdaptor;
import com.xplorer.cookfood.Adaptor.MultiItemRowListAdapter;
import com.xplorer.cookfood.Adaptor.ProductAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {


    private String TAG = "IngredientsFragment";
    private List<Ingredients> ingredientItems;
    int ingredientType;
    String IngTitleName;

    public IngredientAdaptor IngAdapr;
    public ProductAdaptor prodAdaptr;
    private boolean loadingMore = false;
    private boolean GotAllData = false;
    View footerView;

    @InjectView(R.id.lv_fragement_ingredient)
    ListView lv_fragement_ingredient;

    @InjectView(R.id.tv_fragement_ingredient)
    TextView tv_fragement_ingredient;



    public IngredientsFragment() {
        // Required empty public constructor
    }

    public static IngredientsFragment newInstance(int someInt) {
        IngredientsFragment myFragment = new IngredientsFragment();

        Bundle args = new Bundle();
        args.putInt("ingredientType", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.inject(this, view);

        ingredientType = getArguments().getInt("ingredientType", 1);
        IngTitleName = CookFoodApp.getInstance().IngredientsTitle[ingredientType];

        TAG += " "+CookFoodApp.getInstance().IngredientsTitle[ingredientType];
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(ingredientType!=0) {
            ingredientItems = new ArrayList<Ingredients>();
            IngAdapr = new IngredientAdaptor(getActivity(),ingredientItems, ingredientType );
            MultiItemRowListAdapter twoRowAdapter = new MultiItemRowListAdapter(getActivity(), IngAdapr, 3, 5 );

            footerView = getActivity().getLayoutInflater().inflate(R.layout.footer_loadmore, null, false);
            GotAllData=false;
            lv_fragement_ingredient.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    //is the bottom item visible & not loading more already ? Load more !
                    if ((lastInScreen == totalItemCount) && !(loadingMore) && !GotAllData) {
                        loadingMore = true;
                        lv_fragement_ingredient.addFooterView(footerView);
                        FetchIngredientsForThisFragment();
                    }
                }
            });
            lv_fragement_ingredient.setAdapter(twoRowAdapter);

        }else{
            if(CookFoodApp.getInstance().CustomProductList.size()==0){
                tv_fragement_ingredient.setVisibility(View.VISIBLE);
                lv_fragement_ingredient.setVisibility(View.GONE);
            }else{
                tv_fragement_ingredient.setVisibility(View.GONE);
                lv_fragement_ingredient.setVisibility(View.VISIBLE);
                prodAdaptr = new ProductAdaptor(getActivity(), CookFoodApp.getInstance().CustomProductList, "Ingredients");
                lv_fragement_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CookFoodApp.getInstance().CurrentCustomIngredient = CookFoodApp.getInstance().CustomProductList.get(position);
                        getActivity().finish();
                    }
                });
                lv_fragement_ingredient.setAdapter(prodAdaptr);
            }

        }




    }


    public void FetchIngredientsForThisFragment(){
        ParseQuery<Ingredients> query = ParseQuery.getQuery("Ingredients");
        query.whereEqualTo("Category", CookFoodApp.getInstance().IngredientsTitle[ingredientType]);
        query.orderByAscending("Name");


        query.setLimit(5);
        query.setSkip(ingredientItems.size());

        query.findInBackground(new FindCallback<Ingredients>() {
            @Override
            public void done(final List<Ingredients> IngredientsList, ParseException e) {

                if (e != null) {    // There was some error.
                    Log.d(TAG, e.toString());
                    if(getActivity()!=null) Toast.makeText(getActivity(), "Server Database Error.", Toast.LENGTH_LONG).show();

                    return;
                }

                if(IngredientsList.size()==0){
                    GotAllData=true;
                }

                if (IngredientsList != null) {

                    ingredientItems.addAll(IngredientsList);
                    if(lv_fragement_ingredient.getFooterViewsCount()!=0){
                        try {
                             lv_fragement_ingredient.removeFooterView(footerView);
                        }catch (Exception e2){
                             Log.d(TAG+" lv_fragement_ingredient exception", e2.toString());
                        }
                    }
                    IngAdapr.notifyDataSetChanged();
                    loadingMore = false;
                }

                /*
                ParseObject.unpinAllInBackground(IngTitleName, IngredientsList, new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {    // There was some error.
                            Log.d(TAG, e.toString());
                            if(getActivity()!=null) Toast.makeText(getActivity(), "Error in deleting cache.", Toast.LENGTH_LONG).show();

                        }

                        ParseObject.pinAllInBackground(IngTitleName, IngredientsList);
                    }
                });*/


            }
        });
    }


}
