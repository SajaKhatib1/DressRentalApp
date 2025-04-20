package com.example.dressrentalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DressAdapter extends ArrayAdapter<Dress> {
    private Context context;
    private int resource;

    public DressAdapter(@NonNull Context context, int resource, @NonNull List<Dress> dresses) {
        super(context, resource, dresses);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.dressImage);
            holder.name = convertView.findViewById(R.id.dressName);
            holder.price = convertView.findViewById(R.id.dressPrice);
            holder.quantity = convertView.findViewById(R.id.dressQuantity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Dress dress = getItem(position);

        if (dress != null) {
            holder.image.setImageResource(dress.getImageRes());
            holder.name.setText(dress.getName());
            holder.price.setText(String.format("%d₪", dress.getPrice()));
            holder.quantity.setText(String.format(context.getString(R.string.remaining), dress.getQuantity()));
        }

        return convertView;
    }

    public void updateDresses(List<Dress> newDresses) {
        clear();
        addAll(newDresses);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView price;
        TextView quantity;
    }
}
