package com.example.doit1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> mEmployees;
    private Context context;

    public EmployeeAdapter(List<Employee> mEmployees, Context context) {
        this.mEmployees = mEmployees;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = mEmployees.get(position);
        holder.name.setText(employee.getName());
        holder.email.setText(employee.getEmail());
        holder.mobile.setText(employee.getMobile());
        holder.nationality.setText(employee.getNationality());
        holder.employeeCode.setText(employee.getEmployeeCode());
    }

    @Override
    public int getItemCount() {
        return mEmployees.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, mobile, nationality, employeeCode;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            email = itemView.findViewById(R.id.textEmail);
            mobile = itemView.findViewById(R.id.textMobile);
            nationality = itemView.findViewById(R.id.textNationality);
            employeeCode = itemView.findViewById(R.id.textEmployeeCode);
        }
    }
}
