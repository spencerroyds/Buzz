package com.example.spencerroyds.buzz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    public static SharedPreferences mSharedPreferences;
    FirebaseAuth firebaseAuth1;
    private FirebaseAuth.AuthStateListener authStateListener1;
    FirebaseUser user;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> mySettingsList;
    boolean theme_chosen = false;
    int theme_choice = 0;
    Dialog myDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent j = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(j);
                    return true;
                case R.id.navigation_favorites:
                    try {
                        startActivityForResult(builder.build(SettingsActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myDialog = new Dialog(this);
        listView = (ListView) findViewById(R.id.SettingsList);
        mySettingsList = new ArrayList<String>();
        mySettingsList.add("Log Out");
        mySettingsList.add("Map Themes");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_settings);

        firebaseAuth1 = FirebaseAuth.getInstance();
        authStateListener1 = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1,
                mySettingsList)
        {@Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Get the Item from ListView
            View view = super.getView(position, convertView, parent);

            // Initialize a TextView for ListView each Item
            TextView tv = (TextView) view.findViewById(android.R.id.text1);

            // Set the text color of TextView (ListView Item)
            tv.setTextColor(Color.WHITE);

            // Generate ListView Item using TextView
            return view;
        }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    firebaseAuth1.getInstance().signOut();
                    Toast.makeText(SettingsActivity.this, "Signed Out",
                            Toast.LENGTH_SHORT).show();

                    Intent j = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(j);
                }
                else if(i == 1)
                {

                    mSharedPreferences = getSharedPreferences("THEME_PREFERENCE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    ShowPopUp(null, mEditor);

                }

           }
        });
    }

    @Override
    public void onBackPressed() {

        if (user == null)
        {
            Intent nextpagge = new Intent(SettingsActivity.this,LoginActivity.class);
        }
        else
            super.onBackPressed();

    }

    public void ShowPopUp(View v, SharedPreferences.Editor editor)
    {
        final SharedPreferences.Editor mEditor = editor;
        myDialog.setContentView(R.layout.theme_popup);
        TextView txtClose;
        final ImageView darkMap;
        darkMap = (ImageView) myDialog.findViewById(R.id.dark_map);
        final ImageView standardMap;
        standardMap = (ImageView) myDialog.findViewById(R.id.standard_map);
        final ImageView aubergineMap;
        aubergineMap = (ImageView) myDialog.findViewById(R.id.aubergine_map);
        final ImageView nightMap;
        nightMap = (ImageView) myDialog.findViewById(R.id.night_map);
        final ImageView retroMap;
        retroMap = (ImageView) myDialog.findViewById(R.id.retro_map);
        final ImageView silverMap;
        silverMap = (ImageView) myDialog.findViewById(R.id.silver_map);

        txtClose = (TextView) myDialog.findViewById(R.id.popUpClose);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                setThemeChoice(0);
                setFlag();
            }
        });

        darkMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(0);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("DARK", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        standardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(1);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("STANDARD", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        silverMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(2);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("SILVER", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        retroMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(3);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("RETRO", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        nightMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(4);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("NIGHT", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        aubergineMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeChoice(5);
                mEditor.clear();
                mEditor.apply();
                mEditor.putString("AUBERGINE", "Theme");
                mEditor.apply();
                myDialog.dismiss();
                setFlag();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void setThemeChoice(int theme_int)
    {
        theme_choice = theme_int;
    }

    public void setFlag()
    {
        theme_chosen = true;
    }

}
