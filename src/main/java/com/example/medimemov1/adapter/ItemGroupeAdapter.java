package com.example.medimemov1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.medimemov1.R;
import com.example.medimemov1.models.ItemGroupes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupeAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<ItemGroupes> listGroupe;
    private List<ItemGroupes> listFilter;
    private LayoutInflater inflater;
    private String TAG = "Groupes";
    private static final String NOM_KEY = "Nom";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CustomFilter filter;

    public ItemGroupeAdapter(Context context, List<ItemGroupes> plistGroupe){
        this.context = context;
        this.listGroupe = plistGroupe;
        this.listFilter = plistGroupe;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listGroupe.size();
    }

    @Override
    public Object getItem(int position) {
        return listGroupe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_groupe, null);

        TextView itemNameView = view.findViewById(R.id.nomGroupe);

        db.collection("Groupes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (listGroupe.get(position).getNomGroupe().equals(document.getString(NOM_KEY))) {
                            itemNameView.setText(document.getString(NOM_KEY));
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

            ArrayList<ItemGroupes> filtered = new ArrayList<ItemGroupes>();
            String filterString = constraint.toString().toLowerCase();
            String filterableString;

            for(int i = 0; i<listFilter.size(); i++){
                filterableString = listFilter.get(i).getNomGroupe();
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
            listGroupe = (List<ItemGroupes>) results.values;
            notifyDataSetChanged();
        }
    }
}