package com.example.mandatory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button addTextBtn;
    ImageView imageView;
    Button mChooseBtn;
    AlertDialog.Builder builder;
    Bitmap orgImg;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // VIEWS
        imageView = findViewById(R.id.imageView);
        mChooseBtn = findViewById(R.id.button);
        addTextBtn = findViewById(R.id.button2);
        editText = findViewById(R.id.editTextTextPersonName);
        builder = new AlertDialog.Builder(this);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        orgImg = drawable.getBitmap();

        mChooseBtn.setOnClickListener(v -> {
            openDialog();
        });
        addTextBtn.setOnClickListener(v -> {
            String textToImg = editText.getText().toString();
//            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//            Bitmap imageBitmap = drawable.getBitmap();
            Bitmap manipulated = drawTextToBitmap(orgImg, textToImg);
            imageView.setImageBitmap(manipulated);
        });
//        imageView.setOnClickListener(v -> {
//            if(v.equals(imageView)) {
//
//                System.out.println("hej");
//                System.out.println(imageView);
//                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//                if (bitmap instanceof Bitmap){
//                    System.out.println("This is bitmap");
//                }
//            }
//        });
    }



    private void openDialog(){
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



    // Drawing text on image


    public Bitmap drawTextToBitmap(Bitmap image, String gText) {
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
        paint.setTextSize((int) (20)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // text shadow
        canvas.drawText(gText, 10, 100, paint);
        return image;
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

    //handle result of runtime permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if(resultCode == RESULT_OK){
                    imageView.setImageURI(data.getData());
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    orgImg = drawable.getBitmap();
                }
                break;
            case 1001:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    orgImg = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(orgImg);
                }
                break;

        }
    }
}