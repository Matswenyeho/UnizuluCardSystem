package com.example.unizulucardsystem;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class LoginActivity extends AppCompatActivity {
    private EditText user_name, pass_word;
    TextView registerRedirected;
    FirebaseAuth auth;
    Button loginbtn, forgotpswdbtn;
    ProgressBar progressBar;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name = findViewById(R.id.editTextemail);
        pass_word = findViewById(R.id.editTextpassword_login);
        progressBar = findViewById(R.id.progressBar);
        registerRedirected = findViewById(R.id.registrationRedirectText);
        loginbtn = findViewById(R.id.loginbtn);
        forgotpswdbtn = findViewById(R.id.forgotpswdbtn);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        forgotpswdbtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        showHidePassword();

        loginbtn.setOnClickListener(v -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            if (email.isEmpty()) {
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_name.setError("Enter a valid email");
                user_name.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                pass_word.setError("Password is empty");
                pass_word.requestFocus();
                return;
            }
            if (password.length() < 6) {
                pass_word.setError("Password must be at least 6 characters");
                pass_word.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = auth.getCurrentUser().getUid();
                    checkUserAccessLevel(uid);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful, Check Your login Credentials", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        registerRedirected.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fstore.collection("users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());
            String Admin = documentSnapshot.getString("isAdmin");
            String Student = documentSnapshot.getString("isStudent");
            String Employee = documentSnapshot.getString("isEmployee");

            if (Admin!=null) {
                Log.d("TAG", "User is an Admin");
                startActivity(new Intent(LoginActivity.this, VirtualCardActivity.class));
                finish();
            } else if (Student != null || Employee != null) {
                Log.d("TAG", "User is a Student or Employee");
                startActivity(new Intent(LoginActivity.this, CardActivity.class));
                finish();
            } else {
                Log.d("TAG", "User type not found or is not set correctly.");
            }
        });
    }

    private void showHidePassword() {
        ImageView imageViewShowHidepswd = findViewById(R.id.hideshowpswrd);
        imageViewShowHidepswd.setImageResource(R.drawable.show_pswd);
        imageViewShowHidepswd.setOnClickListener(v -> {
            if (pass_word.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                pass_word.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidepswd.setImageResource(R.drawable.hide_pswd);
            } else {
                pass_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidepswd.setImageResource(R.drawable.show_pswd);
            }
        });
    }
}
