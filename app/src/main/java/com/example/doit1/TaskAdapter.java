package com.example.doit1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> taskList;
    private Context context;

    public TaskAdapter(List<TaskModel> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    public TaskAdapter(home_employee homeEmployee) {
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.assignedEmployees.setText("Assigned: " + task.getAssignedEmployees());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), Show_task_details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("TaskId",task.getTaskId());
                intent.putExtra("TaskName",task.getTaskName());
                context.startActivity(intent);
            }
        });
        holder.time.setText("Time: " + task.getTimeFrom() + " - " + task.getTimeTo());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, assignedEmployees, time;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskname);
            assignedEmployees = itemView.findViewById(R.id.text_assignees);
            time = itemView.findViewById(R.id.time);

        }

    }
    public void assignTask(TaskModel task) {
        if (taskList != null) {
            taskList.add(task);
            notifyItemInserted(taskList.size() - 1); // تحديث السطر الجديد فقط
        }
    }
}


