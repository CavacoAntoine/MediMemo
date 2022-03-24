package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfil extends AppCompatActivity {

    private EditText aName, aSurname, aMail, aTel, aTelg , aProf;
    private ImageButton aSave, aAnnuler;
    private FirebaseAuth afAuth;
    private FirebaseFirestore afStore;
    private FirebaseUser aUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        Intent vData = getIntent();
        String vNom = vData.getStringExtra("nom");
        String vPrenom = vData.getStringExtra("prenom");
        String vMail = vData.getStringExtra("mail");
        String vTel = vData.getStringExtra("tel");
        String vTelg = vData.getStringExtra("telg");
        String vProf = vData.getStringExtra("prof");

        Log.d("TAG", "onCreate: "+ vNom + " " + vPrenom + " " + vMail + " " + vTel);

        this.afAuth = FirebaseAuth.getInstance();
        this.afStore = FirebaseFirestore.getInstance();
        this.aUser = afAuth.getCurrentUser();
        this.aSave = findViewById(R.id.save);
        this.aAnnuler = findViewById(R.id.annuler);
        this.aName = findViewById(R.id.profilName);
        this.aSurname = findViewById(R.id.profilSurname);
        this.aMail = findViewById(R.id.profilMail);
        this.aTel = findViewById(R.id.profilNumPers);
        this.aTelg = findViewById(R.id.profilNumGarde);
        this.aProf = findViewById(R.id.profilProf);

        this.aName.setText(vNom);
        this.aSurname.setText(vPrenom);
        this.aMail.setText(vMail);
        this.aTel.setText(vTel);
        this.aTelg.setText(vTelg);
        this.aProf.setText(vProf);

        this.aAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MonCompte.class));
                finish();
            }
        });

        this.aSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vName = aName.getText().toString();
                String vSurname = aSurname.getText().toString();
                String vMail = aMail.getText().toString().trim();
                String vTel = aTel.getText().toString().trim();
                String vTelg = aTelg.getText().toString().trim();
                String vProf = aProf.getText().toString().trim();

                if (vName.isEmpty()){
                    aName.setError("Un nom est requis.");
                    return;
                }else if(vSurname.isEmpty()){
                    aSurname.setError("Un prénom est requis.");
                    return;
                }else if(vMail.isEmpty()){
                    aMail.setError("Un email est requis.");
                    return;
                }else{
                    aUser.updateEmail(vMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DocumentReference vDocRef = afStore.collection("Personnes").document(aUser.getUid());
                            Map<String,Object> vEdit = new HashMap<>();
                            vEdit.put("Nom",vName);
                            vEdit.put("Prenom",vPrenom);
                            vEdit.put("Tel",vTel);
                            vEdit.put("Tel garde", vTelg);
                            vEdit.put("Poste",vProf);
                            vDocRef.update(vEdit).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfil.this, "Changement effectué",Toast.LENGTH_SHORT);
                                    startActivity(new Intent(getApplicationContext(),MonCompte.class));
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfil.this,e.getMessage(), Toast.LENGTH_SHORT).show();
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

}