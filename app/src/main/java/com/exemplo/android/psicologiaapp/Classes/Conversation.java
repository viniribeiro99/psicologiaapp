package com.exemplo.android.psicologiaapp.Classes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Conversation {
    String id;
    String currentUserName;
    String anotherUserName;
    Message lastMessage;
    ArrayList<Message> messages = new ArrayList<>();
    public static DatabaseReference reference;


    public void saveOnDatabase() {
            reference = FirebaseDatabase.getInstance().getReference().child("conversations");
            reference.child(id).child(currentUserName+":"+anotherUserName).push().setValue(lastMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("database", "conversa salva no database");
                }
            });
    }

    public Conversation(String id, String anotherUserName, String currentUserName) {
        this.id = id;
        this.anotherUserName = anotherUserName;
        this.currentUserName = currentUserName;
    }
    public Conversation() {
    }


    public String getId() {
        return id;
    }

    public void addMessage(Message message) {
        messages.add(message);
        lastMessage = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnotherUserName() {
        return anotherUserName;
    }

    public void setAnotherUserName(String anotherUserName) {
        this.anotherUserName = anotherUserName;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

}
