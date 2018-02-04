 package com.example.user.studytracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

 public class AddActivity extends AppCompatActivity {

     List<EventRule> lectureRules = new ArrayList<EventRule>();
     List<EventRule> exerciseRules = new ArrayList<EventRule>();
     List<EventRule> homeworkRules = new ArrayList<EventRule>();
     String file_lectureRule = " \\lecture_list.txt";
     String file_homeworkRule = " \\homework_list.txt";
     String file_exerciseRule = " \\exercise_list.txt";

     EventRule tes2;
     Boolean saveRules;

     Button btnDiscard;
     Button btnAccept;
     EditText editName;

     final String[] weekDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
     final String[] oddOrEvenString = new String[]{"even", "odd"};


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);


         //build layout
         setContentView(R.layout.activity_add);

         editName = findViewById(R.id.editText_name);
         editName.setText("");


     }

     @Override
     protected void onStart() {
         try {
             File fileLecture = new File(getFilesDir() + file_lectureRule);
             if (fileLecture.exists()) {
                 FileInputStream fIS = new FileInputStream(fileLecture);
                 ObjectInputStream oIS = new ObjectInputStream(fIS);

                 lectureRules.addAll((List<EventRule>) oIS.readObject());
             }

             File fileExercise = new File(getFilesDir() + file_exerciseRule);
             if (fileExercise.exists()) {
                 FileInputStream fIS = new FileInputStream(fileExercise);
                 ObjectInputStream oIS = new ObjectInputStream(fIS);

                 exerciseRules.addAll((List<EventRule>) oIS.readObject());
             }

             File fileHomework = new File(getFilesDir() + file_homeworkRule);
             if (fileHomework.exists()) {
                 FileInputStream fIS = new FileInputStream(fileHomework);
                 ObjectInputStream oIS = new ObjectInputStream(fIS);

                 homeworkRules.addAll((List<EventRule>) oIS.readObject());
             }
         } catch (Exception ex) {
             if(!(ex instanceof EOFException)) {
                 Toast.makeText(this, getString(R.string.txt_oops), Toast.LENGTH_SHORT).show();
             }
         }



         //get Intent that called tis Activity + check for extra
         Intent receivedIntent = getIntent();
         if (receivedIntent.getSerializableExtra("eventRule") != null && receivedIntent.getIntExtra("type", 0) > 0) {
             EventRule ruleFromIntent = (EventRule) receivedIntent.getSerializableExtra("eventRule");
             if (receivedIntent.getIntExtra("type", 0) == 1) {
                 lectureRules.add(ruleFromIntent);
             } else if (receivedIntent.getIntExtra("type", 0) == 2) {
                 exerciseRules.add(ruleFromIntent);
             }
             if (receivedIntent.getIntExtra("type", 0) == 3) {
                 homeworkRules.add(ruleFromIntent);
             }
         }

         buildContentView();

         //set logic for discard and accept buttons
         setButtonLogic();
         saveRules = true;


         super.onStart();

     }

     @Override
     protected void onStop() {
         if (saveRules) {
             try {
                 File file_lecture = new File(getFilesDir() + file_lectureRule);
                 if (!file_lecture.exists()) {
                     try {
                         file_lecture.createNewFile();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 if (file_lecture.exists()) {
                     FileOutputStream fOS = new FileOutputStream(file_lecture);
                     ObjectOutputStream oOS = new ObjectOutputStream(fOS);
                     oOS.writeObject(lectureRules);
                     oOS.close();
                 }


                 File file_exercise = new File(getFilesDir() + file_exerciseRule);
                 if (!file_exercise.exists()) {
                     try {
                         file_exercise.createNewFile();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 if (file_exercise.exists()) {
                     FileOutputStream fOS = new FileOutputStream(file_exercise);
                     ObjectOutputStream oOS = new ObjectOutputStream(fOS);
                     oOS.writeObject(exerciseRules);
                     oOS.close();
                 }

                 File file_homework = new File(getFilesDir() + file_homeworkRule);
                 if (!file_homework.exists()) {
                     try {
                         file_homework.createNewFile();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 if (file_homework.exists()) {
                     FileOutputStream fOS = new FileOutputStream(file_homework);
                     ObjectOutputStream oOS = new ObjectOutputStream(fOS);
                     oOS.writeObject(homeworkRules);
                     oOS.close();
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
         }


         super.onStop();


     }


     // navigates back when you press the back button in the menu
     @Override
     public boolean onSupportNavigateUp() {
         onBackPressed();
         return true;
     }

     /**
      * calls buildLinearLayout for each type of event
      */
     public void buildContentView() {
         buildLinearLayout(lectureRules, 1, (LinearLayout) findViewById(R.id.linLayoutLecture));
         buildLinearLayout(exerciseRules, 2, (LinearLayout) findViewById(R.id.linLayoutExercise));
         buildLinearLayout(homeworkRules, 3, (LinearLayout) findViewById(R.id.linLayoutHomework));
     }

     /**
      * Builds an edit button and a TextView with description for every rule that has been added
      *
      * @param eR       a list of EventRules that the function iterates over
      * @param whatType 1->lectureRule
      *                 2->exerciseRule
      *                 3->homeworkRule
      * @param linLay   the LinearLayout in which the TextViews and Buttons will be added
      */
     private void buildLinearLayout(final List<EventRule> eR, final int whatType, LinearLayout linLay) {
         final int type = whatType;
         final Intent intent = new Intent(getApplicationContext(), EditRulesActivity.class);
         intent.putExtra("number", type);
         for (int i = 0; i < eR.size(); i++) {
             LinearLayout childLinLay = new LinearLayout(this);
             childLinLay.setOrientation(LinearLayout.HORIZONTAL);
             LinearLayout.LayoutParams cllparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
             childLinLay.setLayoutParams(cllparam);
             linLay.addView(childLinLay);
             final Button btnEdit = new Button(this);
             btnEdit.setTag(i);
             btnEdit.setText(getString(R.string.btn_edit));
             btnEdit.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     intent.putExtra("rule", eR.get(Integer.valueOf(String.valueOf(btnEdit.getTag()))));
                     eR.remove(Integer.valueOf(String.valueOf(btnEdit.getTag())));
                     startActivity(intent);
                 }
             });
             TextView text = new TextView(this);
             text.setText(displayTextForRules(eR.get(i)));
             childLinLay.addView(btnEdit);
             childLinLay.addView(text);
         }
         Button btnNewRule = new Button(this);
         btnNewRule.setText(getString(R.string.btn_new));
         btnNewRule.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(intent);
             }
         });
         linLay.addView(btnNewRule, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
     }

     /**
      * builds the String that will be used as description of an EventRule
      *
      * @param rule the rule that is to be displayed
      * @return the String that will be used to represent the EventRule
      */
     private String displayTextForRules(EventRule rule) {
         Calendar startDate = rule.startDate;
         Calendar endDate = rule.endDate;
         Calendar startTime = rule.startTime;
         Calendar endTime = rule.endTime;
         int dayOfWeek = rule.dayOfWeek;
         int oddOrEven = rule.oddOrEven;


         SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);


         if (!rule.repeating) {
             return "From " + dateFormat.format(startDate.getTime()) + " at " + timeFormat.format(startTime.getTime()) + " \n" +
                     "To " + dateFormat.format(endDate.getTime()) + " at " + timeFormat.format(endTime.getTime());
         } else {
             String repeat;
             switch (rule.increment) {
                 case 1:
                     repeat = "daily ";
                     break;
                 case 7:
                     repeat = "every " + weekDays[dayOfWeek - 1];
                     break;
                 case 14:
                     repeat = "every " + oddOrEvenString[oddOrEven - 1] + weekDays[dayOfWeek - 1];
                     break;
                 default:
                     repeat = getString(R.string.txt_oops);
             }
             return repeat + " " + timeFormat.format(startTime.getTime()) + " - " + timeFormat.format(endTime.getTime()) + " \n" +
                     "Start: " + dateFormat.format(startDate.getTime()) + ", End: " + dateFormat.format(endDate.getTime());
         }
     }

     /**
      * defines the behaviours of the Discard and Accept buttons.
      */
     private void setButtonLogic() {
         btnDiscard = findViewById(R.id.add_btnDiscard);
         btnAccept = findViewById(R.id.add_btnAccept);


         //goes back to MainActivity without saving anything
         btnDiscard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 deleteFiles();

                 Intent intent = new Intent(view.getContext(), MainActivity.class);
                 startActivity(intent);
             }
         });

         //checks whether name is empty and builds the subject otherwise.
         btnAccept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (editName.getText().toString().trim().length() == 0) {
                     AlertDialog.Builder builder;
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                         builder = new AlertDialog.Builder(AddActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                     } else {
                         builder = new AlertDialog.Builder(AddActivity.this);
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
                 } else {
                     buildSubject();
                 }
             }
         });
     }

     /**
      * creates a new Subject using all EventRules that have been added.
      */
     //TODO: sort list before starting new activity
     private void buildSubject() {
         String name = editName.getText().toString();
         List<Occasion> lectureOccasion = new ArrayList<Occasion>(buildOccasions(lectureRules, "lecture"));
         List<Occasion> exerciseOccasion = new ArrayList<Occasion>(buildOccasions(exerciseRules, "exercise"));
         List<Occasion> homeworkOccasion = new ArrayList<Occasion>(buildOccasions(homeworkRules, "homework"));

         Subject forIntent = new Subject(name, lectureOccasion, exerciseOccasion, homeworkOccasion);
         Intent intent = new Intent(this, MainActivity.class);
         intent.putExtra("subject", forIntent);
         deleteFiles();
         startActivity(intent);


     }

     /**
      * function that takes in a list of rules and converts it into a list of occasions
      *
      * @param rule the list of rules that is used to create the list of occasions
      * @return the list of occasions
      */
     private List<Occasion> buildOccasions(List<EventRule> rule, String defaultName) {
         List<Occasion> occ = new ArrayList<>();
         int iterator = 1;
         //iterates over each rule
         for (EventRule er : rule) {
             Date start;
             Date end;
             Calendar startCal = Calendar.getInstance();
             Calendar endCal = Calendar.getInstance();

             if (!er.repeating) {
                 startCal.setTime(er.startDate.getTime());
                 startCal.setTime(er.startTime.getTime());
                 start = startCal.getTime();
                 endCal.setTime(er.endDate.getTime());
                 endCal.setTime(er.endTime.getTime());
                 end = endCal.getTime();
                 occ.add(new Occasion(defaultName + iterator, start, end, false, false));
                 iterator++;
             } else {
                 if (er.increment == 1) {
                     Calendar iterateDate = er.startDate;
                     iterateDate.setLenient(true);
                     while (er.endDate.after(iterateDate)) {
                         if (iterateDate.get(iterateDate.DAY_OF_WEEK) > 1 && iterateDate.get(iterateDate.DAY_OF_WEEK) < 7) {
                             startCal.setTime(iterateDate.getTime());
                             startCal.setTime(er.startTime.getTime());
                             start = startCal.getTime();
                             endCal.setTime(iterateDate.getTime());
                             endCal.setTime(er.endTime.getTime());
                             end = endCal.getTime();
                             occ.add(new Occasion(defaultName + iterator, start, end, false, false));
                         }
                         iterateDate.add(iterateDate.DATE, 1);
                     }
                 } else if (er.increment == 7) {
                     Calendar iterateDate = er.startDate;
                     iterateDate.setLenient(true);
                     while (er.endDate.after(iterateDate)) {
                         if (iterateDate.get(iterateDate.DAY_OF_WEEK) == er.dayOfWeek) {
                             startCal.setTime(iterateDate.getTime());
                             startCal.setTime(er.startTime.getTime());
                             start = startCal.getTime();
                             endCal.setTime(iterateDate.getTime());
                             endCal.setTime(er.endTime.getTime());
                             end = endCal.getTime();
                             occ.add(new Occasion(defaultName + iterator, start, end, false, false));
                         }
                         iterateDate.add(iterateDate.DATE, 1);
                     }
                 } else if (er.increment == 14) {
                     Calendar iterateDate = er.startDate;
                     iterateDate.setLenient(true);
                     while (er.endDate.after(iterateDate)) {
                         if (iterateDate.get(iterateDate.DAY_OF_WEEK) == er.dayOfWeek && iterateDate.get(iterateDate.WEEK_OF_YEAR) % 2 == (er.oddOrEven - 1)) {
                             startCal.setTime(iterateDate.getTime());
                             startCal.setTime(er.startTime.getTime());
                             start = startCal.getTime();
                             endCal.setTime(iterateDate.getTime());
                             endCal.setTime(er.endTime.getTime());
                             end = endCal.getTime();
                             occ.add(new Occasion(defaultName + iterator, start, end, false, false));
                         }
                         iterateDate.add(iterateDate.DATE, 1);
                     }
                 }


             }

         }
         return occ;
     }

     private void deleteFiles() {
         clearFile(new File(getFilesDir() + file_lectureRule));
         clearFile(new File(getFilesDir() + file_exerciseRule));
         clearFile(new File(getFilesDir() + file_homeworkRule));
         saveRules = false;
     }

     public void clearFile(File file) {
         try {
             if (file.exists()) {
                 PrintWriter pw = new PrintWriter(file);
                 pw.close();
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
     }
 }