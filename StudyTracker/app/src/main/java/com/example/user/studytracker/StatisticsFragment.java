package com.example.user.studytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    View fragmentView;
    int selected;
    List<Subject> subjectsList = new ArrayList<>();
    MainActivity ma;
    Button Share;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_statistics, container, false);

        Share=(Button) fragmentView.findViewById(R.id.Share);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {     Intent sharing= new Intent(Intent.ACTION_SEND);
                String shareBody= "Your body here";
                String shareSub="Your Subjekt here";
                sharing.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                sharing.putExtra(Intent.EXTRA_TEXT,shareSub);


                sharing.setType("text/plain");
                startActivity(Intent.createChooser(sharing,"Use Share"));



            }


        });

        ma = (MainActivity) getActivity();


        return fragmentView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            subjectsList.addAll(ma.subjectList);
            calculateStats();

            Button btn = fragmentView.findViewById(R.id.btn_stats_redo);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subjectsList.clear();
                    subjectsList.addAll(ma.subjectList);
                    calculateStats();
                }
            });



        }



    }

    void calculateStats(){
        selected = ma.selected;
        if(selected==-1){
            Toast.makeText(getActivity(), "please select a subject first", Toast.LENGTH_SHORT).show();
        }
        else if(selected>=0 && selected<subjectsList.size()){
            Subject tempS =ma.subjectList.get(selected);
            subjectsList.clear();
            subjectsList.add(tempS);

            fillTextViews(1, (TextView) fragmentView.findViewById(R.id.txt_stats_lec_01), (TextView) fragmentView.findViewById(R.id.txt_stats_lec_02));
            fillTextViews(2, (TextView) fragmentView.findViewById(R.id.txt_stats_exe_01), (TextView) fragmentView.findViewById(R.id.txt_stats_exe_02));
            fillTextViews(3, (TextView) fragmentView.findViewById(R.id.txt_stats_home_01), (TextView) fragmentView.findViewById(R.id.txt_stats_home_02));
        }
        else{
            fillTextViews(1, (TextView) fragmentView.findViewById(R.id.txt_stats_lec_01), (TextView) fragmentView.findViewById(R.id.txt_stats_lec_02));
            fillTextViews(2, (TextView) fragmentView.findViewById(R.id.txt_stats_exe_01), (TextView) fragmentView.findViewById(R.id.txt_stats_exe_02));
            fillTextViews(3, (TextView) fragmentView.findViewById(R.id.txt_stats_home_01), (TextView) fragmentView.findViewById(R.id.txt_stats_home_02));

        }



    }

    void fillTextViews(int index, TextView t1, TextView t2){
        int factor = 0;
        int denominator = 0;
        for(Subject s: subjectsList){
            if(index ==1){
                for(Occasion o:s.lecture){
                    denominator+=1;
                    if(o.attended){
                        factor+=1;
                    }
                }
            }
            else if(index ==2){
                for(Occasion o:s.exercise){
                    denominator+=1;
                    if(o.attended){
                        factor+=1;
                    }
                }
            }
            else if(index ==3){
                for(Occasion o:s.dueDate){
                    denominator+=1;
                    if(o.attended){
                        factor+=1;
                    }
                }
            }
        }
        if(denominator!=0){
            int x =factor*100/denominator;
            t1.setText(x +"%");
        }
        else{
            t1.setText("none");
        }

        t2.setText(factor+" / "+denominator+" attended");
    }



}
