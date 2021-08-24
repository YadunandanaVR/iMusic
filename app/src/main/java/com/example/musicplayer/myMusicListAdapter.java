package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class myMusicListAdapter extends ArrayAdapter<String> {

    private String[] items;
    public myMusicListAdapter(@NonNull Context context, int resource, @NonNull String[] items) {
        super(context, resource, items);
        this.items = items;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items[position];
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_layout, parent, false);
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(getItem(position));
        textView.setSelected(true);
        return convertView;
    }
}
