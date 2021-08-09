package com.example.autos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static SQLite mySqLite;
    public static SQLiteDatabase BD;
    private FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ft  = getSupportFragmentManager().beginTransaction();
        mySqLite = new SQLite(this,"administracion",null,1);
        BD = mySqLite.getWritableDatabase();
        Cursor fila = BD.rawQuery("select * from usuarios",null);

        if (fila.moveToFirst()){
            Logeo myLogeo = new Logeo();
            ft.replace(R.id.mainfrag,myLogeo);
            ft.commit();
            return;
        }

        Registro myRegistro = new Registro();
        ft.replace(R.id.mainfrag, myRegistro);
        ft.commit();
        return;
    }

    public void print(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
    }

}