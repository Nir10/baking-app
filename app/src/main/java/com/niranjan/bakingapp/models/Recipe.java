package com.niranjan.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable{

    public int mId;
    public String mName;
    public List<Ingredient> mIngredients;
    public List<Step> mSteps;
    public int mServings;
    public String mImage;


    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings,
                  String image ){
        this.mIngredients = new ArrayList<Ingredient>();
        this.mSteps = new ArrayList<Step>();
        this.mId = id;
        this.mName = name;
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mServings = servings;
        this.mImage = image;
    }

    public Recipe(Parcel in){
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mIngredients = new ArrayList<Ingredient>();
        in.readList(this.mIngredients,Ingredient.class.getClassLoader());
        this.mSteps = new ArrayList<Step>();
        in.readList(this.mSteps,Step.class.getClassLoader());
        this.mServings = in.readInt();
        this.mImage = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeList(this.mIngredients);
        dest.writeList(this.mSteps);
        dest.writeInt(this.mServings);
        dest.writeString(this.mImage);

    }

    @Override
    public String toString() {
        return this.mId+"--"+this.mName+"--"+this.mIngredients+"--"+this.mSteps+"--"
                +this.mServings+"--"+this.mImage;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
