package com.example.medimemov1.models;

import androidx.appcompat.app.AppCompatActivity;


public class ItemMedicament extends AppCompatActivity {

    private String nom;

    public ItemMedicament(String nom){
        this.nom = nom;
    }

    public String getNom(){return nom;}

}