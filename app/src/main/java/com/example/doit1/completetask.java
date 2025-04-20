package com.example.doit1;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class completetask extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MICROPHONE_PERMISSION_CODE = 200;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private boolean permissionToRecordAccepted = false;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private ImageButton btnRecord, btnStop, btnPlay;
    private Button btnSubmit;
    private ImageView img1, img2, img3;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri1, imageUri2, imageUri3;
    int index = 0;
    private String outputFile, taskId, taskName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completetask);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        taskId = getIntent().getStringExtra("TaskId");
        taskName = getIntent().getStringExtra("TaskName");
        databaseReference = FirebaseDatabase.getInstance().getReference("TasksComplete");
        storageReference = FirebaseStorage.getInstance().getReference("TaskImages");

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                index = 0;
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                index = 1;
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                index = 2;
            }
        });
        Button button = findViewById(R.id.btnSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTaskToFirebase();
            }
        });

        btnRecord = findViewById(R.id.image_btn_record);
        btnStop = findViewById(R.id.image_btn_stop);
        btnStop.setVisibility(View.GONE);
        btnPlay = findViewById(R.id.image_btn_play);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRecording.3gp";
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    if (mediaRecorder != null) {
                        stopRecording();

                        btnRecord.setImageResource(android.R.drawable.ic_btn_speak_now);
                    } else {
                        btnRecord.setImageResource(android.R.drawable.ic_media_pause);
                        startRecording();
                    }

                } else {
                    Log.d(TAG, "onClick: not checkPermissions ");

                    RequestPermissions();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer == null) {
                    playRecording();

                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

            }
        });
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    private void saveTaskToFirebase() {
        if (imageUri1 == null || imageUri2 == null || imageUri3 == null) {
            Toast.makeText(getApplicationContext(), "Please select all images", Toast.LENGTH_SHORT).show();
            return;
        }
        String nTaskId = databaseReference.push().getKey();
        uploadFilesToFirebase(nTaskId, taskName, taskId);
    }

    private void uploadFilesToFirebase(String nTaskId, String taskName, String taskId) {
        uploadFile(imageUri1, taskId, "image1", uri1 -> {
            String imageUrl1Final = uri1.toString();
            uploadFile(imageUri2, taskId, "image2", uri2 -> {
                String imageUrl2Final = uri2.toString();
                uploadFile(imageUri3, taskId, "image3", uri3 -> {
                    String imageUrl3 = uri3.toString();
                    Map<String, Object> newTask = new HashMap<>();
                    newTask.put("id", nTaskId);
                    newTask.put("taskId", taskId);
                    newTask.put("taskName", taskName);
                    newTask.put("imageUrl", imageUrl1Final);
                    newTask.put("imageUrl2", imageUrl2Final);
                    newTask.put("imageUrl3", imageUrl3);
                    newTask.put("status", true);

                    databaseReference.child(nTaskId).setValue(newTask)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Task Assigned Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(completetask.this, home_employee.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to Assign Task", Toast.LENGTH_SHORT).show();
                                }
                            });

                });
            });
        });
    }

    private void uploadFile(Uri fileUri, String taskId, String fileType, AssignTask.OnUploadCompleteListener listener) {
        if (fileUri != null) {
            StorageReference fileRef = storageReference.child(taskId + "_" + fileType + ".jpg");
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(listener::onComplete)
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to get " + fileType + " URL", Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to upload " + fileType, Toast.LENGTH_SHORT).show());
        } else {
            listener.onComplete(null);
        }
    }

    interface OnUploadCompleteListener {
        void onComplete(Uri uri);
    }

    private void saveTaskData(TaskModel taskData) {

        databaseReference.child(taskData.getTaskId()).setValue(taskData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Task Assigned Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(completetask.this, homesupervisor.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Assign Task", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private boolean checkPermissions() {
        int recordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return recordPermission == PackageManager.PERMISSION_GRANTED &&
                writePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();


                    ActivityCompat.requestPermissions(
                            completetask.this,
                            new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE},
                            REQUEST_AUDIO_PERMISSION_CODE
                    );

                } else {
                    Log.d(TAG, "RequestPermissions: NO should");
                    ActivityCompat.requestPermissions(
                            completetask.this,
                            new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE},
                            REQUEST_AUDIO_PERMISSION_CODE
                    );
                }

            } else {

                Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "Permissions not required for this Android version", Toast.LENGTH_SHORT).show();
        }
    }


    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                            if (index == 0) {
                                imageUri1 = selectedImageUri;
                                img1.setImageBitmap(selectedImageBitmap);
                            } else if (index == 1) {
                                imageUri2 = selectedImageUri;
                                img2.setImageBitmap(selectedImageBitmap);
                            } else {
                                imageUri3 = selectedImageUri;

                                img3.setImageBitmap(selectedImageBitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void playRecording() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing recording", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private boolean isMicrophonePresent() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }


}



