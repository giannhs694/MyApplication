package com.mydomaingiannis.dev.myapplication;

/**
 * Created by Iannos on 16/11/2017.
 */

public class Recipe {
    private String title;
    private String recipeUrl;
    private String imgUrl;

    public Recipe(String title,String recipeUrl){
        this.title = title;
        this.recipeUrl = recipeUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getRecipeUrl(){
        return recipeUrl;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
