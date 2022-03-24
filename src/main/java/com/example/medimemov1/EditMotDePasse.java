package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditMotDePasse extends AppCompatActivity {

    private EditText aNouveau, aConfirm;
    private ImageButton aSave, aAnnuler;
    private FirebaseAuth afAuth;
    private FirebaseUser aUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mot_de_passe);

        this.aNouveau = findViewById(R.id.new_mdp);
        this.aConfirm = findViewById(R.id.confirm_mdp);
        this.aSave = findViewById(R.id.save);
        this.aAnnuler = findViewById(R.id.annuler);
        this.afAuth = FirebaseAuth.getInstance();
        this.aUser = afAuth.getCurrentUser();

        aSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vNouveau = aNouveau.getText().toString().trim();
                String vConfirm = aConfirm.getText().toString().trim();

                if(vNouveau.isEmpty()){
                    aNouveau.setError("Un mot de passe est requis.");
                    return;
                }else if(vConfirm.isEmpty()){
                    aConfirm.setError("Vous devez confirmer le mot de passe.");
                }else if(!vNouveau.equals(vConfirm)){
                    aConfirm.setError("Les mots de passe doivent être identiques.");
                }else{
                    aUser.updatePassword(vConfirm).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditMotDePasse.this, "Changement effectué",Toast.LENGTH_SHORT);
                            startActivity(new Intent(getApplicationContext(),MonCompte.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditMotDePasse.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        aAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MonCompte.class));
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}