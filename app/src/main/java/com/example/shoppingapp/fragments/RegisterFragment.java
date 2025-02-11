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
import com.example.shoppingapp.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, confirmPasswordInput, phoneInput;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users"); // Firebase Database Reference
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize UI components
        firstNameInput = view.findViewById(R.id.RegFirstName);
        lastNameInput = view.findViewById(R.id.RegLastName);
        emailInput = view.findViewById(R.id.RegEmailAddress);
        passwordInput = view.findViewById(R.id.regPassword);
        confirmPasswordInput = view.findViewById(R.id.editTextTextPassword2);
        phoneInput = view.findViewById(R.id.RegPhoneNumber);
        registerButton = view.findViewById(R.id.btnRegister);

        // Handle register button click
        registerButton.setOnClickListener(v -> registerUser(view));

        return view;
    }

    private void registerUser(View view) {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        if (!validateInput(view, firstName, lastName, email, password, confirmPassword, phone)) {
            return;
        }

        // Register the user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserData(view, userId, firstName, lastName, email, phone);
                    } else {
                        Snackbar.make(view, "Registration Failed: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserData(View view, String userId, String firstName, String lastName, String email, String phone) {
        User newUser = new User(firstName, lastName, email);
        newUser.setUserId(userId);
        newUser.setPhone(phone);

        databaseRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(view, "Registration Successful!", Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_logInFragment);
                    } else {
                        Snackbar.make(view, "Failed to save user data!", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(View view, String firstName, String lastName, String email, String password, String confirmPassword, String phone) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Snackbar.make(view, "All fields must be filled", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(view, "Invalid Email Format", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Snackbar.make(view, "Password must be at least 6 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (phone.length() != 10) {
            Snackbar.make(view, "Phone number must be 10 digits", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
