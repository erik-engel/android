package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
    }
    public void nextPage(View view) {
        Intent intent = new Intent(this, MainActivity5.class);
        startActivity(intent);
    }

    public void prevPage(View view) {
        finish();
    }
}