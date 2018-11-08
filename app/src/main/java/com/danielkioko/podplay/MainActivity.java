package com.danielkioko.podplay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.isSmoothScrollbarEnabled();
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Audio");
        firebaseAuth = FirebaseAuth.getInstance();

        floatingActionButton = findViewById(R.id.addNew);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadAudioActivity.class);
                startActivity(intent);
            }
        });

        FirebaseRecyclerAdapter<Item, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder> (
                Item.class,
                R.layout.item_card,
                ItemViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, int position) {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.setLabel(model.getLabel());
                viewHolder.setCover(MainActivity.this, model.getCover());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AudioPlayerActivity.class);
                        //intent.putExtra("postID", post_key);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
