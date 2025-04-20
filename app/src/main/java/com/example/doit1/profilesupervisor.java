package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class profilesupervisor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_supervisor);
        Button employee = findViewById(R.id.button14);
        Button logout = findViewById(R.id.button15);
        ImageView home=findViewById(R.id.imageView23);
        ImageView profile=findViewById(R.id.imageView39);

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profilesupervisor.this, sowhallemployee.class);
                startActivity(intent);}


        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profilesupervisor.this, loginsupervisor.class);
                startActivity(intent);}


        });
    }
    public void profile(View view) {
        Intent intent = new Intent(profilesupervisor.this, profilesupervisor.class);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent = new Intent(profilesupervisor.this, homesupervisor.class);
        startActivity(intent);
    }
}