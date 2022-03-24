package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medimemov1.adapter.ItemContactAdapter;
import com.example.medimemov1.adapter.ItemListeAdapter;
import com.example.medimemov1.models.ItemContact;
import com.example.medimemov1.models.ItemListe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Annuaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annuaire);

        ListView listeContact = findViewById(R.id.listeContact);
        List<String> items = new ArrayList<>();
        ItemContactAdapter[] adapter = new ItemContactAdapter[1];

        String[] files = fileList();
        for (String file : files) {
            FileInputStream fis = null;
            try {
                fis = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;

                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }

                String test = sb.toString();
                String[] tests = test.split("Wiedersehen");
                String nom = tests[0] + tests[1];
                Log.d("Annuaire",nom);
                if(file.startsWith("Contact : ")){
                    items.add(nom);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Collections.sort(items);
        List<ItemContact> sortedItems = new ArrayList<>();
        for(String vNom : items){
            sortedItems.add(new ItemContact(vNom));
        }
        adapter[0] = new ItemContactAdapter(this, sortedItems);
        listeContact.setAdapter(adapter[0]);

        EditText search = findViewById(R.id.search_bar);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter[0].getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageButton ajouter = findViewById((R.id.ajout));

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreerContact.class));
            }
        });

        //Initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.telephone);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.telephone:
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
}