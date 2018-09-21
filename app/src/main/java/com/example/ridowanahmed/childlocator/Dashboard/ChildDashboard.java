package com.example.ridowanahmed.childlocator.Dashboard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ridowanahmed.childlocator.GPS_Service;
import com.example.ridowanahmed.childlocator.Map.ChildMap;
import com.example.ridowanahmed.childlocator.R;
import com.example.ridowanahmed.childlocator.services.NewService;

public class ChildDashboard extends AppCompatActivity {
    private Button button_showLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ridowanahmed.childlocator.R.layout.activity_child_dashboard);

        button_showLocation = (Button) findViewById(R.id.button_showLocation);
        button_showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildDashboard.this, ChildMap.class));
            }
        });
        

        Intent intent = new Intent(ChildDashboard.this, NewService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
