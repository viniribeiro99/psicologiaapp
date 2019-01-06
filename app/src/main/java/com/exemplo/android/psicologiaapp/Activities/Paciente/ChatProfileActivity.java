package com.exemplo.android.psicologiaapp.Activities.Paciente;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.exemplo.android.psicologiaapp.Classes.SimpleFragmentAdapter;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatProfileActivity extends AppCompatActivity {
    User currentUser, userClicked;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        userClicked = (User) getIntent().getSerializableExtra("userClicked");

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getUid() != null) {
                    reference.child(currentUser.getId()).child("isOn").setValue("online");
                } else {
                    reference.child(currentUser.getId()).child("isOn").setValue("offline");
                }

            }
        });


        ViewPager viewPager = findViewById(R.id.viewPager);
        SimpleFragmentAdapter adapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
