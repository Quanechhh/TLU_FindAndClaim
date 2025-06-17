package com.example.tlulostandclaim.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.Date;

/**
 * Lớp GlobalFunction chứa các hàm tiện ích dùng chung cho toàn ứng dụng
 */
public class GlobalFunction {

    /**
     * Hiển thị thông báo ngắn (Toast) lên màn hình
     *
     * @param context Context từ Activity hoặc Fragment
     * @param message Nội dung thông báo
     */
    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Lấy độ rộng màn hình thiết bị (theo pixel)
     *
     * @param context Context từ Activity hoặc Fragment
     * @return độ rộng màn hình (px)
     */
    public static int getWidthScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Với Android 11 trở lên
            Display display = context.getDisplay();
            if (display != null) {
                display.getRealMetrics(displayMetrics);
            }
        } else {
            // Với các phiên bản cũ hơn
            if (context instanceof Activity) {
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }
        }

        return displayMetrics.widthPixels;
    }

    /**
     * Định dạng thời gian từ mili giây thành chuỗi "HH:mm dd/MM/yyyy"
     *
     * @param milli thời gian dưới dạng mili giây
     * @return chuỗi thời gian đã định dạng
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTimeFromMillisecond(long milli) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8 trở lên dùng DateTimeFormatter
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime dt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(milli),
                    ZoneId.systemDefault()
            );
            return dt.format(dateTimeFormatter);
        } else {
            // Android cũ hơn dùng SimpleDateFormat
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            return simpleDateFormat.format(new Date(milli));
        }
    }

    /**
     * Hiển thị hộp thoại chọn ngày (DatePickerDialog)
     *
     * @param context  Context từ Activity hoặc Fragment
     * @param listener callback nhận kết quả ngày đã chọn
     */
    @SuppressLint("DefaultLocale")
    public static void showDatePickerDialog(Context context, final OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);      // Năm hiện tại
        int month = calendar.get(Calendar.MONTH);    // Tháng (tính từ 0)
        int day = calendar.get(Calendar.DAY_OF_MONTH); // Ngày

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format ngày được chọn thành dạng dd/MM/yyyy
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

        datePickerDialog.show(); // Hiển thị hộp thoại
    }

    /**
     * Interface callback dùng để nhận kết quả khi chọn ngày
     */
    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
