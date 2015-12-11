package com.example.gavriltonev.petpark.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.adapters.PetsDetailGridAdapter;
import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.PetImage;
import com.example.gavriltonev.petpark.models.services.PetApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class PetDetailActivity extends AppCompatActivity {
    public static String EXTRA_PET = "extra_pet";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private GridView mGridView;
    private PetsDetailGridAdapter petsDetailGridAdapter;
    private Pet pet;
    private ImageView profileImageView;
    private Boolean foreignUser;

    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void attatchProfileImage(PetImage petImage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // TODO: Add pagination?
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<ResponseBody> call = petApiInterface.attatchProfileImage(
                "Bearer " + Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR),
                pet.getPetID(),
                petImage
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.raw().code() == 200) refreshProfileImageView();
                else Toast.makeText(getApplicationContext(), "Failed to set profile picture.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(Bitmap bitmapImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
        final byte[] bitmapData = stream.toByteArray();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bitmapData);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // TODO: Add pagination?
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<ResponseBody> call = petApiInterface.uploadImage(
                "Bearer " + Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR),
                pet.getPetID(),
                requestBody
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.raw().code() == 200) getPetImages();
                else Toast.makeText(getApplicationContext(), "Failed to upload.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPetImages() {
        petsDetailGridAdapter.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Gets all pets from the database
        // TODO: Add pagination?
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<List<PetImage>> call = petApiInterface.getPetImages(
                "Bearer " + Preferences.getInstance(this).getString(Preferences.Key.TOKEN_STR),
                pet.getPetID()
        );
        call.enqueue(new Callback<List<PetImage>>() {
            @Override
            public void onResponse(Response<List<PetImage>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), "Failure on request", Toast.LENGTH_SHORT).show();
                } else {
                    for (PetImage image : response.body()) {
                        petsDetailGridAdapter.add(image);
                    }

                    petsDetailGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (foreignUser) return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_pet_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.petDetailCamera) {
            startCamera();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImage(imageBitmap);
        }
    }

    private void refreshProfileImageView() {
        if (pet.getProfilePic().isEmpty() || pet.getProfilePic() == null ) {
            profileImageView.setImageResource(R.drawable.ic_menu_camera);
            profileImageView.setColorFilter(R.color.colorPrimary);
            if (foreignUser) return;
            profileImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startCamera();
                }
            });
        } else {
            profileImageView.setColorFilter(null);
            Picasso.with(this).load(pet.getProfilePic()).into(profileImageView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        profileImageView = (ImageView) findViewById(R.id.petDetailImage);
        TextView name = (TextView) findViewById(R.id.petDetailName);
        TextView species = (TextView) findViewById(R.id.petDetailSpecies);
        TextView breed = (TextView) findViewById(R.id.petDetailBreed);
        TextView age = (TextView) findViewById(R.id.petDetailAge);
        TextView gender = (TextView) findViewById(R.id.petDetailGender);
        TextView weight = (TextView) findViewById(R.id.petDetailWeight);

        mGridView = (GridView) findViewById(R.id.petDetailGrid);
        petsDetailGridAdapter = new PetsDetailGridAdapter(this,0);
        mGridView.setAdapter(petsDetailGridAdapter);

        pet = getIntent().getExtras().getParcelable(EXTRA_PET);
        foreignUser = getIntent().getExtras().getBoolean("foreignUser", true);

        getPetImages();
        refreshProfileImageView();

        name.setText(pet.getName());
        age.setText(pet.getAge() + " years old");
        gender.setText(pet.getGender());
        weight.setText(String.valueOf(pet.getWeight()) + "kg");
        species.setText(pet.getSpecies());
        breed.setText(pet.getBreed());

        if (!foreignUser){
            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    new AlertDialog.Builder(PetDetailActivity.this)
                            .setTitle("Profile Picture")
                            .setMessage("Set this as profile picture?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    attatchProfileImage(petsDetailGridAdapter.getItem(pos));
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    return false;
                }
            });
        }
    }
}
