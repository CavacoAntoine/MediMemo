package com.example.medimemov1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.medimemov1.models.ItemMedicament;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calculateurs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calculateurs extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Calculateurs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calculateurs.
     */
    // TODO: Rename and change types and number of parameters
    public static Calculateurs newInstance(String param1, String param2) {
        Calculateurs fragment = new Calculateurs();
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
        View rootView = inflater.inflate(R.layout.fragment_calculateurs, container, false);
        ListView listeCalculateur = rootView.findViewById(R.id.listeCalculateur);
        List<String> items = new ArrayList<>();
        items.add("IMC");
        items.add("Clairance mesurée de la créatinine");
        items.add("DFG, clairance créatinine");
        items.add("Calcémie Corrigé");
        items.add("APGAR");
        items.add("Glasgow");
        items.add("Pancréatite BISAP");
        items.add("SOFA");
        items.add("WFNS");
        items.add("Volume Courant");
        items.add("BPS");
        items.add("RASS");


        listeCalculateur.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items));
        listeCalculateur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    startActivity(new Intent(getActivity(),IMC.class));
                }else if(position == 1){
                    startActivity(new Intent(getActivity(),clairancecreatinine.class));
                }else if(position == 2){
                    startActivity(new Intent(getActivity(),DFG.class));
                }else if(position == 3){
                    startActivity(new Intent(getActivity(),CalcemieCorrige.class));
                }
            }
        });

        return rootView;
    }
}