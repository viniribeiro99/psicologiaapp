package com.exemplo.android.psicologiaapp.Activities.Psicologo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.exemplo.android.psicologiaapp.Activities.Paciente.ChatActivity;
import com.exemplo.android.psicologiaapp.Adapters.ChatAdapter;
import com.exemplo.android.psicologiaapp.Classes.Conversation;
import com.exemplo.android.psicologiaapp.Classes.Message;
import com.exemplo.android.psicologiaapp.Classes.Psicologo;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivityPsicologo extends AppCompatActivity {
    EditText messageInputView;
    ImageButton sendButtonView;
    ArrayList<Message> messages;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
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

        if (FirebaseAuth.getInstance().getUid() != null) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("online");
                }
            }, 3000);
        } else {
            reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("offline");
        }

        conversation = new Conversation(userClicked.getId() + ":" + currentUser.getId(), currentUser.getName(), userClicked.getName());

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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                time = dateFormat.format(date);
                message = new Message(messageInputView.getText().toString(), currentUser.getId(), time, millis);
                conversation.setLastMessage(message);
                Log.i(LOG_TAG, ((reference.child(currentUser.getId() + ":" + userClicked.getId())).toString()));
                conversation.saveOnDatabase();
                messageInputView.setText("");
            }
        });


        Log.i(LOG_TAG, reference.toString());
        reference.child("conversations").child(conversation.getId()).child(userClicked.getName() + ":" + currentUser.getName()).orderByChild("count").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Log.i(LOG_TAG, dataSnapshot.getValue().toString());
                    message = new Message();
                    message.setTime(dataSnapshot.child("time").getValue().toString());
                    message.setText(dataSnapshot.child("text").getValue().toString());
                    message.setRemetentId(dataSnapshot.child("remetentId").getValue().toString());
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
                    Log.e(LOG_TAG, e.getMessage().toString());
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
        reference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String token = dataSnapshot.child(currentUser.getId()).child("token").getValue().toString();
                Log.i(LOG_TAG, token.toString());
                if (token.equals("0")) {

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivityPsicologo.this);
                    dialog.setTitle("deseja iniciar uma chamada com você. Iniciar chamada?");
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("https://talky.io/" + token);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            reference.child("usuarios").child(currentUser.getId()).child("token").setValue("0");

                        }
                    });
                    dialog.setNegativeButton("Agora não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            reference.child("usuarios").child(currentUser.getId()).child("token").setValue("0");
                        }
                    });
                    dialog.show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("offline");
        super.onStop();
        Log.i(LOG_TAG, "stop");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("online");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivityPsicologo.this, PsicologoActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }
}