package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class Home extends AppCompatActivity {
BottomNavigationView bottomNavigationView;

//private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment f = null;
//        switch (item.getItemId()){
//            case R.id.frag_home:
//                f= new FragmentHome();
//                break;
//            case R.id.frag_points:
//                f= new FragmentPoints();
//                break;
//            case R.id.frag_rewards:
//                f= new FragmentRewards();
//                break;
//            case R.id.frag_more:
//                f=new FragmentMore();
//                break;
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, f).commit();
//        return true;
//    }
//};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment, new FragmentHome()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          //Fragment selectedFragment = null;
          switch ((item.getItemId())){
              case R.id.frag_home:
                  getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new FragmentHome()).commit();
                  return true;

              case R.id.frag_points:
                  //selectedFragment = new FragmentPoints();
                  getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new FragmentPoints()).commit();
                    return true;
              case R.id.frag_rewards:
                  //selectedFragment = new FragmentRewards();
                  getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new FragmentRewards()).commit();
                  return true;
              case R.id.frag_more:
                  //selectedFragment = new FragmentMore();
                  getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new FragmentMore()).commit();
                  return true;
          }
          //getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,selectedFragment).commit();
          return false;
        }
    };

    public void home(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
