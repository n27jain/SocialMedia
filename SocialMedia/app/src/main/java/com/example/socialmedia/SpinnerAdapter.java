package com.example.socialmedia;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerObject> {


    public SpinnerAdapter(Context context, ArrayList<SpinnerObject> list) {
        super(context, 0, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.custom_spinner,parent,false);
        }
        ImageView iconImageView = convertView.findViewById(R.id.iconView);
        TextView label = convertView.findViewById(R.id.labelView);

        SpinnerObject object = getItem(position);

        if (object != null) {
            iconImageView.setImageResource(object.iconNum);
            label.setText(object.label);
        }
        return convertView;

    }
}
