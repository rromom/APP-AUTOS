package com.example.autos;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Comprar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comprar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> brands,year,owner;
    View view;
    private Button send,calcular;
    private SQLite mySqLite;
    private SQLiteDatabase bd;
    private EditText code;
    private TextView combrand,commodel,comyear,commiliage,comcost,comowner,res;
    private Spinner comtap,compaint,combody;
    private int anio,kilo,precio,duenio;
    private RadioButton c1,c2,a1,a2;



    public Comprar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comprar.
     */
    // TODO: Rename and change types and number of parameters
    public static Comprar newInstance(String param1, String param2) {
        Comprar fragment = new Comprar();
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

        view = inflater.inflate(R.layout.fragment_comprar, container, false);
        brands = new ArrayList<String>();
        year = new ArrayList<String>();
        owner = new ArrayList<String>();
        code = (EditText) view.findViewById(R.id.comcode);

        send = (Button) view.findViewById(R.id.comsend);
        calcular = (Button) view.findViewById(R.id.combtn);

        combrand = (TextView) view.findViewById(R.id.combrand);
        commodel = (TextView) view.findViewById(R.id.commodel);
        comcost = (TextView) view.findViewById(R.id.comcost);
        commiliage = (TextView) view.findViewById(R.id.commiliage);
        comowner = (TextView) view.findViewById(R.id.comowner);
        comyear = (TextView) view.findViewById(R.id.comyear);
        res = (TextView) view.findViewById(R.id.comres);

        comtap = (Spinner) view.findViewById(R.id.comtap);
        compaint = (Spinner) view.findViewById(R.id.compaint);
        combody = (Spinner) view.findViewById(R.id.combody);

        c1 = (RadioButton) view.findViewById(R.id.c1);
        c2 = (RadioButton) view.findViewById(R.id.c2);
        a1 = (RadioButton) view.findViewById(R.id.a1);
        a2 = (RadioButton) view.findViewById(R.id.a2);


        String [] tap = {"excelente","regular","mala"};
        ArrayAdapter<String> estado = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,tap);
        comtap.setAdapter(estado);
        compaint.setAdapter(estado);
        combody.setAdapter(estado);



        brands();
        //print(brands.get(0).toString());
        gowner();
        years();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (code.getText().toString().isEmpty()){
                    print("Campo Código Vacío");
                    return;
                }
                consultar();

            }
        });
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().toString().isEmpty()){
                    print("Ingrese codigo para realziar los calculos");
                    return;
                }
                if (!c1.isChecked()&&!c2.isChecked()){
                    print("Seleccione si o no en el campo choques");
                    return;
                }
                if (!a1.isChecked() && !a2.isChecked()){
                    print("Seleccione si o no en el campo aire acondicionado");
                    return;
                }

                Calendar c = Calendar.getInstance();
                int anio_actual = c.get(Calendar.YEAR);
                int anios_pasados = anio_actual-anio;

                if (anios_pasados>10){
                    print("El vehiculo excede los 10 años ");
                    return;
                }

                double descuento_anio=(precio*0.015)*anios_pasados;
                double calculo_anio = precio-descuento_anio;
                String resultados = "->DESCUENTO POR "+anios_pasados+" AÑOS: "+precio+"-"+descuento_anio+"="+calculo_anio+"\n \n";


                //Kilometraje
                double descuento_kilo=0;
                if (kilo>80000){
                    descuento_kilo=precio*0.025;
                }

                double calculo_kilo = calculo_anio-descuento_kilo;
                resultados+="->DESCUENTO POR "+kilo+" KM: "+calculo_anio+"-"+descuento_kilo+"="+calculo_kilo+"\n \n";

                //dueño

                double descuento_duenio = 0;
                if (duenio>1){
                    descuento_duenio=(precio*0.02)*duenio;
                }
                double calculo_duenio=calculo_kilo-descuento_duenio;
                resultados+="->DESCUENTO POR "+duenio+" DUEÑO: "+calculo_kilo+"-"+descuento_duenio+"="+calculo_duenio+"\n \n";

                //choques
                double descuento_choque=0;
                if (c1.isChecked()){
                    descuento_choque=precio*0.15;
                }
                double calculo_choque = calculo_duenio-descuento_choque;
                resultados+="->DESCUENTO POR CHOQUE: "+calculo_duenio+"-"+descuento_choque+"="+calculo_choque+"\n \n";


                //aire

                double descuento_air=0;
                if (a2.isChecked()){
                    descuento_air=precio*0.005;
                }
                double calculo_air = calculo_choque-descuento_air;
                resultados+="->DESCUENTO POR AIRE ACONDICIONAD: "+calculo_choque+"-"+descuento_air+"="+calculo_air+"\n \n";

                //tapiceria
                int opctap = comtap.getSelectedItemPosition();
                double descuento_tapiceria=0;
                //print(opctap+"");

                switch (opctap){
                    case 0:
                        descuento_tapiceria=-(precio*0.01);
                        break;
                    case 1:
                        descuento_tapiceria=(precio*0.015);
                        break;
                    case 2:
                        descuento_tapiceria=(precio*0.03);
                        break;
                }
                double calculo_tapiceria = calculo_air-descuento_tapiceria;
                resultados+="-> DESCUENTO POR TAPICERIA: "+calculo_air+"-("+descuento_tapiceria+")="+calculo_tapiceria+"\n \n";

                //pintura

                int opcpint=compaint.getSelectedItemPosition();
                double descuento_pintura=0;
                switch (opcpint){
                    case 0:
                        descuento_pintura=-(precio*0.02);
                        break;
                    case 1:
                        descuento_pintura=(precio*0.02);
                        break;
                    case 2:
                        descuento_pintura=(precio*0.1);
                        break;
                }

                double calculo_pintura = calculo_tapiceria-descuento_pintura;
                resultados+="->DeSCUENTO POR PINTURA: "+calculo_tapiceria+"-("+descuento_pintura+")="+calculo_pintura+"\n \n";

                //Carroceria

                int opccarr = combody.getSelectedItemPosition();
                double descuento_carroceria=0;
                switch (opccarr){
                    case 0:
                        descuento_carroceria=-(precio*0.02);
                        break;
                    case 1:
                        descuento_carroceria=(precio*0.02);
                        break;
                    case 2:
                        descuento_carroceria=(precio*0.1);
                        break;
                }
                double calculo_carroceria = calculo_pintura-descuento_carroceria;
                resultados+="->DESCUENTO POR CARROCERIA: "+calculo_pintura+"-("+descuento_carroceria+")="+calculo_carroceria;
                res.setText(resultados);

            }
        });

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
    private void gowner(){
        owner.add("1");
        owner.add("2");
        owner.add("3");
        owner.add("4");
        owner.add("5");
    }
    public void  print(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }

    private void conection(){
        mySqLite = new SQLite(getContext(),"administracion",null,1);
        bd = mySqLite.getWritableDatabase();
    }

    private void consultar(){
        conection();
        String et_code = code.getText().toString();
        Cursor fila = bd.rawQuery("Select marca,modelo,anio,kilometraje,precio,duenio from vehiculo where codigo="+et_code,null);
        if (!fila.moveToFirst()){
            print("No hay vehículos registrados con ese código");
            return;
        }

        combrand.setText("MARCA: "+brands.get(Integer.parseInt(fila.getString(0))).toString());
        commodel.setText("MODELO: "+fila.getString(1));
        comyear.setText("AÑO: "+year.get(Integer.parseInt(fila.getString(2))).toString());
        anio=Integer.parseInt(year.get(Integer.parseInt(fila.getString(2))).toString());
        commiliage.setText("KILOMETRAJE: "+fila.getString(3));
        kilo=Integer.parseInt(fila.getString(3));
        comcost.setText("PRECIO: "+fila.getString(4));
        precio=Integer.parseInt(fila.getString(4));
        comowner.setText("N° DE DUEÑOS: "+owner.get(Integer.parseInt(fila.getString(5))).toString());
        duenio=Integer.parseInt(owner.get(Integer.parseInt(fila.getString(5))).toString());
        bd.close();

    }

}