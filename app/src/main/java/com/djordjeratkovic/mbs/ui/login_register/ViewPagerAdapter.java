package com.djordjeratkovic.mbs.ui.login_register;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.djordjeratkovic.mbs.ui.login_register.login.LoginFragment;
import com.djordjeratkovic.mbs.ui.login_register.register.RegisterFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new RegisterFragment();
        }
        return new LoginFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}