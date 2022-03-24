package com.example.medimemov1;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

public class PopupMedicamentSeul extends Dialog {
    private Button aUn,aDeux;
    private String titre;
    private TextView titreView;

    public PopupMedicamentSeul(Activity activity){
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.activity_popup_medicament_seul);

        this.aUn = findViewById(R.id.modif);
        this.aDeux = findViewById(R.id.telecharger);
        this.titreView = findViewById(R.id.titre);
    }

    public void setTitle(String title){ this.titre=title; }

    public Button getUn(){ return aUn; }

    public Button getDeux(){ return aDeux; }

    public void build(){
        show();
        titreView.setText(titre);
    }
}