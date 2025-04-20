package com.example.doit1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.collections.ArrayDeque;

public class AssignTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AssignTask";
    private ImageButton btnTaskImage;
    private EditText etTaskName;
    private Spinner spinnerWorker1, spinnerWorker2, spinnerWorker3, timestart, timeend;
    private Button btnSubmit;
    private DatabaseReference databaseReference,databaseReferenceEmployee;
    private StorageReference storageReference;

    private List<Employee> mEmployeeList;
    private List<TaskModel> taskList;

    List<String> employeeNames = new ArrayList<>();

    private ImageView img1,img2,img3;
    int index =0;
    private String outputFile;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private ImageButton btnRecord, btnStop, btnPlay;

    private Uri imageUri, imageUri1, imageUri2, imageUri3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigntask);

        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        storageReference = FirebaseStorage.getInstance().getReference("TaskImages");
        databaseReferenceEmployee = FirebaseDatabase.getInstance().getReference("Employees");
        mEmployeeList = new ArrayList<>();
        taskList = new ArrayList<>();
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);

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


        etTaskName = findViewById(R.id.etTaskName);
        btnTaskImage = findViewById(R.id.btnTaskImage);
        spinnerWorker1 = findViewById(R.id.spinnerWorker1);
        spinnerWorker2 = findViewById(R.id.spinnerWorker2);
        spinnerWorker3 = findViewById(R.id.spinnerWorker3);
        timestart = findViewById(R.id.timestart);
        timeend = findViewById(R.id.timeend);
        btnSubmit = findViewById(R.id.button);

        btnRecord = findViewById(R.id.image_btn_record);
        btnStop = findViewById(R.id.image_btn_stop);
        btnPlay = findViewById(R.id.image_btn_play);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRecording.3gp";
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder!=null){
                    stopRecording();
                    btnRecord.setImageResource(android.R.drawable.ic_btn_speak_now);
                }else {
                    btnRecord.setImageResource(android.R.drawable.ic_media_pause);
                    startRecording();
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

                if (mediaPlayer==null){
                    playRecording();

                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                }else {
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

            }
        });
        fetchTasks();
        fetchEmployees();
        spinnerWorker1.setOnItemSelectedListener(this);
        spinnerWorker2.setOnItemSelectedListener(this);
        spinnerWorker3.setOnItemSelectedListener(this);

        btnTaskImage.setOnClickListener(v -> {
            openGallery();
            index=5;
        });

        btnSubmit.setOnClickListener(v -> saveTaskToFirebase());
    }
    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    private void loadSpinners() {
//        ArrayAdapter<CharSequence> adName = ArrayAdapter.createFromResource(
//                this, R.array.The_First_Worke, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adTime = ArrayAdapter.createFromResource(
                this, R.array.time, android.R.layout.simple_spinner_item);

//        adName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Adapter and spinner setup
        ArrayAdapter<String> adName = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, employeeNames);
//        adName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adName.setDropDownViewResource(R.layout.custom_spinner_items);

        spinnerWorker1.setAdapter(adName);
        spinnerWorker2.setAdapter(adName);
        spinnerWorker3.setAdapter(adName);
        timestart.setAdapter(adTime);
        timeend.setAdapter(adTime);
    }
    private void fetchEmployees() {
        databaseReferenceEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mEmployeeList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    com.example.doit1.Employee employee = taskSnapshot.getValue(com.example.doit1.Employee.class);
                    if (employee != null) {
                        // Log.d(TAG, "onDataChange: Task"+employee);
                        //mEmployeeList.add(employee);
                        employeeNames.add(employee.getName());

                    }
                }
                loadSpinners();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssignTask.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void fetchTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    com.example.doit1.TaskModel taskModel = taskSnapshot.getValue(com.example.doit1.TaskModel.class);
                    if (taskModel != null) {
                        // Log.d(TAG, "onDataChange: Task"+employee);
                        taskList.add(taskModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssignTask.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveTaskToFirebase() {
        String taskName = etTaskName.getText().toString();
        String worker1 = spinnerWorker1.getSelectedItem().toString();
        String worker2 = spinnerWorker2.getSelectedItem().toString();
        String worker3 = spinnerWorker3.getSelectedItem().toString();
        int timeFrom = Integer.parseInt(timestart.getSelectedItem().toString());
        int timeTo = Integer.parseInt(timeend.getSelectedItem().toString());

        if (taskName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Task Name", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskId = "T"+(taskList.size()+1);//databaseReference.push().getKey();
        Log.d(TAG, "saveTaskToFirebase: taskid"+taskId);
        uploadFilesToFirebase(taskId, taskName, worker1, worker2, worker3, timeFrom, timeTo);
    }

    private void uploadFilesToFirebase(String taskId, String taskName, String worker1, String worker2, String worker3, int timeFrom, int timeTo) {

        uploadFile(imageUri, taskId, "image", uri -> {
            String imageUrlFinal = uri.toString();
            uploadFile(imageUri1, taskId, "image1", uri1 -> {
                String imageUrl1Final = uri1.toString();

                uploadFile(imageUri2, taskId, "image2", uri2 -> {
                    String imageUrl2Final = uri2.toString();

                    uploadFile(imageUri3, taskId, "image3", uri3 -> {
                        String imageUrl3 = uri3.toString();
                        TaskModel taskData = new TaskModel(taskId, taskName, worker1, worker2, worker3, timeFrom, timeTo, imageUrlFinal, imageUrl1Final, imageUrl2Final, imageUrl3, imageUrl3);
                        Log.d(TAG, "uploadFilesToFirebase: Read to Save Task");
                        saveTaskData(taskData);
                        fetchTasks();
                    });
                });
            });
        });
    }

    private void uploadFile(Uri fileUri, String taskId, String fileType, OnUploadCompleteListener listener) {
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
                        startActivity(new Intent(AssignTask.this, homesupervisor.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Assign Task", Toast.LENGTH_SHORT).show();
                    }
                });
    }

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launchSomeActivity.launch(intent);
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        switch (index) {
                            case 0:
                                imageUri = selectedImageUri;
                                img1.setImageURI(selectedImageUri);
                                break;
                            case 1:
                                imageUri1 = selectedImageUri;
                                img2.setImageURI(selectedImageUri);
                                break;
                            case 2:
                                imageUri2 = selectedImageUri;
                                img3.setImageURI(selectedImageUri);
                                break;
                            default:
                                imageUri3 = selectedImageUri;
                                btnTaskImage.setImageURI(selectedImageUri);
                                break;
                        }
                    }
                }
            });


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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: spinnerWorker1 "+employeeNames.get(position));

        Toast.makeText(this, "Selected: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: spinnerWorker1 "+employeeNames.get(0));

    }
}
