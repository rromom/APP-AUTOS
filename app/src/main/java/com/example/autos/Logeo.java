package com.example.autos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Logeo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Logeo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText user,pass;
    private Button login;
    private SQLite mySqLite;
    private SQLiteDatabase mySqLiteDatabase;
    View view;

    public Logeo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Logeo.
     */
    // TODO: Rename and change types and number of parameters
    public static Logeo newInstance(String param1, String param2) {
        Logeo fragment = new Logeo();
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
        view = inflater.inflate(R.layout.fragment_logeo, container, false);
        login = (Button) view.findViewById(R.id.logbtn);
        user = (EditText) view.findViewById(R.id.loguser);
        pass = (EditText) view.findViewById(R.id.logpass);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                    print("Alguno de los campos esta vacio");
                    return;
                }
                String us = user.getText().toString();

                    mySqLite = new SQLite(getContext(),"administracion",null,1);
                    mySqLiteDatabase = mySqLite.getWritableDatabase();
                    Cursor row = mySqLiteDatabase.rawQuery("select password from usuarios where usuario=\""+us+"\"",null);
                    if (row.moveToFirst()){
                        String passbd = row.getString(0);
                        if (passbd.equals(pass.getText().toString())){
                            mySqLiteDatabase.close();
                            Intent main_int = new Intent(getContext(),MAIN.class);
                            startActivity(main_int);
                            return;
                        }
                        print("Credenciales invalidas");
                        mySqLiteDatabase.close();
                        return;
                    }
                    print("Credenciales invalidas");
                    mySqLiteDatabase.close();
                    return;
            }
        });

        return view;
    }
    public void print(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }

}