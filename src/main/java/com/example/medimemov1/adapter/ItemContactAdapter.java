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

import com.example.medimemov1.Annuaire;
import com.example.medimemov1.Contact;
import com.example.medimemov1.ModifierContact;
import com.example.medimemov1.PopupContact;
import com.example.medimemov1.R;
import com.example.medimemov1.models.ItemContact;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemContactAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<ItemContact> listeContact;
    private List<ItemContact> listFilter;
    private LayoutInflater inflater;
    private CustomFilter filter;

    public ItemContactAdapter(Context context, List<ItemContact> plisteContact) {
        this.context = context;
        this.listeContact = plisteContact;
        this.listFilter = plisteContact;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listeContact.size();
    }

    @Override
    public Object getItem(int position) {
        return listeContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_contacts, null);
        TextView itemNameView = view.findViewById(R.id.nomContact);
        Button petitsPoints = view.findViewById(R.id.petitsPoints);

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
                if(file.startsWith("Contact : ") && listeContact.get(position).getNom().equals(tests[0]+tests[1])){
                    itemNameView.setText(tests[0]+" "+tests[1]);
                    itemNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), Contact.class);
                            i.putExtra("Contact", tests[0]+" "+tests[1]);
                            context.startActivity(i);
                        }
                    });
                    petitsPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final PopupContact popup = new PopupContact((Activity) context);
                            popup.setTitle(itemNameView.getText().toString());
                            popup.getUn().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(v.getContext(), ModifierContact.class);
                                    i.putExtra("Contact", itemNameView.getText().toString());
                                    context.startActivity(i);
                                }
                            });
                            popup.getDeux().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(getApplicationContext(),Partager.class));
                                }
                            });
                            popup.getTrois().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] files = context.fileList();
                                    context.deleteFile("Contact : "+itemNameView.getText());
                                    context.startActivity(new Intent(v.getContext(),Annuaire.class));
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
            filter = new ItemContactAdapter.CustomFilter();
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

            ArrayList<ItemContact> filtered = new ArrayList<ItemContact>();
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
            listeContact = (List<ItemContact>) results.values;
            notifyDataSetChanged();
        }
    }
}