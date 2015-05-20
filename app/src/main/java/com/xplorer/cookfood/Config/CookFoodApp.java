package com.xplorer.cookfood.Config;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.xplorer.cookfood.Object.Ingredients;
import com.xplorer.cookfood.Object.Product;
import com.xplorer.cookfood.Object.RecipeData;
import com.xplorer.cookfood.Object.UserInfo;
import com.xplorer.cookfood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raghavendra on 14-03-2015.
 */

public class CookFoodApp extends Application {

    private static CookFoodApp instance;
    public static ProgressDialog pd;

    public static HashMap<String, String> ActionDescription = new HashMap();

    public static HashMap<String, Integer> ActionImg = new HashMap();

    public static HashMap<String, String> Actionverb = new HashMap();

    public static HashMap<String, Boolean> ActionTemp = new HashMap();

    public static HashMap<String, Boolean> ActionStopCriteria = new HashMap();

    public static final String[] Actions = {
            "Add",
            "Bake",
            "Blend",
            "Boil",
            "Bread",
            "Broil",
            "Chop",
            "Cut",
            "Dip",
            "Drain",
            "Fry",
            "Grate",
            "Grill",
            "Layer",
            "Level",
            "Melt",
            "Pinch",
            "Pour",
            "Scramble",
            "Serve",
            "Simmer",
            "Slice",
            "Soak",
            "Spread",
            "Stir",
            "Taste"

    };

    public String[] CategoryFood = {
            "Appetizer",
            "Beverage",
            "Breads & Roll",
            "Brownies, Bars & Candy",
            "Burgers, Brats & Dogs",
            "Cakes & Cheesecake",
            "Chinese",
            "Cookie",
            "Dessert",
            "Dips, Spreads & Sauce",
            "Kids Cuisine",
            "Pasta & Pizza",
            "Pie",
            "Salad",
            "Sandwiches & Wrap",
            "Sides",
            "Snacks",
            "Soups & Stews",
            "South Indian",
            "Vegetables"

    };

    public static final String[] IngredientsTitle = {
            "Custom",
            "Vegetable",
            "Fruit",
            "Dry Fruit",
            "Pulses",
            "Flours/Grains",
            "Dairy Product",
            "Spices/Condiments",
            "Fast Food",

    };

    public static final String[] DrawerItems = {
            "Create Recipe",
            "My Recipe",
            "Saved Recipe"

    };
    public static final int[] DrawerItemsImages = {
            R.drawable.create,
            R.drawable.mine,
            R.drawable.saved
    };

    public String RecipeTag = "Saved Recipes";
    public String ProductTag = "Saved Product";
    public String IngredientTag = "Saved Ingredient";

    public String RecipeTagUnsaved = "Unsaved Recipes";
    public String ProductTagUnsaved = "Unsaved Product";
    public String IngredientTagUnsaved = "Unsaved Ingredient";



    public static HashMap<String, Integer> palletColor = new HashMap();

    public int currentAction = 0;

    public static List<Product> CustomProductList = new ArrayList<Product>();

    public static Product CurrentCustomIngredient;

    public static Product EditProduct;

    public UserInfo user;

    public static String PROXIMITY_ON = "true";


    public RecipeData RecipeObject;

    public CookFoodApp() {
        super();
    }

public static int OrderId = 0;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

