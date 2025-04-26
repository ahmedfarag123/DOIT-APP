package com.example.doit1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        codeInput = findViewById(R.id.code);
        button = findViewById(R.id.btn_edit);

        button.setOnClickListener(v -> {
            String enteredCode = codeInput.getText().toString().trim();

            if (enteredCode.isEmpty()) {
                Toast.makeText(login_by_code.this, "الرجاء إدخال الكود", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference("Employees");
            employeesRef.orderByChild("code").equalTo(enteredCode)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                                    String empId = employeeSnapshot.getKey();
                                    String empName = employeeSnapshot.child("name").getValue(String.class);
                                    String empMobile = employeeSnapshot.child("mobile").getValue(String.class);
                                    String empEmail = employeeSnapshot.child("email").getValue(String.class);
                                    String empCode = employeeSnapshot.child("code").getValue(String.class);

                                    // تخزين البيانات في SharedPreferences
                                    SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("id", empId);
                                    editor.putString("name", empName);
                                    editor.putString("email", empEmail);
                                    editor.putString("mobile", empMobile);
                                    editor.putString("code", empCode);
                                    editor.apply();

                                    // الانتقال للصفحة التالية
                                    Intent intent = new Intent(login_by_code.this, Employee_profile.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            } else {
                                Toast.makeText(login_by_code.this, "لم يتم العثور على بيانات الموظف", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login_by_code.this, "حدث خطأ في الاتصال بقاعدة البيانات", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}