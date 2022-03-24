package com.example.medimemov1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medimemov1.adapter.ItemMedicamentsAdapter;
import com.example.medimemov1.models.ItemMedicament;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TousLesMedicaments#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class TousLesMedicaments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TousLesMedicaments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TousLesMedicaments.
     */
    // TODO: Rename and change types and number of parameters
    public static TousLesMedicaments newInstance(String param1, String param2) {
        TousLesMedicaments fragment = new TousLesMedicaments();
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
        View rootView = inflater.inflate(R.layout.fragment_tous_les_medicaments, container, false);

        ListView[] listeMedicaments = {rootView.findViewById(R.id.listeMedicaments)};
        List < ItemMedicament > items = new ArrayList<>();
        ItemMedicamentsAdapter[] adapter = new ItemMedicamentsAdapter[1];

        if(ConnexionInternet.isConnectedInternet(getActivity())) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Medicaments").orderBy("Nom").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            items.add(new ItemMedicament(document.getString("Nom")));
                        }
                        adapter[0] = new ItemMedicamentsAdapter(getActivity(), items);
                        listeMedicaments[0].setAdapter(adapter[0]);
                    }

                }
            });
        }else{
            Toast.makeText(getActivity(),"Une connexion internet est requise pour afficher tout les mÃ©dicaments.",Toast.LENGTH_SHORT).show();
        }

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
                startActivity(new Intent(getActivity(),CreerMedicament.class));
            }
        });

        return rootView;
    }

}