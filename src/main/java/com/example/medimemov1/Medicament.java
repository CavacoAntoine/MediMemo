package com.example.medimemov1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Medicament extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);

        //Assign variable
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        //Initialize adapter
        adapter = new MainAdapter(getSupportFragmentManager());

        //Add Fragments
        adapter.AddFragment(new TousLesMedicaments(),"Tous les Médicaments");
        adapter.AddFragment(new MesMedicaments(), "Mes Médicaments");

        //Set Adapter
        viewPager.setAdapter(adapter);

        //Connect tab layout with view pager
        tabLayout.setupWithViewPager(viewPager);

        //Initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.medicament);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.telephone:
                        startActivity(new Intent(getApplicationContext(),
                                Annuaire.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.medicament:
                        return true;
                    case R.id.procedures:
                        startActivity(new Intent(getApplicationContext(),
                                Procedures.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.groupes:
                        startActivity(new Intent(getApplicationContext(),
                                Groupe.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void goMenu(View view){
        startActivity(new Intent(getApplicationContext(),Menu.class));
        finish();
    }

    public void goCompte(View view){
        if(ConnexionInternet.isConnectedInternet(this)) {
            startActivity(new Intent(getApplicationContext(), MonCompte.class));
            finish();
        }else{
            Toast.makeText(this,"Une connexion est requise pour accéder à cette page.",Toast.LENGTH_SHORT).show();
        }
    }

    private class MainAdapter extends FragmentPagerAdapter {

        //Initialize array list
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        //Create constructor
        public void AddFragment(Fragment fragment,String s){
            //Add fragment
            fragmentArrayList.add(fragment);
            //Add string
            stringArrayList.add(s);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //Return Fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            //Return fragment list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //Return tab title
            return stringArrayList.get(position);
        }

    }
}