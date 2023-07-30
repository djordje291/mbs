package com.djordjeratkovic.mbs.ui.login_register.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.FragmentLoginBinding;
import com.djordjeratkovic.mbs.ui.login_register.login.LoadingFragmentViewModel;
import com.djordjeratkovic.mbs.util.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private Context context;
    private LoadingFragmentViewModel viewModel;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(LoadingFragmentViewModel.class);

        context = getContext();

        setListeners();

        return root;
    }

    private void setListeners() {
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.checkField(binding.emailEditText, binding.email, context) &&
                    CommonUtils.checkField(binding.passwordEditText, binding.password, context)){

                    CommonUtils.loading(binding.loading, true);
                    CommonUtils.loading(binding.login, false);

                    viewModel.login(binding.emailEditText.getText().toString().trim(),
                            binding.passwordEditText.getText().toString().trim()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (!aBoolean) {
                                CommonUtils.loading(binding.loading, false);
                                CommonUtils.loading(binding.login, true);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), getString(R.string.popunite_sva_polja) , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}