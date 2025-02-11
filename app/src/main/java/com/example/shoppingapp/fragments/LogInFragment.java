package com.example.shoppingapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText emailLogin, passwordLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);


        emailLogin = view.findViewById(R.id.EmailAddress);
        passwordLogin = view.findViewById(R.id.Password);
        Button logInButton = view.findViewById(R.id.buttonLogIn);
        Button registerButton = view.findViewById(R.id.buttonReg);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.action_logInFragment_to_registerFragment);
            }
        });

        return view;
    }

    private void login() {
        View view = getView(); // Retrieve fragment's root view
        if (view == null) return; // Avoid NullPointerException

        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (!logInValidate(view, email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Login Successful", Snackbar.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_shoppingFragment);
                        } else {
                            Snackbar.make(view, "Login Failed. Check credentials.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean logInValidate(View view, String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "All fields must be filled", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(view, "Invalid Email", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Snackbar.make(view, "Password must be at least 6 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
