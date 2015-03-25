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
import android.widget.GridView;
import android.widget.TextView;

import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.adapters.GridViewAdapter;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;
import com.roodie.pifagormatrix.model.MatrixItem;
import com.roodie.pifagormatrix.model.User;

import java.io.IOException;

/**
 * Created by Roodie on 17.03.2015.
 */
public class MatrixFragment extends Fragment {

    public static final String TAG = "MatrixFragment";

    private PythagorasMatrixDatabase dbHelper;

    Context context;

    private TextView descriptionTV;
    private GridView matrixGridView;
    private TextView matrixText;
    private TextView matrixTitle;
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
        matrixGridView = (GridView) view.findViewById(R.id.grid_view);

        descriptionTV.setText(this.user.getStringFullBirthday());
        matrixTitle.setText(R.string.hint_matrix);

       // String str = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("")).append( getResources().getString(R.string.first_number)).append(String.valueOf(this.user.matrixPythagorasFirstNumber())).toString())).append("\n").append(getResources().getString(R.string.second_number)).append(String.valueOf(this.user.matrixPythagorasSecondNumber())).toString())).append("\n").append(getResources().getString(R.string.third_number)).append(String.valueOf(this.user.matrixPythagorasThirdNumber())).toString())).append("\n").append(getResources().getString(R.string.fourth_number)).append(String.valueOf(this.user.matrixPythagorasFourthNumber())).append("\n").toString() + getResources().getString(R.string.all_numbers) + " " + String.valueOf(this.user.matrixPythagorasAllNumbers());
        String str = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("")).append(getResources().getString(R.string.first_number)).append(String.valueOf(this.user.matrixPythagorasFirstNumber())).toString())).append("\n").append(getResources().getString(R.string.second_number)).append(String.valueOf((Integer.valueOf(this.user.matrixPythagorasSecondNumber()) < 10 ) ? getResources().getString(R.string.none_number) : this.user.matrixPythagorasSecondNumber()  )).toString())).append("\n").append(getResources().getString(R.string.third_number)).append(String.valueOf(this.user.matrixPythagorasThirdNumber())).toString())).append("\n").append(getResources().getString(R.string.fourth_number)).append(String.valueOf( (Integer.valueOf(this.user.matrixPythagorasFourthNumber()) < 10) ? getResources().getString(R.string.none_number) : this.user.matrixPythagorasFourthNumber()  )).append("\n") + getResources().getString(R.string.all_numbers) + " " + String.valueOf(this.user.matrixPythagorasAllNumbers());
        matrixText.setText(str);
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
        this.matrixGridView.setNumColumns(3);
        dbHelper = new PythagorasMatrixDatabase(this.context);
        final String[] gridData = this.user.matrixPythagorasGridData();
        final String[] matrixPyphagoresAllNumbersArray = this.user.matrixPythagorasAllNumbersArray();
        this.matrixGridView.setAdapter(new GridViewAdapter(this.context, gridData));
        this.matrixGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                matrixTitle.setText(dbHelper.getTitleByValue("matrix_pythagoras",matrixPyphagoresAllNumbersArray[position + 1].toString()));
                matrixText.setText(dbHelper.getDescriptionByValue("matrix_pythagoras",matrixPyphagoresAllNumbersArray[position + 1].toString()));
            }

        });
    }
}

