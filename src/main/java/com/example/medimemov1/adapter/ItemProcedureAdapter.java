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

import com.example.medimemov1.Medicament;
import com.example.medimemov1.ModifierMedicament;
import com.example.medimemov1.ModifierProcedure;
import com.example.medimemov1.PopupMedicamentSeul;
import com.example.medimemov1.PopupProcedure;
import com.example.medimemov1.ProcedureSeule;
import com.example.medimemov1.Procedures;
import com.example.medimemov1.R;
import com.example.medimemov1.models.ItemProcedure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemProcedureAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<ItemProcedure> listeProcedures;
    private List<ItemProcedure> listFilter;
    private LayoutInflater inflater;
    private ItemProcedureAdapter.CustomFilter filter;

    public ItemProcedureAdapter(Context context, List<ItemProcedure> plisteProcedure){
        this.context = context;
        this.listeProcedures = plisteProcedure;
        this.listFilter = plisteProcedure;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listeProcedures.size();
    }

    @Override
    public Object getItem(int position) {
        return listeProcedures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_procedures, null);

        TextView itemNameView = view.findViewById(R.id.nomProcedure);
        Button troisPetisPoints = view.findViewById(R.id.petitsPoints);

        String[] files = context.fileList();
        for (String file : files) {
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;

                while((text = br.readLine()) != null){
                    sb.append(text).append("\n");
                }

                String test = sb.toString();
                String[] tests = test.split("Wiedersehen");
                if(file.startsWith("Procedure : ") && listeProcedures.get(position).getNom().equals(tests[0])){
                    itemNameView.setText(tests[0]);
                    itemNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), ProcedureSeule.class);
                            i.putExtra("nomProcedure", file);
                            context.startActivity(i);
                        }
                    });
                    troisPetisPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final PopupProcedure popup = new PopupProcedure((Activity) context);
                            popup.setTitle(itemNameView.getText().toString());
                            popup.getUn().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(v.getContext(), ModifierProcedure.class);
                                    i.putExtra("nomProcedure","Procedure : " + itemNameView.getText().toString());
                                    context.startActivity(i);
                                }
                            });
                            popup.getQuatre().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.deleteFile("Procedure : "+itemNameView.getText().toString());
                                    context.startActivity(new Intent(v.getContext(), Procedures.class));
                                }
                            });
                            popup.build();
                        }
                    });
                    break;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new ItemProcedureAdapter.CustomFilter();
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

            ArrayList<ItemProcedure> filtered = new ArrayList<ItemProcedure>();
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
            listeProcedures = (List<ItemProcedure>) results.values;
            notifyDataSetChanged();
        }
    }
}