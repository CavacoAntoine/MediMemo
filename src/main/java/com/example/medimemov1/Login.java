package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    ImageButton mLoginButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.editTextMail);
        mPassword = findViewById(R.id.editTextMotDePasseL);
        mLoginButton = findViewById(R.id.imageButtonL);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        if(fAuth.getCurrentUser() != null && user.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),Menu.class));
            finish();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Un email est requis.");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Un mot de passe est requis.");
                    return;
                }else{
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this, "Vous êtes connecté !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Menu.class));
                            }else {
                                Toast.makeText(Login.this, "Connexion échoué : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void creerCompte(View view){
        startActivity(new Intent(getApplicationContext(),Register.class));
        finish();
    }

    public void mdpOublie(View view){
        startActivity(new Intent(getApplicationContext(),MdpOublie.class));
        finish();
    }
}