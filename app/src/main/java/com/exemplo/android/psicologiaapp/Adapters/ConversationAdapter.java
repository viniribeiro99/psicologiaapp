package com.exemplo.android.psicologiaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.exemplo.android.psicologiaapp.Classes.Conversation;
import com.exemplo.android.psicologiaapp.R;

import java.util.ArrayList;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
    public ConversationAdapter(@NonNull Context context, ArrayList<Conversation> conversations) {
        super(context, 0, conversations);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Conversation conversation = getItem(position);
        if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.conversation_item, parent, false);
            }

        TextView username = convertView.findViewById(R.id.usernameView);
        username.setText(conversation.getAnotherUserName());
        TextView lastMessage = convertView.findViewById(R.id.lastMessageView);
        lastMessage.setText(conversation.getLastMessage().getText());


        return convertView;
    }
}

