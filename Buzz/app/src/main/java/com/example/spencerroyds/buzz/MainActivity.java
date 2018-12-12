package com.example.spencerroyds.buzz;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private TextView mTextMessage;
    private GoogleMap mMap;
    int PLACE_PICKER_REQUEST = 1;
    protected GeoDataApi mGeoDataClient = null;
    private PlaceDetectionApi mPlaceDetectionClient = null;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView tvEmail;
    Menu damenu;
    boolean exists;
    boolean exists2;
    private int PROXIMITY_RADIUS = 24140;
    double latitude;
    double longitude;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ArrayList<MarkerOptions> favorites;
    Location savloc;
    LatLng latLng;

    ArrayList<String> arrayList;
    public static ArrayAdapter<String> adapter;
    public static ListView nearbyListView;
    Boolean clicked = false;
    public static String selectedPlace;
    ImageButton nearbyListBTN;
    public static ArrayList<String>TitleList;
    ArrayList<String> tempList;
    public static ArrayList<Double>LatList;
    public static ArrayList<Double>LngList;
    public static ArrayList<String>VicinityList;
    public static ArrayList<String>RatingList;
    public static ArrayList<String>PriceList;

    //////////////////////////////FAVORITES DATA
    ArrayList<Float> _alpha;
    ArrayList<Float> _anchorU;
    ArrayList<Float> _anchorV;
    ArrayList<Boolean> _draggable;
    ArrayList<Boolean> _flat;
    ArrayList<Float> _infoWindowAnchorU;
    ArrayList<Float> _infoWindowAnchorV;
    ArrayList<Double> _latitude;
    ArrayList<Double> _longitude;
    ArrayList<Float> _rotation;
    ArrayList<String> _snippet;
    ArrayList<String> _title;
    ArrayList<Boolean> _visible;
    ArrayList<Float> _zindex;
    ////////////////////////////////////////////

    private SharedPreferences mSharedPreferences;
    boolean favoritePopupOK;
    Dialog myDialog;
    boolean listLong;
    int nearbyListIndex;

    ShareDialog facebookShareDialog;
    CallbackManager callbackManager;

    String search;
    String shareName;
    public static SharedPreferences mSharedPreferences1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_favorites:
                    Intent j = new Intent(MainActivity.this, MessagingActivity.class);
                    startActivity(j);
                    return true;
                case R.id.navigation_settings:
                    Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSharedPreferences1 = getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
        final SharedPreferences.Editor mEditor = mSharedPreferences1.edit();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        favorites = new ArrayList<>();

        //////////////////////
        _alpha = new ArrayList<>();
        _anchorU = new ArrayList<>();
        _anchorV = new ArrayList<>();
        _draggable = new ArrayList<>();
        _flat = new ArrayList<>();
        _infoWindowAnchorU = new ArrayList<>();
        _infoWindowAnchorV = new ArrayList<>();
        _latitude = new ArrayList<>();
        _longitude = new ArrayList<>();
        _rotation = new ArrayList<>();
        _snippet = new ArrayList<>();
        _title = new ArrayList<>();
        _visible = new ArrayList<>();
        _zindex = new ArrayList<>();
        /////////////////////

        TitleList = new ArrayList<String>();
        tempList = new ArrayList<String>();
        LatList = new ArrayList<Double>();
        LngList = new ArrayList<Double>();
        VicinityList = new ArrayList<String>();
        PriceList = new ArrayList<String>();
        RatingList = new ArrayList<String>();

        myDialog = new Dialog(this);
        favoritePopupOK = false;
        listLong = false;
        nearbyListIndex = 0;





        facebookShareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        facebookShareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });



        nearbyListView = (ListView) findViewById(R.id.nearbyView);
        nearbyListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1,
                TitleList){@Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Get the Item from ListView
            View view = super.getView(position, convertView, parent);

            // Initialize a TextView for ListView each Item
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
            //Set the text color of TextView (ListView Item)
            tv.setTextColor(Color.WHITE);
            tv2.setTextColor(Color.DKGRAY);
            tv.setMinLines(1);
            tv.setMaxLines(2);
            tv2.setMinLines(1);
            tv2.setMaxLines(2);
            tv.setFocusable(false);
            tv.setClickable(false);
            tv2.setFocusable(false);
            tv2.setClickable(false);

            tv.setText(TitleList.get(position).toString() + "\n" + "Rating: " + RatingList.get(position).toString() + " " + PriceList.get(position).toString());
            tv2.setText(VicinityList.get(position).toString());

            // Generate ListView Item using TextView
            return view;
        }
        };

        nearbyListView.setAdapter(adapter);

        String _email = user.getEmail();
        String[] parts = _email.split("@");
        _email = parts[0];

        databaseReference.child("users").child(_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child: children) {
                    String email = user.getEmail();
                    String[] parts = email.split("@");
                    email = parts[0];
                    if (child.getKey().toString().contains("Age"))
                    {

                        break;
                    }
                    else
                    {

                        ShowPopUpAccountInfo(null, mEditor);


                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child: children) {
                    String email = user.getEmail();
                    String[] parts = email.split("@");
                    email = parts[0];
                    if (child.getKey().toString().contains(email))
                    {
                        exists = true;
                        break;
                    }
                    else
                    {

                    }

                }
                if(exists == false)
                {
                    exists = false;
                    saveUserInfo();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        databaseReference.child("users").child(_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child: children) {
                    String email = user.getEmail();
                    String[] parts = email.split("@");
                    email = parts[0];
                    if (child.getValue().toString().contains("Favorites"))
                    {
                        exists2 = true;
                        break;
                    }
                    else
                    {

                    }

                }
                if(exists2 == false)
                {
                    exists2 = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        databaseReference.child("users").child(_email).child("Favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

                _alpha.clear();
                _anchorU.clear();
                _anchorV.clear();
                _draggable.clear();
                _flat.clear();
                _infoWindowAnchorU.clear();
                _infoWindowAnchorV.clear();
                _latitude.clear();
                _longitude.clear();
                _rotation.clear();
                _snippet.clear();
                _title.clear();
                _visible.clear();
                _zindex.clear();

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    Iterable<DataSnapshot> children2 = child.getChildren();
                    for (DataSnapshot child2: children2) {

                        if (child2.getKey().toString().contains("alpha") && child2.exists())
                        {
                             _alpha.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("anchorU"))
                        {
                             _anchorU.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("anchorV"))
                        {
                             _anchorV.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("draggable"))
                        {
                             _draggable.add(child2.getValue(Boolean.class));
                        }
                        else if (child2.getKey().toString().contains("flat"))
                        {
                             _flat.add(child2.getValue(Boolean.class));
                        }
                        else if (child2.getKey().toString().contains("infoWindowAnchorU"))
                        {
                             _infoWindowAnchorU.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("infoWindowAnchorV"))
                        {
                             _infoWindowAnchorV.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("position"))
                        {
                            String pos[] = child2.getValue().toString().split(",");
                            String latstring = pos[0].toString();
                            String _lat[] = latstring.split("=");
                            _latitude.add(Double.parseDouble(_lat[1]));

                            String longstring = pos[1].toString();
                            String _long[] = longstring.split("=");
                            _long[1] = _long[1].toString();
                            _long[1] = _long[1].substring(0, _long[1].length() - 1);
                            _longitude.add(Double.parseDouble(_long[1]));

                        }
                        else if (child2.getKey().toString().contains("rotation"))
                        {
                             _rotation.add(child2.getValue(Float.class));
                        }
                        else if (child2.getKey().toString().contains("snippet"))
                        {
                             _snippet.add(child2.getValue(String.class));
                        }
                        else if (child2.getKey().toString().contains("title"))
                        {
                             _title.add(child2.getValue(String.class));
                        }
                        else if (child2.getKey().toString().contains("visible"))
                        {
                             _visible.add(child2.getValue(Boolean.class));
                        }
                        else if (child2.getKey().toString().contains("zindex"))
                        {
                             _zindex.add(child2.getValue(Float.class));
                        }
                    }

                    favorites.clear();
                    mMap.clear();
                    for (int x = 0; x < _alpha.size(); x++)
                    {
                        LatLng place = new LatLng(_latitude.get(x), _longitude.get(x));

                        MarkerOptions _options = new MarkerOptions().position(place).title(_title.get(x)).snippet
                                (_snippet.get(x)).alpha(_alpha.get(x)).draggable(_draggable.get(x))
                                .anchor(_anchorV.get(x), _anchorU.get(x))
                                .flat(_flat.get(x))
                                .infoWindowAnchor(_infoWindowAnchorV.get(x), _infoWindowAnchorU.get(x))
                                .visible(_visible.get(x)).rotation(_rotation.get(x)).zIndex(_zindex.get(x));

                        favorites.add(_options);
                        mMap.addMarker(_options);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        nearbyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String _search = TitleList.get(i).toString() + " " + VicinityList.get(i).toString();
                setSearch(_search);
                String _name = TitleList.get(i).toString();
                setName(_name);

                String strUri = "http://maps.google.com/maps?q=" + TitleList.get(i).toString() + " " + VicinityList.get(i).toString();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);

            }
        });


        nearbyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String _search = TitleList.get(i).toString() + " " + VicinityList.get(i).toString();
                setSearch(_search);
                String _name = TitleList.get(i).toString();
                setName(_name);
                nearbyListIndex = i;
                listLong = true;
                ShowPopUp(null);
                return true;
            }
        });



    }


    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mSharedPreferences = getSharedPreferences("THEME_PREFERENCE", Context.MODE_PRIVATE);
        mMap = googleMap;

        if(mSharedPreferences.contains("DARK")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.nightmapstylejson)));
        }
        else if(mSharedPreferences.contains("STANDARD")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.standardmapstylejson)));
        }
        else if(mSharedPreferences.contains("SILVER")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.silvermapstylejson)));
        }
        else if(mSharedPreferences.contains("RETRO")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.retromapstylejson)));
        }
        else if(mSharedPreferences.contains("NIGHT")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.nighttimemapstylejson)));
        }
        else if(mSharedPreferences.contains("AUBERGINE")){
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.auberginemapstylejson)));
        }




        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {

            }
        }

        mMap.setOnMyLocationButtonClickListener(this);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String locationTitle = marker.getTitle().toString();
                String address = marker.getSnippet().toString();

                String search = locationTitle + " " + address;
