package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_last.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Register_page extends AppCompatActivity {

    private ProgressDialog progressDialog;

    EditText reg_name, reg_email;

    TextView phone;
    FirebaseUser firebaseUser;
    private Button reg_button;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    public void onStart() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String id = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("User").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User user=snapshot.getValue(User.class);
                    if (!user.getName().isEmpty()){

                        Intent intent = new Intent(getApplicationContext(), Home_page.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");


        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_email = (EditText) findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_Phone);
        reg_button = (Button) findViewById(R.id.reg_button);
        Intent intent = getIntent();
        String num=getIntent().getStringExtra("Number");
        phone.setText(num);
        mAuth = FirebaseAuth.getInstance();

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_name = reg_name.getText().toString();
                String t_email = reg_email.getText().toString();
                String t_phone = phone.getText().toString();

                if (t_name.isEmpty()) {
                    reg_name.setError("Enter Name");
                    reg_name.requestFocus();
                    return;
                }

                if (t_email.isEmpty()) {
                    reg_email.setError("Enter an email address");
                    reg_email.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(t_email).matches()) {
                    reg_email.setError("Enter a valid email address");
                    reg_email.requestFocus();
                    return;
                }
                 else {
                    progressDialog.show();
                    userregister(t_name, t_email, t_phone);

                }
            }
        });
    }

    private void userregister(String t_name, String t_email, String t_phone) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("User").child(id);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", t_name);
        hashMap.put("email", t_email);
        hashMap.put("phone", t_phone);
        hashMap.put("imageURL", "default");

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Home_page.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register_page.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}