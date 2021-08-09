package com.example.autos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MAIN extends AppCompatActivity {
    private Spinner mySpinner;
    private ArrayList<String> list;
    private FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_a_i_n);
        list = new ArrayList<String>();
        list.add("Vender Vehiculo");
        list.add("Comprar Vehiculo");
        mySpinner = (Spinner) findViewById(R.id.mnspinner);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_dropdown_item,list);
        mySpinner.setAdapter(myArrayAdapter);
        ft= getSupportFragmentManager().beginTransaction();
        Vender myVender = new Vender();
        ft.replace(R.id.mnfrag,myVender);
        ft.commit();

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ft= getSupportFragmentManager().beginTransaction();
                switch (position){
                    case 0:
                        Vender myVender = new Vender();
                        ft.replace(R.id.mnfrag,myVender);
                        ft.addToBackStack(null);
                        break;
                    case 1:
                        Comprar myComprar = new Comprar();
                        ft.replace(R.id.mnfrag,myComprar);
                        ft.addToBackStack(null);
                        break;
                }
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}