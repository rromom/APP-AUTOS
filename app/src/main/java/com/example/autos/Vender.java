package com.example.autos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Vender#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Vender extends Fragment {

    private ArrayList<String> brands,year,status,owner;
    private Spinner mySpinner,spinneryear,spinnerstatus,spinnerowner;
    private EditText codigo,modelo,precio,cilindraje,pais,kilometraje;
    private Button registrar,consultar,actualizar,eliminar,cotizar;
    private SQLite mySqLite;
    public static String cbrand,cmodel,ccost,cyear;
    private SQLiteDatabase bd;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Vender() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vender.
     */
    // TODO: Rename and change types and number of parameters
    public static Vender newInstance(String param1, String param2) {
        Vender fragment = new Vender();
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
        view = inflater.inflate(R.layout.fragment_vender, container, false);

        //EditText
        codigo = (EditText) view.findViewById(R.id.vencode);
        modelo = (EditText) view.findViewById(R.id.venmodel);
        precio = (EditText) view.findViewById(R.id.vencost);
        cilindraje = (EditText) view.findViewById(R.id.vencylinder);
        pais = (EditText) view.findViewById(R.id.vencountry);
        kilometraje = (EditText) view.findViewById(R.id.venmiliage);

        //Buttons
        registrar = (Button) view.findViewById(R.id.venreg);
        consultar = (Button) view.findViewById(R.id.vencon);
        actualizar = (Button) view.findViewById(R.id.venact);
        eliminar = (Button) view.findViewById(R.id.veneli);
        cotizar = (Button) view.findViewById(R.id.vencot);



        //spinners
        brands = new ArrayList<String>();
        year = new ArrayList<String>();
        status = new ArrayList<String>();
        owner = new ArrayList<String>();


        mySpinner = (Spinner) view.findViewById(R.id.vendbrand);
        spinneryear = (Spinner) view.findViewById(R.id.venyear);
        spinnerstatus = (Spinner) view.findViewById(R.id.venstatus);
        spinnerowner = (Spinner) view.findViewById(R.id.venowner);

        brands();
        years();
        gstatus();
        gowner();

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,brands);
        ArrayAdapter<String> myArrayAdapteryear = new ArrayAdapter<String> (getContext(), android.R.layout.simple_spinner_dropdown_item,year);
        ArrayAdapter<String> myArrayAdapterstatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,status);
        ArrayAdapter<String> myArrayAdapterowner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,owner);

        mySpinner.setAdapter(myArrayAdapter);
        spinneryear.setAdapter(myArrayAdapteryear);
        spinnerstatus.setAdapter(myArrayAdapterstatus);
        spinnerowner.setAdapter(myArrayAdapterowner);

        //oncliks
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //print(String.valueOf(mySpinner.getSelectedItemPosition()));
                register();
            }
        });
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        cotizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quote();
            }
        });

        //view
        return view;
    }

    private void brands(){
        brands.add("BMW");
        brands.add("Chevrolet");
        brands.add("Ford");
        brands.add("Honda");
        brands.add("Isuzu");
        brands.add("Kia");
        brands.add("Mazda");
        brands.add("Mercedes-Benz");
        brands.add("Mitsubishi");
        brands.add("Nissan");
        brands.add("Peugeot");
        brands.add("Porsche");
        brands.add("Renault");
        brands.add("Suzuki");
        brands.add("Toyota");
        brands.add("Volkswagen");
        brands.add("Volvo");
    }
    private void years(){
        Calendar c  =  Calendar.getInstance();
        int year_num = c.get(Calendar.YEAR);
        for (int i=2000; i<year_num+1;i++){
            year.add(String.valueOf(i));
        }
    }
    private void gstatus(){
        status.add("Nuevo");
        status.add("Usado");
    }
    private void gowner(){
        owner.add("1");
        owner.add("2");
        owner.add("3");
        owner.add("4");
        owner.add("5");
    }
    public void print(String texto){
        Toast.makeText(getContext(),texto,Toast.LENGTH_LONG).show();
    }
    private void conection(){
        mySqLite = new SQLite(getContext(),"administracion",null,1);
        bd = mySqLite.getWritableDatabase();
    }
    private void clean(){
        codigo.setText("");
        mySpinner.setSelection(0);
        modelo.setText("");
        spinneryear.setSelection(0);
        precio.setText("");
        cilindraje.setText("");
        pais.setText("");
        spinnerstatus.setSelection(0);
        kilometraje.setText("");
        spinnerowner.setSelection(0);
    }

    private void register(){
        conection();
        String et_code = codigo.getText().toString();
        String et_brand = String.valueOf(mySpinner.getSelectedItemPosition());
        String et_model = modelo.getText().toString();
        String et_year = String.valueOf(spinneryear.getSelectedItemPosition());
        String et_cost = precio.getText().toString();
        String et_cylinder = cilindraje.getText().toString();
        String et_country = pais.getText().toString();
        String et_status = String.valueOf(spinnerstatus.getSelectedItemPosition());
        String et_miliage = kilometraje.getText().toString();
        String et_owner = String.valueOf(spinnerowner.getSelectedItemPosition());
        if (et_code.isEmpty() || et_model.isEmpty() || et_cost.isEmpty() || et_cylinder.isEmpty() || et_country.isEmpty() || et_miliage.isEmpty()){
            print("Uno de los campos anteriores se encuentra vacio");
            return;
        }
        ContentValues values = new ContentValues();
        values.put("codigo",et_code);
        values.put("marca",et_brand);
        values.put("modelo",et_model);
        values.put("anio",et_year);
        values.put("precio",et_cost);
        values.put("cilindraje",et_cylinder);
        values.put("pais",et_country);
        values.put("estado",et_status);
        values.put("kilometraje",et_miliage);
        values.put("duenio",et_owner);
        bd.insert("vehiculo",null,values);
        bd.close();
        clean();
        print("Vehiculo Registrado con exito");

    }
    private void read(){
        conection();
        String et_code = codigo.getText().toString();
        if (et_code.isEmpty()){
            print("es necesario el código del vehículo para consultar");
            return;
        }
        Cursor fila = bd.rawQuery("select marca,modelo,anio,precio,cilindraje,pais,estado,kilometraje,duenio from vehiculo where codigo="+et_code,null);
        if (!fila.moveToFirst()){
            print("No hay ningun vehículo registrado con el codigo ingresado");
            return;
        }
        mySpinner.setSelection(Integer.parseInt(fila.getString(0)));
        modelo.setText(fila.getString(1));
        spinneryear.setSelection(Integer.parseInt(fila.getString(2)));
        precio.setText(fila.getString(3));
        cilindraje.setText(fila.getString(4));
        pais.setText(fila.getString(5));
        spinnerstatus.setSelection(Integer.parseInt(fila.getString(6)));
        kilometraje.setText(fila.getString(7));
        spinnerowner.setSelection(Integer.parseInt(fila.getString(8)));
        bd.close();
    }
    private void update(){
        conection();
        String et_code = codigo.getText().toString();
        String et_brand = String.valueOf(mySpinner.getSelectedItemPosition());
        String et_model = modelo.getText().toString();
        String et_year = String.valueOf(spinneryear.getSelectedItemPosition());
        String et_cost = precio.getText().toString();
        String et_cylinder = cilindraje.getText().toString();
        String et_country = pais.getText().toString();
        String et_status = String.valueOf(spinnerstatus.getSelectedItemPosition());
        String et_miliage = kilometraje.getText().toString();
        String et_owner = String.valueOf(spinnerowner.getSelectedItemPosition());

        if (et_code.isEmpty() || et_model.isEmpty() || et_cost.isEmpty() || et_cylinder.isEmpty() || et_country.isEmpty() || et_miliage.isEmpty()){
            print("Uno de los campos anteriores se encuentra vacio");
            return;
        }

        ContentValues values = new ContentValues();
        values.put("codigo",et_code);
        values.put("marca",et_brand);
        values.put("modelo",et_model);
        values.put("anio",et_year);
        values.put("precio",et_cost);
        values.put("cilindraje",et_cylinder);
        values.put("pais",et_country);
        values.put("estado",et_status);
        values.put("kilometraje",et_miliage);
        values.put("duenio",et_owner);

        int flag = bd.update("vehiculo",values,"codigo="+et_code,null);
        bd.close();
        if (flag!=1){
            print("No se pudo modificar los campos");
            return;
        }

        print("Campos modificados con exito");

        clean();

    }
    private void delete(){
        conection();
        String et_code = codigo.getText().toString();
        if (et_code.isEmpty()){
            print("El campo codigo se encuentra vacio");
            return;
        }
        int flag =  bd.delete("vehiculo","codigo="+et_code,null);
        if (flag!=1){
            print("No se pudo eliminar el vehiculo");
            return;
        }
        print("Vehículo eliminado con exito");
        bd.close();
        codigo.setText("");
        clean();
    }
    private void quote(){
        conection();
        String et_code = codigo.getText().toString();

        if (et_code.isEmpty()){
            print("Codigo de vehículo vacio");
            return;
        }

        Cursor fila = bd.rawQuery("select marca,modelo,precio,anio from vehiculo where codigo="+et_code,null);

        if (!fila.moveToFirst()){
            print("No existe vehículo registrado con el código ingresado");
            return;
        }

        cbrand = mySpinner.getItemAtPosition(Integer.parseInt(fila.getString(0))).toString();
        cmodel = fila.getString(1);
        ccost = fila.getString(2);
        cyear = spinneryear.getItemAtPosition(Integer.parseInt(fila.getString(3))).toString();
        bd.close();
        Intent cot_int = new Intent(getContext(),Cotizar.class);
        startActivity(cot_int);

    }

}