package com.example.plannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewPlan extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewPlan";
    private EditText planEditText;
    private LinearLayout savePlanButton;
    private FirebaseFirestore firestore;
    private Context context;


    public static AddNewPlan newPlan() {
        return new AddNewPlan();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        planEditText = view.findViewById(R.id.planEditText);
        savePlanButton = view.findViewById(R.id.savePlanLayout);

        firestore = FirebaseFirestore.getInstance();

        planEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    savePlanButton.setEnabled(false);
                } else {
                    savePlanButton.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String planText = planEditText.getText().toString().trim();

                if (planText.isEmpty()) {
                    Toast.makeText(context, "Plan cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, String> planMap = new HashMap<>();
                    planMap.put("planText", planText);

                    firestore.collection("plan").add(planMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Plan saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, task.getException().getMessage());
                                Toast.makeText(context, "Error in saving plan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(context, "Error in saving plan", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                dismiss();

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
