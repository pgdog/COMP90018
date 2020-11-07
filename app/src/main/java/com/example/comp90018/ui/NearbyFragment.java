package com.example.comp90018.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comp90018.R;
import com.example.comp90018.utils.DataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NearbyFragment extends Fragment {

    private View view;
    private TextView weather;
    private ImageView weatherIcon;
    private DatabaseReference mDatabaseRef;

    public NearbyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nearby, container, false);
        initView();
        return view;
    }

    public void initView() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(DataManager.getDataManager(getActivity()).getUser().getID()).child("position");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!TextUtils.isEmpty((String) snapshot.child("lat").getValue())) {
                    updateWeather(Double.valueOf((String) snapshot.child("lat").getValue()),
                            Double.valueOf((String) snapshot.child("lng").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        updateWeather(30.58,114.27);
    }

    public void updateWeather(double latitude, double longitude) {
        weather = (TextView) view.findViewById(R.id.nearby_weather);
        weatherIcon = (ImageView) view.findViewById(R.id.nearby_weather_icon);
        String url = String.format("http://api.openweathermap.org/data/2.5/find?lat=%s&lon=%s&appid=55f0e84cc2fb75990bf6252f08651ee8&units=metric", latitude, longitude);
        JsonObjectRequest jsondata = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response = response.getJSONArray("list").getJSONObject(0);
                    JSONObject main_object = response.getJSONObject("main");
                    //Select the object in the JSON
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject obj = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = obj.getString("description");
                    String city = response.getString("name");
                    String icon = obj.getString("icon").substring(0, 2) + "d";
                    weather.setText(String.format("%s: %s℃", city, temp));
                    Picasso.get().load("https://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
                    Log.i("weather api response", temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("weather api error", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jsondata);
    }

}