        parseInit();
        InitActionDescription();
        InitActionImage();
        InitActionVerb();
        initActionTemp();
        initActionStopCriteria();

    }

    public void InitActionDescription(){
        ActionDescription.put("Add", "To add two ingredients together");
        ActionDescription.put("Bake", "To cook in the oven. The cooking of food slowly with gentle heat, concentrating the flavor.");
        ActionDescription.put("Blend", "To mix or fold two or more ingredients together to obtain equal distribution throughout the mixture.");
        ActionDescription.put("Boil", "To cook food in heated water or other liquid that is bubbling vigorously.");
        ActionDescription.put("Bread", "To coat food with breadcrumbs, biscuit crumbs or cereal crumbs.");
        ActionDescription.put("Broil", "To cook food directly under the heat source.");
        ActionDescription.put("Chop", "To cut into irregular pieces.");
        ActionDescription.put("Cut", "To separate a physical object, into two or more portions, through the application of an acutely directed force.");
        ActionDescription.put("Dip", "To immerse briefly into a liquid so as to wet, coat, or saturate");
        ActionDescription.put("Drain", "To withdraw or draw off (a liquid) gradually");
        ActionDescription.put("Fry", "To cook food in hot cooking oil, usually until a crisp brown crust forms.");
        ActionDescription.put("Grate", "To shred or cut down a food into fine pieces by rubbing it against a rough surface or in a grater.");
        ActionDescription.put("Grill", "To cook over the heat source (traditionally over wood coals) in the open air in a grill.");
        ActionDescription.put("Layer", "To make or form a layer.");
        ActionDescription.put("Level", "To tear down ingredient so as to make flat with the ground.");
        ActionDescription.put("Melt", "To reduce or cause to be reduced from a solid to a liquid state, usually by heating.");
        ActionDescription.put("Pinch", "To cut the top off.");
        ActionDescription.put("Pour", "To cause (a liquid) to flow out from a container.");
        ActionDescription.put("Scramble", "To move hurriedly.");
        ActionDescription.put("Serve", "To give a portion of food or drink.");
        ActionDescription.put("Simmer", "To boil slowly at low temperature.");
        ActionDescription.put("Slice", "To make a clean cut through.");
        ActionDescription.put("Soak", "To allow something to become thoroughly wet by immersing it in liquid.");
        ActionDescription.put("Spread", "To distribute over a surface in a layer.");
        ActionDescription.put("Stir", "To move an implement through.");
        ActionDescription.put("Taste", "To A brief experience of something.");
    }

    public void InitActionImage(){
        ActionImg.put("Add", R.drawable.add);
        ActionImg.put("Bake", R.drawable.bake);
        ActionImg.put("Blend", R.drawable.blend);
        ActionImg.put("Boil", R.drawable.boil);
        ActionImg.put("Bread", R.drawable.bread);
        ActionImg.put("Broil", R.drawable.broil);
        ActionImg.put("Chop", R.drawable.chop);
        ActionImg.put("Cut", R.drawable.cut);
        ActionImg.put("Dip", R.drawable.dip);
        ActionImg.put("Drain", R.drawable.drain);
        ActionImg.put("Fry", R.drawable.fry);
        ActionImg.put("Grate", R.drawable.grate);
        ActionImg.put("Grill", R.drawable.grill);
        ActionImg.put("Layer", R.drawable.layer);
        ActionImg.put("Level", R.drawable.level);
        ActionImg.put("Melt", R.drawable.melt);
        ActionImg.put("Pinch", R.drawable.pinch);
        ActionImg.put("Pour", R.drawable.pour);
        ActionImg.put("Scramble", R.drawable.scramble);
        ActionImg.put("Serve", R.drawable.serve);
        ActionImg.put("Simmer", R.drawable.simmer);
        ActionImg.put("Slice", R.drawable.slice);
        ActionImg.put("Soak", R.drawable.soak);
        ActionImg.put("Spread", R.drawable.spread);
        ActionImg.put("Stir", R.drawable.stir);
        ActionImg.put("Taste", R.drawable.taste);
    }

    public void InitActionVerb(){
        Actionverb.put("Add", "mixture");
        Actionverb.put("Bake", "baked product");
        Actionverb.put("Blend", "blended mixture");
        Actionverb.put("Boil", "boiled product");
        Actionverb.put("Bread", "breaded mixture");
        Actionverb.put("Broil", "broiled product");
        Actionverb.put("Chop", "chopped product");
        Actionverb.put("Cut", "cut product");
        Actionverb.put("Dip", "dipped mixture");
        Actionverb.put("Drain", "drained mixture");
        Actionverb.put("Fry", "fried product");
        Actionverb.put("Grate", "grated product");
        Actionverb.put("Grill", "grilled product");
        Actionverb.put("Layer", "layered product");
        Actionverb.put("Level", "leveled product");
        Actionverb.put("Melt", "melted mixture");
        Actionverb.put("Pinch", "pinched product");
        Actionverb.put("Pour", "mixture");
        Actionverb.put("Scramble", "scrambled mixture");
        Actionverb.put("Serve", "served product");
        Actionverb.put("Simmer", "simmered product");
        Actionverb.put("Slice", "sliced product");
        Actionverb.put("Soak", "soaked product");
        Actionverb.put("Spread", "mixture");
        Actionverb.put("Stir", "stirred mixture");
        Actionverb.put("Taste", "tasted product");
    }

    public void initActionTemp(){
        ActionTemp.put("Add", false);
        ActionTemp.put("Bake", true);
        ActionTemp.put("Blend", false);
        ActionTemp.put("Boil", true);
        ActionTemp.put("Bread", false);
        ActionTemp.put("Broil", true);
        ActionTemp.put("Chop", false);
        ActionTemp.put("Cut", false);
        ActionTemp.put("Dip", false);
        ActionTemp.put("Drain", false);
        ActionTemp.put("Fry", true);
        ActionTemp.put("Grate", false);
        ActionTemp.put("Grill", true);
        ActionTemp.put("Layer", false);
        ActionTemp.put("Level", false);
        ActionTemp.put("Melt", true);
        ActionTemp.put("Pinch", false);
        ActionTemp.put("Pour", false);
        ActionTemp.put("Scramble", false);
        ActionTemp.put("Serve", false);
        ActionTemp.put("Simmer", true);
        ActionTemp.put("Slice", false);
        ActionTemp.put("Soak", false);
        ActionTemp.put("Spread", false);
        ActionTemp.put("Stir", false);
        ActionTemp.put("Taste", false);
    }

    public void initActionStopCriteria(){
        ActionStopCriteria.put("Add", false);
        ActionStopCriteria.put("Bake", true);
        ActionStopCriteria.put("Blend", false);
        ActionStopCriteria.put("Boil", true);
        ActionStopCriteria.put("Bread", false);
        ActionStopCriteria.put("Broil", true);
        ActionStopCriteria.put("Chop", false);
        ActionStopCriteria.put("Cut", false);
        ActionStopCriteria.put("Dip", false);
        ActionStopCriteria.put("Drain", false);
        ActionStopCriteria.put("Fry", true);
        ActionStopCriteria.put("Grate", false);
        ActionStopCriteria.put("Grill", true);
        ActionStopCriteria.put("Layer", false);
        ActionStopCriteria.put("Level", false);
        ActionStopCriteria.put("Melt", true);
        ActionStopCriteria.put("Pinch", false);
        ActionStopCriteria.put("Pour", false);
        ActionStopCriteria.put("Scramble", false);
        ActionStopCriteria.put("Serve", false);
        ActionStopCriteria.put("Simmer", true);
        ActionStopCriteria.put("Slice", false);
        ActionStopCriteria.put("Soak", true);
        ActionStopCriteria.put("Spread", false);
        ActionStopCriteria.put("Stir", false);
        ActionStopCriteria.put("Taste", false);
    }

    public String UpperCaseEachWord(String source){
        StringBuffer res = new StringBuffer();

        String[] strArr = source.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }

        return res.toString().trim();
    }
    public String UpperCase(String source){
        String upperString="";
        if(!source.equalsIgnoreCase("")) {
            upperString = source.substring(0, 1).toUpperCase() + source.substring(1);
            upperString = upperString.replace("\n", " ");
        }

        return upperString;
    }

