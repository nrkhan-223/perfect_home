package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_page extends AppCompatActivity {
    private ProgressDialog progressDialog;


    @Override
    public void onStart() {
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
                Intent intent = new Intent(Login_page.this, Home_page.class);
                startActivity(intent);
                finish();
        }
        super.onStart();
    }


    EditText login_email, login_password;
    TextView no_account;
    private Button login_button;

    FirebaseUser firebaseUser;
    FirebaseAuth reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        no_account = findViewById(R.id.no_account);

        mAuth = FirebaseAuth.getInstance();

        no_account.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register_page.class);
            startActivity(intent);
        });


        login_button.setOnClickListener(v -> {

            String email = login_email.getText().toString();
            String password = login_password.getText().toString();

            if (email.isEmpty()) {
                login_email.setError("Enter an email address");
                login_email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                login_email.setError("Enter a valid email address");
                login_email.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                login_password.setError("Enter a password");
                login_password.requestFocus();
            } else {
                progressDialog.show();
                signInUser(email, password);
            }
        });


    }

    private void signInUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                finish();
                progressDialog.dismiss();
                Intent intent = new Intent(Login_page.this, Home_page.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(Login_page.this, "not sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }


}