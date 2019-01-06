package com.exemplo.android.psicologiaapp.Activities.Paciente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.exemplo.android.psicologiaapp.Activities.MainActivity;
import com.exemplo.android.psicologiaapp.R;
import com.exemplo.android.psicologiaapp.Adapters.GridAdapter;
import com.exemplo.android.psicologiaapp.Classes.DayHourAvailable;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
    static String LOG_TAG = BrowseActivity.class.getName();
    User currentUser;
    DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Conversations");
    GridView gridView;
    ArrayList<User> users;
    GridAdapter adapter;
    Spinner spinner;
    String methodologySelected;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        currentUser = (User) getIntent().getSerializableExtra("user");
        emptyView = findViewById(R.id.empty_user_list);
        spinner = findViewById(R.id.spinnerBrowse);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.methodologies, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getUid() != null) {
                    databaseReference.child(currentUser.getId()).child("isOn").setValue("online");
                } else {
                    databaseReference.child(currentUser.getId()).child("isOn").setValue("offline");
                }

            }
        });
        databaseReference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String category = dataSnapshot.child("category").getValue().toString();

                if (category.equals("Psicólogo")) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();
                    String id = dataSnapshot.child("id").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    User user = new User(id, name, category, email, password);
                    ArrayList<DayHourAvailable> dayHourAvailable = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("daysAvailable").getChildren()) {
                        String time = snapshot.child("time").getValue().toString();
                        String day = snapshot.child("day").getValue().toString();
                        Boolean isDeString = (Boolean) snapshot.child("isDe").getValue();
                        dayHourAvailable.add(new DayHourAvailable(day, time, isDeString));
                    }

                    user.setDaysAvailable(dayHourAvailable);
                    user.setDescription(description);
                    Log.i(LOG_TAG, name);
                    users.add(user);
                    gridView.setAdapter(adapter);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                users.clear();
                gridView.setAdapter(adapter);
                gridView.setEmptyView(emptyView);
                methodologySelected = parent.getItemAtPosition(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(BrowseActivity.this);
                switch (methodologySelected) {
                    case "Psicanálise":
                        builder.setView(LayoutInflater.from(BrowseActivity.this).inflate(R.layout.psicanalise_dialog, null))
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();
                        break;
                    case "Terapia comportamental":
                        builder.setView(LayoutInflater.from(BrowseActivity.this).inflate(R.layout.comportamental_dialog, null))
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();
                        break;
                    case "Terapia cognitiva":
                        builder.setView(LayoutInflater.from(BrowseActivity.this).inflate(R.layout.cognitiva_dialog, null))
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();
                        break;

                }
                databaseReference.orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String category = dataSnapshot.child("category").getValue().toString();

                        if (category.equals("Psicólogo")) {
                            String methodology = dataSnapshot.child("methodology").getValue().toString();
                            if (methodology.equals(methodologySelected)) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                String email = dataSnapshot.child("email").getValue().toString();
                                String password = dataSnapshot.child("password").getValue().toString();
                                String id = dataSnapshot.child("id").getValue().toString();
                                String description = dataSnapshot.child("description").getValue().toString();
                                User user = new User(id, name, category, email, password);
                                ArrayList<DayHourAvailable> dayHourAvailable = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.child("daysAvailable").getChildren()) {
                                    String time = snapshot.child("time").getValue().toString();
                                    String day = snapshot.child("day").getValue().toString();
                                    Boolean isDeString = (Boolean) snapshot.child("isDe").getValue();
                                    dayHourAvailable.add(new DayHourAvailable(day, time, isDeString));
                                }

                                user.setDaysAvailable(dayHourAvailable);
                                user.setDescription(description);
                                Log.i(LOG_TAG, name);
                                users.add(user);
                                gridView.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        users = new ArrayList<>();
        gridView = findViewById(R.id.gridView);
        adapter = new GridAdapter(this, users);
        adapter.notifyDataSetChanged();
        //final UserAdapter adapter;
        //listView = findViewById(R.id.listBrowse);
        //adapter = new UserAdapter(this, users);
        //listView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userClicked = users.get(position);
                Intent intent = new Intent(BrowseActivity.this, ChatProfileActivity.class);
                intent.putExtra("userClicked", userClicked);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }
}
