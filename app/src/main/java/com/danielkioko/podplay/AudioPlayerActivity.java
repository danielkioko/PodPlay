package com.danielkioko.podplay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AudioPlayerActivity extends AppCompatActivity {

    String audioID = null;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener listener;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    ImageView coverImage;
    TextView theLabel;
    Button play, pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        if (listener == null) {
            Intent intent = new Intent(AudioPlayerActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
        }

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("allAudio");

        audioID = getIntent().getExtras().getString("audioID");

//        coverImage = findViewById(R.id.cover);
        theLabel = findViewById(R.id.audioLabel);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String cover = (String) dataSnapshot.child("cover").getValue();
                String label = (String) dataSnapshot.child("label").getValue();
                int audio = (int) dataSnapshot.child("audio").getValue();

                theLabel.setText(label);
//                Picasso.with(AudioPlayerActivity.this).load(cover).into(coverImage);
                final MediaPlayer mp = MediaPlayer.create(AudioPlayerActivity.this, audio);
                ///AUDIO REFERENCE

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mp.start();
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mp.pause();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
