package com.roodie.pifagormatrix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Roodie on 18.03.2015.
 */
public class GridViewAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> matrixValues;  // Values for GridItems
        private static final Integer[] matrixColors = {3, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1}; // Normal values in Grid


        public GridViewAdapter(Context context, ArrayList<String> textViewValues) {
            this.context = context;
            this.matrixValues = textViewValues;
            //this.textViewValues = textViewValues;
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
                textView.setText(matrixValues.get(position));
                // if ( position % 4 - 3 != 0) { //position != 3, 7, 11 ..
                    textView.setBackgroundColor(Utils.getColor(context, Utils.simplifyNumberHalf(Utils.getValueByString(matrixValues.get(position))), matrixColors[position]));
                //}
                } else {
                gridView = (View) convertView;
            }

            return gridView;
        }

        @Override
        public int getCount() {
            return matrixValues.size();
        }

        @Override
        public Object getItem(int position) {
            return matrixValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    @Override
    public boolean isEnabled(int position) {
      //  if ( position % 4 - 3 == 0) {
       //     return false;
       // }
        return true;
    }
}