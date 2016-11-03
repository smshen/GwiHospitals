package com.gwi.selfplatform.module.net.response;

import java.util.List;

public class G1611 {
    private String TotalFee;
    private List<RecipeList> RecipeList;

    public String getTotalFee() {
        return TotalFee;
    }

    public void setTotalFee(String totalFee) {
        TotalFee = totalFee;
    }

    public List<RecipeList> getRecipeList() {
        return RecipeList;
    }

    public void setRecipeList(List<RecipeList> recipeList) {
        RecipeList = recipeList;
    }
}
