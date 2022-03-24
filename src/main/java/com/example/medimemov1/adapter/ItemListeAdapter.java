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
import com.example.medimemov1.MedicamentSeul;
import com.example.medimemov1.ModifierMedicament;
import com.example.medimemov1.PopupMedicamentCreer;
import com.example.medimemov1.R;
import com.example.medimemov1.models.ItemListe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemListeAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<ItemListe> listeListe;
    private List<ItemListe> listFilter;
    private LayoutInflater inflater;
    private CustomFilter filter;

    public ItemListeAdapter(Context context, List<ItemListe> plisteListe) {
        this.context = context;
        this.listeListe = plisteListe;
        this.listFilter = plisteListe;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listeListe.size();
    }

    @Override
    public Object getItem(int position) {
        return listeListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_listes, null);
        TextView itemNameView = view.findViewById(R.id.nomListe);
        Button troisPetitsPoints = view.findViewById(R.id.petitsPoints);

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
                if(file.startsWith("Medicament : ") && listeListe.get(position).getNom().equals(tests[0])){
                    itemNameView.setText(tests[0]);
                    itemNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), MedicamentSeul.class);
                            i.putExtra("nomMedicament", file);
                            context.startActivity(i);
                        }
                    });
                    troisPetitsPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final PopupMedicamentCreer popup = new PopupMedicamentCreer((Activity) context);
                            popup.setTitle(itemNameView.getText().toString());
                            popup.getUn().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(v.getContext(), ModifierMedicament.class);
                                    i.putExtra("nomMedicament", "Medicament : "+ itemNameView.getText().toString());
                                    context.startActivity(i);
                                }
                            });
                            popup.getDeux().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(getApplicationContext(),Telecharger.class));
                                }
                            });
                            popup.getTrois().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(getApplicationContext(),Telecharger.class));
                                }
                            });
                            popup.getQuatre().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] files = context.fileList();
                                    context.deleteFile("Medicament : "+itemNameView.getText().toString());
                                    Log.d("Adapter", "Medicament : "+itemNameView.getText().toString());
                                    context.startActivity(new Intent(context, Medicament.class));
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
        if (filter == null) {
            filter = new ItemListeAdapter.CustomFilter();
        }
        return filter;
    }

    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults Result = new FilterResults();

            // if constraint is empty return the original names
            if (constraint.length() == 0) {
                Result.values = listFilter;
                Result.count = listFilter.size();
                return Result;
            }

            ArrayList<ItemListe> filtered = new ArrayList<ItemListe>();
            String filterString = constraint.toString().toLowerCase();
            String filterableString;

            for (int i = 0; i < listFilter.size(); i++) {
                filterableString = listFilter.get(i).getNom();
                if (filterableString.toLowerCase().contains(filterString)) {
                    filtered.add(listFilter.get(i));
                }
            }

            Result.values = filtered;
            Result.count = filtered.size();

            return Result;

        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            listeListe = (List<ItemListe>) results.values;
            notifyDataSetChanged();
        }
    }
}