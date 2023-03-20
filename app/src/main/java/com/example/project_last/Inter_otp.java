package com.example.project_last;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Inter_otp extends AppCompatActivity {
    EditText otpn1, otpn2, otpn3, otpn4, otpn5, otpn6;
    TextView Nnumber, resend;
    Button submit;
    String getotp, tName, tEmail, number, tpassword;
    FirebaseAuth mAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_otp);
        Nnumber = findViewById(R.id.number_show);
        otpn1 = findViewById(R.id.otp1);
        otpn2 = findViewById(R.id.otp2);
        otpn3 = findViewById(R.id.otp3);
        otpn4 = findViewById(R.id.otp4);
        otpn5 = findViewById(R.id.otp5);
        otpn6 = findViewById(R.id.otp6);
        mAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.submit);
        Nnumber.setText(String.format("+880%s", getIntent().getStringExtra("Number")));

        number = getIntent().getStringExtra("Number");
        getotp = getIntent().getStringExtra("fullotp");

        submit.setOnClickListener(v -> {

            if (!otpn1.getText().toString().trim().isEmpty() && !otpn2.getText().toString().trim().isEmpty() && !otpn3.getText().toString().trim().isEmpty() && !otpn4.getText().toString().trim().isEmpty() && !otpn5.getText().toString().trim().isEmpty() && !otpn6.getText().toString().trim().isEmpty()) {
                String fotp = otpn1.getText().toString() + otpn2.getText().toString() + otpn3.getText().toString() + otpn4.getText().toString() +
                        otpn5.getText().toString() + otpn6.getText().toString();

                if (getotp != null) {
                    //submit.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getotp, fotp);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String id = firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("User").child(id);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Intent intent=new Intent(getApplicationContext(),Home_page.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else {
                                        Intent intent=new Intent(getApplicationContext(),Register_page.class);
                                        intent.putExtra("Number",Nnumber.getText().toString());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    });
                }
            } else {
                Toast.makeText(Inter_otp.this, "Please enter OTP number", Toast.LENGTH_SHORT).show();
            }
        });
        otpnumbermove();

        resend = findViewById(R.id.resend);
        resend.setOnClickListener(v -> PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880" + getIntent().getStringExtra("number"),
                Long.parseLong("60"),
                TimeUnit.SECONDS,
                Inter_otp.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(Inter_otp.this, "Error check your network", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newfulotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //super.onCodeSent(fulotp, forceResendingToken);
                        getotp = newfulotp;

                    }
                }

        ));

    }



    private void otpnumbermove() {

        otpn1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    otpn2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpn2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    otpn3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpn3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    otpn4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpn4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    otpn5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpn5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    otpn6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}