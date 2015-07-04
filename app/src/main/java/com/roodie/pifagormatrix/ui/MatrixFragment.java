package com.roodie.pifagormatrix.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.adapters.GridViewAdapter;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;

import com.roodie.pifagormatrix.model.User;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roodie on 17.03.2015.
 */
public class MatrixFragment extends Fragment {

    public static final String TAG = "MatrixFragment";

    private PythagorasMatrixDatabase dbHelper;

    Context context;

    private int previousClicked = -1; //position of last clicked grid item

    private TextView descriptionTV;
    private GridView basicGridView;
    private GridView extraGridView;
    private TextView matrixText;
    private TextView matrixTitle;
    private TextView matrixZeroItem;
    private User user;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity.getApplicationContext();
        ActionBar actionBar = ((MainActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.matrix_title);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_matrix, container, false);
        return root;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (User) bundle.getParcelable(User.USER_INSTANCE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null){
            dbHelper.close();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionTV = (TextView) view.findViewById(R.id.description_tv);
        matrixText = (TextView) view.findViewById(R.id.matrix_text);
        matrixTitle = (TextView) view.findViewById(R.id.matrix_title);
        matrixZeroItem = (TextView) view.findViewById(R.id.zero_tv);
        basicGridView = (GridView) view.findViewById(R.id.grid_view_basic);
        extraGridView = (GridView) view.findViewById(R.id.grid_view_extra);

        descriptionTV.setText(this.user.getStringFullBirthday(Utils.BirthdayFormat.LONG));
        matrixTitle.setText(R.string.hint_matrix);

        //set Extra numbers
        StringBuilder builder = new StringBuilder();

        String[] basicNumsTitle = getResources().getStringArray(R.array.basic_numbers);
        String[] extraNumsTitle = getResources().getStringArray(R.array.extra_numbers);
        String[] basicNums = this.user.matrixPythagorasBasicNumbers();
        String[] extraNums = this.user.matrixPythagorasExtraNumbers();
        for (int i = 0; i < basicNums.length; i++) {
            builder.append(basicNumsTitle[i]);
            try {
                int j = Integer.valueOf(basicNums[i]);
                builder.append(basicNums[i]).append("\n");
            } catch (NumberFormatException e) {
                builder.append(getResources().getString(R.string.none_number));
            }
        }
        for (int i = 0; i < extraNums.length; i++) {
            builder.append(extraNumsTitle[i]).append(extraNums[i]).append("\n");

        }
        builder.append(getResources().getString(R.string.all_numbers)).append(this.user.matrixPythagorasAllNumbers());
        matrixText.setText(builder.toString());
        try {
            adjustGridView();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.matrix_title);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                return true;
            case R.id.action_dark_theme:
                Prefs.setDarkTheme(getActivity(), !item.isChecked());
                ((MainActivity) getActivity()).onChangeTheme();
                return true;
            case R.id.action_about:
                ((MainActivity) getActivity()).showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_dark_theme).setChecked(Prefs.isDarkTheme(getActivity()));
    }





    private void adjustGridView() throws IOException{
        this.basicGridView.setNumColumns(3);
        this.extraGridView.setNumColumns(1);
        dbHelper = new PythagorasMatrixDatabase(this.context);
        final ArrayList<String> gridDataBasic = this.user.matrixPythagorasGridDataBasic();
        final String[] gridDataExtra = this.user.matrixPythagorasGridDataExtra();
        String zeroNumbers = gridDataBasic.get(0);
        gridDataBasic.remove(0);
        matrixZeroItem.setText(zeroNumbers);
        final String[] matrixPythagorasAllNumbersArray = this.user.matrixPythagorasBasicNumbersArray(Utils.ArrayDestination.DATABASE);
        this.basicGridView.setAdapter(new GridViewAdapter(this.context, gridDataBasic));
        this.extraGridView.setAdapter(new ArrayAdapter<String>(this.context, R.layout.grid_item,R.id.grid_text, gridDataExtra));
        this.basicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != previousClicked) {
                    matrixTitle.setText(dbHelper.getTitleByValue("matrix_pythagoras", matrixPythagorasAllNumbersArray[position + 1].toString()));
                    matrixText.setText(dbHelper.getDescriptionByValue("matrix_pythagoras", matrixPythagorasAllNumbersArray[position + 1].toString()));
                    previousClicked = position;
                }
            }

        });
    }
}

