package com.example.ridowanahmed.childlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ridowanahmed.childlocator.Dashboard.ChildDashboard;
import com.example.ridowanahmed.childlocator.Login.ChildLoginActivity;
import com.example.ridowanahmed.childlocator.Login.ParentLoginActivity;
import com.example.ridowanahmed.childlocator.Map.ChildMap;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void parentLoginBtn(View view){
        Intent intent = new Intent(MainActivity.this,ParentLoginActivity.class);
        startActivity(intent);
    }

    public void childLoginBtn(View view){
        Intent intent = new Intent(MainActivity.this,ChildLoginActivity.class);
        startActivity(intent);
//        startActivity(new Intent(MainActivity.this, ChildDashboard.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
