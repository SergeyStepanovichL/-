package com.example.propyski4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelToImage {

    public static void convertExcelToImage(String[][] data, File imageFile) throws IOException {
        int width = 44 * 100;
        int height = 25 * 50;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        for (int i = 0; i < data.length; i++) {
            String[] row = data[i];
            for (int j = 0; j < row.length; j++) {
                String value = row[j];

                // Добавление пустого столбца после второго столбца
                float x = j < 2 ? j * 120 + 50 : (j + 2) * 120 + 50;
                float y = i * 50 + 40;
                canvas.drawText(value, x, y, paint);
            }
        }

        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close();
    }




}