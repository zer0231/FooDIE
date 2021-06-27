package com.zero.foodie;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseQueryHandler {


    public DatabaseQueryHandler() {

    }

    public void copy(String destination, String source) {
        FirebaseDatabase.getInstance().getReference(source).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference(destination).child(snapshot.getKey()).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                            }

                        }
                    });
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void move(String destination, String source) {

//        DatabaseQueryHandler databaseQueryHandler = new DatabaseQueryHandler(MainActivity.this);
//        databaseQueryHandler.move("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Waiting","pendingOrder/a20210617091203");


        FirebaseDatabase.getInstance().getReference(source).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference(destination).child(snapshot.getKey()).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                FirebaseDatabase.getInstance().getReference(source).removeValue();
                            }

                        }
                    });
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
