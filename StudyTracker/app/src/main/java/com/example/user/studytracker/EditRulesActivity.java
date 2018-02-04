package com.example.user.studytracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.getInstance;

public class EditRulesActivity extends AppCompatActivity{

    private EditText fromDateEdit;
    private EditText toDateEdit;
    private EditText dayOfWeekEdit;
    private EditText startTimeEdtit;
    private EditText endTimeEdit;
    private EditText oddOrEvenEdit;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;

    RadioButton radioRegularly;
    RadioButton radioOneTime;
    RadioButton radioDaily;
    RadioButton radioWeekly;
    RadioButton radioBiWeekly;

    Button btnDiscard;
    Button btnAccept;

    int eventType;
    EventRule eventRuleIntent;
    Intent receivedIntent;

    final String[] weekDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    final String[] oddOrEvenString = new String [] {"even", "odd"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rules);
        receivedIntent = getIntent();

        Toolbar myToolbar = findViewById(R.id.toolbar_editRules);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);



        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.GERMANY);

        fromDateEdit = (EditText) findViewById(R.id.fromDate);
        fromDateEdit.setInputType(InputType.TYPE_NULL);
        fromDateEdit.requestFocus();

        dayOfWeekEdit = findViewById(R.id.edit_dayOfWeek);
        dayOfWeekEdit.setInputType(InputType.TYPE_NULL);

        oddOrEvenEdit = findViewById(R.id.edit_oddOrEven);
        oddOrEvenEdit.setInputType(InputType.TYPE_NULL);

        startTimeEdtit = findViewById(R.id.edit_startTime);
        startTimeEdtit.setInputType(InputType.TYPE_NULL);

        endTimeEdit = findViewById(R.id.edit_endTime);
        endTimeEdit.setInputType(InputType.TYPE_NULL);

        toDateEdit = (EditText) findViewById(R.id.toDate);
        toDateEdit.setInputType(InputType.TYPE_NULL);

        radioRegularly = findViewById(R.id.radio_regularly);
        radioOneTime = findViewById(R.id.radio_oneTime);
        radioDaily = findViewById(R.id.radio_daily);
        radioWeekly = findViewById(R.id.radio_weekly);
        radioBiWeekly = findViewById(R.id.radio_biWeekly);

        setEdittextData();
        setRadioListeners();
        setButtonLogic();


    }

    /**
     * creates onClickListeners for each EditText (each containing the corresponding dialog)
     */
    private void setEdittextData() {
        fromDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        toDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDatePickerDialog.show();
            }
        });

        dayOfWeekEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayOfWeekDialog();
            }
        });
        oddOrEvenEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOddOrEvenDialog();
            }
        });

        startTimeEdtit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeDialog.show();
            }
        });

        endTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimeDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEdit.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEdit.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startTimeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                startTimeEdtit.setText(timeFormatter.format(newTime.getTime()));
            }

        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        endTimeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                endTimeEdit.setText(timeFormatter.format(newTime.getTime()));
            }

        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        setEditTextIfRuleIntent();
    }

    /**
     * creates dialog that allows you to choose the day of the week
     */
    private void dayOfWeekDialog(){
        final Dialog d = new Dialog(this);
        d.setTitle("Choose day of week");
        d.setContentView(R.layout.dialog_numberpicker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(7);
        np.setMinValue(1);
        np.setDisplayedValues(weekDays);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dayOfWeekEdit.setText(weekDays[np.getValue()-1]);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    /**
     * creates the Dialog that allows you to choose between odd and even weeks
     */
    private void setOddOrEvenDialog(){
        final Dialog d = new Dialog(this);
        d.setTitle("Choose odd or even week");
        d.setContentView(R.layout.dialog_numberpicker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(2);
        np.setMinValue(1);
        np.setDisplayedValues(oddOrEvenString);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                oddOrEvenEdit.setText(oddOrEvenString[np.getValue()-1]);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    /**
     * hides or displays parts of the layout depending on which RadioListeners are ticked
     */
    private void setRadioListeners(){
        final LinearLayout linear_radio = findViewById(R.id.linear_Radio_repeatRate);
        final LinearLayout linear_dayOfWeek = findViewById(R.id.linear_dayOfWeek);
        final LinearLayout linear_oddOrEven = findViewById(R.id.linear_oddOrEven);

        radioRegularly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_radio.setVisibility(View.VISIBLE);
                if(radioBiWeekly.isChecked() || radioWeekly.isChecked()){
                    linear_dayOfWeek.setVisibility(View.VISIBLE);
                }
                if(radioBiWeekly.isChecked()){
                    linear_oddOrEven.setVisibility(View.VISIBLE);
                }
            }
        });

        radioOneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_radio.setVisibility(View.INVISIBLE);
                linear_dayOfWeek.setVisibility(View.INVISIBLE);
                linear_oddOrEven.setVisibility(View.INVISIBLE);
            }
        });

        radioDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_dayOfWeek.setVisibility(View.INVISIBLE);
                linear_oddOrEven.setVisibility(View.INVISIBLE);
            }
        });

        radioWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_dayOfWeek.setVisibility(View.VISIBLE);
                linear_oddOrEven.setVisibility(View.INVISIBLE);
            }
        });

        radioBiWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_dayOfWeek.setVisibility(View.VISIBLE);
                linear_oddOrEven.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * creates OnClickListeners for Discard and Accept buttons
     */
    private void setButtonLogic(){
        btnDiscard = findViewById(R.id.btnDiscard);
        btnAccept = findViewById(R.id.btnAccept);

        final Calendar calFrom = Calendar.getInstance();
        final Calendar calTo = Calendar.getInstance();
        final Calendar calStartTime = Calendar.getInstance();
        final Calendar calEndTime = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        final SimpleDateFormat sdfHourMinute = new SimpleDateFormat("HH:mm", Locale.GERMAN);


        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //if any information is missing it displays an error dialog
                if((!radioOneTime.isChecked() && !radioRegularly.isChecked())
               ||isEmpty(fromDateEdit)||isEmpty(toDateEdit)
               ||isEmpty(startTimeEdtit)||isEmpty(endTimeEdit)
               ||(radioRegularly.isChecked()&&(!radioDaily.isChecked()&&!radioWeekly.isChecked()&&!radioBiWeekly.isChecked()))
               ||(radioRegularly.isChecked()&&(radioWeekly.isChecked()||radioBiWeekly.isChecked())&&isEmpty(dayOfWeekEdit))){

                   AlertDialog.Builder builder;
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       builder = new AlertDialog.Builder(EditRulesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                   } else {
                       builder = new AlertDialog.Builder(EditRulesActivity.this);
                   }
                   builder.setTitle("Information missing")
                           .setMessage(R.string.txt_missingEntry)
                           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();
               }
               //else converts String from EditTexts containing Dates to Calendars
               else{
                   try {
                       calFrom.setTime(sdf.parse(fromDateEdit.getText().toString()));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                    try {
                        calTo.setTime(sdf.parse(toDateEdit.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // error if startDate is after endDate
                    if (calFrom.after(calTo)) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(EditRulesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(EditRulesActivity.this);
                        }
                        builder.setTitle("Wrong Information")
                                .setMessage(R.string.txt_fromAfterTo)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    //if no error convert Edittexts containing times to Calendars
                    else {
                        try {
                            calStartTime.setTime(sdfHourMinute.parse(startTimeEdtit.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            calEndTime.setTime(sdfHourMinute.parse(endTimeEdit.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //if startTime after EndTime, throw error dialog
                        if (calStartTime.after(calEndTime)) {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(EditRulesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(EditRulesActivity.this);
                            }
                            builder.setTitle("Wrong Information")
                                    .setMessage(R.string.txt_startAfterEnd)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        //if no errors so far build the rule from the information
                        else {
                        try {
                            buildRuleForIntent();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                   }
                }

            }
        });
    }

    /**
     * checks whether the given Edittext is empty or not.
     * @param etText the EditTexts that is to be checked
     * @return returns true if EditTexts is empty, otherwise false
     */
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    /**
     * function that builds the EventRule, adds it to the event and returns to the AddActivity
     * @throws ParseException this shouldn't happen, but is required for compiling
     */
    private void buildRuleForIntent() throws ParseException {
        eventType = receivedIntent.getIntExtra("number", 1);
        boolean repeat = radioRegularly.isChecked();
        Calendar fromDate = getInstance();
        fromDate.setTime(dateFormatter.parse(fromDateEdit.getText().toString()));
        Calendar toDate = getInstance();
        toDate.setTime(dateFormatter.parse(toDateEdit.getText().toString()));
        int type = eventType;
        int inc;
        int oddOrEvenNumber=0;
        if(radioOneTime.isChecked()){
            inc=0;
        }
        else{
            if (radioDaily.isChecked()) {
                inc=1;
            }
            else if(radioWeekly.isChecked()){
                inc=7;
            }
            else{
                inc=14;
                for (int i = 0; i<oddOrEvenString.length; i++){
                    if(oddOrEvenEdit.getText().toString().equals(oddOrEvenString[i])){
                        oddOrEvenNumber=i+1;
                        break;


                    }
                }
            }
        }
        int dayOfWeek=0;
        if(radioRegularly.isChecked() && (radioWeekly.isChecked() || radioBiWeekly.isChecked())){
            for (int i = 0; i<weekDays.length; i++){
                if((dayOfWeekEdit.getText().toString()).equals(weekDays[i])){
                    dayOfWeek=i+1;
                    break;
                }
            }
        }
        Calendar fromTime = getInstance();
        fromTime.setTime(timeFormatter.parse(startTimeEdtit.getText().toString()));
        Calendar toTime = getInstance();
        toTime.setTime(timeFormatter.parse(endTimeEdit.getText().toString()));

        EventRule eventForIntent = new EventRule(repeat, fromDate, toDate, type, inc, dayOfWeek, oddOrEvenNumber, fromTime, toTime);

        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("eventRule", eventForIntent);
        intent.putExtra("type", type);
        startActivity(intent);


    }

    /**
     * checks whether the intent contains an EventRule (meaning coming from an "edit" button)
     * and if so adds the information in the corresponding pieces of the layout
     */
    private void setEditTextIfRuleIntent(){
        final LinearLayout linear_radio = findViewById(R.id.linear_Radio_repeatRate);
        final LinearLayout linear_dayOfWeek = findViewById(R.id.linear_dayOfWeek);
        final LinearLayout linear_oddOrEven = findViewById(R.id.linear_oddOrEven);

        if(receivedIntent.hasExtra("rule")) {
            eventRuleIntent = (EventRule) receivedIntent.getSerializableExtra("rule");

            if(eventRuleIntent.repeating){
                radioRegularly.setChecked(true);
                radioOneTime.setChecked(false);
                if(eventRuleIntent.increment==1){
                    radioDaily.setChecked(true);
                    linear_oddOrEven.setVisibility(View.INVISIBLE);
                    linear_dayOfWeek.setVisibility(View.INVISIBLE);
                }
                if(eventRuleIntent.increment==7){
                    radioWeekly.setChecked(true);
                    linear_oddOrEven.setVisibility(View.INVISIBLE);
                }
                if(eventRuleIntent.increment==14) {
                    radioBiWeekly.setChecked(true);
                    oddOrEvenEdit.setText(oddOrEvenString[eventRuleIntent.oddOrEven-1]);
                }
                if(eventRuleIntent.dayOfWeek>0){
                    dayOfWeekEdit.setText(weekDays[eventRuleIntent.dayOfWeek-1]);
                }
            }
            else{
                radioRegularly.setChecked(false);
                radioOneTime.setChecked(true);
                linear_radio.setVisibility(View.INVISIBLE);
                linear_oddOrEven.setVisibility(View.INVISIBLE);
                linear_dayOfWeek.setVisibility(View.INVISIBLE);

            }
            fromDateEdit.setText(dateFormatter.format(eventRuleIntent.startDate.getTime()));
            toDateEdit.setText(dateFormatter.format(eventRuleIntent.endDate.getTime()));
            startTimeEdtit.setText(timeFormatter.format(eventRuleIntent.startTime.getTime()));
            endTimeEdit.setText(timeFormatter.format(eventRuleIntent.endTime.getTime()));
        }
    }
}
