package com.example.android.myandroidfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RelaxListActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax);

        TextView title = (TextView) findViewById(R.id.activityTitle1);
        title.setText("This is Activity Relax");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                    switch (item.getItemId()) {
                        case R.id.ic_task:
                            Intent intent2 = new Intent(RelaxListActivity.this, TaskListActivity.class);
                            startActivity(intent2);
                            break;

                        case R.id.ic_timer:
                            Intent intent1 = new Intent(RelaxListActivity.this, MainActivity.class);
                            startActivity(intent1);
                            break;

                        case R.id.ic_relax:

                            break;
                    }
                    return false;



            }
        });
    }
}
