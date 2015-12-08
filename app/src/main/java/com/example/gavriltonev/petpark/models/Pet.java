package com.example.gavriltonev.petpark.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gavriltonev on 12/7/15.
 */
public class Pet {
    private Integer PetID;
    private String Species;
    private String Breed;
    private String Name;
    private Integer Age;
    private Double Weight;

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
}
