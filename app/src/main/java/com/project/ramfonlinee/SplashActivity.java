package com.project.ramfonlinee;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import sdk.chat.ui.HomeActivity;


public class SplashActivity  extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, HomeActivity.class));


    }
}
