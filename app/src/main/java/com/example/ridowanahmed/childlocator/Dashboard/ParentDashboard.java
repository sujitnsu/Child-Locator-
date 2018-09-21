package com.example.ridowanahmed.childlocator.Dashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ridowanahmed.childlocator.Map.ParentMap;
import com.example.ridowanahmed.childlocator.R;

public class ParentDashboard extends AppCompatActivity {
    private Button button_trackChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        button_trackChild = (Button) findViewById(R.id.button_track_child);
        button_trackChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParentDashboard.this, ParentMap.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
