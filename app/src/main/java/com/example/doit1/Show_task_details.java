package com.example.doit1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Show_task_details extends AppCompatActivity {

    ImageView img1, img2, img3;
    Button completeBtn;
    String taskKey; // سيكون فارغ أول مرة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task_details);

        img1 = findViewById(R.id.employee_attachment_img1);
        img2 = findViewById(R.id.employee_attachment_img2);
        img3 = findViewById(R.id.employee_attachment_img3);
        completeBtn = findViewById(R.id.CompleteButton);

        // تحقق إن كان هناك taskKey من intent (هذا فقط عند الرجوع من completetask)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskKey")) {
            taskKey = intent.getStringExtra("taskKey");
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TasksComplete");

        // إذا لم يكن هناك مفتاح مرّر، اجلب أول مهمة (أو حدد من الداتابيز المنطق الذي تريده)
        if (taskKey == null || taskKey.isEmpty()) {
            ref.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        DataSnapshot firstTask = snapshot.getChildren().iterator().next();
                        taskKey = firstTask.getKey();
                        loadTask(taskKey);
                    } else {
                        Toast.makeText(Show_task_details.this, "لا توجد مهام متاحة!", Toast.LENGTH_SHORT).show();
                        setViewsForNoTask();
                    }
                }
                @Override public void onCancelled(@NonNull DatabaseError error) {}
            });
        } else {
            loadTask(taskKey);
        }

        // عندما تضغط صورة وتريد رفع صور (اذهب الى completetask مع ارسال taskKey)
        View.OnClickListener listener = v -> {
            if (taskKey != null && !taskKey.isEmpty()) {
                Intent go = new Intent(Show_task_details.this, completetask.class);
                go.putExtra("taskKey", taskKey);
                startActivity(go);
                finish();  // حتى عند العودة يبدأ من جديد ويتم تحميل الصور الجديدة
            } else {
                Toast.makeText(this, "لم يوجد مفتاح مهمة!", Toast.LENGTH_SHORT).show();
            }
        };

        img1.setOnClickListener(listener);
        img2.setOnClickListener(listener);
        img3.setOnClickListener(listener);
    }

    private void loadTask(String taskKey) {
        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("TasksComplete").child(taskKey);

        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url1 = snapshot.child("imageUrl").getValue(String.class);
                String url2 = snapshot.child("imageUrl2").getValue(String.class);
                String url3 = snapshot.child("imageUrl3").getValue(String.class);

                Log.d("TASK_DEBUG", "url1=" + url1 + ", url2=" + url2 + ", url3=" + url3);

                loadImageToView(img1, url1, 1);
                loadImageToView(img2, url2, 2);
                loadImageToView(img3, url3, 3);

                boolean imagesExist = url1 != null && !url1.isEmpty()
                        && url2 != null && !url2.isEmpty()
                        && url3 != null && !url3.isEmpty();

                if (imagesExist) {
                    completeBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
                    completeBtn.setEnabled(true);
                } else {
                    completeBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
                    completeBtn.setEnabled(false);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadImageToView(ImageView imageView, String url, int index) {
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GLIDE", "Image " + index + " Load Failed: " + url, e);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("GLIDE", "Image " + index + " Loaded successfully!");
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void setViewsForNoTask() {
        completeBtn.setEnabled(false);
    }
}