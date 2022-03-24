package com.example.medimemov1.models;

import androidx.appcompat.app.AppCompatActivity;

public class ItemGroupes extends AppCompatActivity {

    private String nomGroupe;

    public ItemGroupes(String nomGroupe){
        this.nomGroupe = nomGroupe;
    }

    public String getNomGroupe(){return nomGroupe;}

}