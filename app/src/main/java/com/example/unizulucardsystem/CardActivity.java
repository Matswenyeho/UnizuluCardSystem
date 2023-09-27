package com.example.unizulucardsystem;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class CardActivity extends AppCompatActivity {
    TextView Surname, Tittle, Identity, Initials, Studentno, Res, Year, Message;
    private String surname, tittle, identity, initials, studentno, res, year;
    ImageView imageView;
    Button Upload, Save;
    FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private static final int CAMERA_PERMISSION_REQUEST = 101;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String EXTRA_TITLE = "tittle";
    private static final String EXTRA_IDENTITY = "identity";
    private static final String EXTRA_INITIALS = "initials";
    private static final String EXTRA_STUDENT_NO = "studentno";
    private static final String EXTRA_SURNAME = "surname";
    private static final String EXTRA_RES = "res";
    private static final String EXTRA_YEAR = "year";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Tittle = findViewById(R.id.display_tittle);
        Surname = findViewById(R.id.display_surname);
        Identity = findViewById(R.id.display_identity);
        Initials = findViewById(R.id.display_initials);
        Studentno = findViewById(R.id.display_studentno);
        Res = findViewById(R.id.display_res);
        Message = findViewById(R.id.applicant);
        imageView = findViewById(R.id.noProfile);
        Year = findViewById(R.id.display_year);
        Message = findViewById(R.id.applicant);
        Upload = findViewById(R.id.uploadbtn);
        Save = findViewById(R.id.Savebtn);

        Upload.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(CardActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CardActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            } else {
                openCamera();
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(CardActivity.this, "User details are not available!", Toast.LENGTH_SHORT).show();
        } else {
            showProfileActivity(firebaseUser);
        }
    }

    private void showProfileActivity(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    surname = document.getString("surname");
                    tittle = document.getString("tittle");
                    identity = document.getString("identity");
                    initials = document.getString("initials");
                    studentno = document.getString("studentno");
                    res = document.getString("res");
                    year = document.getString("year");

                    Message.setText(tittle + " " + initials + " " + surname +"'s" +" " + "card");
                    Initials.setText(initials);
                    Surname.setText(surname);
                    Tittle.setText(tittle);
                    Identity.setText("ID NO:" + identity);
                    Initials.setText(initials);
                    Studentno.setText("Student No:" + " " + studentno);
                    Res.setText(res);
                    Year.setText("Student" + " " + year);
                } else {
                    Toast.makeText(CardActivity.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void openCamera() {
        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

                    byte[] byteArray = bitmapToByteArray(photo);

                    Intent intent = new Intent(CardActivity.this, VirtualCardActivity.class);
                    intent.putExtra("capturedImage", byteArray);
                    intent.putExtra(EXTRA_TITLE, tittle);
                    intent.putExtra(EXTRA_IDENTITY, identity);
                    intent.putExtra(EXTRA_INITIALS, initials);
                    intent.putExtra(EXTRA_STUDENT_NO, studentno);
                    intent.putExtra(EXTRA_SURNAME, surname);
                    intent.putExtra(EXTRA_RES, res);
                    intent.putExtra(EXTRA_YEAR, year);
                    startActivity(intent);
                }


            }


    private byte[] bitmapToByteArray(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_update) {
            Intent intent = new Intent(CardActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_changepswd) {
            Intent intent = new Intent(CardActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            auth.signOut();
            Toast.makeText(CardActivity.this,"logout",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        else {
            Toast.makeText(CardActivity.this, "Something wen wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}

