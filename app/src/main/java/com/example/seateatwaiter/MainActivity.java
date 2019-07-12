package com.example.seateatwaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static int TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences;
        preferences = getSharedPreferences("loginref", MODE_PRIVATE);
        boolean savelogin = preferences.getBoolean("savelogin", false);
        if (!savelogin) { // se non è loggato

            Toast.makeText(this, "prima volta", Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();

        }
        else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();
                }
            }, TIME_OUT);
        }
    }
}

