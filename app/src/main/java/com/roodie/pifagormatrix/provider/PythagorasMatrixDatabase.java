package com.roodie.pifagormatrix.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.roodie.pifagormatrix.model.User;
import com.roodie.pifagormatrix.model.User;
import com.roodie.pifagormatrix.provider.PythagorasMatrixContract.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



/**
 * Created by Roodie on 18.03.2015.
 */
public class PythagorasMatrixDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pythagoras_matrix.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Context context;
    private SQLiteDatabase db;
    private String DB_PATH;

    public interface Tables {

        String USERS = "users";

        String MATRIX_PYTHAGORaS = "matrix_pythagoras";
    }

    public PythagorasMatrixDatabase(Context context)throws IOException{

        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory)null, DATABASE_VERSION);
        this.context = context;
        DB_PATH = "/data/data/" + this.context.getApplicationContext().getPackageName() + "/databases/";
        opendatabase();

    }


    public void createDatabase() throws IOException {
        boolean dbExists = checkDataBase();
        if (dbExists) {}
        else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean checkDataBase(){
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();

    }

    private void copyDatabase() throws IOException{
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[5120];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public synchronized void close(){
        if(db != null){
            db.close();
        }
        super.close();
    }

    public void opendatabase()
    {
        try {
            //Open the database
            String mypath = DB_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }





    public String getTitleByValue(final String s, final String s2) {
        String string = "";
        final Cursor query = this.db.query(s, new String[] {MatrixColumns.TITLE}, "value=?", new String[] { s2 }, (String)null, (String)null, (String)null, (String)null);
        if (query != null) {
            query.moveToFirst();
            string = query.getString(query.getColumnIndex(MatrixColumns.TITLE));
        }
        query.close();
        return string;
    }

    public String getDescriptionByValue(final String s, final String s2) {
        String string = "";
        final Cursor query = this.db.query(s, new String[] { MatrixColumns.DESCRIPTION }, "value=?", new String[] { s2 }, (String)null, (String)null, (String)null, (String)null);
        if (query != null && query.moveToFirst()) {
            string = query.getString(query.getColumnIndex(MatrixColumns.DESCRIPTION));
        }
        query.close();
        return string;
    }

    public void deleteUser(final int n) {

        db.delete(Tables.USERS, UserColumns.ID + " = " + n, null);
    }

    public User getUserById(final int id) {

        final User user = new User();
        final Cursor query = db.query(Tables.USERS, new String[] {
                UserColumns.ID, UserColumns.NAME,
                UserColumns.BIRTHDAY, UserColumns.BIRTHMONTH,
                UserColumns.BIRTHYEAR, }, UserColumns.ID + " = " + id, (String[])null, (String)null, (String)null, (String)null);
        if (query != null) {
            query.moveToFirst();
            user.setId(id);
            user.setUserName(query.getString(query.getColumnIndex(UserColumns.NAME)));
            user.setBirthday(query.getInt(query.getColumnIndex(UserColumns.BIRTHDAY)));
            user.setBirthmonth(query.getInt(query.getColumnIndex(UserColumns.BIRTHMONTH)));
            user.setBirthyear(query.getInt(query.getColumnIndex(UserColumns.BIRTHYEAR)));

        }
        query.close();
        return user;
    }

    public ArrayList<User> getAllUsers() {

        final ArrayList<User> list = new ArrayList<User>();
        String selectQuery = "SELECT * FROM " + Tables.USERS;
        final Cursor query = this.db.rawQuery(selectQuery, null);
        //final Cursor query = this.db.query(Tables.USERS, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);
        if (query != null) {
            while (query.moveToNext()) {
                final User user = new User();
                user.setId(query.getInt(query.getColumnIndex(UserColumns.ID)));
                user.setUserName(query.getString(query.getColumnIndex(UserColumns.NAME)));
                user.setBirthday(query.getInt(query.getColumnIndex(UserColumns.BIRTHDAY)));
                user.setBirthmonth(query.getInt(query.getColumnIndex(UserColumns.BIRTHMONTH)));
                user.setBirthyear(query.getInt(query.getColumnIndex(UserColumns.BIRTHYEAR)));
                list.add(user);
            }
        }
        query.close();
        return list;
    }

    public void insertUser(final String s, final int n, final int n2, final int n3 ) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(UserColumns.NAME, s);
        contentValues.put(UserColumns.BIRTHDAY, n);
        contentValues.put(UserColumns.BIRTHMONTH, n2);
        contentValues.put(UserColumns.BIRTHYEAR, n3);
        this.db.insert(Tables.USERS, (String)null, contentValues);
    }

    public void updateUser(final int n, final String s, final int n2, final int n3, final int n4) {

        final ContentValues contentValues = new ContentValues();
        contentValues.put(UserColumns.NAME, s);
        contentValues.put(UserColumns.BIRTHDAY, n2);
        contentValues.put(UserColumns.BIRTHMONTH, n3);
        contentValues.put(UserColumns.BIRTHYEAR, n4);
        this.db.update(Tables.USERS, contentValues,UserColumns.ID + " = ?",new String[] {String.valueOf(n)});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getVersion() {
        return this.db.getVersion();
    }
}
