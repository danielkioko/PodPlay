package com.danielkioko.podplay;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadAudioActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dbUsers;
    FirebaseUser currentUser;

    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;

    ImageView cover;
    TextView label;
    Button newAudio, btnUpload;
    int audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        firebaseAuth = FirebaseAuth.getInstance();

        cover = findViewById(R.id.imgSelectCover);
        label = findViewById(R.id.etLabel);
        btnUpload = findViewById(R.id.uploadFiles);

        databaseReference = firebaseDatabase.getReference().child("Podcasts");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        dbUsers = FirebaseDatabase.getInstance().getReference().child("").child(currentUser.getUid());

        newAudio = findViewById(R.id.btnSelectAudio);
        newAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String newLabel = label.getText().toString().trim();

                if (!newLabel.isEmpty()) {

                    StorageReference storageReference = storage.getReference().child("");
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final Uri downloadUrl = uri;
                            final DatabaseReference newPodcast = databaseReference.push();

                            dbUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    newPodcast.child("cover").setValue(downloadUrl.toString());
                                    newPodcast.child("label").setValue(newLabel);
                                    newPodcast.child("uid").setValue(currentUser.getUid());

                                    Intent intent = new Intent(UploadAudioActivity.this, MainActivity.class);
                                    startActivity(intent);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });

                } else {
                    Snackbar.make(view, "Error!", 10).show();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();
            cover.setImageURI(uri);
        }
        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
            }
        }
    }
}