/*

ActionDescription.put("Add", "");
        ActionDescription.put("Bake", "");
        ActionDescription.put("Blend", "");
        ActionDescription.put("Boil", "");
        ActionDescription.put("Bread", "");
        ActionDescription.put("Broil", "");
        ActionDescription.put("Chop", "");
        ActionDescription.put("Cut", "");
        ActionDescription.put("Dip", "");
        ActionDescription.put("Fry", "");
        ActionDescription.put("Grate", "");
        ActionDescription.put("Grill", "");
        ActionDescription.put("Layer", "");
        ActionDescription.put("Level", "");
        ActionDescription.put("Melt", "");
        ActionDescription.put("Pinch", "");
        ActionDescription.put("Pour", "");
        ActionDescription.put("Scramble", "");
        ActionDescription.put("Serve", "");
        ActionDescription.put("Simmer", "");
        ActionDescription.put("Slice", "");
        Actionverb.put("Soak", "");
        ActionDescription.put("Spread", "");
        ActionDescription.put("Stir", "");
        ActionDescription.put("Taste", "");

*/

    public int getDarkColor(String myColorString){
        int color = (int)Long.parseLong(myColorString, 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;

        r = (int) (r * 0.25);
        g = (int) (g * 0.25);
        b = (int) (b * 0.25);

        return getIntFromColor(r, g, b);

    }

    public int getDarkColor(int color){
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;

        r = (int) (r * 0.25);
        g = (int) (g * 0.5);
        b = (int) (b * 0.75);

        return getIntFromColor(r, g, b);

    }
    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static CookFoodApp getInstance() {
        if (instance == null) {
            instance = new CookFoodApp();
            return instance;

        } else {
            return instance;
        }
    }

    public static void setSPString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSPString(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getInstance());
        return sharedPreferences.getString(key, "");
    }

    public void RunPreLoader(Context context) {
        pd = new ProgressDialog(context);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.show();


    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public void parseInit() {
        ParseObject.registerSubclass(Ingredients.class);
        ParseObject.registerSubclass(Product.class);
        ParseUser.registerSubclass(UserInfo.class);
        ParseUser.registerSubclass(RecipeData.class);


        ParseCrashReporting.enable(this);   // Initialize Crash Reporting.
        Parse.enableLocalDatastore(this);   // Enable Local Datastore.
        Parse.initialize(this, "mUThvDq3ALAPebSQPkp0tHg1plp3euCVkPL3g17E", "Zi3FSnSrAjQJhNmMUkiy1AspYGnOzdllNiLk4Xzm");

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
