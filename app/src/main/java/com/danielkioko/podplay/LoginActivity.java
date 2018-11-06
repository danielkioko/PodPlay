package com.danielkioko.podplay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseUser == null) {

        } else {
            Intent toClass = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(toClass);
//                    toClass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    toClass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

//        firebaseAuth.addAuthStateListener(listener);
//        listener = new FirebaseAuth.AuthStateListener(){
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser()==null){
//                    Toast.makeText(getApplicationContext(), "Please Login To Continue", Toast.LENGTH_LONG).show();
//                } else {
//                    Intent toClass = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(toClass);
//                    toClass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    toClass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                }
//            }
//        };

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();

                if (!strEmail.isEmpty() && !strPassword.isEmpty()) {
                    signIn(strEmail, strPassword);
                } else {
                    Snackbar.make(view, "Fill In All The Fields!", 15).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginDirect();
    }

    private void loginDirect() {
        String defaultEmail = "new@new.com";
        String defaultPassword = "password";

        firebaseAuth.signInWithEmailAndPassword(defaultEmail, defaultPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent toClass = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toClass);
                    toClass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toClass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signIn(String strEmail, String strPassword) {
        firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent toClass = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toClass);
                    toClass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toClass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
