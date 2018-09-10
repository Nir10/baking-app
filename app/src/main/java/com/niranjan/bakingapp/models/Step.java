package com.niranjan.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Niranjan on 15/03/2018.
 */

public class Step implements Parcelable{

    public int mId;
    public String mShortDescription;
    public String mDescription;
    public String mVideoUrl;
    public String mThumbnailUrl;


    public Step(int id,String shortDescription,String description,String videoUrl,String thumbnailUrl){

        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;

    }

    public Step(Parcel in){

        this.mId = in.readInt();
        this.mShortDescription = in.readString();
        this.mDescription = in.readString();
        this.mVideoUrl = in.readString();
        this.mThumbnailUrl = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.mId);
        dest.writeString(this.mShortDescription);
        dest.writeString(this.mDescription);
        dest.writeString(this.mVideoUrl);
        dest.writeString(this.mThumbnailUrl);

    }

    @Override
    public String toString() {
        return this.mId+"--"+this.mShortDescription+"--"+this.mDescription+"--"+this.mVideoUrl+"--"
                +this.mThumbnailUrl;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
