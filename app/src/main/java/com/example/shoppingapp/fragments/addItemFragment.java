package com.example.shoppingapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.item;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;

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
        nameInput = view.findViewById(R.id.itemNameInput);
        descInput = view.findViewById(R.id.itemDescriptionInput);
        priceInput = view.findViewById(R.id.itemPriceInput);
        amountInput = view.findViewById(R.id.itemAmountInput);
        btnAddItem = view.findViewById(R.id.btnAddItem);
        btnCancel = view.findViewById(R.id.btnCancel);

        // Handle "Add Item" button click
        btnAddItem.setOnClickListener(v -> addItem(view));

        // Handle "Cancel" button click
        btnCancel.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }

    private void addItem(View view) {
        // Get user input
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String amountStr = amountInput.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || amountStr.isEmpty()) {
            Snackbar.make(view, "Please fill in all fields!", Snackbar.LENGTH_LONG).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int amount = Integer.parseInt(amountStr);

            // Create new item
            item newItem = new item(name, desc, price, amount);

            // Pass item back to ShoppingFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("newItem", newItem);
            getParentFragmentManager().setFragmentResult("requestKey", bundle);

            // Show confirmation and navigate back
            Snackbar.make(view, "Item added!", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();

        } catch (NumberFormatException e) {
            Snackbar.make(view, "Invalid price or quantity!", Snackbar.LENGTH_LONG).show();
        }
    }
}

