package com.xplorer.cookfood.Object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Raghavendra on 22-03-2015.
 */

@ParseClassName("Product")
public class Product extends ParseObject {


    public Product() {
    }

    public String getAction() {
        return getString("Action");
    }

    public void setAction(String Action) {
        put("Action", Action);
    }


    public List<Product> getProductList() {
        return getList("ProductList");
    }

    public void setProductList(List<Product> ProductList) {
        put("ProductList", ProductList);
    }

    public Ingredients getIngredient() {
        return (Ingredients) getParseObject("Ingredient");
    }

    public void setIngredient(Ingredients Ingredient) {
        put("Ingredient", Ingredient);
    }

    public String getTemperature() {
        return getString("Temperature");
    }

    public void setTemperature(String Temperature) {
        put("Temperature", Temperature);
    }

    public String getTime() {
        return getString("Time");
    }

    public void setTime(String Time) {
        put("Time", Time);
    }

    public String getStopCriteria() {
        return getString("StopCriteria");
    }

    public void setStopCriteria(String StopCriteria) {
        put("StopCriteria", StopCriteria);
    }

    public String getStopReason() {
        return getString("StopReason");
    }

    public void setStopReason(String StopReason) {
        put("StopReason", StopReason);
    }



    public String getQuantityType() {
        return getString("QuantityType");
    }

    public void setQuantityType(String QuantityType) {
        put("QuantityType", QuantityType);
    }


    public String getQuantityCount() {
        return getString("QuantityCount");
    }

    public void setQuantityCount(String QuantityCount) {
        put("QuantityCount", QuantityCount);
    }

    public String getRecipeDataId() {
        return getString("RecipeDataId");
    }

    public void setRecipeDataId(String RecipeDataId) {
        put("RecipeDataId", RecipeDataId);
    }

    public int getOrder() {
        return getInt("Order");
    }

    public void setOrder(int Order) {
        put("Order", Order);
    }

}
