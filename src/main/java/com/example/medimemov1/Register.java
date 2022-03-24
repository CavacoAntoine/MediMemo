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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mName, mSurname, mEmail, mPassword, mPassword2;
    ImageButton mRegisterButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.editTextPersonName);
        mSurname = findViewById(R.id.editTextPersonSurname);
        mEmail = findViewById(R.id.editTextMail);
        mPassword = findViewById(R.id.editTextMDP);
        mPassword2 = findViewById(R.id.editTextMDP2);
        mRegisterButton = findViewById(R.id.imageButton1);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String surname = mSurname.getText().toString();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password2 = mPassword2.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    mName.setError("Un nom est requis.");
                    return;
                }else if(TextUtils.isEmpty(surname)){
                    mSurname.setError("Un prénom est requis.");
                    return;
                }else if(TextUtils.isEmpty(email)){
                    mEmail.setError("Un email est requis.");
                    return;
                }else if(!email.contains("@")){
                    mEmail.setError("Doit être de la forme : example@exemple.com");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Un mot de passe est requis.");
                    return;
                }else if(password.length()<=6){
                    mPassword.setError("Le mot de passe doit être supérieur à 6 caractères");
                    return;
                }else if(!password.equals(password2)){
                    mPassword.setError("Les mots de passe doivent être identiques");
                    mPassword2.setError("Les mots de passe doivent être identiques");
                }else {
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Register.this,"Email de vérification envoyé !",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG","Email de vérification non envoyé"+ e.getMessage());
                                    }
                                });
                                Toast.makeText(Register.this, "Votre compte a bien été créé", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Personnes").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("Nom",name);
                                user.put("Prenom", surname);
                                user.put("Tel","");
                                user.put("Tel garde","");
                                user.put("Poste","");
                                documentReference.set(user);
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            } else {
                                Toast.makeText(Register.this, "Votre compte n'a pas pu être créé", Toast.LENGTH_SHORT).show();
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

    public void retourLogIn(View view){
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}