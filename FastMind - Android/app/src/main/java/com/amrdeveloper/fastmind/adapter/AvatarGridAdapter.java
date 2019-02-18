package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.amrdeveloper.fastmind.R;

import java.util.List;

public class AvatarGridAdapter extends ArrayAdapter<Integer> {

    public AvatarGridAdapter(@NonNull Context context, List<Integer> avatars) {
        super(context, 0,avatars);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.avatar_item, parent, false);
        }
        ImageView avatar = convertView.findViewById(R.id.avatarImage);
        avatar.setImageResource(getItem(position));
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }
}
