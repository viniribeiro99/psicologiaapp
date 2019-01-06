package com.exemplo.android.psicologiaonline.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exemplo.android.psicologiaapp.Activities.Paciente.ChatProfileActivity;
import com.exemplo.android.psicologiaapp.Classes.DayHourAvailable;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Random;

public class PsicologoProfileFragment extends Fragment {
    public PsicologoProfileFragment() {
    }
    User currentUser, userClicked;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
    String statusValue;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_psicologo_profile, container, false);
        final ImageView profileImage = rootView.findViewById(R.id.profileImage);
        TextView descriptionText = rootView.findViewById(R.id.descriptionText);
        ImageView videoCall = rootView.findViewById(R.id.videoCall);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ListView daysAvailableView = rootView.findViewById(R.id.daysAvailable);
        ArrayList<DayHourAvailable> daysAvailableList = new ArrayList<>();
        ArrayList<String> daysAvailableStringList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.hour_list_item, daysAvailableStringList);
        final ImageView statusImage = rootView.findViewById(R.id.status);
        ChatProfileActivity activity = (ChatProfileActivity) getActivity();

        currentUser = (User) activity.getIntent().getSerializableExtra("currentUser");
        userClicked = (User) activity.getIntent().getSerializableExtra("userClicked");

        descriptionText.setText(userClicked.getDescription());
        toolbar.setTitle(userClicked.getName());

        Task<Uri> storage = FirebaseStorage.getInstance().getReference().child("images/" + userClicked.getId())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).
                                load(uri.toString()).
                                into(profileImage);
                    }
                });

        reference.child(userClicked.getId()).child("isOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statusValue = dataSnapshot.getValue().toString();
                if (statusValue.equals("online")) {
                    statusImage.setImageResource(R.drawable.greencircle);
                } else {
                    statusImage.setImageResource(R.drawable.whitecircle);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusValue.equals("online")) {
                    Random random = new Random();
                    Integer number = random.nextInt(9000000 - 10000);
                    reference.child(userClicked.getId()).child("token").setValue(currentUser.getName() + "-" + number);
                    Uri uri = Uri.parse("https://talky.io/" + number);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "O psicólogo deve estar online para iniciar a chamada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        daysAvailableList = userClicked.getDaysAvailable();
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<daysAvailableList.size(); i++) {
            String dayAndHour;
            DayHourAvailable item = daysAvailableList.get(i);
            if(item.getIsDe()) {
                String firstLetter = item.getDay().substring(0, 1).toUpperCase();
                builder.append(firstLetter+item.getDay().substring(1) + ": De " + item.getTime());
            } else {
                builder.append(" até " + item.getTime());
                dayAndHour = builder.toString();
                daysAvailableStringList.add(dayAndHour);
                builder.setLength(0);
            }

        }
        daysAvailableView.setAdapter(adapter);

        return rootView;
    }
}
