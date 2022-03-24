package com.example.medimemov1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModifierMedicament extends AppCompatActivity {

    private EditText aNom, aFam, aPosologie, aIndications, aContreI, aEffets, aProtocole, aSurveillance, aPresentation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "Medicaments";
    private String DESC_KEY = "Description";
    private String PRE_KEY = "Présentation";
    private String POSO_KEY = "Posologie";
    private String SUR_KEY = "Surveillance";
    private String IND_KEY = "Indications";
    private String CONTI_KEY = "Contre-indications";
    private String EFF_KEY = "Effets secondaires";
    private String PRO_KEY = "Protocole";
    private ImageButton annuler;
    private ImageButton sauvegarder;
    String nom, fam, posologie, indications, contreind, effets, protocole, surveillance, presentation;
    private boolean choisir = false;
    String SEPARE = "Wiedersehen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_medicament);

        Intent vData = getIntent();
        String vNomMedoc = ""+vData.getStringExtra("medoc");
        String vNomMedoc1 = vData.getStringExtra("nomMedicament");

        if (vNomMedoc.equals("null")) {
            choisir = false;
        } else {
            choisir = true;
        }

        this.aNom = findViewById(R.id.nom);
        this.aFam = findViewById(R.id.famille);
        this.aPosologie = findViewById(R.id.posologie);
        this.aIndications = findViewById(R.id.indications);
        this.aContreI = findViewById(R.id.contreindication);
        this.aEffets = findViewById(R.id.effets);
        this.aProtocole = findViewById(R.id.protocole);
        this.aSurveillance = findViewById(R.id.surveillance);
        this.aPresentation = findViewById(R.id.presentation);

        if (choisir) {
            db.collection("Medicaments").whereEqualTo("Nom", vNomMedoc).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String desc = retourLigne(document.getString(DESC_KEY));
                                    String poso = retourLigne(document.getString(POSO_KEY));
                                    String ind = retourLigne(document.getString(IND_KEY));
                                    String conti = retourLigne(document.getString(CONTI_KEY));
                                    String eff = retourLigne(document.getString(EFF_KEY));
                                    String pro = retourLigne(document.getString(PRO_KEY));
                                    String sur = retourLigne(document.getString(SUR_KEY));
                                    String pre = retourLigne(document.getString(PRE_KEY));
                                    aNom.setText(document.getString("Nom"));
                                    aFam.setText(desc);
                                    aPosologie.setText(poso);
                                    aIndications.setText(ind);
                                    aContreI.setText(conti);
                                    aEffets.setText(eff);
                                    aProtocole.setText(pro);
                                    aSurveillance.setText(sur);
                                    aPresentation.setText(pre);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        if(!choisir){
            FileInputStream fis = null;
            try {
                fis = openFileInput(vNomMedoc1);
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
                aFam.setText(tests[1]);
                aPresentation.setText(tests[2]);
                aSurveillance.setText(tests[3]);
                aPosologie.setText(tests[4]);
                aIndications.setText(tests[5]);
                aContreI.setText(tests[6]);
                aEffets.setText(tests[7]);
                aProtocole.setText(tests[8]);

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
        }

        sauvegarder = findViewById(R.id.enregistrer);
        sauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = aNom.getText().toString();
                fam = aFam.getText().toString();
                posologie = aPosologie.getText().toString();
                indications = aIndications.getText().toString();
                contreind = aContreI.getText().toString();
                effets = aEffets.getText().toString();
                protocole = aProtocole.getText().toString();
                surveillance = aSurveillance.getText().toString();
                presentation = aPresentation.getText().toString();


                FileOutputStream fos = null;

                try {
                    if(choisir && !vNomMedoc.equals(nom)) {
                        deleteFile(vNomMedoc);
                    }if(!choisir && !vNomMedoc1.equals(nom)){
                        deleteFile(vNomMedoc1);
                    }
                    fos = openFileOutput("Medicament : "+nom, Context.MODE_PRIVATE);
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
                }finally {
                    if(fos != null){
                        try {
                            Toast.makeText(getApplicationContext(), "Le médicament a été modifié.", Toast.LENGTH_SHORT).show();
                            fos.close();
                            startActivity(new Intent(getApplicationContext(),Medicament.class));
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
                startActivity(new Intent(getApplicationContext(),Medicament.class));
            }
        });
    }

    public String retourLigne(String mot) {
        if(mot != null){
            String phrase[] = mot.split("€");
            mot = "";
            if (phrase.length > 1) {
                for (int i = 0; i < phrase.length - 1; i++) {
                    mot = mot + phrase[i] + System.getProperty("line.separator") + System.getProperty("line.separator") + phrase[i + 1];

                }
                Log.d("retour", "vrai " + mot);
            } else {
                mot = phrase[0];
                Log.d("retour", "faux " + mot);
            }
        }
        return mot;
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