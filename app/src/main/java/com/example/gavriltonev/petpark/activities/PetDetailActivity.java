package com.example.gavriltonev.petpark.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Pet;
import com.squareup.picasso.Picasso;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class PetDetailActivity extends AppCompatActivity {

    public static String EXTRA_PET = "extra_pet";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pet_detail);

        Pet pet = getIntent().getExtras().getParcelable(EXTRA_PET);

        TextView species = (TextView) findViewById(R.id.petDetailSpecies);
        TextView breed = (TextView) findViewById(R.id.petDetailBreed);
        ImageView image = (ImageView) findViewById(R.id.petDetailImage);

        species.setText(pet.getSpecies());
        breed.setText(pet.getBreed());
        // TODO: add this instead Picasso.with(this).load(pet.getImage()).into(image);
        Picasso.with(this).load("http://www.cats.org.uk/uploads/branches/211/5507692-cat-m.jpg").into(image);
    }
}
