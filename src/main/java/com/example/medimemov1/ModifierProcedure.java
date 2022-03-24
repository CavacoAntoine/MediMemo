package com.example.medimemov1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModifierProcedure extends AppCompatActivity {

    private EditText aNom, aDesc, aCorps;
    private ImageButton annuler;
    private ImageButton sauvegarder;
    String nom, desc, corps;
    String SEPARE = "Wiedersehen ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_procedure);

        Intent vData = getIntent();
        String vNomProcedure = ""+vData.getStringExtra("nomProcedure");

        this.aNom = findViewById(R.id.nom);
        this.aDesc = findViewById(R.id.description);
        this.aCorps = findViewById(R.id.procedure);

        FileInputStream fis = null;
        try {
            fis = openFileInput(vNomProcedure);
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
            aDesc.setText(tests[1]);
            aCorps.setText(tests[2]);
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

        sauvegarder = findViewById(R.id.enregistrer);
        sauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = aNom.getText().toString();
                desc = aDesc.getText().toString();
                corps = aCorps.getText().toString();

                FileOutputStream fos = null;

                try {
                    fos = openFileOutput(vNomProcedure, Context.MODE_PRIVATE);
                    fos.write(nom.getBytes());
                    fos.write(SEPARE.getBytes());
                    fos.write(desc.getBytes());
                    fos.write(SEPARE.getBytes());
                    fos.write(corps.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(fos != null){
                        try {
                            Toast.makeText(getApplicationContext(), "La procédure a été modifié.", Toast.LENGTH_SHORT).show();
                            fos.close();
                            startActivity(new Intent(getApplicationContext(),Procedures.class));
                        } catch (IOException e) {
                            e.printStackTrace();
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