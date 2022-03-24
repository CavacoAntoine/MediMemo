package com.example.medimemov1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MedicamentSeul extends AppCompatActivity {

    private TextView aNom, aDesc, aPosologie, aIndications, aContreI, aEffets, aProtocole, aSurveillance, aPresentation;
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
    private ImageButton popupBTBN;
    private MedicamentSeul activity = this;
    private boolean choisir = false;
    private String FILENAME;
    private String debut = "Medicament : ";
    private String desc, poso, ind, conti, eff, pro, sur, pre;
    private String SEPARE = "Wiedersehen";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_seul);

        Intent vData = getIntent();
        String vNomMedoc = ""+vData.getStringExtra("medoc");

        String vNomMedoc1 = vData.getStringExtra("nomMedicament");

        this.aNom = findViewById(R.id.nom);
        this.aDesc = findViewById(R.id.desc);
        this.aPosologie = findViewById(R.id.posologie);
        this.aIndications = findViewById(R.id.indication);
        this.aContreI = findViewById(R.id.contreindication);
        this.aEffets = findViewById(R.id.effets);
        this.aProtocole = findViewById(R.id.protocole);
        this.aSurveillance = findViewById(R.id.surveillance);
        this.aPresentation = findViewById(R.id.presentation);


        if (vNomMedoc.equals("null")) {
            choisir = false;
        } else {
            choisir = true;
        }

        this.aNom.setText(vNomMedoc);
        if (choisir) {
            db.collection("Medicaments").whereEqualTo("Nom", vNomMedoc).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    desc = retourLigne(document.getString(DESC_KEY));
                                    poso = retourLigne(document.getString(POSO_KEY));
                                    ind = retourLigne(document.getString(IND_KEY));
                                    conti = retourLigne(document.getString(CONTI_KEY));
                                    eff = retourLigne(document.getString(EFF_KEY));
                                    pro = retourLigne(document.getString(PRO_KEY));
                                    sur = retourLigne(document.getString(SUR_KEY));
                                    pre = retourLigne(document.getString(PRE_KEY));
                                    aDesc.setText(desc);
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
        if (!choisir) {
            String[] files = this.fileList();
            for (String file : files) {
                if (file.equals(vNomMedoc1)) {
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
                        if (file.startsWith("Medicament : ")) {
                            if(tests.length>0) {
                                aNom.setText((tests[0]));
                            }if(tests.length>1) {
                                aDesc.setText(tests[1]);
                            }if(tests.length>2) {
                                aPresentation.setText(tests[2]);
                            }if(tests.length>3) {
                                aSurveillance.setText(tests[3]);
                            }if(tests.length>4) {
                                aPosologie.setText(tests[4]);
                            }if(tests.length>5) {
                                aIndications.setText(tests[5]);
                            }if(tests.length>6) {
                                aContreI.setText(tests[6]);
                            }if(tests.length>7) {
                                aEffets.setText(tests[7]);
                            }if(tests.length>8) {
                                aProtocole.setText(tests[8]);
                            }
                        }
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
        }
        if(choisir) {
            //Popup
            popupBTBN = findViewById(R.id.popup_btn);
            popupBTBN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMedicamentSeul popup = new PopupMedicamentSeul(activity);
                    popup.setTitle(vNomMedoc);
                    popup.getUn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), ModifierMedicament.class);
                            i.putExtra("medoc", vNomMedoc);
                            startActivity(i);
                        }
                    });
                    popup.getDeux().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FILENAME = debut + aNom.getText().toString();
                            boolean existe = false;
                            String[] files = fileList();
                            for (String file : files) {
                                if (file.equals(FILENAME)) {
                                    existe = true;
                                    Toast.makeText(getApplicationContext(), "Ce médicament est déjà sur le téléphone.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!existe) {
                                FileOutputStream fos = null;
                                try {
                                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                                    fos.write(vNomMedoc.getBytes());
                                    fos.write(SEPARE.getBytes());
                                    if(!(desc==null)) {
                                        fos.write(desc.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(pre==null)) {
                                        fos.write(pre.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(sur==null)) {
                                        fos.write(sur.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(poso==null)) {
                                        fos.write(poso.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(ind==null)) {
                                        fos.write(ind.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(conti==null)){
                                        fos.write(conti.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(eff==null)) {
                                        fos.write(eff.getBytes());
                                    }
                                    fos.write(SEPARE.getBytes());
                                    if(!(pro==null)) {
                                        fos.write(pro.getBytes());
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fos != null) {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Le médicament a été télécharger dans mes médicaments.", Toast.LENGTH_SHORT).show();
                                            fos.close();
                                            startActivity(new Intent(getApplicationContext(), Medicament.class));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            startActivity(new Intent(getApplicationContext(),Medicament.class));
                        }
                    });
                    popup.build();
                }
            });
            //finpopUp
        }

        if(!choisir) {
            //Popup
            popupBTBN = findViewById(R.id.popup_btn);
            popupBTBN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMedicamentCreer popup = new PopupMedicamentCreer(activity);
                    popup.setTitle(vNomMedoc1.substring(13));
                    popup.getUn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), ModifierMedicament.class);
                            i.putExtra("nomMedicament", vNomMedoc1);
                            startActivity(i);
                        }
                    });
                    popup.getDeux().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //
                        }
                    });
                    popup.getTrois().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //startActivity(new Intent(getApplicationContext(),Telecharger.class));
                        }
                    });
                    popup.getQuatre().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteFile(vNomMedoc1);
                            startActivity(new Intent(getApplicationContext(),Medicament.class));
                        }
                    });
                    popup.build();
                }
            });
            //finpopUp
        }

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

    public void goMedicament(View view){
        startActivity(new Intent(getApplicationContext(), Medicament.class));
        finish();
    }

    public String retourLigne (String mot){
        if (mot != null) {
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
}