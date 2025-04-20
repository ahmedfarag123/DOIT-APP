package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ImageView iconHome = findViewById(R.id.icon_home);
        ImageView iconSettings = findViewById(R.id.icon_setting);
        ImageView sign = findViewById(R.id.icon_sign);
        Button employeeButton = findViewById(R.id.employeeButton);
        Button supervisorButton = findViewById(R.id.supervisorButton);

        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), home_employee.class));
            finish();

        });
        iconSettings.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Employee_profile.class));
            finish();


        });
        sign.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), login_by_code.class));
            finish();


        });

        employeeButton.setOnClickListener(v -> {
            // Navigate to EmployeeActivity
            Intent intent = new Intent(login.this,login_by_code.class);
            startActivity(intent);

        });

        supervisorButton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, homesupervisor.class);
            startActivity(intent);

        });
    }
}
