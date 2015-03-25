package com.roodie.pifagormatrix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roodie.pifagormatrix.R;

/**
 * Created by Roodie on 18.03.2015.
 */
public class GridViewAdapter extends BaseAdapter {
        private Context context;
        private final String[] textViewValues;

        public GridViewAdapter(Context context, String[] textViewValues) {
            this.context = context;
            this.textViewValues = textViewValues;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(context);

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.grid_item, null);

                // set value into textview
                TextView textView = (TextView) gridView
                        .findViewById(R.id.grid_text);
                textView.setText(textViewValues[position]);
            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }

        @Override
        public int getCount() {
            return textViewValues.length;
        }

        @Override
        public Object getItem(int position) {
            return textViewValues[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }