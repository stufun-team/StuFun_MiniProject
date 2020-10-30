package com.example.stufun.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.example.stufun.Student.StudentHomeFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView menuBar;
    static final float END_SCALE = 0.5f;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private RelativeLayout contentview;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_bar);

        if(Prevalent.type.equals("Teacher"))
        {
            Fragment fragment = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit();
        }
        else{
            Fragment fragment = new StudentHomeFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit();
        }

        menuBar = findViewById(R.id.menu_bar);
        contentview = findViewById(R.id.content_hoder);

        navigationDrawer();

    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_bar);

        menuBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateDrawer();
    }

    private void animateDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffscale = slideOffset * (1-END_SCALE);
                final float setscle = 1 - diffscale;
                contentview.setScaleY(setscle);
                contentview.setScaleX(setscle);

                final float xset = drawerView.getWidth() * slideOffset;
                final float xsetdiff = contentview.getWidth() * diffscale/4;
                final float xtrans = xset - xsetdiff;
                contentview.setTranslationX(xtrans);

            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                .commit();
    }
}