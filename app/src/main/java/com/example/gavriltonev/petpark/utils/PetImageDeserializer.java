package com.example.gavriltonev.petpark.utils;

import com.example.gavriltonev.petpark.models.PetImage;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by gavriltonev on 12/9/15.
 */
public class PetImageDeserializer implements JsonDeserializer<PetImage>
{
    @Override
    public PetImage deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        JsonElement petImage = je.getAsJsonObject().get("ProfileImage");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(petImage, PetImage.class);

    }
}
