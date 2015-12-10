package com.example.gavriltonev.petpark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Pet;
import com.squareup.picasso.Picasso;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class PetsGalleryAdapter extends ArrayAdapter<Pet> {

    public PetsGalleryAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_pets_gallery_thumbnail, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.petGalleryImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String profilePic = getItem(position).getProfilePic();
        if (profilePic != null) {
            holder.image.setColorFilter(null);
            Picasso.with(getContext()).load(profilePic).into(holder.image);
        } else {
            holder.image.setColorFilter(R.attr.colorPrimary);
            holder.image.setImageResource(R.drawable.ic_menu_gallery);
        }

        return convertView;

    }

    private class ViewHolder {
        ImageView image;
    }
}
