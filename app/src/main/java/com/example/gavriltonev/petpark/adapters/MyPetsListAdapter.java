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
        ViewHolder holder;

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

        // TODO: add this instead Picasso.with(getContext()).load(getItem(position).getThumbnail() ).into(holder.petThumbnail);
        Picasso.with(getContext ()).load("http://wac.450f.edgecastcdn.net/80450F/hudsonvalleycountry.com/files/2015/01/cat4.jpg" ).into(holder.petThumbnail);
        return convertView;
    }

    class ViewHolder {
        ImageView petThumbnail;
        TextView petName;
        TextView petSpecies;
    }
}
