package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class homesupervisor extends AppCompatActivity {
    private static final String TAG = "homesupervisor";

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<TaskModel> taskList;
    private DatabaseReference databaseReference;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_supervisor);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");


        fetchTasks();


        addButton = findViewById(R.id.imageButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(homesupervisor.this, AssignTask.class);
            startActivity(intent);
        });
    }

    private void fetchTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    TaskModel task = taskSnapshot.getValue(TaskModel.class);
                    if (task != null) {
                        Log.d(TAG, "onDataChange: Task"+task);
                        taskList.add(task);
                    }
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(homesupervisor.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void profile(View view) {
        Intent intent = new Intent(homesupervisor.this, profilesupervisor.class);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent = new Intent(homesupervisor.this, homesupervisor.class);
        startActivity(intent);
    }
}
