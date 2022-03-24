package com.example.medimemov1;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends Dialog {
    private Button aUn,aDeux,aTrois,aQuatre;
    private String titre;
    private TextView titreView;

    public Popup(Activity activity){
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.activity_popup);

        this.titre="Mon compte";
        this.aUn = findViewById(R.id.mdp);
        this.aDeux = findViewById(R.id.modif);
        this.aTrois = findViewById(R.id.param);
        this.aQuatre = findViewById(R.id.deco);
        this.titreView = findViewById(R.id.titre);
    }

    public void setTitle(String title){ this.titre=title; }

    public Button getUn(){ return aUn; }

    public Button getDeux(){ return aDeux; }

    public Button getTrois(){ return aTrois; }

    public Button getQuatre(){ return aQuatre; }

    public void build(){
        show();
        titreView.setText(titre);
    }
}
