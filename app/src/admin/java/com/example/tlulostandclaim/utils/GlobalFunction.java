package com.example.tlulostandclaim.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class GlobalFunction {

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static int getWidthScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Display display = context.getDisplay();
            if (display != null) {
                display.getRealMetrics(displayMetrics);
            }
        } else {
            if (context instanceof Activity) {
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }
        }
        return displayMetrics.widthPixels;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateTimeFromMillisecond(long milli) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime dt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(milli),
                    ZoneId.systemDefault()
            );
            return dt.format(dateTimeFormatter);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            return simpleDateFormat.format(new Date(milli));
        }
    }

    @SuppressLint("DefaultLocale")
    public static void showDatePickerDialog(Context context, final OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                    );
                    listener.onDateSelected(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
