package com.example.medimemov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcedureSeule extends AppCompatActivity {

    private TextView aNom, aDesc, aCorps;
    private ImageButton popupBTBN;
    private ProcedureSeule activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_seule);

        Intent vData = getIntent();
        String vNomProcedure = ""+vData.getStringExtra("nomProcedure");

        this.aNom = findViewById(R.id.nom);
        this.aDesc = findViewById(R.id.desc);
        this.aCorps = findViewById(R.id.procedure);

        String[] files = this.fileList();
        for (String file : files) {
            if (file.equals(vNomProcedure)) {
                FileInputStream fis = null;
                try {
                    fis = this.openFileInput(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String text;

                    while ((text = br.readLine()) != null) {
                        sb.append(text).append("\n");
                    }

                    String test = sb.toString();
                    String[] tests = test.split("Wiedersehen");
                    if (file.startsWith("Procedure : ")) {
                        if (tests.length > 0) {
                            aNom.setText((tests[0]));
                        }
                        if (tests.length > 1) {
                            aDesc.setText(tests[1]);
                        }
                        if (tests.length > 2) {
                            aCorps.setText(tests[2]);
                        }
                        break;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Popup
        popupBTBN = findViewById(R.id.popup_btn);
        popupBTBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupProcedure popup = new PopupProcedure(activity);
                popup.setTitle(vNomProcedure.substring(12));
                popup.getUn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ModifierProcedure.class);
                        i.putExtra("nomProcedure", vNomProcedure);
                        startActivity(i);
                    }
                });
                popup.getDeux().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(getApplicationContext(),Telecharger.class));
                    }
                });
                popup.getTrois().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(getApplicationContext(),TelechargerMedicament.class));
                    }
                });
                popup.getQuatre().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] files = fileList();
                        for (String file : files) {
                            deleteFile(vNomProcedure);
                        }
                        startActivity(new Intent(getApplicationContext(),Procedures.class));
                    }
                });
                popup.build();
            }
        });
        //finpopUp

        //Initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.medicament);

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

    public void goMenu (View view){
        startActivity(new Intent(getApplicationContext(), Menu.class));
        finish();
    }

    @Override
    public void onPause () {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void goCompte (View view){
        if (ConnexionInternet.isConnectedInternet(this)) {
            startActivity(new Intent(getApplicationContext(), MonCompte.class));
            finish();
        } else {
            Toast.makeText(this, "Une connexion est requise pour accéder à cette page.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goProcedure(View view){
        startActivity(new Intent(getApplicationContext(), Procedures.class));
        finish();
    }
}