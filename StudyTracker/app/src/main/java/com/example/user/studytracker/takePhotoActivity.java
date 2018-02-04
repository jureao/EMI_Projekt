package com.example.user.studytracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class takePhotoActivity extends AppCompatActivity {
    private static final int ACTIVITY_START_CAMERA_APP=0;
    private ImageView CapturedImageView;
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        CapturedImageView= (ImageView) findViewById(R.id.Captured);

    }

    public void Capture(View view) {


        Intent CallCameraApplication = new Intent ();
        CallCameraApplication.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(CallCameraApplication,ACTIVITY_START_CAMERA_APP);


    }


    protected void onActivityResult (int requestCode, int resultCode,Intent data){
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode== RESULT_OK){
            Toast.makeText(this,"Picture taken successfully",Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            CapturedImageView.setImageBitmap(photoCapturedBitmap);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFilename= "Image_" + timeStamp + "_";
            File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = new File (storageDirectory, imageFilename);

            try{
                FileOutputStream out = new FileOutputStream(image);
                photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                out.flush();
                out.close();
                Toast.makeText(this, image.toString()+" hello toast!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
