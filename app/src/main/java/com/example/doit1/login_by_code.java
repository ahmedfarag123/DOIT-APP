package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_by_code extends AppCompatActivity {


    EditText codeInput;
    Button button;
    ImageView iconHome, iconSettings, sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_code);

        // الربط مع الواجهات
        codeInput = findViewById(R.id.code);
        button = findViewById(R.id.btn_edit);
        iconHome = findViewById(R.id.icon_home);
        iconSettings = findViewById(R.id.icon_setting);
        sign = findViewById(R.id.icon_sign);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredCode = codeInput.getText().toString().trim();

                if (enteredCode.isEmpty()) {
                    Toast.makeText(login_by_code.this, "Please enter the code", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference("Employees");

                employeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean codeFound = false;

                        for (DataSnapshot empSnapshot : snapshot.getChildren()) {
                            if (empSnapshot.hasChild("code")) {
                                String code = empSnapshot.child("code").getValue(String.class);

                                if (code != null && code.equals(enteredCode)) {
                                    codeFound = true;

                                    // تسجيل دخول مؤقت باستخدام تسجيل الدخول المجهول
                                    FirebaseAuth.getInstance().signInAnonymously()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(login_by_code.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                                                    // إن Intent للانتقال لصفحة home_employee باستخدام FLAG_ACTIVITY_CLEAR_TOP فقط
                                                    Intent intent = new Intent(login_by_code.this, Show_task_details.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(login_by_code.this, "login failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break;
                                }
                            }
                        }

                        if (!codeFound) {
                            Toast.makeText(login_by_code.this, "Invalid code!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(login_by_code.this, "An error occurred while connecting to the database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // عند الضغط على أيقونة home يتم الانتقال لصفحة home_employee
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_by_code.this, login_by_code.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // عند الضغط على أيقونة settings يتم الانتقال لصفحة Employee_profile
        iconSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_by_code.this, Employee_profile.class));
                finish();
            }
        });

        // عند الضغط على أيقونة sign يتم إعادة تحميل نفس الصفحة
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_by_code.this, login.class));
                finish();
            }
        });
    }
}
