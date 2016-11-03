package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class RecipeInfo implements Serializable {
    private String RecipeType;
    private String RecipeID;
    private String RecipeTime;
    private String DeptName;
    private String DoctName;
    private String TotalFee;
    private String PayFlag;
    private String Diagnosis;
    private String Note;

    private String RecipeName;

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getRecipeType() {
        return RecipeType;
    }

    public void setRecipeType(String recipeType) {
        RecipeType = recipeType;
    }

    public String getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(String recipeID) {
        RecipeID = recipeID;
    }

    public String getRecipeTime() {
        return RecipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        RecipeTime = recipeTime;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDoctName() {
        return DoctName;
    }

    public void setDoctName(String doctName) {
        DoctName = doctName;
    }

    public String getTotalFee() {
        return TotalFee;
    }

    public void setTotalFee(String totalFee) {
        TotalFee = totalFee;
    }

    public String getPayFlag() {
        return PayFlag;
    }

    public void setPayFlag(String payFlag) {
        PayFlag = payFlag;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

}
