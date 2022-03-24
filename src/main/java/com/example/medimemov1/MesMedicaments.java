package com.example.medimemov1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.medimemov1.adapter.ItemListeAdapter;
import com.example.medimemov1.adapter.ItemMedicamentsAdapter;
import com.example.medimemov1.models.ItemListe;
import com.example.medimemov1.models.ItemMedicament;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MesMedicaments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MesMedicaments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MesMedicaments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MesListes.
     */
    // TODO: Rename and change types and number of parameters
    public static MesMedicaments newInstance(String param1, String param2) {
        MesMedicaments fragment = new MesMedicaments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mes_listes, container, false);

        Activity activity = getActivity();

        ListView[] listeListe = {rootView.findViewById(R.id.listeListe)};
        List<String> items = new ArrayList<>();
        ItemListeAdapter[] adapter = new ItemListeAdapter[1];

        String[] files = activity.fileList();
        for (String file : files) {
            FileInputStream fis = null;
            try {
                fis = activity.openFileInput(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;

                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }

                String test = sb.toString();
                String[] tests = test.split("Wiedersehen");
                String nom = tests[0];
                if(file.startsWith("Medicament : ")) {
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
        List<ItemListe> sortedListe = new ArrayList<>();
        for(String vNom : items){
            sortedListe.add(new ItemListe(vNom));
        }
        adapter[0] = new ItemListeAdapter(getActivity(), sortedListe);
        listeListe[0].setAdapter(adapter[0]);

        EditText search = rootView.findViewById(R.id.search_bar);

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

        ImageButton ajouter = rootView.findViewById((R.id.ajout));

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreerMedicament.class));
            }
        });

        return rootView;
    }
}