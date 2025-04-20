package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Employee_profile extends AppCompatActivity {
    private TextView name, code, mobileNumber, email;
    private Button editButton;
    private ImageView iconHome, iconSettings, sign;


    // تعريف مرجع الفايربيز لعقدة "Employees"
    private DatabaseReference databaseEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile);

        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        mobileNumber = findViewById(R.id.mobile_number);
        email = findViewById(R.id.email);
        editButton = findViewById(R.id.btn_edit);
        iconHome = findViewById(R.id.icon_home);
        iconSettings = findViewById(R.id.icon_setting);
        sign = findViewById(R.id.icon_sign);

        // ربط عقدة "Employees" من قاعدة البيانات
        databaseEmployees = FirebaseDatabase.getInstance().getReference("Employees");

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateInputs()) {
                    saveEmployeeData(); // حفظ بيانات الموظف في الفايربيز
                    Toast.makeText(Employee_profile.this, "Save successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Employee_profile.this, login_by_code.class);
                    startActivity(intent);
                }
            }
        });

        // باقي الكود الخاص بالـ iconHome, iconSettings, sign ..
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        iconSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Employee_profile.class));
                finish();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
    }

    // دالة التحقق من صحة البيانات
    private boolean validateInputs() {
        String nameInput = name.getText().toString().trim();
        String codeInput = code.getText().toString().trim();
        String mobileInput = mobileNumber.getText().toString().trim();
        String emailInput = email.getText().toString().trim();

        if (TextUtils.isEmpty(nameInput)) {
            name.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(codeInput)) {
            code.setError("Code is required");
            return false;
        }

        if (TextUtils.isEmpty(mobileInput)) {
            mobileNumber.setError("Mobile number is required");
            return false;
        }

        if (TextUtils.isEmpty(emailInput)) {
            email.setError("Email is required");
            return false;
        }

        return true;
    }

    // دالة حفظ بيانات الموظف باستخدام push()
    private void saveEmployeeData() {
        // قراءة البيانات من الواجهات
        String empName = name.getText().toString().trim();
        String empCode = code.getText().toString().trim();
        String empMobile = mobileNumber.getText().toString().trim();
        String empEmail = email.getText().toString().trim();

        // توليد مفتاح تلقائي للمستخدم الجديد
        String employeeId = databaseEmployees.push().getKey();

        Employees employees = new Employees(employeeId, empName, empCode, empMobile, empEmail);

        if (employeeId != null) {
            databaseEmployees.child(employeeId).setValue(employees);
        }
    }
}

// كلاس Employee لتخزين بيانات الموظف
class Employees {
    public String id;
    public String name;
    public String code;
    public String mobile;
    public String email;


    // كونستركتر افتراضي ضروري لفايربيز
    public Employees() {
    }

    public Employees(String id, String name, String code, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.email = email;
    }
}