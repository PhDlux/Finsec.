package com.example.finsec_finalfinalnajud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class GoalSavings extends AppCompatActivity implements View.OnClickListener {
    TextView txtGoal;
    TextView txtCurrentSavings;
    TextView txtTotalCurrentSavings;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    String email3;
    LinearLayout layoutlist;
    Button btnAddSavings;
    AlertDialog addSavingsDialog;
    AlertDialog newGoalDialog;
    String goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_savings);

        Button back = (Button) findViewById(R.id.btnGoalSavingsBack);
        layoutlist = (LinearLayout) findViewById(R.id.layout_list);
        btnAddSavings = (Button) findViewById(R.id.btnAddSavings);
        Intent i = getIntent();
        email3 = i.getStringExtra("email2");

        txtGoal = (TextView) findViewById(R.id.txtGoal);
        txtTotalCurrentSavings = (TextView ) findViewById(R.id.txtTotalCurrentSavings);
        txtCurrentSavings = findViewById(R.id.txtCurrentSavings);
        Button newGoal = (Button) findViewById(R.id.btnNewGoal);

        btnAddSavings.setOnClickListener(this);
        back.setOnClickListener(this);
        newGoal.setOnClickListener(this);

        buildNewGoalDialog();
        buildAddSavingsDialog();
//        dbFinsec.child("users").child(email3).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.hasChild("goal")) {
//                    double getSavings = snapshot.child("goal").getValue(Double.class);
//                    setTxtGoal(getSavings);
//                    String num = txtGoal.getText().toString().substring(1);
//                    try {
//                        goal = NumberFormat.getInstance(Locale.US).parse(num).toString();
//                        System.out.println(goal);
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



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

    private void buildAddSavingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.another_customdialog, null);

        EditText etGoal = view.findViewById(R.id.etGoalName);
        EditText etSavingsAdded = view.findViewById(R.id.etSavingsAdded);

        builder.setView(view);
        builder.setTitle("Add Goal Savings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double savings = Double.parseDouble(etSavingsAdded.getText().toString());
                        try {
                            NumberFormat n = NumberFormat.getInstance();

                            double temp = n.parse(txtCurrentSavings.getText().toString().substring(1)).doubleValue();
                            double goals = Double.parseDouble(goal);
                            int percent;
                            System.out.println(goals);
                            if(goals >= savings) {
                                percent = (int) ((savings/goals) * 100);
                            } else {
                                throw new IllegalArgumentException();
                            }

                            setTxtCurrentSavings(temp + savings);
                            addView(etGoal.getText().toString(), Double.parseDouble(etSavingsAdded.getText().toString()), percent);
                        } catch (IllegalArgumentException il) {
                            Toast.makeText(GoalSavings.this, "Savings cannot be greater than goal",Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        addSavingsDialog = builder.create();
    }

    private void buildNewGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        EditText etGoalSavings = view.findViewById(R.id.etGoalSavings);

        builder.setView(view);
        builder.setTitle("New Goal")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTxtGoal(Double.parseDouble(etGoalSavings.getText().toString()));

                        goal = etGoalSavings.getText().toString();
//                        dbFinsec.child("users").child(email3).child("goal").setValue(Double.parseDouble(etGoalSavings.getText().toString()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        newGoalDialog = builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnAddSavings:
                addSavingsDialog.show();
                break;
            case R.id.btnNewGoal:
                newGoalDialog.show();
                break;
            case R.id.btnGoalSavingsBack:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("totalCurrentSavings", txtTotalCurrentSavings.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }
    }

    public void addView(String goalname, double savings, int percent) {
        View view = getLayoutInflater().inflate(R.layout.add_savings, null);

        TextView txtSavingsAdded = view.findViewById(R.id.txtSavingsAdded);
        TextView txtGoalName = view.findViewById(R.id.txtGoalName);
        TextView txtPercent = view.findViewById(R.id.txtPercent);

        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);

        txtGoalName.setText(goalname);
        txtSavingsAdded.setText("₱ " + n.format(savings));

        txtPercent.setText("+" + percent + "%");

        layoutlist.addView(view, 0);
    }

    public void setTxtGoal(double goal) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        txtGoal.setText("₱ " + n.format(goal));
    }

    public void setTxtCurrentSavings(double savings) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        txtCurrentSavings.setText("₱ " + n.format(savings));
        txtTotalCurrentSavings.setText("₱ " + n.format(savings));
    }
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}