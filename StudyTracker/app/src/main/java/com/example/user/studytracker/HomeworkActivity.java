package com.example.user.studytracker;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeworkActivity extends AppCompatActivity {

    List<Subject> subjectList = new ArrayList<Subject>();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        Toolbar myToolbar = findViewById(R.id.toolbar_homework);
        myToolbar.setTitle("Homework");
        setSupportActionBar(myToolbar);

        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra("subject")) {
            subjectList.add((Subject) receivedIntent.getSerializableExtra("subject"));
            position = receivedIntent.getIntExtra("position", 0);
        }
        if (receivedIntent.hasExtra("subjects")) {
            subjectList.addAll((List<Subject>) receivedIntent.getSerializableExtra("subjects"));
        }


        setButtonLogic();
        buildLayout();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setButtonLogic(){
        Button btnSave = findViewById(R.id.btn_saveChanges_homework);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if(position>=0){
                    intent.putExtra("position", position);
                    intent.putExtra("subjectChanged", (Serializable) subjectList.get(0));
                }
                else{
                    intent.putExtra("subjectsChanged", (Serializable) subjectList);
                }
                startActivity(intent);
            }
        });
    }
    private void buildLayout(){
        boolean boo = subjectList.size()>1;
        SimpleDateFormat sDF = new SimpleDateFormat("MMM dd, HH:mm");
        LinearLayout parentLin = findViewById(R.id.lin_act_homework);
        for(Subject s:subjectList){
            for(final Occasion o:s.dueDate){
                LinearLayout childLinLay = new LinearLayout(this);
                childLinLay.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams cllparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                childLinLay.setLayoutParams(cllparam);
                parentLin.addView(childLinLay);

                //use a GradientDrawable with only one color set, to make it a solid color
                GradientDrawable border = new GradientDrawable();
                border.setColor(0xFFFFFFFF); //white background
                border.setStroke(1, 0xFF000000); //black border with full opacity
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    childLinLay.setBackgroundDrawable(border);
                } else {
                    childLinLay.setBackground(border);
                }

                if(boo){
                    TextView textSubject = new TextView(this);
                    textSubject.setText("Subject: "+s.name);
                    childLinLay.addView(textSubject);
                }
                TextView textStart = new TextView(this);
                textStart.setText("Start: "+sDF.format(o.start));
                childLinLay.addView(textStart);
                TextView textEnd = new TextView(this);
                textEnd.setText("End: "+sDF.format(o.end));
                childLinLay.addView(textEnd);
                CheckBox check = new CheckBox(this);
                check.setChecked(o.attended);
                check.setText("attended?");
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        o.attended = b;
                    }
                });
                childLinLay.addView(check);

            }
        }
    }

}
