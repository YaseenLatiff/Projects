package com.said.icare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.said.icare.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavView =findViewById(R.id.bottom_nav_view);
        AppBarConfiguration appConfig = new AppBarConfiguration.Builder(R.id.profile_Fragment,R.id.appointment_Fragment,R.id.groups_Fragment).build();
        NavController navCont = Navigation.findNavController(this,R.id.nav_fragment2);
        NavigationUI.setupActionBarWithNavController(this,navCont,appConfig);
        NavigationUI.setupWithNavController(binding.bottomNavView, navCont);

    }

}