package com.djordjeratkovic.mbs.ui.login_register.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.djordjeratkovic.mbs.R;
import com.djordjeratkovic.mbs.databinding.FragmentRegisterBinding;
import com.djordjeratkovic.mbs.util.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterFragmentViewModel viewModel;

    public RegisterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(RegisterFragmentViewModel.class);

        setListeners();

        return root;
    }

    private void setListeners() {
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean empty = false;
//                if (binding.nameEditText.getText().toString().trim().isEmpty()) {
//                    binding.name.setError(getString(R.string.popunite_polje));
//                    empty = true;
//                } else {
//                    binding.name.setError(null);
//                }
//                if (binding.emailEditText.getText().toString().trim().isEmpty()) {
//                    binding.email.setError(getString(R.string.popunite_polje));
//                    empty = true;
//                }else {
//                    binding.email.setError(null);
//                }
//                if (binding.passwordEditText.getText().toString().trim().isEmpty()) {
//                    binding.password.setError(getString(R.string.popunite_polje));
//                    empty = true;
//                }else {
//                    binding.password.setError(null);
//                }
                if (checkFields(binding.nameEditText, binding.name) &&
                    checkFields(binding.emailEditText, binding.email) &&
                    checkFields(binding.passwordEditText, binding.password)) {
//                if (!empty) {
                    CommonUtils.loading(binding.loading, true);
                    CommonUtils.loading(binding.createAccount, false);

                    viewModel.createAccount(binding.nameEditText.getText().toString().trim(),
                            binding.emailEditText.getText().toString().trim(),
                            binding.passwordEditText.getText().toString().trim()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (!aBoolean) {
                                CommonUtils.loading(binding.loading, false);
                                CommonUtils.loading(binding.createAccount, true);
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