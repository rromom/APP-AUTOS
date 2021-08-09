package com.example.autos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText name, id,code,user,pass,confirm;
    private Button singup;
    View view;
    private  SQLiteDatabase BD;

    public Registro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registro.
     */
    // TODO: Rename and change types and number of parameters
    public static Registro newInstance(String param1, String param2) {
        Registro fragment = new Registro();
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
        view =  inflater.inflate(R.layout.fragment_registro, container, false);
        name = (EditText) view.findViewById(R.id.regname);
        id = (EditText) view.findViewById(R.id.regid);
        code = (EditText) view.findViewById(R.id.regcode);
        pass = (EditText) view.findViewById(R.id.regpass);
        confirm = (EditText) view.findViewById(R.id.regconfirm);
        user = (EditText) view.findViewById(R.id.reguser);
        singup = (Button) view.findViewById(R.id.regbtn);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || id.getText().toString().isEmpty() || code.getText().toString().isEmpty() ||
                pass.getText().toString().isEmpty() || confirm.getText().toString().isEmpty() || user.getText().toString().isEmpty() ){
                    print("ALGUNO DE LOS CAMPOS SE ENCUENTRAN VACIOS");
                    return;
                }

                if (!validadorDeCedula(id.getText().toString())){
                    print("CEDULA NO VALIDA");
                    return;
                }

                if (!PassCheck(pass.getText().toString())){
                    print("La contraseña tiene que tener minimo 8 caracteres, 1 Mayuscula y al menos un numero");
                    return;
                }

                if (!pass.getText().toString().equals(confirm.getText().toString())){
                    print("Las constraseñas no coinciden");
                    return;
                }

                insert();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Logeo myLogeo = new Logeo();
                ft.replace(R.id.mainfrag,myLogeo);
                ft.commit();

            }
        });
        return  view;
    }



    public void insert() {
        this.BD = MainActivity.BD;
        String et_name,et_id,et_code,et_user,et_pass;
        et_name = name.getText().toString();
        et_id = id.getText().toString();
        et_code = code.getText().toString();
        et_user = user.getText().toString();
        et_pass = pass.getText().toString();
        ContentValues values = new ContentValues();
        values.put("codigo",et_code);
        values.put("nombre",et_name);
        values.put("cedula",et_id);
        values.put("usuario",et_user);
        values.put("password",et_pass);
        BD.insert("usuarios",null,values);
        BD.close();
        clean();
        print("Usuario Registrado");
    }

    public void clean(){
        name.setText("");
        id.setText("");
        code.setText("");
        user.setText("");
        pass.setText("");
        confirm.setText("");
    }

    public boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10)
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                    int verificador = Integer.parseInt(cedula.substring(9,10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    }
                    else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            print("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }
        return cedulaCorrecta;
    }

    public boolean PassCheck (String Password) {

        boolean resultado =true;
        int length = 0;
        int numCount = 0;
        int capCount = 0;

        for (int x =0; x < Password.length(); x++) {
            if ((Password.charAt(x) >= 47 && Password.charAt(x) <= 58) || (Password.charAt(x) >= 64 && Password.charAt(x) <= 91) ||
                    (Password.charAt(x) >= 97 && Password.charAt(x) <= 122)) {

            }

            if ((Password.charAt(x) > 47 && Password.charAt(x) < 58)) {
                numCount ++;
            }

            if ((Password.charAt(x) > 64 && Password.charAt(x) < 91)) {
                capCount ++;
            }

            length = (x + 1);

        }

        if (numCount < 1){
            resultado = false;
        }

        if (capCount < 1) {
            resultado = false;
        }

        if (length < 8){
            resultado = false;
        }

        return (resultado);

    }

    public  void print(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }
}