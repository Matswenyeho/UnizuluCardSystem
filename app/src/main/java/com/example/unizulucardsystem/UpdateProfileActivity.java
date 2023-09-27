package com.example.unizulucardsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText editTextFirstname, editTextSurname, editTextEmail, editTextPhone, editTextTittle, editTextIdentity, editTextInitials, editTextStudentno, editTextRes, editTextYear;
    TextView Firstname, Surname, Phone, Tittle, Identity, Initials, Studentno, Res, Year;
    private String firstname, surname, phone, tittle, identity, initials, studentno, res, year;
    ProgressBar progressBar;
    Button Update;
    FirebaseFirestore firestore;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        editTextFirstname = findViewById(R.id.editTextupdatefirstname);
        editTextPhone = findViewById(R.id.editTextupdatephone);
        editTextTittle = findViewById(R.id.editTextupdateTittle);
        editTextIdentity = findViewById(R.id.editTextupdateIdentity);
        editTextInitials = findViewById(R.id.editTextupdateinitials);
        editTextStudentno = findViewById(R.id.editTextupdatestudentno);
        editTextSurname = findViewById(R.id.editTextupdatesurname);
        editTextRes = findViewById(R.id.editTextupdateres);
        progressBar = findViewById(R.id.progressBar);
        editTextYear = findViewById(R.id.editTextupdateyear);
        Update = findViewById(R.id.updatebtn);

        Update.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProfileActivity.this, CardActivity.class);
            intent.putExtra("tittle", tittle);
            intent.putExtra("identity", identity);
            intent.putExtra("initials", initials);
            intent.putExtra("studentno", studentno);
            intent.putExtra("surname", surname);
            intent.putExtra("res", res);
            intent.putExtra("year", year);

            startActivity(intent);
        });


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(UpdateProfileActivity.this, "User details are not available!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
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
                    firstname = firebaseUser.getDisplayName();
                    firstname = document.getString("firstname");
                    surname = document.getString("surname");
                    phone = document.getString("phone");
                    tittle = document.getString("tittle");
                    identity = document.getString("identity");
                    initials = document.getString("initials");
                    studentno = document.getString("studentno");
                    res = document.getString("res");
                    year = document.getString("year");


                    editTextFirstname.setText(firstname);
                    editTextSurname.setText(surname);
                    editTextPhone.setText(phone);
                    editTextTittle.setText(tittle);
                    editTextIdentity.setText(identity);
                    editTextInitials.setText(initials);
                    editTextStudentno.setText(studentno);
                    editTextRes.setText(res);
                    editTextYear.setText(year);
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

        });
    }
}
