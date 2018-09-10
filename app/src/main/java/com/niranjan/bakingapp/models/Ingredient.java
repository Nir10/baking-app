package com.niranjan.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    public int mQuantity;
    public String mMeasure;
    public String mIngredient;

    public Ingredient(int quantity,String measure,String ingredient ){
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }


    public Ingredient(Parcel in){
        this.mQuantity = in.readInt();
        this.mMeasure = in.readString();
        this.mIngredient = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mQuantity);
        dest.writeString(this.mMeasure);
        dest.writeString(this.mIngredient);
    }


    @Override
    public String toString() {
        return this.mQuantity+"--"+this.mMeasure+"--"+this.mIngredient;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
