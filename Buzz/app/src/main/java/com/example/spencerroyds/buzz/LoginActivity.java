package com.example.spencerroyds.buzz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText txtEmailAddress;
    private EditText txtPassword;
    FirebaseAuth firebaseAuth;
    private String email;
    private String pass;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "LoginActivity";
    public Button reg;
    public Button log;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user;

    private static final int RC_SIGN_IN = 1;

    private GoogleSignInClient mGoogleSignInClient;
    public Button mGoogleButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ///////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("962476874449-hubsifpma4pa1m6gknkmubqtb2v9gkoj.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleButton = (Button) findViewById(R.id.googleSignInButton);

        ///////////////////////

        txtEmailAddress = (EditText) findViewById(R.id.txtEmailLogin);
        txtPassword = (EditText) findViewById(R.id.txtPwd);
        reg = (Button) findViewById(R.id.registerBTN);
        log = (Button) findViewById(R.id.loginBTN);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(LoginActivity.this, Splash.class);
                    // i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                    startActivity(i);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email = txtEmailAddress.getText().toString();
                pass = txtPassword.getText().toString();

                if(!email.equals("") && !pass.equals(""))
                {
                    if (!email.contains(" "))
                    {
                        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                Log.d(TAG, "Sign in Complete" + task.isSuccessful());


                                if (!task.isSuccessful())
                                {
                                    Log.w(TAG, "Sign in Failed" + task.getException());
                                    txtEmailAddress.setError("Email or Password Incorrect");
                                    txtEmailAddress.requestFocus();
                                }
                            }
                        });
                    }
                    else
                    {
                        txtEmailAddress.setError("Email can't contain a space");
                        txtEmailAddress.requestFocus();
                    }
                }
                else
                {
                    txtEmailAddress.setError("Missing Information");
                    txtEmailAddress.requestFocus();
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmailAddress.getText().toString();
                pass = txtPassword.getText().toString();
                if (pass.length() >= 8)
                {
                    if(!email.equals("") && !pass.equals("")){
                        if(!email.contains(" ")){
                            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String userId = user.getUid();
                                        Toast.makeText(LoginActivity.this, "Registration Successful",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        txtEmailAddress.setError("Account Exists Already");
                                        txtEmailAddress.requestFocus();
                                    }
                                }
                            });
                        }
                        else {
                            txtEmailAddress.setError("Email can't contain a space");
                            txtEmailAddress.requestFocus();
                        }
                    }
                    else {
                        txtEmailAddress.setError("Missing Information");
                        txtEmailAddress.requestFocus();
                    }

                }
                else {
                    txtPassword.setError("Password must be 8 characters long");
                    txtPassword.requestFocus();
                }

            }


        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch(ApiException e) {

                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, Splash.class);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "User logged in successfully",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "User log in failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if (authStateListener != null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onBackPressed() {

        if (user == null)
        {
            Intent nextpagge = new Intent(LoginActivity.this,LoginActivity.class);
        }
        else
            super.onBackPressed();

    }

    public void NextPage(View V)
    {
        Intent nextpage = new Intent(LoginActivity.this, Splash.class);
        startActivity(nextpage);
    }

}

