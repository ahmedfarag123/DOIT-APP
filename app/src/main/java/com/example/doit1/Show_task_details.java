package com.example.doit1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Show_task_details extends AppCompatActivity {

    ImageView img1, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task_details);

        img1 = findViewById(R.id.employee_attachment_img1);
        img2 = findViewById(R.id.employee_attachment_img2);
        img3 = findViewById(R.id.employee_attachment_img3);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TasksComplete");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    // نحصل على أول مهمة ونستخرج الصور
                    DataSnapshot firstTask = snapshot.getChildren().iterator().next();

                    String url1 = firstTask.child("imageUrl").getValue(String.class);
                    String url2 = firstTask.child("imageUrl2").getValue(String.class);
                    String url3 = firstTask.child("imageUrl3").getValue(String.class);

                    // تحميل الصور إذا موجودة
                    if (url1 != null && !url1.isEmpty())
                        Glide.with(Show_task_details.this).load(url1).into(img1);
                    if (url2 != null && !url2.isEmpty())
                        Glide.with(Show_task_details.this).load(url2).into(img2);
                    if (url3 != null && !url3.isEmpty())
                        Glide.with(Show_task_details.this).load(url3).into(img3);

                } else {
                    Toast.makeText(Show_task_details.this, "لا يوجد بيانات مهام!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Show_task_details.this, "فشل الاتصال بالقاعدة", Toast.LENGTH_SHORT).show();
            }
        });
    }
}