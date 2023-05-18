package com.example.finsec_finalfinalnajud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageButton btnFrame = view.findViewById(R.id.imgbtntotalsavings);
        ImageButton btnFrame1 = view.findViewById(R.id.imgbtnexpenses);
        TextView txtTotalSavingsNum = view.findViewById(R.id.txttotalsavingsnum); // Assuming this is the TextView you want to update.

        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String totalCurrentSavings = data.getStringExtra("totalCurrentSavings");
                                txtTotalSavingsNum.setText(totalCurrentSavings); // Updating the TextView
                            }
                        }
                    }
                });
        btnFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments() != null){
                    Intent i = new Intent(getActivity(), GoalSavings.class);
                    i.putExtra("email2", getArguments().getString("email1"));
                    mStartForResult.launch(i);
                }
            }
        });
        btnFrame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments() != null){
                    Intent i = new Intent(getActivity(), ExpensePage.class);
                    mStartForResult.launch(i);
                }
            }
        });
    }
}