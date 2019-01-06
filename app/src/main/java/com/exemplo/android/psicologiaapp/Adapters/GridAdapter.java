package com.exemplo.android.psicologiaapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exemplo.android.psicologiaapp.Classes.User;
import com.exemplo.android.psicologiaapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<User> {
    public GridAdapter(@NonNull Context context, @NonNull ArrayList<User> users) {
        super(context, R.layout.grid_item, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        User user = getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);
        name.setText(user.getName());
        ratingBar.setRating(4);
        final ImageView image = convertView.findViewById(R.id.imageView);
        Task<Uri> storage = FirebaseStorage.getInstance().getReference().child("images/" + user.getId())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).
                                load(uri.toString()).
                                into(image);
                    }
                });
        return convertView;
    }
}
