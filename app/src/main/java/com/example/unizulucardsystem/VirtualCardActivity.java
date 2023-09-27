package com.example.unizulucardsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class VirtualCardActivity extends AppCompatActivity {
    ImageView virtualCardImageView;
    private String surname, tittle, identity, initials, studentno, res, year;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_card);

        virtualCardImageView = findViewById(R.id.virtual_card_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tittle = extras.getString("tittle");
            identity = extras.getString("identity");
            initials = extras.getString("initials");
            studentno = extras.getString("studentno");
            surname = extras.getString("surname");
            res = extras.getString("res");
            year = extras.getString("year");
        }

        byte[] byteArray = getIntent().getByteArrayExtra("capturedImage");
        if (byteArray != null && byteArray.length > 0) {
            Bitmap capturedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            virtualCardImageView.setImageBitmap(capturedImage);
        } else {
            Toast.makeText(VirtualCardActivity.this, "NO IMAGE!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VirtualCardActivity.this, CardActivity.class);
            startActivity(intent);
        }
    }
}
