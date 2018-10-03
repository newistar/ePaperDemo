package com.subratgupta.epaperdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);
        mStorage = FirebaseStorage.getInstance().getReference();

        StorageReference filePath = mStorage.child("NewsPhotos/image.png");
//        Picasso.with(MainActivity.this).load(filePath.toString()).into(mImageView);
        Glide.with(this /* context */).load(mStorage.child("image.png")).into(mImageView);

        Toast.makeText(getApplicationContext(),filePath.toString(),Toast.LENGTH_LONG).show();

    }
}
