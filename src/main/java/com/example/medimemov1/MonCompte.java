package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MonCompte extends AppCompatActivity {

    private TextView mMail, mName, mSurname, mTel, mTelg, mProf;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private ImageButton popupBTBN;
    private Dialog mDialog;
    private MonCompte activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);

        this.activity = this;

        fAuth = FirebaseAuth.getInstance();
        mMail = findViewById(R.id.profilMail);
        mName = findViewById(R.id.profilNom);
        mSurname = findViewById(R.id.profilPrenom);
        mTel = findViewById(R.id.profilNumPers);
        mTelg = findViewById(R.id.profilNumGarde);
        mProf = findViewById(R.id.profilProf);


        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        mMail.setText(fAuth.getCurrentUser().getEmail());
        DocumentReference documentReference = fStore.collection("Personnes").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                mName.setText(documentSnapshot.getString("Nom"));
                mSurname.setText(documentSnapshot.getString("Prenom"));
                mTel.setText(documentSnapshot.getString("Tel"));
                mTelg.setText(documentSnapshot.getString("Tel garde"));
                mProf.setText(documentSnapshot.getString("Poste"));
                return;
            }
        });

        //Popup
        popupBTBN = findViewById(R.id.popupBTN);
        popupBTBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Popup popup = new Popup(activity);
                popup.setTitle("Mon compte");
                popup.getUn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),EditMotDePasse.class));
                    }
                });
                popup.getDeux().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(),EditProfil.class);
                        i.putExtra("nom",mName.getText().toString());
                        i.putExtra("prenom",mSurname.getText().toString());
                        i.putExtra("mail",mMail.getText().toString());
                        i.putExtra("tel",mTel.getText().toString());
                        i.putExtra("telg",mTelg.getText().toString());
                        i.putExtra("prof",mProf.getText().toString());
                        startActivity(i);
                    }
                });
                popup.getTrois().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),Parametres.class));
                    }
                });
                popup.getQuatre().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fStore.terminate();
                        fAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                });
                popup.build();
            }
        });
        //Fin popup

        //Initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        //bottomNavigationView.setSelectedItemId(R.id.medicament);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.telephone:
                        startActivity(new Intent(getApplicationContext(),
                                Annuaire.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.medicament:
                        startActivity(new Intent(getApplicationContext(),
                                Medicament.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.procedures:
                        startActivity(new Intent(getApplicationContext(),
                                Procedures.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.groupes:
                        startActivity(new Intent(getApplicationContext(),
                                Groupe.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void goMenu(View view){
        startActivity(new Intent(getApplicationContext(),Menu.class));
        finish();
    }
}