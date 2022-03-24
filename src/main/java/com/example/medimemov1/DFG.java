package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class DFG extends AppCompatActivity {

    private EditText créatininémie;
    private EditText poid;
    private EditText age;
    private TextView resultat1;
    private TextView resultat2;
    private TextView resultat3;
    RadioButton radioButtonA, radioButtonN, radioButtonH, radioButtonF;
    Button calculer;
    private boolean sexe;
    private boolean race;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfg);

        créatininémie = findViewById(R.id.editTextC);
        poid = findViewById(R.id.editTextP);
        age = findViewById(R.id.editTextA);
        resultat1 = findViewById(R.id.res);
        resultat2 = findViewById(R.id.res1);
        resultat3 = findViewById(R.id.res2);
        radioButtonA = findViewById(R.id.radioButtonA);
        radioButtonN = findViewById(R.id.radioButtonN);
        radioButtonH = findViewById(R.id.radioButtonH);
        radioButtonF = findViewById(R.id.radioButtonF);
        calculer = findViewById(R.id.DFGcalcul);
        final float[] Cockroft = new float[1];
        final float[] MDRD = new float[1];
        calculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonF.isChecked()){
                    Cockroft[0] = (float) (1.04 * Float.parseFloat(poid.getText().toString()) * (140 - Float.parseFloat(age.getText().toString()))/Float.parseFloat(créatininémie.getText().toString()));
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    float twoCockroft = Float.valueOf(decimalFormat.format(Cockroft[0]));
                    resultat1.setText(""+ twoCockroft);
                    if(radioButtonA.isChecked()){
                        MDRD[0] = (float)((186 * Math.pow((Float.parseFloat(créatininémie.getText().toString()) * 0.0113),-1.154)) * Math.pow(Float.parseFloat(age.getText().toString()),-0.203) * 0.742 * 1.21 * 0.95);
                        float twoMDRD = Float.valueOf(decimalFormat.format(MDRD[0]));
                        resultat2.setText(""+ twoMDRD);
                    }else if(radioButtonN.isChecked()){
                        MDRD[0] = (float)((186 * Math.pow((Float.parseFloat(créatininémie.getText().toString()) * 0.0113),-1.154)) * Math.pow(Float.parseFloat(age.getText().toString()),-0.203) * 0.742);
                        float twoMDRD = Float.valueOf(decimalFormat.format(MDRD[0]));
                        resultat2.setText(""+ twoMDRD);
                    }else{
                        resultat2.setText("Veuillez renseigner l'éthnie");
                    }
                }else if(radioButtonH.isChecked()){
                    Cockroft[0] = (float) (1.23 * Float.parseFloat(poid.getText().toString()) * (140 - Float.parseFloat(age.getText().toString()))/Float.parseFloat(créatininémie.getText().toString()));
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    float twoCockroft = Float.valueOf(decimalFormat.format(Cockroft[0]));
                    resultat1.setText(""+ twoCockroft);
                    if(radioButtonA.isChecked()){
                        MDRD[0] = (float)((186 * Math.pow((Float.parseFloat(créatininémie.getText().toString()) * 0.0113),-1.154)) * Math.pow(Float.parseFloat(age.getText().toString()),-0.203) * 1.21);
                        float twoMDRD = Float.valueOf(decimalFormat.format(MDRD[0]));
                        resultat2.setText(""+ twoMDRD);
                    }else if(radioButtonN.isChecked()){
                        MDRD[0] = (float)((186 * Math.pow((Float.parseFloat(créatininémie.getText().toString()) * 0.0113),-1.154)) * Math.pow(Float.parseFloat(age.getText().toString()),-0.203));
                        float twoMDRD = Float.valueOf(decimalFormat.format(MDRD[0]));
                        resultat2.setText(""+ twoMDRD);
                    }else{
                        resultat2.setText("Veuillez renseigner l'éthnie");
                    }
                }else{
                    resultat1.setText("Veuillez sélectionner un sexe svp");
                }

            }
        });
        Log.d("testoo", "onCreate: 2");

        //Initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.procedures);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.telephone:
                        startActivity(new Intent(getApplicationContext(),
                                Annuaire.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.medicament:
                        startActivity(new Intent(getApplicationContext(),
                                Medicament.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.procedures:
                        startActivity(new Intent(getApplicationContext(),
                                Procedures.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.groupes:
                        startActivity(new Intent(getApplicationContext(),
                                Groupe.class));
                        overridePendingTransition(0,0);
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

    public void goProcedures(View view){
        Intent i = new Intent(getApplicationContext(),Procedures.class);
        i.putExtra("var",1);
        startActivity(i);
        finish();
    }
}