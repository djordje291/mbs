package com.djordjeratkovic.mbs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.ActivityHomeBinding;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigationEmployee, R.id.navigationCustomer, R.id.navigationWeather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setListeners();
    }

    private void setListeners() {
        viewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

}