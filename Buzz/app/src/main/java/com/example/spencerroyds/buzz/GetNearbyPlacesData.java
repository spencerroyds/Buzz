package com.example.spencerroyds.buzz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by spencerroyds on 5/29/18.
 */

public class GetNearbyPlacesData extends AsyncTask <Object, String, String>{

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    public static ArrayList<String>nearbyTitleList;
    public static ArrayList<Double>nearbyLatList;
    public static ArrayList<Double>nearbyLngList;
    public static ArrayList<String>nearbyVicinityList;
    public static ArrayList<String>nearbyPriceList;
    public static ArrayList<String>nearbyRatingList;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadURL downloadUrl = new DownloadURL();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);

        for(int i = 0; i < nearbyTitleList.size(); i++)
        {
            MainActivity.addTitle(nearbyTitleList.get(i).toString());
            MainActivity.notifyAdapter();
            MainActivity.addAddress(nearbyVicinityList.get(i).toString());
            MainActivity.addLatLng(nearbyLatList.get(i), nearbyLngList.get(i));
            MainActivity.addPrice(nearbyPriceList.get(i));
            MainActivity.addRating(nearbyRatingList.get(i));
        }

        MainActivity.setVisible();

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        nearbyTitleList = new ArrayList<String>();
        nearbyLatList = new ArrayList<Double>();
        nearbyLngList = new ArrayList<Double>();
        nearbyVicinityList = new ArrayList<String>();
        nearbyPriceList = new ArrayList<>();
        nearbyRatingList = new ArrayList<>();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String rating = googlePlace.get("rating");
            String price = googlePlace.get("price_level");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);

            if (price.contains("0") || price == null || price.isEmpty())
            {
                price = "";
            }
            else if (price.contains("1"))
            {
                price = "$";
            }
            else if (price.contains("2"))
            {
                price = "$$";
            }
            else if (price.contains("3"))
            {
                price = "$$$";
            }
            else if (price.contains("4"))
            {
                price = "$$$$";
            }

            nearbyTitleList.add(placeName);
            nearbyLatList.add(lat);
            nearbyLngList.add(lng);
            nearbyVicinityList.add(vicinity);
            nearbyRatingList.add(rating);
            nearbyPriceList.add(price);

            markerOptions.title(placeName);

            markerOptions.snippet(vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }

    }

}
