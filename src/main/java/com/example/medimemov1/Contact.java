package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Contact extends AppCompatActivity {

    TextView aNumberp, aNumberg, aNotes, aNom;
    ImageButton bt_call1, bt_call2;
    private ImageButton popupBTBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        aNumberp = findViewById(R.id.contactNumPers);
        aNumberg = findViewById(R.id.contactNumGarde);
        bt_call1 = findViewById(R.id.Appelnumpers);
        bt_call2 = findViewById(R.id.Appelnumg);
        aNom = findViewById(R.id.contactNom);
        aNotes = findViewById(R.id.contactNote);
        popupBTBN = findViewById(R.id.popupBTN);

        Intent vData = getIntent();
        String vContact = vData.getStringExtra("Contact");

        String[] files = this.fileList();
        for (String file : files) {
            if (file.contains(vContact)) {
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
                    aNom.setText((tests[0]+" "+tests[1]));
                    aNumberp.setText(tests[2].trim());
                    aNumberg.setText(tests[3].trim());
                    aNotes.setText(tests[4]);
                    break;
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
        }

        bt_call1.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(hasPermission(v.getContext(), Manifest.permission.CALL_PHONE)){
                String phone = aNumberp.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Modifier le contact pour entrer un numéro de téléphone",Toast.LENGTH_SHORT).show();
                }else{
                    String s = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            }else{
                Toast.makeText(Contact.this, "Autorisez l'application à passer des appels dans les paramètres du téléphone", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.CALL_PHONE},102);
                }
            }
        });

        bt_call2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(hasPermission(v.getContext(), Manifest.permission.CALL_PHONE)){
                    String phone = aNumberg.getText().toString();
                    if (phone.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Modifier le contact pour entrer un numéro de téléphone",Toast.LENGTH_SHORT).show();
                    }else{
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(Contact.this, "Autorisez l'application à passer des appels dans les paramètres du téléphone", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.CALL_PHONE},103);
                }
            }
        });

        popupBTBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupContact popup = new PopupContact(Contact.this);
                popup.setTitle(aNom.getText().toString());
                popup.getUn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ModifierContact.class);
                        i.putExtra("Contact", aNom.getText().toString());
                        startActivity(i);
                    }
                });
                popup.getDeux().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(getApplicationContext(),Partager.class));
                    }
                });
                popup.getTrois().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] files = fileList();
                        for (String file : files) {
                            deleteFile("Contact : "+aNom.getText());
                        }
                        startActivity(new Intent(v.getContext(),Annuaire.class));
                    }
                });
                popup.build();
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

    public void goAnnuaire(View view){
        startActivity(new Intent(getApplicationContext(),Annuaire.class));
        finish();
    }

    private boolean hasPermission(Context pContext, String pPermission){
        return ContextCompat.checkSelfPermission(pContext,pPermission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 102){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String phone = aNumberp.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Modifier le contact pour entrer un numéro de téléphone",Toast.LENGTH_SHORT).show();
                }else{
                    String s = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            }
        }else if(requestCode == 103){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String phone = aNumberg.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Modifier le contact pour entrer un numéro de téléphone",Toast.LENGTH_SHORT).show();
                }else{
                    String s = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                }
            }
        }else{
            Toast.makeText(this, "Permission refusé", Toast.LENGTH_SHORT).show();
        }

    }
}