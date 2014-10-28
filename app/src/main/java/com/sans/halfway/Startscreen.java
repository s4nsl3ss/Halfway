package com.sans.halfway;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class Startscreen extends Activity {

    private EditText uname;
    private EditText pwd;
    private EditText email;
    private Button signin;
    private Button login;
    private Button skip;
    private Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_start);
        email = (EditText) findViewById(R.id.tv_mail);
        uname = (EditText) findViewById(R.id.tv_user);
        pwd = (EditText) findViewById(R.id.tv_pwd);
        signin = (Button) findViewById(R.id.bu_signin);
        login = (Button) findViewById(R.id.bu_login);
        skip = (Button) findViewById(R.id.bu_skip);

        ref = new Firebase("https://boiling-inferno-8767.firebaseio.com/");

        signin.setOnClickListener(new View.OnClickListener() {

          public void onClick(View v) {

               ref.createUser(email.getText().toString(), pwd.getText().toString(), new Firebase.ResultHandler() {
                     public void onSuccess() {
                         Toast.makeText(getApplicationContext(), "Registering was successful.\n You can login now.", Toast.LENGTH_LONG).show();
                                    }

                         public void onError(FirebaseError firebaseError) {
                         Toast.makeText(getApplicationContext(), "Registering failed: "+firebaseError, Toast.LENGTH_LONG).show();
                  }
                 });
              }
            });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.authWithPassword(email.getText().toString(), pwd.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Toast.makeText(getApplicationContext(), "User ID: " + authData.getUid(), Toast.LENGTH_LONG).show();
                        ref.child("name").setValue(uname.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Firebase login failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

}