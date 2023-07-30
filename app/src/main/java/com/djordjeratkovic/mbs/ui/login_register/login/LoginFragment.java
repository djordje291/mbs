package com.djordjeratkovic.mbs.ui.login_register.login;

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
    //TODO: change to commonUtils.checkField

    private FragmentLoginBinding binding;

    private LoadingFragmentViewModel viewModel;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(LoadingFragmentViewModel.class);

        setListeners();

        return root;
    }

    private void setListeners() {
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean empty = false;
                if (binding.emailEditText.getText().toString().trim().isEmpty()) {
                    binding.email.setError(getString(R.string.popunite_polje));
                    empty = true;
                } else {
                    binding.email.setError(null);
                }
                if (binding.passwordEditText.getText().toString().trim().isEmpty()) {
                    binding.password.setError(getString(R.string.popunite_polje));
                    empty = true;
                } else {
                    binding.password.setError(null);
                }
                if (!empty) {
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

    private boolean checkFields(TextInputEditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.popunite_polje));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}