package com.exemplo.android.psicologiaapp.Activities.Paciente;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.exemplo.android.psicologiaapp.R;
import com.exemplo.android.psicologiaapp.Adapters.ChatAdapter;
import com.exemplo.android.psicologiaapp.Classes.Conversation;
import com.exemplo.android.psicologiaapp.Classes.Message;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    EditText messageInputView;
    ImageButton sendButtonView;
    ArrayList<Message> messages;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("conversations");
    FirebaseAuth firebaseAuth;
    Message message;
    User currentUser, userClicked;
    String time;
    ListView list;
    ChatAdapter adapter;
    Conversation conversation;
    static String LOG_TAG = ChatActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        userClicked = (User) getIntent().getSerializableExtra("userClicked");

        conversation = new Conversation(currentUser.getId() + ":" + userClicked.getId(), userClicked.getName(), currentUser.getName());

        messageInputView = findViewById(R.id.messageInputId);
        sendButtonView = findViewById(R.id.sendButtonId);
        list = findViewById(R.id.listChat);


        messages = new ArrayList<>();
        adapter = new ChatAdapter(this, messages, currentUser.getId());

        adapter.notifyDataSetChanged();


        sendButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Long millisLong = date.getTime();
                int millis = millisLong.intValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
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

    }


}