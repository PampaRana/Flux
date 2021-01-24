package com.velectico.rbm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class BeatDateActivity extends AppCompatActivity {

    RecyclerView rv_date_list;
    BeatDateAdapter beatDateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beat_date);
        rv_date_list=findViewById(R.id.rv_date_list);
        rv_date_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        

    }
}