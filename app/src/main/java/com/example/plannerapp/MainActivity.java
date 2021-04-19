package com.example.plannerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.plannerapp.Adapter.PlanAdapter;
import com.example.plannerapp.Model.Plan;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView addPlanButton;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    RecyclerView plannerRecView;
    private FirebaseFirestore firestore;
    private PlanAdapter planAdapter;
    private List<Plan> planList;

    private static final String TAG = "MainActivity";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPlanButton = (ImageView) findViewById(R.id.addPlan);
        plannerRecView = (RecyclerView) findViewById(R.id.plannerRecView);


        addPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewPlan.newPlan().show(getSupportFragmentManager(), AddNewPlan.TAG);

            }
        });
        firestore = FirebaseFirestore.getInstance();
        planList = new ArrayList<>();
        planAdapter = new PlanAdapter(MainActivity.this, planList);

        plannerRecView.setAdapter(planAdapter);
        plannerRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        showData();
    }

    private void showData(){
        firestore.collection("plan")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        Plan plan  = documentChange.getDocument().toObject(Plan.class).withId(id);

                        planList.add(0, plan);
                        planAdapter.notifyDataSetChanged();
                    }
                }
//                Collections.reverse(planList);
            }
        });
    }

}