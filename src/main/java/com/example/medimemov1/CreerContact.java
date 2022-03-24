package com.example.medimemov1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreerContact extends AppCompatActivity {

    private ImageButton aSauvegarder, aAnnuler;
    private EditText aNom, aPrenom, aTelp, aTelg, aNotes;
    private String aSNom, aSPrenom, aSTelp, aSTelg, aSNotes, aFileName;
    private String aDebut = "Contact : ";
    private String aSepare = "Wiedersehen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_contact);

        this.aNom = findViewById(R.id.nom);
        this.aPrenom = findViewById(R.id.prenom);
        this.aTelp = findViewById(R.id.numpers);
        this.aTelg = findViewById(R.id.numg);
        this.aNotes = findViewById(R.id.notes);
        this.aSauvegarder = findViewById(R.id.enregistrer);
        this.aAnnuler = findViewById(R.id.annuler);

        aSauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aFileName = aDebut + aNom.getText().toString()+ " " + aPrenom.getText().toString();
                if (aNom.getText().toString().contentEquals("")) {
                    aNom.setError("Un nom est requis.");
                    return;
                }else if(aPrenom.getText().toString().contentEquals("")){
                    aPrenom.setError("Un prénom est requis.");
                }

                boolean existe = false;
                String[] files = fileList();
                for (String file : files) {
                    if (file.equals(aFileName)) {
                        existe = true;
                        Toast.makeText(getApplicationContext(), "Ce contact existe déjà.", Toast.LENGTH_SHORT).show();
                    }
                }

                aSNom = aNom.getText().toString();
                aSPrenom = aPrenom.getText().toString();
                aSTelp = aTelp.getText().toString();
                aSTelg = aTelg.getText().toString();
                aSNotes = aNotes.getText().toString();

                if (!existe) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(aFileName, Context.MODE_PRIVATE);
                        fos.write(aSNom.getBytes());
                        fos.write(aSepare.getBytes());
                        fos.write(aSPrenom.getBytes());
                        fos.write(aSepare.getBytes());
                        fos.write(aSTelp.getBytes());
                        fos.write(aSepare.getBytes());
                        fos.write(aSTelg.getBytes());
                        fos.write(aSepare.getBytes());
                        fos.write(aSNotes.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                Toast.makeText(getApplicationContext(), "Le contact a été créé.", Toast.LENGTH_SHORT).show();
                                fos.close();
                                startActivity(new Intent(getApplicationContext(), Annuaire.class));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void goMenu(View view){
        startActivity(new Intent(getApplicationContext(),Menu.class));
        finish();
    }

    public void goCompte(View view){
        if(ConnexionInternet.isConnectedInternet(this)) {
            startActivity(new Intent(getApplicationContext(), MonCompte.class));
            finish();
        }else{
            Toast.makeText(this,"Une connexion est requise pour accéder à cette page.",Toast.LENGTH_SHORT).show();
        }
    }

    public void goContact(View view){
        startActivity(new Intent(getApplicationContext(),Annuaire.class));
        finish();
    }
}