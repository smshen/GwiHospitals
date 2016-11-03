package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;
import java.util.List;

public class RecipeList implements Serializable {
    private RecipeInfo RecipeInfo;
    private List<RecipeDetail> RecipeDetailList;

    public RecipeInfo getRecipeInfo() {
        return RecipeInfo;
    }

    public List<RecipeDetail> getRecipeDetailList() {
        return RecipeDetailList;
    }

    public void setRecipeDetailList(List<RecipeDetail> recipeDetailList) {
        RecipeDetailList = recipeDetailList;
    }

    public void setRecipeInfo(com.gwi.selfplatform.module.net.response.RecipeInfo recipeInfo) {
        RecipeInfo = recipeInfo;
    }
}
