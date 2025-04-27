package com.example.doit1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class completetask extends AppCompatActivity {
    private ImageView img1, img2, img3;
    private Button btnSubmit;

    private Uri[] imageUris = new Uri[3]; // 0,1,2
    private int imageSelectIndex = -1;

    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference("TaskImages");
    private final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("TasksComplete");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completetask);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        btnSubmit = findViewById(R.id.btnSubmit);

        img1.setOnClickListener(v -> { imageSelectIndex = 0; selectImageFromDevice(); });
        img2.setOnClickListener(v -> { imageSelectIndex = 1; selectImageFromDevice(); });
        img3.setOnClickListener(v -> { imageSelectIndex = 2; selectImageFromDevice(); });

        btnSubmit.setOnClickListener(v -> uploadImagesAndSaveTask());
    }

    private void selectImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null && imageSelectIndex != -1) {
                        imageUris[imageSelectIndex] = selectedImageUri;
                        if (imageSelectIndex == 0)
                            Glide.with(this).load(selectedImageUri).into(img1);
                        else if (imageSelectIndex == 1)
                            Glide.with(this).load(selectedImageUri).into(img2);
                        else if (imageSelectIndex == 2)
                            Glide.with(this).load(selectedImageUri).into(img3);
                    }
                }
            });

    private void uploadImagesAndSaveTask() {
        if (imageUris[0] == null || imageUris[1] == null || imageUris[2] == null) {
            Toast.makeText(this, "اختر الصور الثلاثة أولاً", Toast.LENGTH_SHORT).show();
            return;
        }
        String taskKey = databaseRef.push().getKey();
        if (taskKey == null) {
            Toast.makeText(this, "تعذر إنشاء مهمة جديدة", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadImage(taskKey, 0, imageUrl1 ->
                uploadImage(taskKey, 1, imageUrl2 ->
                        uploadImage(taskKey, 2, imageUrl3 -> {
                            // بعد رفع الثلاث صور نحفظ الداتا في ريلتايم
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", taskKey);
                            map.put("imageUrl", imageUrl1);
                            map.put("imageUrl2", imageUrl2);
                            map.put("imageUrl3", imageUrl3);
                            map.put("status", true);

                            databaseRef.child(taskKey).setValue(map)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(this, "تم حفظ المهمة بنجاح", Toast.LENGTH_SHORT).show();
                                            // انتقل لصفحة Show_task_details مع تمرير المفتاح
                                            Intent intent = new Intent(this, Show_task_details.class);
                                            intent.putExtra("taskKey", taskKey); // taskKey هو Push Key للمهمة
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(this, "فشل حفظ المهمة", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                )
        );
    }

    // رفع صورة واحدة وإرجاع الرابط
    private void uploadImage(String taskKey, int imgIndex, OnImageUploadListener callback) {
        Uri fileUri = imageUris[imgIndex];
        if (fileUri == null) {
            callback.onUploaded("");
            return;
        }
        String imageName = taskKey + "_img" + (imgIndex+1) + "_" + UUID.randomUUID().toString();
        StorageReference imgRef = storageRef.child(imageName);

        imgRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> callback.onUploaded(uri.toString()))
                        .addOnFailureListener(e -> callback.onUploaded("")))
                .addOnFailureListener(e -> callback.onUploaded(""));
    }

    interface OnImageUploadListener { void onUploaded(String url); }
}