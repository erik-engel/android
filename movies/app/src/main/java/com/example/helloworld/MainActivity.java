package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);
        editText = findViewById(R.id.editText1);
    }

    public void savePressed(View view){
        textView.setText(editText.getText());
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}