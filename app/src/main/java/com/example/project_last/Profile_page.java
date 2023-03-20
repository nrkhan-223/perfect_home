package com.example.project_last;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_last.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Profile_page extends AppCompatActivity {
    TextView fullname_field, profile_phone;
    EditText profile_email;
    TextInputEditText full_name_profile;
    ImageView profile_pic;
    Button pic_button,update_button;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private Uri imageUri = null;
    private static final int IMAGE_REQUEST = 1;
    private static final int CAMERA_CODE = 200;
    private static final int GALLERY_CODE = 200;
    private static final int GALLERY_CODE1 = 300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;
    String myID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        fullname_field = (TextView) findViewById(R.id.fullname_field);

        full_name_profile = (TextInputEditText) findViewById(R.id.full_name_profile);
        profile_email =  findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        pic_button=findViewById(R.id.profilepic_button);
        update_button=findViewById(R.id.profileupdate_button);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String myid=firebaseUser.getUid();

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Profile_page.this);
                View v= LayoutInflater.from(Profile_page.this).inflate(R.layout.profile_img_view,null,true);
                builder.setView(v);
                AlertDialog alertDialog=builder.show();
                ImageView imageView=v.findViewById(R.id.profile_img_id);
                reference= FirebaseDatabase.getInstance().getReference("User").child(myid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Glide.with(v).load(user.getImageURL()).into(imageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });



        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                String myid=firebaseUser.getUid();
                String name=full_name_profile.getText().toString();
                String email=profile_email.getText().toString();

                submitData(myid,name,email);
            }
        });

        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("User").child(myid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.drawable.demo_pic);
                }else {
                    Glide.with(Profile_page.this).load(user.getImageURL()).into(profile_pic);
                }

                fullname_field.setText(user.getName());
                full_name_profile.setText(user.getName());
                profile_email.setText(user.getEmail());
                profile_phone.setText(user.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void openImage() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_page.this);
        builder.setTitle("Choose an options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    openCamera();
                }
                if (i == 1) {
                    openGallery();
                }
            }

        });

        builder.create().show();
    }

    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK &&data!=null && data.getData()!=null ) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(profile_pic);
        }else if (requestCode==CAMERA_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(profile_pic);
        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void submitData(String myid, String name, String email) {
        if (imageUri==null){
            reference= FirebaseDatabase.getInstance().getReference("User").child(myid);
            HashMap<String,Object> hashMap=new HashMap<>();

            hashMap.put("name",name);
            hashMap.put("email",email);
            hashMap.put("imageURL","default");

            reference.updateChildren(hashMap);

            Toast.makeText(Profile_page.this,"Submit", Toast.LENGTH_SHORT).show();
        }else {
            storageReference = FirebaseStorage.getInstance().getReference("User");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();

                            reference=FirebaseDatabase.getInstance().getReference("User").child(myid);
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("name",name);
                            hashMap.put("email",email);
                            hashMap.put("imageURL",imageUri);

                            reference.updateChildren(hashMap);

                            Toast.makeText(Profile_page.this,"Submit",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile_page.this,Home_page.class);
        startActivity(intent);
        super.onBackPressed();
    }
}