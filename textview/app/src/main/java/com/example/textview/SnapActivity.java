package com.example.textview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.textview.model.Note;
import com.example.textview.repo.Repo;

public class SnapActivity extends AppCompatActivity implements TaskListener {

    private EditText editText;
    private ImageView imageView;
    private Bitmap storageImageBitmap;
    private Note currentNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap   );
        //editText = findViewById(R.id.editText1);
        imageView = findViewById(R.id.imageView2);
        String noteId = getIntent().getStringExtra("noteid");
        currentNote = Repo.r().getNoteWith(noteId);
        //editText.setText(currentNote.getText());
        //setUpNote();
        //setupStaticImage();

        Repo.r().downloadBitmap(noteId, this);
        waitAndDelete();
    }

    @Override
    public void receive(byte[] bytes) {
        storageImageBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imageView.setImageBitmap(storageImageBitmap);
    }

    private void waitAndDelete() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("This will run after 1 second");
                delete();
                finish();
            }
        }, 10000);
    }

    private void delete(){
        Repo.r().deleteImage(currentNote);
    }
}