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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();


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
