package com.example.gavriltonev.petpark.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gavriltonev on 12/9/15.
 */
public class PetImage implements Parcelable {
    private Integer PetImageID;
    private Integer PetID;
    private String ImageURL;

    public PetImage(Integer PetImageID, Integer PetID, String ImageURL){
        this.PetImageID = PetImageID;
        this.PetID = PetID;
        this.ImageURL = ImageURL;
    }

    public PetImage(Parcel source) {
        PetImageID = source.readInt();
        PetID = source.readInt();
        ImageURL = source.readString();
    }

    public Integer getPetImageID() {
        return PetImageID;
    }

    public void setPetImageID(Integer petImageID) {
        PetImageID = petImageID;
    }

    public Integer getPetID() {
        return PetID;
    }

    public void setPetID(Integer petID) {
        PetID = petID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefaultValues();

        dest.writeInt(PetImageID);
        dest.writeInt(PetID);
        dest.writeString(ImageURL);
    }

    private void applyDefaultValues() {
        if (PetImageID == null) PetImageID = -1;
        if (PetID == null) PetID = -1;
        if (ImageURL == null) ImageURL = "";
    }

    public static Creator<PetImage> CREATOR = new Creator<PetImage>() {
        @Override
        public PetImage createFromParcel(Parcel source) {
            return new PetImage(source);
        }

        @Override
        public PetImage[] newArray(int size) {
            return new PetImage[size];
        }
    };
}
