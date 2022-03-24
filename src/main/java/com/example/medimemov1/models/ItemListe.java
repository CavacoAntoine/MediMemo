package com.example.medimemov1.models;

import androidx.appcompat.app.AppCompatActivity;

public class ItemListe extends AppCompatActivity {
    private String nom;

    public ItemListe(String nom){
        this.nom = nom;
    }

    public String getNom(){return nom;}
}