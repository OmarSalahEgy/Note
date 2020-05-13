package com.example.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bakingapp.R;
import com.example.bakingapp.fragment.StepDetailFragment;
import com.example.bakingapp.model.Steps;

import java.util.ArrayList;


public class StepDetailActivity extends AppCompatActivity {

    Steps step;
    FragmentManager fragmentManager;
    ArrayList<Steps> steps;
    private Button nextButton;
    private Button priviousButton;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent intent = getIntent();
        step = intent.getExtras().getParcelable(DetailActivity.START_DETAIL_STEP);

        steps = intent.getExtras().getParcelableArrayList(DetailActivity.START_DETAIL_STEPS);


        nextButton= findViewById(R.id.next);
        priviousButton=findViewById(R.id.previous);

          index= steps.indexOf(step);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    index=index+1;
                    fragmentManager = getSupportFragmentManager();
                if (index!=0||index!=steps.size()){
                    priviousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }

                    if(index==steps.size()){
                        Toast.makeText(StepDetailActivity.this,"this last step",Toast.LENGTH_LONG).show();
                        nextButton.setVisibility(View.INVISIBLE);
                    }else {

                    // Creating a new head fragment
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setStepDetailFragment(steps,steps.get(index),StepDetailActivity.this);
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_detail_frame_layout, stepDetailFragment)
                            .commit();
            }
            }
        });
        priviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index=index-1;
                fragmentManager = getSupportFragmentManager();
                if (index!=0||index!=steps.size()){
                    priviousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }


                if(index==-1){
                    Toast.makeText(StepDetailActivity.this,"this last step",Toast.LENGTH_LONG).show();
                    priviousButton.setVisibility(View.INVISIBLE);

                }else {

                    // Creating a new head fragment
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setStepDetailFragment(steps,steps.get(index),StepDetailActivity.this);
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_detail_frame_layout, stepDetailFragment)
                            .commit();
                }
            }
        });

        if (savedInstanceState == null) {

            // In two-pane mode, add initial BodyPartFragments to the screen

            fragmentManager = getSupportFragmentManager();

            // Creating a new head fragment
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepDetailFragment(steps,step,this);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_frame_layout, stepDetailFragment)
                    .commit();

        }
    }
}
