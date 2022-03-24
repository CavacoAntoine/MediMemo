package com.example.medimemov1;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

public class PopupContact extends Dialog {
    private Button aUn,aDeux,aTrois;
    private String titre;
    private TextView titreView;

    public PopupContact(Activity activity){
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.activity_popup_contact);

        this.aUn = findViewById(R.id.modif);
        this.aDeux = findViewById(R.id.partager);
        this.aTrois = findViewById(R.id.supprimer);
        this.titreView = findViewById(R.id.titre);
    }

    public void setTitle(String title){ this.titre=title; }

    public Button getUn(){ return aUn; }

    public Button getDeux(){ return aDeux; }

    public Button getTrois(){ return aTrois; }

    public void build(){
        show();
        titreView.setText(titre);
    }
}