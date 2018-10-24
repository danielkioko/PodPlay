package com.danielkioko.podplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadAudioActivity extends AppCompatActivity {

    ImageView cover;
    TextView label;
    int audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        cover = findViewById(R.id.imgSelectCover);
        label = findViewById(R.id.etLabel);



    }
}
