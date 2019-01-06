package com.exemplo.android.psicologiaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.exemplo.android.psicologiaapp.Classes.Message;
import com.exemplo.android.psicologiaapp.R;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<Message> {
    String currentUserId;

    public ChatAdapter(@NonNull Context context, ArrayList<Message> messages, String currentUserId) {
        super(context, 0, messages);
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);
        if (convertView == null) {
            if (message.getRemetentId().equals(currentUserId)) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_sent, parent, false);
                TextView textView = convertView.findViewById(R.id.messageSentView);
                TextView time = convertView.findViewById(R.id.time);
                textView.setText(message.getText());
                time.setText(message.getTime());
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_received, parent, false);
                TextView messageReceived = convertView.findViewById(R.id.messageReceivedId);
                TextView time = convertView.findViewById(R.id.time);
                messageReceived.setText(message.getText());
                time.setText(message.getTime());

            }
        }



        return convertView;
    }
}


