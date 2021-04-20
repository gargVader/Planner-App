package com.example.plannerapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plannerapp.AddNewPlan;
import com.example.plannerapp.MainActivity;
import com.example.plannerapp.Model.Plan;
import com.example.plannerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> planList;
    private MainActivity activity;
    private FirebaseFirestore firestore;


    public PlanAdapter(MainActivity activity, List<Plan> planList) {
        this.planList = planList;
        this.activity = activity;
    }

    public Context getContext() {
        return activity;
    }

    public void deletePlan(int position) {
        Plan plan = planList.get(position);
        firestore.collection("plan").document(plan.PlanId).delete();
        planList.remove(position);
        notifyItemRemoved(position);
    }

    public void editPlan(int position) {
        Plan plan = planList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("planText", plan.getPlanText());
        AddNewPlan addNewPlan = new AddNewPlan();
        addNewPlan.setArguments(bundle);
        addNewPlan.show(activity.getSupportFragmentManager(), addNewPlan.getTag());
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPlan(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = activity.getLayoutInflater().inflate(R.layout.layout_alert_dialog, null);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                view.findViewById(R.id.textDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                        deletePlan(position);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView planTextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planTextTextView = itemView.findViewById(R.id.planText);
        }
    }

}
