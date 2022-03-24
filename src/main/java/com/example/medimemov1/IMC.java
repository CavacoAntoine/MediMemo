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

public class IMC extends AppCompatActivity {

    private EditText taille;
    private EditText poid;
    private TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        taille = findViewById(R.id.editTextTaille);
        poid = findViewById(R.id.editTextPoid);
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

    public void calculIMC(View view){
        float vTaille = Float.parseFloat(taille.getText().toString());
        float vPoid = Float.parseFloat(poid.getText().toString());
        float IMC = vPoid/((vTaille/100)*(vTaille/100));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float twoIMC = Float.valueOf(decimalFormat.format(IMC));

        if(IMC < 18.5){
            resultat.setTextColor(Color.parseColor("#00C7FF"));
        }else if(IMC >= 18.5 && IMC < 25){
            resultat.setTextColor(Color.parseColor("#0CFF00"));
        }
        else if(IMC >= 25 && IMC < 30){
            resultat.setTextColor(Color.parseColor("#D4FF00"));
        }
        else if(IMC >= 30 && IMC < 35){
            resultat.setTextColor(Color.parseColor("#FFDC00"));
        }
        else if(IMC >= 35 && IMC < 40){
            resultat.setTextColor(Color.parseColor("#FF9400"));
        }
        else if(IMC >= 40){
            resultat.setTextColor(Color.parseColor("#FF1500"));
        }
        resultat.setText("IMC : "+twoIMC);
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
        Intent i = new Intent(getApplicationContext(),Procedures.class);
        i.putExtra("var",1);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}