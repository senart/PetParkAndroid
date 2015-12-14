package com.example.gavriltonev.petpark.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.activities.PetDetailActivity;
import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.Pin;
import com.example.gavriltonev.petpark.models.services.PetApiInterface;
import com.example.gavriltonev.petpark.models.services.UserApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by gavriltonev on 12/7/15.
 */
public class PetsMapFragment extends SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final float COORDINATE_OFFSET = 0.00002f;
    static final String ALL_SPECIES = "ALL_SPECIES";
    private HashMap<String, String> petLocations = new HashMap<String, String>();

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    public static PetsMapFragment getInstance() {
        PetsMapFragment fragment = new PetsMapFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Integer petID = Integer.parseInt(marker.getSnippet());
                if (petID != null) {
                    goToPetDetails(petID);
                }
            }
        });

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setIcon(R.drawable.ic_menu_gallery);
        builderSingle.setTitle("Select A Species you're interested in: ");

        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.species_array, android.R.layout.simple_list_item_1
        );

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setPositiveButton("List All Pets!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPetsBySpecies(ALL_SPECIES);
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String species = arrayAdapter.getItem(which).toString();
                        getPetsBySpecies(species);
                    }
                });
        builderSingle.show();
    }

    private void goToPetDetails(Integer petID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Gets all pets from the database
        // TODO: Add pagination?
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<Pet> call = petApiInterface.getPet(
                "Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR),
                petID
        );
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Response<Pet> response, Retrofit retrofit) {
                if (response.body() == null)
                    Toast.makeText(getActivity(), "Error fetching pets for species.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                    intent.putExtra(PetDetailActivity.EXTRA_PET, response.body());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPetsBySpecies(String species) {
        if (species == ALL_SPECIES){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.petparkAPI))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Gets all pets from the database
            // TODO: Add pagination?
            PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
            Call<List<Pet>> call = petApiInterface.getAllPets(
                    "Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR)
            );
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Response<List<Pet>> response, Retrofit retrofit) {
                    if (response.body() == null)
                        Toast.makeText(getActivity(), "Error fetching pets for species.", Toast.LENGTH_SHORT).show();
                    else updateMapWithPets(response.body());
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.petparkAPI))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Gets all pets from the database
            // TODO: Add pagination?
            PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
            Call<List<Pet>> call = petApiInterface.getAllPetsBySpecies(
                    "Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR),
                    species
            );
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Response<List<Pet>> response, Retrofit retrofit) {
                    if (response.body() == null)
                        Toast.makeText(getActivity(), "Error fetching pets for species.", Toast.LENGTH_SHORT).show();
                    else updateMapWithPets(response.body());
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateMapWithPets(List<Pet> pets) {
        petLocations.clear();
        getMap().clear();
        if (pets.isEmpty()) return;

        for (final Pet pet : pets) {
            if (pet.getLatitude() == null || pet.getLongitude() == null) continue;
            else if (pet.getLatitude() != 0 && pet.getLongitude() != 0) {

                Double[] pos = coordinateForMarker(pet.getLatitude(), pet.getLongitude(), pets.size());
                final LatLng adjustedLocation = new LatLng(pet.getLatitude(), pet.getLongitude());

                if (pet.getProfilePic() != null) {
                    Picasso.with(getActivity()).load(pet.getProfilePic()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                            MarkerOptions options = new MarkerOptions().position(adjustedLocation);
                            options.snippet(pet.getPetID() + "");
                            options.title(pet.getName());
                            options.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                            getMap().addMarker(options);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            MarkerOptions options = new MarkerOptions().position(adjustedLocation);
                            options.snippet(pet.getPetID() + "");
                            options.title(pet.getName());
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            getMap().addMarker(options);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                } else {
                    final MarkerOptions options = new MarkerOptions().position(adjustedLocation);
                    options.snippet(pet.getPetID() + "");
                    options.title(pet.getName());
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    getMap().addMarker(options);
                }
            }
        }
    }

    private Double[] coordinateForMarker(double latitude, double longitude, int max) {

        Double[] location = new Double[2];

        for (int i = 0; i <= max; i++) {

            if (petLocations.containsKey((latitude + i
                    * COORDINATE_OFFSET)
                    + "," + (longitude + i * COORDINATE_OFFSET))) {

                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                if (i == 0)
                    continue;

                if (petLocations.containsKey((latitude - i
                        * COORDINATE_OFFSET)
                        + "," + (longitude - i * COORDINATE_OFFSET))) {

                    continue;

                } else {
                    location[0] = latitude - (i * COORDINATE_OFFSET);
                    location[1] = longitude - (i * COORDINATE_OFFSET);
                    break;
                }

            } else {
                location[0] = latitude + (i * COORDINATE_OFFSET);
                location[1] = longitude + (i * COORDINATE_OFFSET);
                break;
            }
        }

        return location;
    }

    private void updateUserLocation() {
        if (mLastLocation == null) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // TODO: Add pagination?
        UserApiInterface userApiInterface = retrofit.create(UserApiInterface.class);
        Call<ResponseBody> call = userApiInterface.updateLocation(
                "Bearer " + Preferences.getInstance(getActivity()).getString(Preferences.Key.TOKEN_STR),
                new Pin("user", mLastLocation.getLatitude(), mLastLocation.getLongitude())
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.raw().code() == 200) ;// Do nothing !
                else Toast.makeText(getActivity(), "Failed to update location.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void createLocationRequest() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        // The location Changed!
//                    }
//                });
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            CameraPosition position = CameraPosition.builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .zoom(16f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();
            getMap().animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
            getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
            getMap().setTrafficEnabled(false);

            updateUserLocation();

            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            options.title("You are here");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            getMap().addMarker(options);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
