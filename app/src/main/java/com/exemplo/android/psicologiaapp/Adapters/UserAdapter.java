package com.exemplo.android.psicologiaonline.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemplo.android.psicologiaapp.R;
import com.exemplo.android.psicologiaapp.Classes.User;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, @NonNull ArrayList<User> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        User user = getItem(position);
        TextView textView = convertView.findViewById(R.id.textView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        textView.setText(user.getName());
        imageView.setImageResource(R.drawable.ic_send_black_24dp);
        return convertView;
    }
}
