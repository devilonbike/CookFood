package com.xplorer.cookfood.Object;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 27-03-2015.
 */
@ParseClassName("RecipeData")
public class RecipeData extends ParseObject {

    public RecipeData() {
    }

    public String getOwner() {
        return getString("Owner");
    }

    public void setOwner(String Owner) {
        put("Owner", Owner);
    }

    public String getTitle() {
        return getString("Title");
    }

    public void setTitle(String Title) {
        put("Title", Title);
    }

    public String getDescription() {
        return getString("Description");
    }

    public void setDescription(String Description) {
        put("Description", Description);
    }

    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    public void setImageFile(ParseFile image) {
        put("image", image);
    }

    public int getViews() {
        return getInt("Views");
    }

    public void setViews(int Views) {
        put("Views", Views);
    }

    public String getCategory() {
        return getString("Category");
    }

    public void setCategory(String Category) {
        put("Category", Category);
    }

    public Boolean getPublished() {
        return getBoolean("Published");
    }

    public void setPublished(Boolean Published) {
        put("Published", Published);
    }



}
