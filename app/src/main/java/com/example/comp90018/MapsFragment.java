package com.example.comp90018;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp90018.dataBean.User;
import com.example.comp90018.dataBean.UserPosition;
import com.example.comp90018.utils.DataManager;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private static LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseReference mDatabaseRef;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(final GoogleMap googleMap) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(DataManager.getDataManager(getActivity()).getUser().getID()).child("position");
            //get user's location from firebase when the map is ready
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> tmp = new ArrayList<String>();
                    //store lat and lng in arraylist and use element in arraylist for creat LatLng object
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        tmp.add((String)postSnapShot.getValue());
                    }
                    if(tmp.size() == 2){
                        LatLng userPos = new LatLng(Double.valueOf(tmp.get(0)),Double.valueOf(tmp.get(1)));
                        //store created Marker in datamanager for removing them later
                        //if not remove previous marker of the user, there will be multiple markers when user change location
                        if(DataManager.getDataManager(getActivity()).getUserMarker() != null) {
                            DataManager.getDataManager(getActivity()).getUserMarker().remove();
                        }
                        DataManager.getDataManager(getActivity()).setMarker(googleMap.addMarker(new MarkerOptions().position(userPos).title("Your location")));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //checking for the permission
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //when user change location, update location to database
                saveUserLocation(location);
            }

            @Override
            public void onStatusChanged(String s,int i ,Bundle bundle){
            }
        };
        //asking for permission, if granted, register LocationManager for monitoring location
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(locationManager==null) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            }
        }
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    //check result for permission request, if success, register LocationManager as well
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            }
        }
    }

    private void saveUserLocation(Location location){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        UserPosition userPos = new UserPosition(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
        mDatabaseRef.child("users").child(DataManager.getDataManager(getActivity()).getUser().getID())
                .child("position").setValue(userPos);
    }
}