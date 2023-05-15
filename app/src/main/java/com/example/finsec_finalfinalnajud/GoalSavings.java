package com.example.finsec_finalfinalnajud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

public class GoalSavings extends AppCompatActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {
    TextView txtGoal;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    String email3;
    LinearLayout layoutlist;
    Button btnAddSavings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_savings);

        Button back = (Button) findViewById(R.id.btnGoalSavingsBack);
        layoutlist = (LinearLayout) findViewById(R.id.layout_list);
        btnAddSavings = (Button) findViewById(R.id.btnAddSavings);

        btnAddSavings.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();
        email3 = i.getStringExtra("email2");

        txtGoal = (TextView) findViewById(R.id.txtGoal);
        Button newGoal = (Button) findViewById(R.id.btnNewGoal);

        dbFinsec.child("users").child(email3).child("goalsavings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("goal")) {
                    double getSavings = snapshot.child("goal").getValue(Double.class);
                    applyChanges(getSavings);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        int backgroundColor = Color.parseColor("#F1F1F1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isColorDark(backgroundColor)) {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        addView();
    }

    private void addView() {
        View addsavingsview = getLayoutInflater().inflate(R.layout.add_savings, null, false);
        layoutlist.addView(addsavingsview, 0);
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    public void openDialog() {
        CustomDialog c = CustomDialog.newInstance(email3);
        c.show(getSupportFragmentManager(), "Custom Dialog");
    }

    @Override
    public void applyChanges(double savings) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        txtGoal.setText(n.format(savings));
    }
}