package com.example.ridowanahmed.childlocator.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.ridowanahmed.childlocator.GPS_Service;
import com.example.ridowanahmed.childlocator.Model.ChildInformation;
import com.example.ridowanahmed.childlocator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ridowan Ahmed on 0016, August, 16, 2017.
 */

public class ParentMap extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    SupportMapFragment mMapFrag;
    Marker mMarker;
    DatabaseReference childData;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_map);

        getSupportActionBar().setTitle("Child Location Activity");
        mMapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFrag.getMapAsync(this);

        mSharedPreferences = ParentMap.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        final String phoneNumber = mSharedPreferences.getString(getString(R.string.PARENT_GIVE_NUMBER), "");
        childData = FirebaseDatabase.getInstance().getReference(phoneNumber);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        childData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if(mMarker !=  null){
                        mMarker.remove();
                    }
                    ChildInformation mChildInformation = dataSnapshot.getValue(ChildInformation.class);
                    LatLng latLng = new LatLng(mChildInformation.getLatitude(), mChildInformation.getLongitude());

                    Log.e("Latitude " + mChildInformation.getLatitude() , "Longitude " + mChildInformation.getLongitude());

                    String title = mChildInformation.getChildName();
                    MarkerOptions locationMarker = new MarkerOptions().position(latLng).title(title);
                    locationMarker.snippet(calculateTime(mChildInformation.getTime()));
                    mMarker = mMap.addMarker(locationMarker);
                    mMarker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

                    Toast toast = Toast.makeText(getApplicationContext(), "Locating " + title, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 30);
                    toast.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Can't find your children. Try again later", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private String calculateTime(long timeInMillis){
        int hours = (int) ((timeInMillis / (1000 * 60 * 60)) % 60);
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int seconds = (int) ((timeInMillis / 1000) % 60);

        String currentTime;
        if(hours >= 12) {
            hours -= 12;
            currentTime = hours+":"+minutes+":"+seconds + " PM";
        } else {
            currentTime = hours+":"+minutes+":"+seconds + " AM";
        }
        return currentTime;
    }
}
