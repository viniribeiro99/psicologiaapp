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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.android.psicologiaapp.Activities.MainActivity;
import com.exemplo.android.psicologiaapp.Activities.Paciente.BrowseActivity;
import com.exemplo.android.psicologiaapp.Adapters.ConversationAdapter;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class PsicologoActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    User currentUser;
    static String LOG_TAG = BrowseActivity.class.getName();
    ArrayList<Conversation> conversations = new ArrayList<>();
    ConversationAdapter adapter;
    ListView listView;
    String[] chatIdSplited;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psicologo);

        TextView emptyView = findViewById(R.id.empty_list_item);
        currentUser = (User) getIntent().getSerializableExtra("user");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(currentUser.getEmail(), currentUser.getPassword());

        conversations = new ArrayList<>();
        adapter = new ConversationAdapter(this, conversations);
        adapter.notifyDataSetChanged();
        listView = findViewById(R.id.conversationList);

        listView.setEmptyView(emptyView);
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


        reference.child("conversations").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String chatId = dataSnapshot.getKey();
                chatIdSplited = chatId.split(":");
                String userId = chatIdSplited[1];
                Log.i(LOG_TAG, dataSnapshot.getValue().toString());
                if (userId.equals(currentUser.getId())) {
                    Conversation conversation = new Conversation();
                    conversation.setId(chatIdSplited[0] + ":" + currentUser.getId());
                    String child = dataSnapshot.getChildren().iterator().next().getKey();
                    Log.i(LOG_TAG, child);
                    String[] anotherUserName = child.split(":");
                    conversation.setAnotherUserName(anotherUserName[0]);
                    Iterator<DataSnapshot> iterator = dataSnapshot.child(anotherUserName[0] + ":" + currentUser.getName()).getChildren().iterator();
                    DataSnapshot element = null;
                    while (iterator.hasNext()) {
                        element = iterator.next();
                    }
                    String text = element.child("text").getValue().toString();
                    String remetentId = element.child("remetentId").getValue().toString();
                    String time = element.child("time").getValue().toString();
                    Message message = new Message(text, remetentId, time);

                    conversation.setLastMessage(message);

                    //Log.i(LOG_TAG, lastMessage.getText());
                    conversations.add(conversation);
                    listView.setAdapter(adapter);

                }
                if (conversations.size() == 0) {
                    Toast.makeText(PsicologoActivity.this, "Não há conversas", Toast.LENGTH_SHORT).show();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Conversation conversation = conversations.get(position);
                String conversationId = conversation.getId();
                final String userClickedId = conversationId.split(":")[0];
                reference.child("usuarios").child(userClickedId).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String id = userClickedId;
                        String name = conversation.getAnotherUserName();
                        User userClicked = new User();
                        userClicked.setName(name);
                        userClicked.setId(id);
                        Intent intent = new Intent(PsicologoActivity.this, ChatActivityPsicologo.class);
                        intent.putExtra("userClicked", userClicked);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
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
        });

            reference.child("usuarios").addValueEventListener(new ValueEventListener() {
                String[] nameAndToken = {};

                @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String token = dataSnapshot.child(currentUser.getId()).child("token").getValue().toString();
                nameAndToken = token.split("-");
                Log.i(LOG_TAG, token.toString());
                if (token.equals("0")) {

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PsicologoActivity.this);
                    dialog.setTitle(nameAndToken[0] + " deseja iniciar uma chamada com você. Iniciar chamada?");
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("https://talky.io/" + nameAndToken[1]);
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
    protected void onPostResume() {
        super.onPostResume();
        reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("online");

    }

    @Override
    protected void onStop() {
        reference.child("usuarios").child(currentUser.getId()).child("isOn").setValue("offline");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PsicologoActivity.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }
}

