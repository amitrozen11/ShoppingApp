package com.example.shoppingapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.R;
import com.example.shoppingapp.adapters.ItemAdapter;
import com.example.shoppingapp.models.item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class shoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<item> itemList;
    private FloatingActionButton addItemButton;
    private TextView welcomeText;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    public shoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        welcomeText = view.findViewById(R.id.welcomeText);
        recyclerView = view.findViewById(R.id.recyclerMain);
        addItemButton = view.findViewById(R.id.addItemButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Load sample data
        itemList = new ArrayList<>();
        itemList.add(new item("Laptop", "Gaming laptop", 1200.99, 3));
        itemList.add(new item("Smartphone", "Latest Android phone", 799.99, 5));
        itemList.add(new item("Headphones", "Noise-canceling", 199.99, 2));

        fetchUserFullName();

        // Set Adapter
        itemAdapter = new ItemAdapter( itemList);
        recyclerView.setAdapter(itemAdapter);

        // Set up add item button click
        addItemButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_shoppingFragment_to_addItemFragment));

        // Listen for new item data
        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            if (bundle != null) {
                item newItem = (item) bundle.getSerializable("newItem");
                if (newItem != null) {
                    itemList.add(newItem);
                    itemAdapter.notifyItemInserted(itemList.size() - 1);
                    Snackbar.make(view, "Item added successfully!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        itemAdapter.setOnItemClickListener(position -> showDeleteConfirmationDialog(position));

        return view;
    }

    private void fetchUserFullName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            welcomeText.setText("Welcome, Guest!");
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    welcomeText.setText("Welcome, " + firstName + " " + lastName + "!");
                } else {
                    welcomeText.setText("Welcome, User!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                welcomeText.setText("Welcome, User!");
            }
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    itemAdapter.removeItem(position);
                    Snackbar.make(getView(), "Item Removed", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
