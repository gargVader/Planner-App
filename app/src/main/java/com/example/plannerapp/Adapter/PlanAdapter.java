package com.example.plannerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannerapp.MainActivity;
import com.example.plannerapp.Model.Plan;
import com.example.plannerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> planList;
    private MainActivity activity;
    private FirebaseFirestore firestore;


    public PlanAdapter(MainActivity activity,List<Plan> planList) {
        this.planList = planList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.planTextTextView.setText(plan.getPlanText());
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView planTextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planTextTextView = itemView.findViewById(R.id.planText);
        }
    }

}
