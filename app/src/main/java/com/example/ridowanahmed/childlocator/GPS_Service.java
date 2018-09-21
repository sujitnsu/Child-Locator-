package com.example.ridowanahmed.childlocator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ridowanahmed.childlocator.Model.ChildInformation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ridowan Ahmed on 0015, August, 15, 2017.
 */

public class GPS_Service extends Service {

    private LocationListener locationListener;
    private LocationManager locationManager;
    SharedPreferences mSharedPreferences;
    DatabaseReference databaseReference;
    private String childName, phoneNumber;

    @Override
    public void onCreate() {
        mSharedPreferences = GPS_Service.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        childName = mSharedPreferences.getString(getString(R.string.CHILD_NAME), "");
        phoneNumber = mSharedPreferences.getString(getString(R.string.CHILD_GIVE_NUMBER), "");
//        childName = "Ridowan Ahmed";
//        phoneNumber = "01820213153";
        databaseReference = FirebaseDatabase.getInstance().getReference(phoneNumber).child(childName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                saveData(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        return START_STICKY;
    }

    private void saveData(Location location) {
        if(databaseReference == null) {
            return;
        }
        ChildInformation childInformation = new ChildInformation(childName, location.getLatitude(), location.getLongitude(), location.getTime());
//                databaseReference.setValue(childInformation);
        databaseReference.setValue(childInformation, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("GPS", "Data could not be saved " + databaseError.getMessage());
                } else {
                    Log.e("GPS", "Data saved successfully" + databaseError.getMessage());
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        Toast.makeText(getApplicationContext(), "Location Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}

