package com.example.propyski4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Excele_Vivod_Dannix extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excele_vivod_dannix);

        Button myButton = (Button) findViewById(R.id.button);
        EditText myEditText = (EditText) findViewById(R.id.editTextNumber);
        myEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditText.setText("");
            }
        });
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.editTextNumber);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);

                String text = textView.getText().toString();

                if (text.equals("366")) {
                    imageView.setVisibility(View.VISIBLE);
                    displayExcelInImageView();


                } else {
                    Toast.makeText(Excele_Vivod_Dannix.this, "Такой группы в базе данных нету", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayExcelInImageView() {
        try {
            // Проверка наличия файла 366.xlsx во внутреннем хранилище
            File file = new File(getExternalFilesDir(null), "366.xlsx");
            if (!file.exists()) {
                throw new FileNotFoundException("Файл " + file.getAbsolutePath() + " не найден.");
            }

            // Теперь вы можете открыть файл как File
            String[][] data = ExcelReader.readExcelFile(file, 0, 25, 0, 33);

            File imageFile = new File(getExternalFilesDir(null), "image.jpg");
            ExcelToImage.convertExcelToImage(data, imageFile);

            ImageView imageView = findViewById(R.id.imageView);
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}