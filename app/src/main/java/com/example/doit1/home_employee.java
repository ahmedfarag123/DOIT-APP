package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home_employee extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseReference databaseReference;
    private String employeeId;
    private ImageView surfaces, bathroom, profileButton, homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_employee);

        // جلب ID الموظف المسجل حاليًا من Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            employeeId = currentUser.getUid();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // تهيئة RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);

        // ربط الأزرار
        bathroom.setOnClickListener(view -> openTaskDetails());
        surfaces.setOnClickListener(view -> openTaskDetails());
        profileButton = findViewById(R.id.profileButton);
        homeButton = findViewById(R.id.homeButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // تحميل المهام المسندة للموظف
        loadEmployeeTasks();

        // أزرار التنقل
        bathroom.setOnClickListener(view -> openTaskDetails());
        surfaces.setOnClickListener(view -> openTaskDetails());
        profileButton.setOnClickListener(view -> openProfile());
        homeButton.setOnClickListener(view -> reloadHome());
    }

    private void loadEmployeeTasks() {
        databaseReference.child("Employees").child(employeeId).child("tasks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                                String taskId = taskSnapshot.getValue(String.class);
                                if (taskId != null) {
                                    loadTaskDetails(taskId);
                                }
                            }
                        } else {
                            Toast.makeText(home_employee.this, "No assigned tasks.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to load employee tasks", error.toException());
                        Toast.makeText(home_employee.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadTaskDetails(String taskId) {
        databaseReference.child("Tasks").child(taskId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TaskModel task = snapshot.getValue(TaskModel.class);
                        if (task != null) {
                            taskAdapter.assignTask(task);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to load task details", error.toException());
                    }
                });
    }

    private void openTaskDetails() {
        Intent intent = new Intent(home_employee.this, Show_task_details.class);
        startActivity(intent);
    }

    private void reloadHome() {
        Intent intent = new Intent(home_employee.this, home_employee.class);
        startActivity(intent);
        finish();
    }

    private void openProfile() {
        Intent intent = new Intent(home_employee.this, Employee_profile.class);
        startActivity(intent);
    }
}
