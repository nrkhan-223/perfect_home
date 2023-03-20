package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;


public class Inter_Number extends AppCompatActivity {
    Button otp;
    EditText number;
    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                Intent intent = new Intent(Inter_Number.this, Register_page.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onStart() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_number);

        number = findViewById(R.id.mobile_number);
        otp = findViewById(R.id.otp_btn);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!number.getText().toString().trim().isEmpty()) {
                    if ((number.getText().toString().trim()).length() == 10) {
                        otp.setVisibility(View.INVISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+880" + number.getText().toString(),
                                Long.parseLong("60"),
                                TimeUnit.SECONDS,
                                Inter_Number.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        otp.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        otp.setVisibility(View.VISIBLE);
                                        Toast.makeText(Inter_Number.this, "Error check your network", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String fulotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        //super.onCodeSent(fulotp, forceResendingToken);
                                        otp.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(getApplicationContext(), Inter_otp.class);
                                        intent.putExtra("Number", number.getText().toString());
                                        intent.putExtra("fullotp",fulotp);
                                        startActivity(intent);

                                    }
                                }

                        );


                    } else {
                        Toast.makeText(Inter_Number.this, "please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Inter_Number.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}