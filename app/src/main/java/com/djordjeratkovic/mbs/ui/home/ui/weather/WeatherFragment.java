package com.djordjeratkovic.mbs.ui.home.ui.weather;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.FragmentWeatherBinding;
import com.djordjeratkovic.mbs.model.WeatherAPI;
import com.djordjeratkovic.mbs.ui.login_register.LoginActivity;
import com.djordjeratkovic.mbs.util.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WeatherFragment extends Fragment {
    //TODO: popravi layout

    private FragmentWeatherBinding binding;
    private WeatherViewModel viewModel;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.context = getContext();

        setObserver();
        setListener();

        setupMenu();

        return root;
    }

    private void setObserver() {
        viewModel.getWeather().observe(getViewLifecycleOwner(), new Observer<WeatherAPI>() {
            @Override
            public void onChanged(WeatherAPI weatherAPI) {
                if (weatherAPI != null) {
                    if (weatherAPI.getWeather() != null) {
                        binding.setWeather(weatherAPI.getWeather());
                        binding.city.setError(null);
                    }
                } else {
                    binding.city.setError(getString(R.string.failed_to_find_city));
                }
            }
        });
    }

    private void setListener() {
        binding.getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.checkField(binding.cityEditText, binding.city, getContext())) {
                    viewModel.checkWeather(binding.cityEditText.getText().toString().trim());
                    binding.city.setError(null);
                } else {
                    binding.city.setError(getString(R.string.popunite_polje));
                }
            }
        });
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.weather_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                                .setMessage(context.getResources().getString(R.string.logout_question))
                                .setPositiveButton(R.string.potvrdi, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        viewModel.logout();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton(R.string.otkazi, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        builder.show();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}