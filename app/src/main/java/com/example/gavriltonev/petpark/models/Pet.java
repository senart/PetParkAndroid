package com.example.gavriltonev.petpark.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gavriltonev on 12/7/15.
 */
public class Pet implements Parcelable{
    private Integer PetID;
    private String Species;
    private String Breed;
    private String Gender;
    private String Name;
    private Integer Age;
    private Double Weight;
    private String ProfilePic;
    private Double Latitude;
    private Double Longitude;

    public Pet(String Species, String Breed, String Gender, String Name, Integer Age, Double Weight, Double Latitude, Double Longitude) {
        this.Species = Species;
        this.Breed = Breed;
        this.Gender = Gender;
        this.Name = Name;
        this.Age = Age;
        this.Weight = Weight;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public Pet(Parcel source) {
        PetID = source.readInt();
        Species = source.readString();
        Breed = source.readString();
        Gender = source.readString();
        Name = source.readString();
        Age = source.readInt();
        Weight = source.readDouble();
        ProfilePic = source.readString();
        Latitude = source.readDouble();
        Longitude = source.readDouble();
    }

    public int getPetID() {
        return PetID;
    }

    public void setPetID(int petID) {
        PetID = petID;
    }

    public String getSpecies() {
        return Species;
    }

    public void setSpecies(String species) {
        Species = species;
    }

    public String getBreed() {
        return Breed;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefaultValues();

        dest.writeInt(PetID);
        dest.writeString(Species);
        dest.writeString(Breed);
        dest.writeString(Gender);
        dest.writeString(Name);
        dest.writeInt(Age);
        dest.writeDouble(Weight);
        dest.writeString(ProfilePic);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
    }

    private void applyDefaultValues() {
        if (PetID == null) PetID = -1;
        if (Species == null) Species = "";
        if (Breed == null) Breed = "";
        if (Gender == null) Gender = "";
        if (Name == null) Name = "";
        if (Age == null) Age = -1;
        if (Weight == null) Weight = -1.0;
        if (ProfilePic == null) ProfilePic = "";
        if (Latitude == null) Latitude = 0.0;
        if (Longitude == null) Longitude = 0.0;
    }

    public static Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel source) {
            return new Pet(source);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };


}
