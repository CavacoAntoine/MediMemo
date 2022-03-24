package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medimemov1.adapter.ItemGroupeAdapter;
import com.example.medimemov1.models.ItemGroupes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    FirebaseAuth fAuth;
    private ListView listGroupe;
    private ItemGroupeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        this.listGroupe = findViewById(R.id.listGroupe);

        if(!user.isEmailVerified()){
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Menu.this,"Un email de vérification vous a été envoyé merci de vérifier votre compte.",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("tag","Email de vérification non envoyé"+ e.getMessage());
                }
            });
            fAuth.signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        List<ItemGroupes> items = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Groupes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        items.add(new ItemGroupes(document.getString("Nom")));
                    }
                    adapter = new ItemGroupeAdapter(Menu.this, items);
                    listGroupe.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void goCompte(View view){
        if(ConnexionInternet.isConnectedInternet(this)) {
            startActivity(new Intent(getApplicationContext(), MonCompte.class));
            finish();
        }else{
            Toast.makeText(this,"Une connexion est requise pour accéder à cette page.",Toast.LENGTH_SHORT).show();
        }
    }

    public void goAnnuaire(View view){
        startActivity(new Intent(getApplicationContext(),Annuaire.class));
        finish();
    }

    public void goProcedures(View view){
        startActivity(new Intent(getApplicationContext(),Procedures.class));
        finish();
    }

    public void goMedicaments(View view){
        startActivity(new Intent(getApplicationContext(),Medicament.class));
        finish();
    }

    public void goGroupes(View view){
        startActivity(new Intent(getApplicationContext(),Groupe.class));
        finish();
    }
}