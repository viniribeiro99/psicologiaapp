package com.exemplo.android.psicologiaapp.Classes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {
    String id;
    String name;
    String category;
    String email;
    String password;
    String description = "";
    String methodology;
    boolean isOn;
    ArrayList<DayHourAvailable> daysAvailable;
    int token;
    Byte[] bytes;

    public static FirebaseAuth firebaseAuth;

   /* public static FirebaseAuth getAuth() {
        if(firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;

    }*/


    public void saveOnDatabase() {
        Task databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(getId()).setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("database", "Informações salvas no database");
                    }
                });
    }

    public User(String id, String name, String category, String email, String password) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public User(String name, String category, String email, String password) {
        this.name = name;
        this.category = category;
        this.email = email;
        this.password = password;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DayHourAvailable> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(ArrayList<DayHourAvailable> daysAvailables) {
        this.daysAvailable = daysAvailables;
    }

    public String getMethodology() {
        return methodology;
    }

    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }


}
