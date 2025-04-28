package com.example.doit1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Employee_profile extends AppCompatActivity {

    EditText nameEditText, codeEditText, mobileEditText, emailEditText;
    Button editButton;
    boolean isEditing = false;
    String empId, empCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile);

        nameEditText   = findViewById(R.id.name);
        codeEditText   = findViewById(R.id.code);
        mobileEditText = findViewById(R.id.mobile_number);
        emailEditText  = findViewById(R.id.email);
        editButton     = findViewById(R.id.btn_login);

        // جلب البيانات من SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        empId    = prefs.getString("id", "");
        empCode  = prefs.getString("code", ""); // ممكن تحتاج تضيف تخزين للكود في صفحة تسجيل الدخول لو ضروري
        String empName  = prefs.getString("name", "");
        String empEmail = prefs.getString("email", "");
        String empMobile = prefs.getString("mobile", "");

        // عرض البيانات
        nameEditText.setText(empName);
        emailEditText.setText(empEmail);
        mobileEditText.setText(empMobile);
        codeEditText.setText(empCode);

        setFieldsEditable(false);

        editButton.setOnClickListener(v -> {
            if (!isEditing) {
                setFieldsEditable(true);
                editButton.setText("Save");
                isEditing = true;
            } else {
                // حفظ البيانات بعد التعديل
                String newName   = nameEditText.getText().toString().trim();
                String newEmail  = emailEditText.getText().toString().trim();
                String newMobile = mobileEditText.getText().toString().trim();

                if (newName.isEmpty() || newEmail.isEmpty() || newMobile.isEmpty()) {
                    Toast.makeText(this, "الرجاء إدخال جميع البيانات", Toast.LENGTH_SHORT).show();
                    return;
                }


                // تحقق أن رقم الهاتف 10 أرقام فقط
                if (newMobile.length() != 10 || !newMobile.matches("\\d{10}")) {
                    Toast.makeText(this, "رقم الهاتف يجب أن يكون 10 أرقام فقط!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(empId != null && !empId.isEmpty()) {
                    DatabaseReference empRef = FirebaseDatabase.getInstance()
                            .getReference("Employees").child(empId);

                    empRef.child("name").setValue(newName);
                    empRef.child("email").setValue(newEmail);
                    empRef.child("mobile").setValue(newMobile)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // تحديث SharedPreferences
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("name", newName);
                                    editor.putString("email", newEmail);
                                    editor.putString("mobile", newMobile);
                                    editor.apply();

                                    Toast.makeText(this, "تم تحديث البيانات بنجاح", Toast.LENGTH_SHORT).show();
                                    setFieldsEditable(false);
                                    editButton.setText("Edit");
                                    isEditing = false;
                                } else {
                                    Toast.makeText(this, "حدث خطأ أثناء التحديث", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "حدثت مشكلة في الحصول على بيانات المستخدم!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ----- شريط التنقل السفلي -----
        ImageView iconHome     = findViewById(R.id.icon_home);
//        ImageView iconSetting  = findViewById(R.id.icon_setting);
        ImageView iconSign     = findViewById(R.id.icon_sign);

        iconHome.setOnClickListener(v -> {
            // استبدل MainActivity باسم الصفحة الرئيسية عندك
            Intent intent = new Intent(Employee_profile.this, Show_task_details.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

//        iconSetting.setOnClickListener(v -> {
//            // استبدل SettingsActivity بصفحة الإعدادات عندك
//            Intent intent = new Intent(Employee_profile.this, Employee_profile.class);
//            startActivity(intent);
//        });

        iconSign.setOnClickListener(v -> {
            // تسجيل خروج ومسح البيانات
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // إعادة توجيه لصفحة تسجيل الدخول
            Intent intent = new Intent(Employee_profile.this, login_by_code.class);
            startActivity(intent);
            finish();
        });
    }

    private void setFieldsEditable(boolean enabled) {
        nameEditText.setEnabled(enabled);
        emailEditText.setEnabled(enabled);
        mobileEditText.setEnabled(enabled);
        codeEditText.setEnabled(false);
    }
}