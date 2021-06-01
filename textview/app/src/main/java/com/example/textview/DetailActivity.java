package com.example.textview;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.textview.model.Note;
import com.example.textview.repo.Repo;

public class DetailActivity extends AppCompatActivity implements TaskListener {

    private Bitmap currenBitMap;
    private ImageView imageView;
    private Note currentNote;
    private EditText editText;
    private Bitmap storageImageBitmap;
    AlertDialog.Builder builder;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailactivity);
        editText = findViewById(R.id.editText1);
        imageView = findViewById(R.id.imageView);
        builder = new AlertDialog.Builder(this);
        String noteId = getIntent().getStringExtra("noteid");
        currentNote = Repo.r().getNoteWith(noteId);
        editText.setText(currentNote.getText());
        //setUpNote();
        setupStaticImage();

        Repo.r().downloadBitmap(noteId, this);
    }

    public void save(View view){
        imageView.buildDrawingCache(true);
        currenBitMap = Bitmap.createBitmap(imageView.getDrawingCache(true));
        System.out.println("The bitmap size: " + currenBitMap.getByteCount());
        currentNote.setText(editText.getText().toString());
        Repo.r().updateNoteAndImage(currentNote, currenBitMap);
    }

    private void setUpNote() {

    }

    private void setupStaticImage() {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        storageImageBitmap = drawable.getBitmap();
//        myst be called AFTER onCreate() has finished. Because otherwise the image
//        is not there yet
//        imageView.buildDrawingCache(true);
//        currentBitmap = Bitmap.createBitmap(imageView.getDrawingCache(true));
    }


    public void openDialog(View view){
        System.out.println("test");
        builder.setMessage("Want to take a pic, or choose one from storage?")
                .setCancelable(false)
                .setPositiveButton("Camera", (dialog, which) -> {
                    pickImageFromCamera();
                })
                .setNegativeButton("Photos", (dialog, id) -> {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            //permission not granted, request it.
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            //show popup for runtime permission
                            requestPermissions(permissions, PERMISSION_CODE);
                        } else {
                            System.out.println("hi");
                            //permission already granted
                            pickImageFromGallery();
                        }
                    } else {
                        //system os is less then marshmallow
                        pickImageFromGallery();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Choose img upload");
        alert.show();


    }

    private void pickImageFromCamera() {
        Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intentCam, IMAGE_CAPTURE_CODE);
        } catch (ActivityNotFoundException e ) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    public void addTxtToImg(View view){
        String textToImg = editText.getText().toString();
        Bitmap manipulated = drawTextToBitmap(storageImageBitmap, textToImg);
        imageView.setImageBitmap(manipulated);
    }

    public Bitmap drawTextToBitmap(Bitmap image, String gText) {
        System.out.println(image.getWidth() + " " + image.getHeight()); // for testing
        int start = image.getWidth()/5;
        int end = image.getHeight()/2;
        int textSize = image.getHeight()/10;
        Bitmap.Config bitmapConfig = image.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        image = image.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new antialised Paint
        paint.setColor(Color.rgb(161, 161, 161));
        paint.setTextSize((int) (textSize)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // text shadow
        canvas.drawText(gText, start, end, paint);
        return image;
    }

    public void delete(View view){
        Repo.r().deleteNote(currentNote);
        Repo.r().deleteImage(currentNote);

        //Intent myIntent = new Intent(DetailActivity.class);
//        myIntent.putExtra("noteid", items.get(position).getId());

//        startActivity(new Intent(view.getContext(), MainActivity.class));
    finish();
    }

    @Override
    public void receive(byte[] bytes) {
        storageImageBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imageView.setImageBitmap(storageImageBitmap);
//figure out, how to get the byte array to an image, and from there to the imageView
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if(resultCode == RESULT_OK){
                    imageView.setImageURI(data.getData());
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    storageImageBitmap = drawable.getBitmap();
                }
                break;
            case 1001:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    storageImageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(storageImageBitmap);
                }
                break;

        }
    }
}