package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginsupervisor extends AppCompatActivity {
    EditText name , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_supervisor);
        name=(EditText) findViewById(R.id.editTextText3);
        password=(EditText) findViewById(R.id.editTextTextPassword);
        Button button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inputUsername=name.getText().toString();
                String inputPassword=password.getText().toString();
                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(loginsupervisor.this, "Please enter your username and password.", Toast.LENGTH_SHORT).show();}
                else {
                    String correctUsername = "doit90";
                    String correctPassword = "1234567";
                    if (inputUsername.equals(correctUsername) && inputPassword.equals(correctPassword)) {
                        Toast.makeText(loginsupervisor.this, "You have been logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(loginsupervisor.this, homesupervisor.class);
                        startActivity(intent);}
                    else {
                        Toast.makeText(loginsupervisor.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();

                    }}}});}}


