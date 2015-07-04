package com.roodie.pifagormatrix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.model.MatrixItem;
import com.roodie.pifagormatrix.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roodie on 15.03.2015.
 */
public class UsersAdapter extends BaseAdapter {

    Context context;
    private ArrayList<User> usersDetails;
    private LayoutInflater inflater;

    public UsersAdapter(Context context , List<User> objects) {
        super();
        this.context = context;
        this.usersDetails = (ArrayList)objects;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return usersDetails == null ? 0 : usersDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return this.usersDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list_item,parent, false);
            holder = new ViewHolder();
            holder.descriptionTV = (TextView)convertView.findViewById(R.id.description_tv);
            holder.titleTV = (TextView)convertView.findViewById(R.id.title_tv);
            holder.imageView = (ImageView)convertView.findViewById(R.id.icon_iv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
            holder.titleTV.setText(this.usersDetails.get(position).getUserName());
            holder.descriptionTV.setText(context.getResources().getString(R.string.birthday_date) + this.usersDetails.get(position).getStringFullBirthday(Utils.BirthdayFormat.LONG));

        int placeholderDrawableResId = Prefs.isDarkTheme(context) ? R.drawable.user_photo_placeholder_dark
                : R.drawable.user_photo_placeholder_light;

        Picasso.with(context)
                .load(placeholderDrawableResId)
                .fit()
                .into(holder.imageView);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView descriptionTV;
        TextView titleTV;
    }
}
