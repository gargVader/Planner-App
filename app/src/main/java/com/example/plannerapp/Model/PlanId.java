package com.example.plannerapp.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PlanId {
    @Exclude
    public String PlanId;

    public <T extends PlanId> T withId(@NonNull final String id){
        this.PlanId = id;
        return (T) this;
    }
}
