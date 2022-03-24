package com.example.medimemov1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.medimemov1.Medicament;
import com.example.medimemov1.MedicamentSeul;
import com.example.medimemov1.ModifierMedicament;
import com.example.medimemov1.PopupMedicamentSeul;
import com.example.medimemov1.R;
import com.example.medimemov1.models.ItemMedicament;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemMedicamentsAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<ItemMedicament> listeMedicaments;
    private List<ItemMedicament> listFilter;
    private LayoutInflater inflater;
    private String TAG = "Medicaments";
    private CustomFilter filter;

    private static final String NOM_KEY = "Nom";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ItemMedicamentsAdapter(Context context, List<ItemMedicament> plisteMedicaments){
        this.context = context;
        this.listeMedicaments = plisteMedicaments;
        this.listFilter = plisteMedicaments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listeMedicaments.size();
    }

    @Override
    public ItemMedicament getItem(int position) {
        return listeMedicaments.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_medicaments, null);

        TextView itemNameView = view.findViewById(R.id.nomMedicament);
        Button troisPetisPoints = view.findViewById(R.id.petitsPoints);

        db.collection("Medicaments").orderBy("Nom").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (listeMedicaments.size() > position && listeMedicaments.get(position).getNom().equals(document.getString(NOM_KEY))) {
                            itemNameView.setText(document.getString(NOM_KEY));
                            itemNameView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(v.getContext(), MedicamentSeul.class);
                                    i.putExtra("medoc", document.getString(NOM_KEY));
                                    context.startActivity(i);
                                }
                            });
                            troisPetisPoints.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final PopupMedicamentSeul popup = new PopupMedicamentSeul((Activity) context);
                                    popup.setTitle(itemNameView.getText().toString());
                                    popup.getUn().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(v.getContext(), ModifierMedicament.class);
                                            i.putExtra("medoc", itemNameView.getText().toString());
                                            context.startActivity(i);
                                        }
                                    });
                                    popup.build();
                                }
                            });
                            break;
                        }
                    }
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return view;
    }

    @Override
    public Filter getFilter(){
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults Result = new FilterResults();
            // if constraint is empty return the original names
            if(constraint.length() == 0 ){
                Result.values = listFilter;
                Result.count = listFilter.size();
                return Result;
            }

            ArrayList<ItemMedicament> filtered = new ArrayList<ItemMedicament>();
            String filterString = constraint.toString().toLowerCase();
            String filterableString;

            for(int i = 0; i<listFilter.size(); i++){
                filterableString = listFilter.get(i).getNom();
                if(filterableString.toLowerCase().contains(filterString)){
                    filtered.add(listFilter.get(i));
                }
            }

            Result.values = filtered;
            Result.count = filtered.size();

            return Result;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listeMedicaments = (List<ItemMedicament>) results.values;
            notifyDataSetChanged();
        }
    }
}
