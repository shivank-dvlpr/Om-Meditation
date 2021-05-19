package com.shivank.om;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GridView grdView;
    static int imgValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        //int[] gridImg = {R.drawable.om, R.drawable.ganeshji, R.drawable.shivji, R.drawable.hanumanji};

        grdView = findViewById(R.id.grdView);

        GridViewAdapter gridViewAdapter = new GridViewAdapter(MainActivity.this);
        grdView.setAdapter(gridViewAdapter);




        grdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                grdOnItemClick(position);
            }
        });


    }

    private void startIntent(){
        startActivity(new Intent(MainActivity.this, MusicActivity.class));
    }

    private void grdOnItemClick(int position){
        switch (position) {

            case 0:
                imgValues = 0;
                startIntent();
                break;

            case 1:
                imgValues = 1;
                startIntent();
                break;

            case 2:
                imgValues = 2;
                startIntent();
                break;

            case 3:
                imgValues = 3;
                startIntent();
                break;

            case 4:
                imgValues = 4;
                startIntent();
                break;

            case 5:
                imgValues = 5;
                startIntent();
                break;

            case 6:
                imgValues = 6;
                startIntent();
                break;

            case 7:
                imgValues = 7;
                startIntent();
                break;

        }


    }

}