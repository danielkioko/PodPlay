package com.danielkioko.podplay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.isSmoothScrollbarEnabled();
//
//        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Podcasts");
        firebaseAuth.addAuthStateListener(authStateListener);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };

        FirebaseRecyclerAdapter <Item, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter <Item, ItemViewHolder>(
                Item.class,
                R.layout.item_card,
                ItemViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Item model, int position) {

                databaseReference.child("").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        viewHolder.setCover(MainActivity.this, model.getCover());
                        viewHolder.setLabel(model.getLabel());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
