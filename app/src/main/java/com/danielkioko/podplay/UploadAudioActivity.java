package com.danielkioko.podplay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class UploadAudioActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    FirebaseAuth firebaseAuth;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri imgUri = null;

    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri audioUri;

    ImageView cover;
    TextView label;
    Button newAudio, btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        firebaseAuth = FirebaseAuth.getInstance();

        cover = findViewById(R.id.imgSelectCover);
        label = findViewById(R.id.etLabel);
        btnUpload = findViewById(R.id.uploadFiles);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Audio");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (audioUri != null && imgUri != null) {
                    final StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("audio/mpeg")
                            .build();

                    final ProgressDialog progressDialog = new ProgressDialog(UploadAudioActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    final DatabaseReference newAudio = databaseRef.push();
                    final StorageReference ref = storageReference.child(label+"/audioFiles/"+UUID.randomUUID().toString());

                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri uri1 = uri;
                                    mDatabaseUsers.child("audioCover").setValue(uri1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Cover Uploaded", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    });

                    ref.putFile(audioUri,metadata)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri url = uri;
                                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    newAudio.child("audioLink").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Audio Uploaded", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    });
                                    Toast.makeText(UploadAudioActivity.this, "All Done!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadAudioActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                } else  {
                    Toast.makeText(getApplicationContext(), "Choose A File First", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                audioUri = data.getData();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            imgUri = data.getData();
            cover.setImageURI(imgUri);
        }
    }
}
