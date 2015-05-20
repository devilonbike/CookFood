package com.xplorer.cookfood.Object;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 14-03-2015.
 */
@ParseClassName("Ingredients")
public class Ingredients extends ParseObject {
    public Ingredients() {
    }

    public String getName() {
        return getString("Name");
    }

    public void setName(String Name) {
        put("Name", Name);
    }

    public String getNameHindiLang() {
        return getString("NameHindiLang");
    }

    public void setNameHindiLang(String NameHindiLang) {
        put("NameHindiLang", NameHindiLang);
    }

    public String getNameHindi() {
        return getString("NameHindi");
    }

    public void setNameHindi(String NameHindi) {
        put("NameHindi", NameHindi);
    }


    public ParseFile getImageFile() {
        return getParseFile("Image");
    }

    public void setImageFile(ParseFile Image) {
        put("Image", Image);
    }

    public String getCategory() {
        return getString("Category");
    }

    public void setCategory(String Category) {
        put("Category", Category);
    }

    public String getMinerals() {
        return getString("Minerals");
    }

    public void setMinerals(String Minerals) {
        put("Minerals", Minerals);
    }

    public String getVitamins() {
        return getString("Vitamins");
    }

    public void setVitamins(String Vitamins) {
        put("Vitamins", Vitamins);
    }


}
