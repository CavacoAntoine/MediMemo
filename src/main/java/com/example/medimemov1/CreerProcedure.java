package com.example.medimemov1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreerProcedure extends AppCompatActivity {

    ImageButton sauvegarder;
    ImageButton annuler;
    private EditText aNom, aDesc, aCorps;
    String FILENAME, nom, desc, corps;
    String debut = "Procedure : ";
    String SEPARE = "Wiedersehen";
    private boolean v = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_procedure);

        aNom = findViewById(R.id.nom);
        aDesc = findViewById(R.id.description);
        aCorps = findViewById(R.id.procedure);

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
                        Toast.makeText(getApplicationContext(), "Cette procédure existe déjà.", Toast.LENGTH_SHORT).show();
                    }
                }

                nom = aNom.getText().toString();
                desc = aDesc.getText().toString();
                corps = aCorps.getText().toString();

                if (!existe) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        fos.write(nom.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(desc.getBytes());
                        fos.write(SEPARE.getBytes());
                        fos.write(corps.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                Toast.makeText(getApplicationContext(), "La procédure a été créée.", Toast.LENGTH_SHORT).show();
                                fos.close();
                                startActivity(new Intent(getApplicationContext(), Procedures.class));
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
                startActivity(new Intent(getApplicationContext(),Procedures.class));
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

    public void goMenu(View view){
        startActivity(new Intent(getApplicationContext(),Menu.class));
        finish();
    }
}