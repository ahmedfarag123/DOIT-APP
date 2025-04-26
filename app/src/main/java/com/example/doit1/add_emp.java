package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class add_emp extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText nameInput, emailInput, mobileInput;
    private Spinner nationalitySpinner;
    private Button addButton;
    private TextView personalCodeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_emp);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees");

        // Initialize UI components
        nameInput = findViewById(R.id.nameinput);
        emailInput = findViewById(R.id.emailinput);
        mobileInput = findViewById(R.id.mobileinput);
        nationalitySpinner = findViewById(R.id.nationalitySpinner);
        addButton = findViewById(R.id.addButton);
        personalCodeTextView = findViewById(R.id.personalCode);  // Display the unique employee code

        // Nationalities array
        String[] nationalities = {
                "American", "Brazilian", "Canadian", "Chinese", "Egyptian",
                "French", "German", "Indian", "Japanese", "Mexican",
                "Russian", "South African", "Turkish", "British"
        };

        // Set up spinner for nationality selection
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nationalities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(adapter);

        // Generate a random UUID as a unique employee code
        String randomEmployeeCode = UUID.randomUUID().toString();
        personalCodeTextView.setText(randomEmployeeCode); // Display the generated code

        // Add employee when the "Add" button is clicked
        addButton.setOnClickListener(v -> addEmployee());
    }

    private void addEmployee() {
        String name = nameInput.getText().toString().trim();
        String nationality = nationalitySpinner.getSelectedItem().toString();
        String email = emailInput.getText().toString().trim();
        String mobile = mobileInput.getText().toString().trim();

        // Validate input data
        if (name.isEmpty() || name.length() < 3) {
            Toast.makeText(this, "Please enter a valid name (at least 3 characters)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty() || !email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mobile.isEmpty() || mobile.length() < 9) {
            Toast.makeText(this, "Please enter a valid mobile number!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique employee ID
        String employeeId = databaseReference.push().getKey();

        // Create employee data map
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("employeeId", employeeId);
        employeeData.put("name", name);
        employeeData.put("email", email);
        employeeData.put("mobile", mobile);
        employeeData.put("nationality", nationality);
//        employeeData.put("employeeCode", personalCodeTextView.getText().toString());
        employeeData.put("code", personalCodeTextView.getText().toString());

        // Save employee data to Firebase
        databaseReference.child(employeeId).setValue(employeeData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Employee added: " + name + " (" + nationality + ")", Toast.LENGTH_SHORT).show();

                        // Create an intent to navigate to sowhallemployee activity
                        Intent intent = new Intent(add_emp.this, sowhallemployee.class);
                        intent.putExtra("EMPLOYEE_NAME", name);  // Send the employee's name
                        intent.putExtra("EMPLOYEE_NATIONALITY", nationality);  // Send the employee's nationality
                        startActivity(intent);  // Start the next activity
                    } else {
                        Toast.makeText(this, "Failed to add employee!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
