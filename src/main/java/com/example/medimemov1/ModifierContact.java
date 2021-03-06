package com.example.medimemov1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModifierContact extends AppCompatActivity {

    private EditText aNom, aPrenom, aTelp, aTelg, aNotes;
    private String aSNom, aSPrenom, aSTelp, aSTelg, aSNotes;
    private ImageButton sauvegarder, annuler;
    private String aSepare = "Wiedersehen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_contact);

        this.aNom = findViewById(R.id.nom);
        this.aPrenom = findViewById(R.id.prenom);
        this.aTelp = findViewById(R.id.nump);
        this.aTelg = findViewById(R.id.numg);
        this.aNotes = findViewById(R.id.notes);
        this.sauvegarder = findViewById(R.id.enregistrer);
        this.annuler = findViewById(R.id.annuler);


        Intent vData = getIntent();
        String vNom = "Contact : " + vData.getStringExtra("Contact");

        FileInputStream fis = null;
        try {
            fis = openFileInput(vNom);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }

            String test = sb.toString();
            String[] tests = test.split("Wiedersehen");
            aNom.setText(tests[0]);
            aPrenom.setText(tests[1]);
            aTelp.setText(tests[2]);
            aTelg.setText(tests[3]);
            aNotes.setText(tests[4]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        sauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aSNom = aNom.getText().toString();
                aSPrenom = aPrenom.getText().toString();
                aSTelp = aTelp.getText().toString();
                aSTelg = aTelg.getText().toString();
                aSNotes = aNotes.getText().toString();

                FileOutputStream fos = null;

                try {
                    if(!vNom.equals(aSNom)){
                        deleteFile(vNom);
                    }
                    fos = openFileOutput("Contact : "+aSNom+ " "+aSPrenom, Context.MODE_PRIVATE);
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
                }finally {
                    if(fos != null){
                        try {
                            Toast.makeText(getApplicationContext(), "Le contact a ??t?? modifi??.", Toast.LENGTH_SHORT).show();
                            fos.close();
                            startActivity(new Intent(getApplicationContext(),Annuaire.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Annuaire.class));
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
            Toast.makeText(this,"Une connexion est requise pour acc??der ?? cette page.",Toast.LENGTH_SHORT).show();
        }
    }

    public void goContact(View view){
        startActivity(new Intent(getApplicationContext(),Annuaire.class));
        finish();
    }
}