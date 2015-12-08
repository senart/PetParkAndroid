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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_pets_gallery_thumbnail, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.petGalleryImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // TODO: replace with this Picasso.with(getContext()).load(getItem(position).getThumbnail()).into(holder.image);
        Picasso.with(getContext()).load("http://wac.450f.edgecastcdn.net/80450F/hudsonvalleycountry.com/files/2015/01/cat4.jpg").into(holder.image);

        return convertView;

    }

    private class ViewHolder {
        ImageView image;
    }
}
