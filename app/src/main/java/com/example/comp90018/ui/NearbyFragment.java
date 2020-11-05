package com.example.comp90018.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.example.comp90018.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NearbyFragment extends Fragment {


    private View view;


    private TextView weather;
    private String test;


    public NearbyFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        view= inflater.inflate(R.layout.fragment_nearby, container, false);

        initView();


        return view;
    }


    public void initView(){

//        weather=(TextView)view.findViewById(R.id.nearby_weather);
//        test = "no change";
//
//        String url = "http://api.openweathermap.org/data/2.5/weather?q=Norheimsund&appid=55f0e84cc2fb75990bf6252f08651ee8&units=metric";
//
//        JsonObjectRequest jsondata = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//                try {
//                    JSONObject main_object = response.getJSONObject("main");
//                    //Select the object in the JSON
//                    JSONArray array = response.getJSONArray("weather");
//                    JSONObject obj = array.getJSONObject(0);
//                    String temp = String.valueOf(main_object.getDouble("temp"));
//                    String description = obj.getString("description");
//                    String city = response.getString("name");
//
//
//                    test = temp;
//
//
//
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }
//
//        );
//        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        queue.add(jsondata);
//
//
//        weather.setText(test);


    }




    public void get_weather(){


    }




}