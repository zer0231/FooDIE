package com.zero.foodie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    FloatingActionButton done;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }


        supportMapFragment.getMapAsync(this);
        done = findViewById(R.id.doneButton);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isComplete()) {
                        Location location = task.getResult();
                        if (location != null) {
                            String longitude = String.valueOf(location.getLongitude());
                            String latitude = String.valueOf(location.getLatitude());

                            SharedPreferences sharedPreferences = getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Longitude", longitude);
                            editor.putString("Latitude", latitude);
                            editor.apply();
                        } else {
                            Toast.makeText(MapActivity.this, "here of course", Toast.LENGTH_SHORT).show();
                            LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();
                                    if (location1 != null) {
                                        String longitude = String.valueOf(location1.getLongitude());
                                        String latitude = String.valueOf(location1.getLatitude());

                                        SharedPreferences sharedPreferences = getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("Longitude", longitude);
                                        editor.putString("Latitude", latitude);
                                        editor.apply();
                                    } else
                                        Toast.makeText(MapActivity.this, "Still fucking null", Toast.LENGTH_SHORT).show();
                                }
                            };
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please provide location service to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        double lan, lon;
        SharedPreferences sharedPreferences = getSharedPreferences("UserLocation", getApplicationContext().MODE_PRIVATE);
        lan = Double.parseDouble(sharedPreferences.getString("Latitude", "0"));
        lon = Double.parseDouble(sharedPreferences.getString("Longitude", "0"));
        if (lan + lon != 0) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lan, lon), 14.0f));
            MarkerOptions temp = new MarkerOptions();
            temp.position(new LatLng(lan, lon));
            gMap.addMarker(temp);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}
