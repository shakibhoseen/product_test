package com.example.producttest.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


import com.example.producttest.HomeActivity;
import com.example.producttest.MainActivity;
import com.example.producttest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logIn , resister;

    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        logIn = findViewById(R.id.btn_login);
        resister= findViewById(R.id.btn_register);



        Intent intent1 = getIntent();
        if (intent1 != null && intent1.getData() != null) {
            Uri data = intent1.getData();
            String path = data.getPath();
            String id = data.getQueryParameter("id");

            Log.d("ID", ": " + id);

            Toast.makeText(this, "ID "+id, Toast.LENGTH_LONG).show();
            

        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null){



                     Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                     startActivity(intent);
                     finish();


        }


        logIn.setOnClickListener(this);
        resister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_login == v.getId()){
            startActivity(new Intent(StartActivity.this, LogInActivity.class));

        }
        if (R.id.btn_register == v.getId()){
            startActivity(new Intent(StartActivity.this , RegisterActivity.class));

        }
    }






}
