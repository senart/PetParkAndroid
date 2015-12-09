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
    private String Name;
    private Integer Age;
    private Double Weight;
    private String ProfilePic;

    public Pet(Integer PetID, String Species, String Breed, String Name, Integer Age, Double Weight, String ProfilePic){
        this.PetID = PetID;
        this.Species = Species;
        this.Breed = Breed;
        this.Name = Name;
        this.Age = Age;
        this.Weight = Weight;
        this.ProfilePic = ProfilePic;
    }

    public Pet(Parcel source) {
        PetID = source.readInt();
        Species = source.readString();
        Breed = source.readString();
        Name = source.readString();
        Age = source.readInt();
        Weight = source.readDouble();
        ProfilePic = source.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        applyDefaultValues();

        dest.writeInt(PetID);
        dest.writeString(Name);
        dest.writeString(Species);
        dest.writeString(Breed);
        dest.writeInt(Age);
        dest.writeDouble(Weight);
        dest.writeString(ProfilePic);
    }

    private void applyDefaultValues() {
        if (PetID == null) PetID = -1;
        if (Name == null) Name = "";
        if (Species == null) Species = "";
        if (Breed == null) Breed = "";
        if (Age == null) Age = -1;
        if (Weight == null) Weight = -1.0;
        if (ProfilePic == null) ProfilePic = "";
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
