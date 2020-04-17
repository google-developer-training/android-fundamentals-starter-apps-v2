package com.example.android.materialme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView sportsTitle;
    ImageView sportsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        sportsTitle=findViewById(R.id.detailNewsTitle);
        sportsTitle.setText(getIntent().getStringExtra("title"));
        sportsImage=findViewById(R.id.detailSportsImage);
        Glide.with(this).load(getIntent().getIntExtra("image_resource",0)).into(sportsImage);
    }
}
