package com.example.textview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailactivity);
        EditText editText = findViewById(R.id.editText1);
        editText.setText(getIntent().getStringExtra("note"));
    }
}