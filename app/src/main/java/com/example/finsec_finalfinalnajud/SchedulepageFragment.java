package com.example.finsec_finalfinalnajud;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchedulepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulepageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SchedulepageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment schedulepage.
     */
    // TODO: Rename and change types and number of parameters
    public static SchedulepageFragment newInstance(String param1, String param2) {
        SchedulepageFragment fragment = new SchedulepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.schedulepage, container, false);
    }

    AlertDialog addNewBudget;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void buildAllocateBudgetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.allocate_budget, null);

        EditText etBudgetName = view.findViewById(R.id.etAllocateBudget);
        EditText etBudget = view.findViewById(R.id.etBudget);

        builder.setView(view);
        builder.setTitle("Allocate Budget")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        NumberFormat n = NumberFormat.getInstance();
                        n.setMaximumFractionDigits(2);
                        n.setMinimumFractionDigits(2);

                        String budgetName = etBudgetName.getText().toString();
                        double budgetValue = Double.parseDouble(etBudget.getText().toString());


//                        goal = String.valueOf((int)goalValue);
//                        dbFinsec.child("users").child(email3).child("goal").setValue(goal);
//
//                        setTxtGoal();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        addNewBudget = builder.create();
    }

    private void addView() {
        View view = getLayoutInflater().inflate(R.layout.budget_frame, null);

        TextView budgetName = view.findViewById(R.id.txtBudgetName);
        TextView budget = view.findViewById(R.id.txtBudget);


    }
}