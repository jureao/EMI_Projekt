package com.example.user.studytracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.studytracker.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class LectureFragment extends Fragment {

    Button btnDelete;
    View fragmentView;
    List<Subject> subjectList;
    int selected;
    MainActivity ma;
    Button Data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Inflates the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_lecture, container, false);
        ma = (MainActivity) getActivity();
        subjectList = ma.subjectList;

        Data=(Button) fragmentView.findViewById(R.id.Data);
        Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wechselMain= new Intent(getActivity(),takePhotoActivity.class);
                startActivity(wechselMain);
            }
        });
        btnDelete = (Button) fragmentView.findViewById(R.id.button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubject();
            }
        });
        setButtonLogic();
        return fragmentView;
    }

    public void deleteSubject() {
        selected = ma.selected;
        if(selected>=0 && selected<subjectList.size()){
            ma.subjectList.remove(selected);
            ma.onStop();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    public void setButtonLogic() {
        Button btnLectures = fragmentView.findViewById(R.id.btn_lectures);
        Button btnExercises = fragmentView.findViewById(R.id.btn_exercises);
        Button btnHomework = fragmentView.findViewById(R.id.btn_homework);

        btnLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLecture = new Intent(getActivity(), LectureActivity.class);
                setExtra(intentLecture);
            }
        });
        btnExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLecture = new Intent(getActivity(), ExerciseActivity.class);
                setExtra(intentLecture);
            }
        });
        btnHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLecture = new Intent(getActivity(), HomeworkActivity.class);
                setExtra(intentLecture);
            }
        });
    }

    private void setExtra(Intent i) {
        selected = ma.selected;

        if (selected == 1000) {
            i.putExtra("subjects", (Serializable) subjectList);
            startActivity(i);
        } else {
            if (selected == -1) {
                Toast.makeText(getActivity(), "please select a Subject first", Toast.LENGTH_SHORT).show();
            } else if (selected < subjectList.size()) {
                i.putExtra("subject", (Serializable) subjectList.get(selected));
                i.putExtra("position", selected);
                startActivity(i);
            }
        }
    }
}


