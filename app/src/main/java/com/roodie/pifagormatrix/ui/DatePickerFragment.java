package com.roodie.pifagormatrix.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.roodie.pifagormatrix.model.User;

import java.util.Calendar;

/**
 * Created by Roodie on 15.03.2015.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{

    public static final String CALENDAR_INSTANCE = "CALENDAR";
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private OnDialogResultListener listener;

    public DatePickerFragment() {}

    public static DatePickerFragment newInstance(Calendar calendar) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(CALENDAR_INSTANCE, calendar);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final DatePickerDialog datePickerDialog;
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.calendar = (Calendar)bundle.getSerializable(CALENDAR_INSTANCE);
            } else {
            this.calendar = Calendar.getInstance();
            }
        datePickerDialog = new DatePickerDialog((Context)getActivity(),(DatePickerDialog.OnDateSetListener)this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        return (Dialog)datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (this.listener != null) {
            this.listener.returnDate(calendar);
        }
        this.dismiss();
    }


    public void setOnDialogResultListener(final OnDialogResultListener listener) {
        this.listener = listener;
    }

    public interface OnDialogResultListener {
        public abstract void returnDate(Calendar calendar);
    }


}
