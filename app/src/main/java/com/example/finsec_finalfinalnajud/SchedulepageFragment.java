package com.example.finsec_finalfinalnajud;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import android.content.Context;

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
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    String email, date;
    FloatingActionButton addBudgetFab, addExpenseFab, addBillsFab;
    ExtendedFloatingActionButton addActionsFab;
    TextView txtBudgetFab, txtExpenseFab, txtBillsFab;
    Button bottomsheet1, bottomsheet2, bottomsheet3;
    View overlay;
    boolean isAllFABVisible;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBudgetFab = view.findViewById(R.id.schedBudget_fab);
        addExpenseFab = view.findViewById(R.id.schedExpense_fab);
        addBillsFab = view.findViewById(R.id.schedBills_fab);
        addActionsFab = view.findViewById(R.id.add_fab);

        txtBudgetFab = view.findViewById(R.id.txtFABbudget);
        txtExpenseFab = view.findViewById(R.id.txtFABexpense);
        txtBillsFab = view.findViewById(R.id.txtFABbills);

        addBudgetFab.setVisibility(View.GONE);
        addExpenseFab.setVisibility(View.GONE);
        addBillsFab.setVisibility(View.GONE);
        txtBudgetFab.setVisibility(View.GONE);
        txtExpenseFab.setVisibility(View.GONE);
        txtBillsFab.setVisibility(View.GONE);

        overlay = view.findViewById(R.id.overlay);
        isAllFABVisible = false;

        addActionsFab.shrink();
        FloatingActionButton bottomsheet1 = view.findViewById(R.id.schedBudget_fab);
        FloatingActionButton bottomsheet2 = view.findViewById(R.id.schedExpense_fab);
        FloatingActionButton bottomsheet3 = view.findViewById(R.id.schedBills_fab);

        bottomsheet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.layout.budgetschedule_bottomsheet);
            }
        });

        bottomsheet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.layout.expenseschedule_bottomsheet);
            }
        });

        bottomsheet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.layout.billsschedule_bottomsheet);
            }
        });
        addActionsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFABVisible) {
                    overlay.setVisibility(View.VISIBLE);

                    addBudgetFab.show();
                    addExpenseFab.show();
                    addBillsFab.show();
                    txtBudgetFab.setVisibility(View.VISIBLE);
                    txtExpenseFab.setVisibility(View.VISIBLE);
                    txtBillsFab.setVisibility(View.VISIBLE);

                    addActionsFab.extend();

                    isAllFABVisible = true;
                } else {
                    overlay.setVisibility(View.GONE);

                    addBudgetFab.hide();
                    addExpenseFab.hide();
                    addBillsFab.hide();
                    txtBudgetFab.setVisibility(View.GONE);
                    txtExpenseFab.setVisibility(View.GONE);
                    txtBillsFab.setVisibility(View.GONE);

                    addActionsFab.shrink();

                    isAllFABVisible = false;
                }
            }
        });


    }

    private void buildAllocateBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
//                        dbFinsec.child("users").child(email3).child("date").child("Budget").child(budgetName).setValue(formattedBudgetValue);
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

    private void showDialog(int layoutResId) {
        // Use the appropriate method to obtain the context, depending on whether you are in an Activity or Fragment
        Context context = getContext(); // For a Fragment
        // or
        // Context context = this; // For an Activity

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}