//
                String strUri = "http://maps.google.com/maps?q=" + search;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
//
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//
                startActivity(intent);
            }
        });


        mMap.setOnInfoWindowCloseListener(this);


        ImageButton btnFindNearMe = (ImageButton) findViewById(R.id.btnFindNearMe);
        btnFindNearMe.setFocusable(false);

        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSeachName);
        btnSearch.setFocusable(false);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        btnFindNearMe.setOnClickListener(new View.OnClickListener() {
            String cafe = "cafe";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();
                String url = getUrl(latitude, longitude, cafe);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);

                Toast.makeText(MainActivity.this,"Searching....", Toast.LENGTH_LONG).show();


                FirebaseUser user = firebaseAuth.getCurrentUser();
                String _email = user.getEmail();
                String[] parts = _email.split("@");
                _email = parts[0];

                databaseReference.child("users").child(_email).child("Favorites").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated

                        _alpha.clear();
                        _anchorU.clear();
                        _anchorV.clear();
                        _draggable.clear();
                        _flat.clear();
                        _infoWindowAnchorU.clear();
                        _infoWindowAnchorV.clear();
                        _latitude.clear();
                        _longitude.clear();
                        _rotation.clear();
                        _snippet.clear();
                        _title.clear();
                        _visible.clear();
                        _zindex.clear();

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child: children) {
                            Iterable<DataSnapshot> children2 = child.getChildren();
                            for (DataSnapshot child2: children2) {

                                if (child2.getKey().toString().contains("alpha") && child2.exists())
                                {
                                    _alpha.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("anchorU"))
                                {
                                    _anchorU.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("anchorV"))
                                {
                                    _anchorV.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("draggable"))
                                {
                                    _draggable.add(child2.getValue(Boolean.class));
                                }
                                else if (child2.getKey().toString().contains("flat"))
                                {
                                    _flat.add(child2.getValue(Boolean.class));
                                }
                                else if (child2.getKey().toString().contains("infoWindowAnchorU"))
                                {
                                    _infoWindowAnchorU.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("infoWindowAnchorV"))
                                {
                                    _infoWindowAnchorV.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("position"))
                                {
                                    String pos[] = child2.getValue().toString().split(",");
                                    String latstring = pos[0].toString();
                                    String _lat[] = latstring.split("=");
                                    _latitude.add(Double.parseDouble(_lat[1]));

                                    String longstring = pos[1].toString();
                                    String _long[] = longstring.split("=");
                                    _long[1] = _long[1].toString();
                                    _long[1] = _long[1].substring(0, _long[1].length() - 1);
                                    _longitude.add(Double.parseDouble(_long[1]));

                                }
                                else if (child2.getKey().toString().contains("rotation"))
                                {
                                    _rotation.add(child2.getValue(Float.class));
                                }
                                else if (child2.getKey().toString().contains("snippet"))
                                {
                                    _snippet.add(child2.getValue(String.class));
                                }
                                else if (child2.getKey().toString().contains("title"))
                                {
                                    _title.add(child2.getValue(String.class));
                                }
                                else if (child2.getKey().toString().contains("visible"))
                                {
                                    _visible.add(child2.getValue(Boolean.class));
                                }
                                else if (child2.getKey().toString().contains("zindex"))
                                {
                                    _zindex.add(child2.getValue(Float.class));
                                }
                            }

                            favorites.clear();
                            mMap.clear();
                            for (int x = 0; x < _alpha.size(); x++)
                            {
                                LatLng place = new LatLng(_latitude.get(x), _longitude.get(x));

                                MarkerOptions _options = new MarkerOptions().position(place).title(_title.get(x)).snippet
                                        (_snippet.get(x)).alpha(_alpha.get(x)).draggable(_draggable.get(x))
                                        .anchor(_anchorV.get(x), _anchorU.get(x))
                                        .flat(_flat.get(x))
                                        .infoWindowAnchor(_infoWindowAnchorV.get(x), _infoWindowAnchorU.get(x))
                                        .visible(_visible.get(x)).rotation(_rotation.get(x)).zIndex(_zindex.get(x));

                                favorites.add(_options);
                                mMap.addMarker(_options);
                            }

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                });

                adapter.notifyDataSetChanged();

            }
        });


    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current Location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
       marker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        marker.hideInfoWindow();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Favorited: %s", place.getName());
                int priceLevel = 0;
                priceLevel = place.getPriceLevel();
                String price = "";

                if (priceLevel == 0)
                {
                    price = "";
                }
                else if (priceLevel == 1)
                {
                    price = "$";
                }
                else if (priceLevel == 2)
                {
                    price = "$$";
                }
                else if (priceLevel == 3)
                {
                    price = "$$$";
                }
                else if (priceLevel == 4)
                {
                    price = "$$$$";
                }

                float rating = place.getRating();

                if(rating < 0)
                {
                    rating = 0;
                }

                LatLng place_picked = place.getLatLng();
                MarkerOptions options = new MarkerOptions().position(place_picked).title((String)place.getName()).snippet
                        ((String) place.getAddress());
                favorites.add(options);
                mMap.clear();
                saveFavorites();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place_picked));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

    private void saveUserInfo()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String[] parts = email.split("@");
        email = parts[0];
        databaseReference.child("users").child(email).setValue(user.getUid());
    }

    private void saveFavorites()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String[] parts = email.split("@");
        email = parts[0];
        databaseReference.child("users").child(email).child("Favorites").setValue(favorites);
    }

    public void ShowPopUpAccountInfo(View V, SharedPreferences.Editor editor) {
        final  FirebaseUser user = firebaseAuth.getCurrentUser();
        String _email = user.getEmail();
        String[] parts = _email.split("@");
        _email = parts[0];

        final SharedPreferences.Editor mEditor = editor;

        myDialog.setContentView(R.layout.accountinfo_popup);
        final RadioButton female = (RadioButton) myDialog.findViewById(R.id.radioButtonFem);
        final RadioButton male = (RadioButton) myDialog.findViewById(R.id.radioButtonMale);
        final EditText age = (EditText) myDialog.findViewById(R.id.actAge);
        final Button save = (Button) myDialog.findViewById(R.id.saveBTN);
        final EditText username = (EditText) myDialog.findViewById(R.id.actUsername);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String email = user.getEmail();
                String[] parts = email.split("@");
                email = parts[0];
        if (username.getText().toString() == "" || age.getText().toString() == "")
        {
            Toast.makeText(MainActivity.this,"Must fill out everything", Toast.LENGTH_LONG).show();
        }
        else

                databaseReference.child("users").child(email).child("Username").setValue(username.getText().toString());
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("USERNAME", username.getText().toString());
                mEditor.apply();
                databaseReference.child("users").child(email).child("Age").setValue(age.getText().toString());

        if (female.isChecked() == false & male.isChecked() == false)
        {
            Toast.makeText(MainActivity.this,"Must fill out everything", Toast.LENGTH_LONG).show();

        }
                if (female.isChecked())
                {
                    databaseReference.child("users").child(email).child("Gender").setValue("Female");

                }
                else if (male.isChecked())
                {
                    databaseReference.child("users").child(email).child("Gender").setValue("Male");

                }
                myDialog.dismiss();
            }
        });
        myDialog.dismiss();
    //  databaseReference.child("users").child(_email).addValueEventListener(new ValueEventListener() {
    //      @Override
    //      public void onDataChange(DataSnapshot dataSnapshot) {
    //          // This method is called once with the initial value and again
    //          // whenever data at this location is updated

    //          Iterable<DataSnapshot> children = dataSnapshot.getChildren();

    //          for (DataSnapshot child: children) {
    //              String email = user.getEmail();
    //              String[] parts = email.split("@");
    //              email = parts[0];
    //              if (child.getValue().toString().contains("Age"))
    //              {
    //                  myDialog.dismiss();
    //              }
    //              else
    //              {
    //                  myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //                  myDialog.show();


    //              }

    //          }


    //      }

    //      @Override
    //      public void onCancelled(DatabaseError error) {
    //          // Failed to read value

    //      }
    //  });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    private void savelatlng(LatLng latLng)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String[] parts = email.split("@");
        email = parts[0];
        databaseReference.child("users").child(email).child("Location").setValue(latLng);
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&key=" + "AIzaSyAUeigkVzRTHER3JGbInmeVBQRkh0jBF-c");
        googlePlacesUrl.append("&sensor=true");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
       //saveCurrentLocation(mLastLocation);
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //savelatlng(latLng);
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                savelatlng(latLng);
            }
        };

        timer.schedule (hourlyTask, 0l, 1000*1*60);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    public void setClicked(boolean clicked)
    {
        clicked = true;
    }

    public boolean getClicked()
    {
        return clicked;
    }

    public static void addTitle(String title)
    {
        TitleList.add(title.toString());
    }

    public static void notifyAdapter()
    {
        adapter.notifyDataSetChanged();
    }

    public static void addAddress(String address)
    {
        VicinityList.add(address.toString());
    }

    public static void setVisible()
    {
        nearbyListView.setVisibility(View.VISIBLE);
    }

    public static void addLatLng(Double lat, Double lng)
    {
        LatList.add(lat);
        LngList.add(lng);
    }

    public static void addPrice(String price)
    {
        PriceList.add(price.toString());
    }

    public static void addRating(String rating)
    {
        RatingList.add(rating.toString());
    }

    public void ShowPopUp(View v)
    {
        myDialog.setContentView(R.layout.favorite_popup);
        TextView txtOK;
        TextView txtCANCEL;
        TextView txtSHARE;
        txtOK = (TextView) myDialog.findViewById(R.id.popupOK);
        txtSHARE = (TextView) myDialog.findViewById(R.id.popupShare);


        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

                if(listLong)
                {
                    LatLng favLatLng = new LatLng(LatList.get(nearbyListIndex), LngList.get(nearbyListIndex));

                    MarkerOptions options = new MarkerOptions().position(favLatLng).title((String)TitleList.get(nearbyListIndex)).snippet
                            ((String) VicinityList.get(nearbyListIndex));
                    favorites.add(options);
                    mMap.clear();
                    saveFavorites();
                    listLong = false;
                }

            }
        });
        txtSHARE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                String _search = getSearch();
                String quote = "Check out this coffee shop, " + getName() + ", that I found using Buzz!";

                String strUri = "http://maps.google.com/maps?q=" + _search;
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(strUri))
                        .setQuote(quote)
                        .build();

                facebookShareDialog.show(content, ShareDialog.Mode.WEB);

                listLong = false;
            }
        });



    //   txtCANCEL = (TextView) myDialog.findViewById(R.id.popupCANCEL);
    //   txtCANCEL.setOnClickListener(new View.OnClickListener() {
    //       @Override
    //       public void onClick(View v) {
    //           myDialog.dismiss();
    //       }
    //   });

    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();

    }

    void setFlag()
    {
        favoritePopupOK = true;
    }

    void setSearch(String _search) {search = _search;}

    String getSearch() {return search;}

    void setName(String _name) {shareName = _name;}

    String getName() {return shareName;}

}
