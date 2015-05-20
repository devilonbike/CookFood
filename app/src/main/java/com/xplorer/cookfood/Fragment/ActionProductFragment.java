package com.xplorer.cookfood.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.xplorer.cookfood.Activity.ActionActivity;
import com.xplorer.cookfood.Adaptor.ActionAdaptor;
import com.xplorer.cookfood.Adaptor.ProductAdaptor;
import com.xplorer.cookfood.Config.CookFoodApp;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionProductFragment extends Fragment {

    public int SCREEN_ID;
    public String TitleName;
    public String TAG = "ActionProductFragment";
    public ProductAdaptor prodAdaptr;
    ActionAdaptor actionAdptr;
    int RecordCounter;

    Boolean UploadInProgress = false;
    public int uploadedCount;
    public int GoingToUpload = 5 ;
    public int FoundCounter =0;

    @InjectView(R.id.lv_fragement_action_product)
    ListView lv_fragement_action_product;

    @InjectView(R.id.tv_fragement_action_productt)
    TextView tv_fragement_action_productt;


    public ActionProductFragment() {
        // Required empty public constructor
    }

    public static ActionProductFragment newInstance(int someInt) {
        ActionProductFragment myFragment = new ActionProductFragment();

        Bundle args = new Bundle();
        args.putInt("SCREEN_ID", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_product, container, false);
        ButterKnife.inject(this, view);

        SCREEN_ID = getArguments().getInt("SCREEN_ID", 1);
        if(SCREEN_ID==0){
            TitleName="Actions";
        }else if(SCREEN_ID==1){
            TitleName="Products";
        }

        TAG += " "+TitleName;
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(SCREEN_ID==0){   //ACTIONS
            actionAdptr = new ActionAdaptor(getActivity());
            // MultiItemRowListAdapter twoRowAdapter = new MultiItemRowListAdapter(mContext, actionAdptr, 2, 0 );
            lv_fragement_action_product.setAdapter(actionAdptr);
            lv_fragement_action_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CookFoodApp.getInstance().CurrentCustomIngredient=null;
                    CookFoodApp.getInstance().EditProduct = null;
                    CookFoodApp.getInstance().currentAction = position;
                    String transitionName = "actionImg";

                    Intent intent = new Intent(getActivity(), ActionActivity.class);
                    startActivityForResult(intent, 3);

                }
            });

        }else if(SCREEN_ID==1){   //ACTIONS

            prodAdaptr = new ProductAdaptor(getActivity(), CookFoodApp.getInstance().CustomProductList, "ActionProduct");
            lv_fragement_action_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CookFoodApp.getInstance().CurrentCustomIngredient=null;

                    CookFoodApp.getInstance().EditProduct = CookFoodApp.getInstance().CustomProductList.get(position);

                    String ActionName = CookFoodApp.getInstance().EditProduct.getAction();
                    CookFoodApp.getInstance().currentAction =0;
                    for(int i=0; i< CookFoodApp.getInstance().Actions.length; i++){
                        if(CookFoodApp.getInstance().Actions[i].equalsIgnoreCase(ActionName)){
                            CookFoodApp.getInstance().currentAction = i;
                        }
                    }
                    String transitionName = "actionImg";

                    Intent intent = new Intent(getActivity(), ActionActivity.class);
                    startActivityForResult(intent, 3);
                }
            });
            lv_fragement_action_product.setAdapter(prodAdaptr);

            if(CookFoodApp.getInstance().CustomProductList.size()==0){
                tv_fragement_action_productt.setVisibility(View.VISIBLE);
                lv_fragement_action_product.setVisibility(View.GONE);
            }else{
                tv_fragement_action_productt.setVisibility(View.GONE);
                lv_fragement_action_product.setVisibility(View.VISIBLE);

            }
        }


    }

    public void refreshProdList(){
        if(prodAdaptr!=null){
            Log.d(TAG, "prodAdaptr!=null");
            prodAdaptr.setList(CookFoodApp.getInstance().CustomProductList);
            prodAdaptr.notifyDataSetChanged();
            if(CookFoodApp.getInstance().CustomProductList.size()==0){
                tv_fragement_action_productt.setVisibility(View.VISIBLE);
                lv_fragement_action_product.setVisibility(View.GONE);
            }else{
                tv_fragement_action_productt.setVisibility(View.GONE);
                lv_fragement_action_product.setVisibility(View.VISIBLE);

            }
        }else{
            Log.d(TAG, "prodAdaptr==null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3){
            Log.d(TAG, "onActivityResult");
            uploadRecipeConstantly();
            refreshProdList();
        }

    }

    public void uploadRecipeConstantly() {


        String owner = CookFoodApp.getInstance().user.getObjectId();

        if (CookFoodApp.getInstance().RecipeObject == null) {
            CookFoodApp.getInstance().RecipeObject = new RecipeData();
            CookFoodApp.getInstance().RecipeObject.setTitle("Unsaved Recipe");
            CookFoodApp.getInstance().RecipeObject.setDescription("");
            CookFoodApp.getInstance().RecipeObject.setCategory("Appetizer");
        }


        CookFoodApp.getInstance().RecipeObject.setOwner(owner);

        CookFoodApp.getInstance().RecipeObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {
                    Log.d("saveInBackground RecipeObject", e.toString());
                    return;
                }

                String RecipeObjectId = CookFoodApp.getInstance().RecipeObject.getObjectId();
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

    public void uploadInChunks(){
        Log.d(TAG,"uploadInChunks" );
        //IF ALL THE RECIPE HAS BEEN UPLOADED
        if(CookFoodApp.getInstance().CustomProductList.size()== uploadedCount || UploadInProgress){
            Log.d("FoundCounter return, UploadInProgress:", UploadInProgress+"");
            // UPLOAD COMPLETE
            return;
        }
        UploadInProgress=true;
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
                        Toast.makeText(getActivity(), "Error while saving Recipe", Toast.LENGTH_SHORT).show();
                    }

                    uploadedCount++;
                    FoundCounter++;
                    Log.d("ProductSaving: ["+FoundCounter+"/"+GoingToUpload+"]", uploadedCount+"/"+CookFoodApp.getInstance().CustomProductList.size());
                    if (FoundCounter == GoingToUpload) {
                        Log.d("ProductSaving","Calling Upload Again");
                        UploadInProgress=false;
                        uploadInChunks();

                    }


                }
            });

        }


    }


}
