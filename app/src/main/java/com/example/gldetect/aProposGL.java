package com.example.gldetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class aProposGL extends AppCompatActivity {
    private static ImageView home_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_propos_glaucome);
        home_img= (ImageView) findViewById(R.id.home_icn);

        final Context context = this;
        home_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
