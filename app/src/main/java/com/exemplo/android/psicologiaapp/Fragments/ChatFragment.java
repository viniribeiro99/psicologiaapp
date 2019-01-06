package com.exemplo.android.psicologiaapp.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.exemplo.android.psicologiaapp.Activities.Paciente.ChatActivity;
import com.exemplo.android.psicologiaapp.Activities.Paciente.ChatProfileActivity;
import com.exemplo.android.psicologiaapp.Adapters.ChatAdapter;
import com.exemplo.android.psicologiaapp.Classes.Conversation;
import com.exemplo.android.psicologiaapp.Classes.Message;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatFragment extends Fragment {
    public ChatFragment() {
    }
    EditText messageInputView;
    ImageButton sendButtonView;
    ArrayList<Message> messages;
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("conversations");
    Message message;
    User currentUser, userClicked;
    String time;
    ListView list;
    ChatAdapter adapter;
    Conversation conversation;
    static String LOG_TAG = ChatActivity.class.getName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat, container, false);
        ChatProfileActivity activity = (ChatProfileActivity) getActivity();


        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        currentUser = (User) activity.getIntent().getSerializableExtra("currentUser");
        userClicked = (User) activity.getIntent().getSerializableExtra("userClicked");

        if(FirebaseAuth.getInstance().getUid() != null) {
            userReference.child(currentUser.getId()).child("isOn").setValue("online");
        } else {
            userReference.child(currentUser.getId()).child("isOn").setValue("offline");
        }
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(userClicked.getName());

        conversation = new Conversation(currentUser.getId() + ":" + userClicked.getId(), userClicked.getName(), currentUser.getName());

        messageInputView = rootView.findViewById(R.id.messageInputId);
        sendButtonView = rootView.findViewById(R.id.sendButtonId);
        list = rootView.findViewById(R.id.listChat);


        messages = new ArrayList<>();
        adapter = new ChatAdapter(getContext(), messages, currentUser.getId());

        adapter.notifyDataSetChanged();


        sendButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Long millisLong = date.getTime();
                int millis = millisLong.intValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                time = dateFormat.format(date);
                if (messageInputView.getText().equals("")) {

                } else {
                    message = new Message(messageInputView.getText().toString(), currentUser.getId(), time, millis);
                    conversation.setLastMessage(message);
                    Log.i(LOG_TAG, ((reference.child(currentUser.getId() + ":" + userClicked.getId())).toString()));
                    conversation.saveOnDatabase();
                    messageInputView.setText("");
                }

            }
        });

        reference = reference.child(conversation.getId()).child(currentUser.getName()+":"+userClicked.getName());
        Log.i(LOG_TAG, reference.toString());

        reference.orderByChild("count").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    String key = dataSnapshot.getKey();
                    message = new Message();
                    String millis = dataSnapshot.child("millis").getValue().toString();
                    message.setTime(dataSnapshot.child("time").getValue().toString());
                    message.setText(dataSnapshot.child("text").getValue().toString());
                    message.setRemetentId(dataSnapshot.child("remetentId").getValue().toString());
                    Log.i(LOG_TAG, millis + message.getRemetentId());
                    messages.add(message);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    list.post(new Runnable() {
                        @Override
                        public void run() {
                            list.setSelection(adapter.getCount() - 1);
                        }
                    });


                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG,e.getMessage().toString());
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
        return rootView;
    }
}
