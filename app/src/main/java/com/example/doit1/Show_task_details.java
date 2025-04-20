package com.example.doit1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Show_task_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task_details);
        String taskId = getIntent().getStringExtra("TaskId");

        String taskName = getIntent().getStringExtra("TaskName");
//by:fajer
            Button button = findViewById(R.id.CompleteButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Show_task_details.this, completetask.class);
                    intent.putExtra("TaskId",taskId);
                    intent.putExtra("TaskName",taskName);

                    startActivity(intent);
                }
            });
    }

    public void actionbtn(View view) {
        Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show();
    }
}
