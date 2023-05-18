package com.example.finsec_finalfinalnajud;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {
    private DatabaseReference dbFinsec;
    private String email3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.homepage, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String encodedEmail = arguments.getString("encodedEmail");
            if (encodedEmail != null) {
                email3 = encodedEmail;
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbFinsec = FirebaseDatabase.getInstance().getReference();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String encodedEmail = arguments.getString("encodedEmail");
            if (encodedEmail != null) {
                email3 = encodedEmail;
            }
        }

        ImageButton btnFrame = view.findViewById(R.id.imgbtntotalsavings);
        ImageButton btnFrame1 = view.findViewById(R.id.imgbtnexpenses);
        TextView txtTotalSavingsNum = view.findViewById(R.id.txttotalsavingsnum); // Assuming this is the TextView you want to update.
        TextView dateTextView = view.findViewById(R.id.txtdate); // Replace with your TextView's ID
        TextView txtHelloUser = view.findViewById(R.id.txthello);
        TextView txtUser = view.findViewById(R.id.txtuser);
        TextView txtSavingsNum = view.findViewById(R.id.txtsavingsnum);
        TextView txtSavingsArrow = view.findViewById(R.id.txtsavingsarrow);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        String originalText = dateTextView.getText().toString();
        String newText = originalText.substring(0, 6) + currentDate.toString();

        dateTextView.setText(newText);
        loadTotalSavings(txtTotalSavingsNum);
        setGreeting(txtHelloUser);
        setUserFullName(txtUser);
        setDailySavings(txtSavingsNum, txtSavingsArrow, currentDate);
        btnFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments() != null){
                    Intent i = new Intent(getActivity(), GoalSavings.class);
                    i.putExtra("email2", getArguments().getString("encodedEmail"));
                    i.putExtra("currentDate", dateTextView.getText().toString().substring(6));
                    startActivity(i);
                }
            }
        });

        btnFrame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments() != null){
                    Intent i = new Intent(getActivity(), ExpensePage.class);
                    i.putExtra("email2", getArguments().getString("encodedEmail"));
                    i.putExtra("currentDate", dateTextView.getText().toString().substring(6));
                    startActivity(i);
                }
            }
        });
    }

    public static HomepageFragment newInstance(String email) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString("encodedEmail", email);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadTotalSavings(TextView textView) {
        if (email3 == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        System.out.println(email3);
        DatabaseReference userRef = dbFinsec.child("users").child(email3);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalSavings = 0;
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    // Skip if it is otherfields
                    if (date.equals("contactNumber") || date.equals("dateofbirth") || date.equals("firstname")
                            || date.equals("gender") || date.equals("goal") || date.equals("lastname")
                            || date.equals("password")) continue;

                    for (DataSnapshot goalSnapshot : dateSnapshot.getChildren()) {
                        if (goalSnapshot.child("savings").getValue() != null) {
                            String savingsStr = goalSnapshot.child("savings").getValue(String.class);

                            double savings = 0;

                            try {
                                savings = Double.parseDouble(savingsStr);
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Failed to parse savings for date " + date);
                            }

                            totalSavings += savings;
                        } else {
                            Log.e(TAG, "Null value for savings for date " + date);
                        }
                    }
                }

                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);
                textView.setText(String.format("₱ " + n.format(totalSavings)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });

    }

    private void setGreeting(TextView txtHelloUser) {
        if (email3 == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        DatabaseReference userRef = dbFinsec.child("users").child(email3).child("firstname");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String fullName = snapshot.getValue(String.class);

                    // Split on space, take the first part as the first name
                    String firstName = fullName.split(" ")[0];
                    txtHelloUser.setText("Hello " + firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });
    }

    private void setUserFullName(TextView txtUser) {
        if (email3 == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        DatabaseReference userRef = dbFinsec.child("users").child(email3);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("firstname").getValue() != null && snapshot.child("lastname").getValue() != null) {
                    String firstName = snapshot.child("firstname").getValue(String.class);
                    String lastName = snapshot.child("lastname").getValue(String.class);
                    txtUser.setText(lastName + ", " + firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });
    }

    private void setDailySavings(TextView txtSavingsNum, TextView txtSavingsArrow, String date) {
        if (email3 == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        DatabaseReference userRef = dbFinsec.child("users").child(email3).child(date);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalSavings = 0;
                for (DataSnapshot goalSnapshot : snapshot.getChildren()) {
                    if (goalSnapshot.child("savings").getValue() != null) {
                        String savingsStr = goalSnapshot.child("savings").getValue(String.class);

                        double savings = 0;
                        try {
                            savings = Double.parseDouble(savingsStr);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Failed to parse savings for date " + date);
                        }

                        totalSavings += savings;
                    } else {
                        Log.e(TAG, "Null value for savings for date " + date);
                    }
                }

                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);
                txtSavingsNum.setText(String.format("₱ " + n.format(totalSavings)));
                if (totalSavings > 0) {
                    txtSavingsArrow.setText("⬆");
                } else {
                    txtSavingsArrow.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });
    }

}