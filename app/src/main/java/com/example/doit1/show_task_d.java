package com.example.doit1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class show_task_d extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task_d);


        TextView time = findViewById(R.id.time);
        @SuppressLint("WrongViewCast") ImageView checkIcon = findViewById(R.id.img_check);
        RatingBar ratingBar = findViewById(R.id.ratingBar1);
        ImageView playButton = findViewById(R.id.play_button);
        ImageView closeButton = findViewById(R.id.close_button);


        time.setText("5:30 - 6:15");


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(show_task_d.this, "Rated: " + rating, Toast.LENGTH_SHORT).show();
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(show_task_d.this,homesupervisor.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
