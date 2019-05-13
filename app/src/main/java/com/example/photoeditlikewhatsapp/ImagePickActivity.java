package com.example.photoeditlikewhatsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImagePickActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 01;
    private static final int PICK_REQUEST = 02;
    private static final int IMAGE_GET_REQUEST = 03;

    private ImageView ImgPicked;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        Button btnPickImage = findViewById(R.id.btn_pick_image);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
                } else {
                    pickImage();
                }
            }
        });

        ImgPicked = findViewById(R.id.img_picked);
    }

    /**
     * Method Call for Image Picker Intent
     */
    private void pickImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("imageUri", data.getData().toString());
                startActivityForResult(intent, IMAGE_GET_REQUEST);
            } else if (requestCode == IMAGE_GET_REQUEST) {

                /**
                 *  Get Back the result image from MainActivity
                 */
                ImgPicked.setImageURI(Uri.fromFile(new File(data.getStringExtra("imagePath"))));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
