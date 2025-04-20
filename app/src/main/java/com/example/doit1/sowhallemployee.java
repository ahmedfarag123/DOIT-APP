package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sowhallemployee extends AppCompatActivity {
    private static final String TAG = "sowhallemployee";
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private List<Employee> mEmployeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sowh_all_employee);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEmployeeList = new ArrayList<>();

        ImageButton addbutton = findViewById(R.id.imageButton2);
        ImageButton Entar = findViewById(R.id.imageButton15);
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees");
        fetchEmployees();
        mEmployeeAdapter = new EmployeeAdapter(mEmployeeList, this);
        recyclerView.setAdapter(mEmployeeAdapter);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sowhallemployee.this, add_emp.class);
                startActivity(intent);
            }
        });
        Entar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sowhallemployee.this, profilesupervisor.class);
                startActivity(intent);

            }
        });
    }
    private void fetchEmployees() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mEmployeeList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    com.example.doit1.Employee task = taskSnapshot.getValue(com.example.doit1.Employee.class);
                    if (task != null) {
                        Log.d(TAG, "onDataChange: Task"+task);
                        mEmployeeList.add(task);
                    }
                }
                mEmployeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(sowhallemployee.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }



}

