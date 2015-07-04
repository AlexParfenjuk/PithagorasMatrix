package com.roodie.pifagormatrix.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.roodie.pifagormatrix.Prefs;
import com.roodie.pifagormatrix.R;
import com.roodie.pifagormatrix.Utils;
import com.roodie.pifagormatrix.model.User;
import com.roodie.pifagormatrix.provider.PythagorasMatrixDatabase;

import java.io.IOException;

/**
 * Created by Roodie on 15.03.2015.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!Prefs.isDatabaseExtracted(this)) {
            System.out.println("Database doesn't exist");
            PythagorasMatrixDatabase database = null;
            try {
                database = new PythagorasMatrixDatabase(this);
                if (!database.checkDataBase()) {
                    database.createDatabase();
                    System.out.println("Database created");
                    Prefs.setDatabaseExtracted(this, true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (database != null) {
                database.close();
            }
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new UsersFragment())
                    .commit();
        }
    }

    public void showAddUserFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_bottom, R.anim.fragment_exit_bottom,R.anim.fragment_enter_top,R.anim.fragment_exit_top)
                .replace(R.id.container, new AddUserFragment(), AddUserFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void showEditUserFragment(User user) {
        final EditUserFragment fragment = new EditUserFragment();
        final Bundle args = new Bundle();
        args.putParcelable(User.USER_INSTANCE, user);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_bottom, R.anim.fragment_exit_bottom, R.anim.fragment_enter_top,R.anim.fragment_exit_top)
                .replace(R.id.container, fragment, EditUserFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void showUsersList() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_top, R.anim.fragment_exit_top)
                .replace(R.id.container, new UsersFragment(), UsersFragment.TAG)
                .commit();
    }

    public void showMatrix(User user){
        final MatrixFragment fragment = new MatrixFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(User.USER_INSTANCE, user);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_right,R.anim.fragment_enter_left,R.anim.fragment_exit_left)
                .replace(R.id.container, fragment, MatrixFragment.TAG)
                .addToBackStack(null)
                .commit();

    }

    public  void showAboutDialog() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(Html.fromHtml(getString(R.string.about_message)))
                .positiveText(R.string.action_source)
                .negativeText(R.string.action_close)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AlexParfenjuk/PithagorasMatrix"));
                        startActivity(browserIntent);
                    }
                })
                .build()
                .show();

    }

    public void onChangeTheme() {recreate(); }
}
