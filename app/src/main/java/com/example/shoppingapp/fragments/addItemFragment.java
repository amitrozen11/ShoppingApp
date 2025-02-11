package com.example.shoppingapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.item;
import com.google.android.material.snackbar.Snackbar;

public class addItemFragment extends Fragment {

    private EditText nameInput, descInput, priceInput, amountInput;
    private Button btnAddItem, btnCancel;

    public addItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fregment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        initializeUI(view);

        // Handle "Add Item" button click
        btnAddItem.setOnClickListener(v -> validateAndAddItem(view));

        // Handle "Cancel" button click
        btnCancel.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }

    // Initialize UI components
    private void initializeUI(View view) {
        nameInput = view.findViewById(R.id.itemNameInput);
        descInput = view.findViewById(R.id.itemDescriptionInput);
        priceInput = view.findViewById(R.id.itemPriceInput);
        amountInput = view.findViewById(R.id.itemAmountInput);
        btnAddItem = view.findViewById(R.id.btnAddItem);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    // Validate user input and create item
    private void validateAndAddItem(View view) {
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String amountStr = amountInput.getText().toString().trim();

        if (!areFieldsValid(view, name, desc, priceStr, amountStr)) {
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int amount = Integer.parseInt(amountStr);

            item newItem = new item(name, desc, price, amount);
            sendItemToParentFragment(newItem);

            Snackbar.make(view, "Item added successfully!", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();

        } catch (NumberFormatException e) {
            Snackbar.make(view, "Invalid price or quantity!", Snackbar.LENGTH_LONG).show();
        }
    }

    // Validate if all fields are filled
    private boolean areFieldsValid(View view, String name, String desc, String priceStr, String amountStr) {
        if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || amountStr.isEmpty()) {
            Snackbar.make(view, "Please fill in all fields!", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // Send the new item to ShoppingFragment
    private void sendItemToParentFragment(item newItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newItem", newItem);
        getParentFragmentManager().setFragmentResult("requestKey", bundle);
    }
}
