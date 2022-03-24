package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class clairancecreatinine extends AppCompatActivity {

    private EditText P;
    private EditText U;
    private EditText V;
    private TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clairancecreatinine);

        P = findViewById(R.id.editTextP);
        U = findViewById(R.id.editTextU);
        V = findViewById(R.id.editTextV);
        resultat = findViewById(R.id.res);

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

    public void calculClairanceCreatinine(View view){
        float vV = Float.parseFloat(V.getText().toString());
        float vU = Float.parseFloat(U.getText().toString());
        float vP = Float.parseFloat(P.getText().toString());
        float CC = (float) (vV * vU /(vP * 1.44));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float twoCC = Float.valueOf(decimalFormat.format(CC));
        resultat.setText("Clairance : "+twoCC+" ml/min");
    }

    public void goMenu(View view){
        startActivity(new Intent(getApplicationContext(),Menu.class));
        finish();
    }

    public void goCompte(View view){
        startActivity(new Intent(getApplicationContext(),MonCompte.class));
        finish();
    }
    public void goProcedures(View view){
        startActivity(new Intent(getApplicationContext(),Procedures.class));
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}