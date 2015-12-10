package com.example.gavriltonev.petpark.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Breed;
import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.services.BreedApiInterface;
import com.example.gavriltonev.petpark.models.services.PetApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddPetActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private EditText _nameText;
    private EditText _ageText;
    private EditText _weightText;
    private Button _addButton;

    private Spinner _speciesSpinner;
    private Spinner _breedSpinner;

    private ArrayAdapter<String> _breedAdapter;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == _speciesSpinner) {
            Toast.makeText(this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            getBreedsForSpecies(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addPet() {
        if (!validate()) return;

        _addButton.setEnabled(false);

        String name = _nameText.getText().toString();
        Integer age = Integer.parseInt(_ageText.getText().toString());
        Double weight = Double.parseDouble(_weightText.getText().toString());
        String species = _speciesSpinner.getSelectedItem().toString();
        String breed = _breedSpinner.getSelectedItem().toString();

        final Pet pet = new Pet(species,breed,name,age,weight);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<ResponseBody> call = petApiInterface.createPet(
                "Bearer " + Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR),
                pet);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.raw().code() == 200) onAddSuccess(pet);
                else onAddFailure("Failed to add pet");
            }

            @Override
            public void onFailure(Throwable t) {
                onAddFailure("Network error, please try again.");
            }
        });
    }

    private void onAddSuccess(Pet pet){
        _addButton.setEnabled(true);

        Toast.makeText(getApplication(), pet.getName() + " joined PetPark!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onAddFailure(String message){
        _addButton.setEnabled(true);

        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String age = _weightText.getText().toString();
        String weight = _weightText.getText().toString();

        if (name.isEmpty()) {
            _nameText.setError("enter a name");
            valid = false;
        } else _nameText.setError(null);

        if (age.isEmpty()) {
            _ageText.setError("enter an age");
            valid = false;
        } else _ageText.setError(null);

        if (weight.isEmpty()) {
            _weightText.setError("enter a weight");
            valid = false;
        } else _weightText.setError(null);

        return valid;
    }

    private void getBreedsForSpecies(String species) {
        _addButton.setEnabled(false);
        _breedAdapter.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BreedApiInterface breedApiInterface = retrofit.create(BreedApiInterface.class);
        Call<List<Breed>> call = breedApiInterface.getBreeds(
                "Bearer " + Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR),
                species
        );
        call.enqueue(new Callback<List<Breed>>() {
            @Override
            public void onResponse(Response<List<Breed>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    Log.e("NULL RESPONSE BODY: ", response.message());
                } else {
                    for (Breed breed : response.body()) {
                        _breedAdapter.add(breed.getBreedID());
                    }

                    _breedAdapter.notifyDataSetChanged();
                    _breedSpinner.setAdapter(_breedAdapter);
                }
                _addButton.setEnabled(true);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplication(), "Failure on request", Toast.LENGTH_SHORT).show();
                _addButton.setEnabled(true);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        _nameText = (EditText) findViewById(R.id.petName_addPet);
        _ageText = (EditText) findViewById(R.id.petAge_addPet);
        _weightText = (EditText) findViewById(R.id.petWeight_addPet);
        _speciesSpinner = (Spinner) findViewById(R.id.petSpecies_addPet);
        _breedSpinner = (Spinner) findViewById(R.id.petBreed_addPet);
        _addButton = (Button) findViewById(R.id.petAdd_addPet);

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPet();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.species_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _speciesSpinner.setAdapter(adapter);
        _speciesSpinner.setOnItemSelectedListener(this);

        _breedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        _breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _breedSpinner.setAdapter(adapter);
        _breedSpinner.setOnItemSelectedListener(this);

        getBreedsForSpecies(_speciesSpinner.getSelectedItem().toString());
    }
}
