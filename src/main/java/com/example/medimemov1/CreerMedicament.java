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
import java.io.IOException;;

public class CreerMedicament extends AppCompatActivity{

    ImageButton sauvegarder;
    ImageButton annuler;
    private EditText aNom, aFam, aPosologie, aIndications, aContreI, aEffets, aProtocole, aSurveillance, aPresentation;
    String FILENAME, nom, fam, posologie, indications, contreind, effets, protocole, surveillance, presentation;
    String debut = "Medicament : ";
    String SEPARE = "Wiedersehen";
    private boolean v = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_medicament);

        aNom = findViewById(R.id.nom);
        aFam = findViewById(R.id.famille);
        aPosologie = findViewById(R.id.posologie);
        aIndications = findViewById(R.id.indications);
        aContreI = findViewById(R.id.contreindication);
        aEffets = findViewById(R.id.effets);
        aProtocole = findViewById(R.id.protocole);
        aSurveillance = findViewById(R.id.surveillance);
        aPresentation = findViewById(R.id.presentation);

        sauvegarder = findViewById(R.id.enregistrer);
        sauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FILENAME = debut + aNom.getText().toString();
                if (FILENAME.contentEquals("")) {
                    aNom.setError("Un nom est requis.");
                    return;
                }

                boolean existe = false;
                String[] files = fileList();
                for (String file : files) {
                    if (file.equals(FILENAME)) {
                        existe = true;
                        Toast.makeText(getApplicationContext(), "Ce médicament existe déjà.", Toast.LENGTH_SHORT).show();
                    }
                }

                nom = aNom.getText().toString();
                fam = aFam.getText().toString();
                posologie = aPosologie.getText().toString();
                indications = aIndications.getText().toString();
                contreind = aContreI.getText().toString();
                effets = aEffets.getText().toString();
                protocole = aProtocole.getText().toString();
                surveillance = aSurveillance.getText().toString();
                presentation = aPresentation.getText().toString();

                if (!existe) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        fos.write(nom.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(fam.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(presentation.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(surveillance.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(posologie.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(indications.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(contreind.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(effets.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(protocole.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                Toast.makeText(getApplicationContext(), "Le médicament a été créé.", Toast.LENGTH_SHORT).show();
                                fos.close();
                                startActivity(new Intent(getApplicationContext(), Medicament.class));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        annuler = findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Medicament.class));
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

}