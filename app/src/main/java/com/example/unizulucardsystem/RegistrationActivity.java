package com.example.unizulucardsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    Button registerbtn;
    EditText email, password, firstname, surname, phone, tittle, identity, initials, studentno, res, year;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    String userId;
    CheckBox Student, Employee, Admin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.editTextemail);
        firstname = findViewById(R.id.editTextfirstname);
        surname = findViewById(R.id.editTextsurname);
        tittle = findViewById(R.id.editTextTittle);
        identity = findViewById(R.id.editTextIdentity);
        initials = findViewById(R.id.editTextinitials);
        studentno = findViewById(R.id.editTextstudentno);
        res = findViewById(R.id.editTextres);
        year = findViewById(R.id.editTextyear);
        password = findViewById(R.id.editTextpassword);
        phone = findViewById(R.id.editTextphone);
        registerbtn = findViewById(R.id.registerbtn);
        progressBar = findViewById(R.id.progressBar);
        Student = findViewById(R.id.student);
        Employee = findViewById(R.id.employee);
        Admin = findViewById(R.id.admin);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        registerbtn.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            String Firstname = firstname.getText().toString();
            String Surname = surname.getText().toString();
            String Tittle = tittle.getText().toString();
            String Identity = identity.getText().toString();
            String Initials = initials.getText().toString();
            String Studentno = studentno.getText().toString();
            String Res = res.getText().toString();
            String Year = year.getText().toString();
            String Phone = phone.getText().toString();


            if (!(Student.isChecked() || Employee.isChecked() || Admin.isChecked())) {
                Toast.makeText(RegistrationActivity.this, "Check user type box!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Firstname.isEmpty()) {
                firstname.setError("name is required");
                firstname.requestFocus();
                return;
            }
            if (Surname.isEmpty()) {
                surname.setError("surname is required");
                surname.requestFocus();
                return;
            }
            if (Tittle.isEmpty()) {
                tittle.setError("tittle is required");
                tittle.requestFocus();
                return;
            }
            if (Initials.isEmpty()) {
                initials.setError("initials is required");
                initials.requestFocus();
                return;
            }
            if (Identity.isEmpty()) {
                identity.setError("id number is required");
                identity.requestFocus();
                return;
            }
            if (identity.length() != 13) {
                identity.setError("Invalid identity number");
                identity.requestFocus();
                return;
            }
            if (Studentno.isEmpty()) {
                studentno.setError("student number is required");
                studentno.requestFocus();
                return;
            }
            if (Studentno.length() != 9) {
                studentno.setError("Student_number musn be 9 charecters");
                studentno.requestFocus();
                return;
            }

            if (Email.isEmpty()) {
                email.setError("Enter an email");
                email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                email.setError("Enter the valid email address");
                email.requestFocus();
                return;
            }
            if (Password.isEmpty()) {
                password.setError("create password");
                password.requestFocus();
                return;
            }
            if (password.length() < 6) {
                password.setError("Weak password!Password should be atleast six charecter ");
                password.requestFocus();
                return;
            }

            if (Res.isEmpty()) {
                res.setError("resident name is required");
                res.requestFocus();
                return;
            }
            if (Phone.isEmpty()) {
                phone.setError("Contact_number is required");
                phone.requestFocus();
                return;
            }
            if (phone.length() != 10) {
                phone.setError("Contact_number should be 10 digits");
                phone.requestFocus();
                return;
            }

            if (Year.isEmpty()) {
                year.setError("year required");
                year.requestFocus();
            }

            progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                    userId = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = firestore.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstname", Firstname);
                    user.put("surname", Surname);
                    user.put("identity", Identity);
                    user.put("initials", Initials);
                    user.put("tittle", Tittle);
                    user.put("year", Year);
                    user.put("studentno", Studentno);
                    user.put("res", Res);
                    user.put("email", Email);
                    user.put("phone", Phone);

                    if (Student.isChecked()) {
                        user.put("isStudent", "Student");
                    } else if (Employee.isChecked()) {
                        user.put("isEmployee", "Employee");
                    } else if (Admin.isChecked()) {
                        user.put("isAdmin", "Admin");
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }
}