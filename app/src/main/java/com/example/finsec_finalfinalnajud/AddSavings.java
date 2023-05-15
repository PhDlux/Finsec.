package com.example.finsec_finalfinalnajud;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;

public class AddSavings extends AppCompatActivity implements AnotherCustomDialog.AnotherCustomDialogListener {

    TextView goalName;
    TextView savingsAdded;
    TextView percent;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_savings);

        goalName = (TextView) findViewById(R.id.txtGoalName);
        savingsAdded = (TextView) findViewById(R.id.txtSavingsAdded);
        percent = (TextView) findViewById(R.id.txtPercent);


    }

    @Override
    public void applyChanges(String name, double savings) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        goalName.setText(name);
        savingsAdded.setText("₱ " + n.format(savings));
    }
}
