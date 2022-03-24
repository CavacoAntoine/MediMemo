package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class CalcemieCorrige extends AppCompatActivity {

    private EditText C;
    private EditText P;
    private EditText A;
    private TextView resultat1, resultat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcemie_corrige);

        C = findViewById(R.id.editTextC);
        P = findViewById(R.id.editTextP);
        A = findViewById(R.id.editTextA);
        resultat1 = findViewById(R.id.res);
        resultat2 = findViewById(R.id.res1);

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

    public void calculCalcemie(View view){
        float vC = Float.parseFloat(C.getText().toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(P.getText().toString().isEmpty()){
            float vA = Float.parseFloat(A.getText().toString());
            resultat1.setText("0");
            float cal = (float)(vC - 0.025*(vA - 40));
            float twocal = Float.valueOf(decimalFormat.format(cal));
            resultat2.setText(""+twocal);
        }else if(A.getText().toString().isEmpty()){
            float vP = Float.parseFloat(P.getText().toString());
            resultat2.setText("0");
            float cal = (float)(vC/(0.55 + vP / 160));
            float twocal = Float.valueOf(decimalFormat.format(cal));
            resultat1.setText(""+twocal);
        }else{
            float vA = Float.parseFloat(A.getText().toString());
            float vP = Float.parseFloat(P.getText().toString());
            float cal = (float)(vC/(0.55 + vP / 160));
            float twocal = Float.valueOf(decimalFormat.format(cal));
            resultat1.setText(""+twocal);
            float cal2 = (float)(vC - 0.025*(vA - 40));
            float twocal2 = Float.valueOf(decimalFormat.format(cal2));
            resultat2.setText(""+twocal2);
        }
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
        startActivity(new Intent(getApplicationContext(),Procedures.class));
        finish();
    }
}