package com.example.medimemov1.models;

import androidx.appcompat.app.AppCompatActivity;

public class ItemContact extends AppCompatActivity {
    private String nom;

    public ItemContact(String nom){
        this.nom = nom;
    }

    public String getNom(){return nom;}
}