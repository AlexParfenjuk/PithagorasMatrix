package com.roodie.pifagormatrix.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.adapters.GridViewAdapter;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;
import com.roodie.pifagormatrix.model.MatrixItem;
import com.roodie.pifagormatrix.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Roodie on 17.03.2015.
 */
public class MatrixFragment extends Fragment {

    public static final String TAG = "MatrixFragment";

    private PythagorasMatrixDatabase dbHelper;

    Context context;

    private int previousClicked = 0; //position of last clicked grid item

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

        descriptionTV.setText(this.user.getStringFullBirthday());
        matrixTitle.setText(R.string.hint_matrix);

       // String str = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("")).append( getResources().getString(R.string.first_number)).append(String.valueOf(this.user.matrixPythagorasFirstNumber())).toString())).append("\n").append(getResources().getString(R.string.second_number)).append(String.valueOf(this.user.matrixPythagorasSecondNumber())).toString())).append("\n").append(getResources().getString(R.string.third_number)).append(String.valueOf(this.user.matrixPythagorasThirdNumber())).toString())).append("\n").append(getResources().getString(R.string.fourth_number)).append(String.valueOf(this.user.matrixPythagorasFourthNumber())).append("\n").toString() + getResources().getString(R.string.all_numbers) + " " + String.valueOf(this.user.matrixPythagorasAllNumbers());
       // String str = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("")).append(getResources().getString(R.string.first_number)).append(String.valueOf(this.user.matrixPythagorasFirstNumber())).toString())).append("\n").append(getResources().getString(R.string.second_number)).append(String.valueOf((Integer.valueOf(this.user.matrixPythagorasSecondNumber()) < 10 ) ? getResources().getString(R.string.none_number) : this.user.matrixPythagorasSecondNumber()  )).toString())).append("\n").append(getResources().getString(R.string.third_number)).append(String.valueOf(this.user.matrixPythagorasThirdNumber())).toString())).append("\n").append(getResources().getString(R.string.fourth_number)).append(String.valueOf( (this.user.matrixPythagorasFourthNumber() < 10) ? getResources().getString(R.string.none_number) : this.user.matrixPythagorasFourthNumber()  )).append("\n") + " ";
       // matrixText.setText(str);
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
            case R.id.action_github: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AlexParfenjuk/PithagorasMatrix"));
                startActivity(browserIntent);
            }
                return true;
            case R.id.action_dark_theme:
                Prefs.setDarkTheme(getActivity(), !item.isChecked());
                ((MainActivity)getActivity()).onChangeTheme();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    private void showAboutDialog() {
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

