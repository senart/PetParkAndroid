package com.example.gavriltonev.petpark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Pet;
import com.squareup.picasso.Picasso;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class MyPetsListAdapter extends ArrayAdapter<Pet> {

    public MyPetsListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_pet_list_item, parent, false);

            holder.petName = (TextView) convertView.findViewById( R.id.petName );
            holder.petSpecies = (TextView) convertView.findViewById( R.id.petSpecies);
            holder.petThumbnail = (ImageView) convertView.findViewById (R.id.petThumbnail);

            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.petName.setText(getItem(position).getName());
        holder.petSpecies.setText(getItem(position).getSpecies());

        String profilePic = getItem(position).getProfilePic();
        if (profilePic != null) {
            holder.petThumbnail.setColorFilter(null);
            Picasso.with(getContext ()).load(getItem(position).getProfilePic() ).into(holder.petThumbnail);
        } else {
            holder.petThumbnail.setColorFilter(R.attr.colorPrimary);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView petThumbnail;
        TextView petName;
        TextView petSpecies;
    }
}
