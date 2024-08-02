package com.example.persistencia_de_date_practica2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button guardar;
    TextView nombre, edad;
    Switch aSwitch;
    SharedPreferences data;
    int currentNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        data = getSharedPreferences("data", Context.MODE_PRIVATE);





    }

    private void Load(){

        String name = data.getString("nombre", "");
        int age = data.getInt("edad", 0);
        boolean mode = data.getBoolean("mode", false);

        if(name.isEmpty() || age == 0){
           return;
        }

        nombre.setText(name);
        edad.setText(String.valueOf(age));



        if(mode){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return;
        }

        aSwitch.setChecked(false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }



    private Boolean check(TextView [] textViews){
        for(TextView i : textViews){
            if(i.getText().toString().isEmpty()){
                Toast.makeText(this, "No se ha ingresado datos", Toast.LENGTH_SHORT).show();
                return false;
            }

            try{
                int age = Integer.parseInt(i.getText().toString());
                if((age < 0) || (age > 100)){
                    Toast.makeText(this, "Edad incorrecta", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }catch (NumberFormatException e){
                System.out.println("Error controlado");
            }

        }
        return  true;

    }

    public void accion(View view){
        if(!check(new TextView[]{nombre, edad})) return;

        SharedPreferences.Editor editor = data.edit();

        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        editor.putString("nombre", nombre.getText().toString());
        editor.putInt("edad", Integer.parseInt(edad.getText().toString()));
        editor.putInt("currMod", currentNightMode);
        editor.putBoolean("mode", aSwitch.isChecked());
        editor.apply();
        Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        guardar = findViewById(R.id.save);
        nombre = findViewById(R.id.name);
        edad = findViewById(R.id.age);
        aSwitch = findViewById(R.id.aSwitch);

        Load();



        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(buttonView.isPressed()) {
                        SharedPreferences.Editor editor = data.edit();
                        if (isChecked) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            editor.putBoolean("mode", aSwitch.isChecked());
                            editor.apply();
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            editor.putBoolean("mode", aSwitch.isChecked());
                            editor.apply();
                        }
                    }



                }
            );







    }
}