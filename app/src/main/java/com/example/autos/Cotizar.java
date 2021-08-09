package com.example.autos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cotizar extends AppCompatActivity {
    private String cbrand,cmodel,ccost,cyear;
    private TextView name,cost,year,res;
    private EditText input;
    private Spinner months;
    private Button quoute,cancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizar);
        this.cbrand = Vender.cbrand;
        this.cmodel = Vender.cmodel;
        this.ccost = Vender.ccost;
        this.cyear = Vender.cyear;

        name = (TextView) findViewById(R.id.cotmm);
        cost = (TextView) findViewById(R.id.cotcost);
        year = (TextView) findViewById(R.id.cotyear);
        input = (EditText) findViewById(R.id.cotinput);
        months = (Spinner) findViewById(R.id.cotmonths);
        quoute = (Button) findViewById(R.id.cotbtn);
        cancelar = (Button) findViewById(R.id.cotcan);
        res = (TextView) findViewById(R.id.cotres);

        String [] opcs = {"12","24","48","60"};
        ArrayAdapter<String> amonths = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,opcs);
        months.setAdapter(amonths);

        name.setText(cbrand+" "+cmodel);
        cost.setText("PRECIO : "+ccost+"$");
        year.setText("AÑO : "+cyear);

        quoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entrada = input.getText().toString();
                if (entrada.isEmpty()){
                    print("Campo entrada vacio");
                    return;
                }
                double entreada_es = Double.parseDouble(ccost)*0.10;
                if (Integer.parseInt(entrada)<entreada_es){
                    print("La entrada es mayor al 10%="+entreada_es);
                    return;
                }
                int meses = Integer.parseInt(months.getSelectedItem().toString());
                double valor_pagar=Double.parseDouble(ccost)-Double.parseDouble(entrada);
                double cuota = (valor_pagar+(Double.parseDouble(ccost)*0.15*meses))/meses;
                res.setText("La cuota a pagar es: "+cuota);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent can_int = new Intent(Cotizar.this,MAIN.class);
                startActivity(can_int);
            }
        });

    }
    public void print(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}