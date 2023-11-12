package com.example.propyski4;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;

import java.io.FileOutputStream;
import java.io.IOException;

import android.widget.ViewFlipper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    int [][] arrN;
    int [] sumN;
    TextView[][] arrTextViewWithN;
    TextView[] textViews;
    int studentCount;
    int resIDN;
    int resID;
    int [][] VseN;
    String [] ArrPar;
    TextView Date;
    TextView sel;
    String strN;
    int year ;
    int month ;
    int day ;
    TextView textView;
    Spinner[] spinners;
    Spinner namberGrypp;
    final Calendar c = Calendar.getInstance();
    String str;
    TextView selection;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreationSpinnerWithTextView();
        KolichestvoChasovPropyskovKashdogoYchashegosia();
        updateTableRows();
        setupDateToday();






    }

    @SuppressLint("ClickableViewAccessibility")
    public void animalcia(){


        // Найдите ViewFlipper в вашем макете
        ViewFlipper viewFlipper = findViewById(R.id.view_flipper);
        // Получите корневое представление вашей активности
        View rootView = findViewById(android.R.id.content);

// Создайте аниматор для свойства "translationX"
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootView, "translationX", 0f, rootView.getWidth());
        animator.setDuration(4000); // Установите продолжительность анимации (в миллисекундах)

// Установите интерполятор для более плавного перехода
        animator.setInterpolator(new DecelerateInterpolator());

// Начните анимацию
        animator.start();


// Используйте GestureDetector для обнаружения свайпов
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 != null && e2 != null) {
                    if (e1.getX() - e2.getX() > 50) {
                        // Свайп влево: перейти к следующему представлению
                        animator.start();
                        viewFlipper.showNext();
                        NextDateMenuItemSelected();
                        return true;
                    } else if (e2.getX() - e1.getX() > 50) {
                        // Свайп вправо: перейти к предыдущему представлению

                        animator.start();
                        viewFlipper.showPrevious();
                        BackDateMenuItemSelected();
                        return true;
                    }
                }
                return false;
            }
        });

        ScrollView scrollView = findViewById(R.id.ScrollView);
        scrollView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));


    }



    public void WorkWithExcel(){
        int studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
         arrN = new int[studentCount][4];
         sumN = new int[studentCount];
         arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                String strN = "N" + (i + 1) + (j + 1);
                resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }
        sumN = sumArray(arrN, sumN);
        String fileName = "366.xlsx";


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        int[] finalSumN = sumN;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                writeArrayToExcel(finalSumN, fileName);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Обновление UI
                    }
                });
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void writeArrayToExcel(int[] array, String fileName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Получаем значение из TextView
                     textView = (TextView) findViewById(R.id.Data);
                    String textValue = textView.getText().toString();
                    int dotIndex = textValue.indexOf(".");
                    int columnNumber = Integer.parseInt(textValue.substring(0, dotIndex));

                    // Проверка наличия файла 366.xlsx во внутреннем хранилище
                    File file = new File(getExternalFilesDir(null), fileName);
                    if (!file.exists()) {
                        throw new FileNotFoundException("Файл " + file.getAbsolutePath() + " не найден.");
                    }

                    Workbook workbook;
                    Sheet sheet;

                    // Если файл существует, открываем его, иначе создаем новый
                    InputStream is = Files.newInputStream(file.toPath());
                    workbook = WorkbookFactory.create(is);//////////////////////W/System.err:     at com.example.propyski4.MainActivity$3.doInBackground(MainActivity.java:212)

                    // Если лист существует, используем его, иначе создаем новый
                    if (workbook.getNumberOfSheets() > 0) {
                        sheet = workbook.getSheetAt(0);
                    } else {
                        sheet = workbook.createSheet("Sheet1");
                    }

                    // Начинаем с ячейки в выбранном столбце и второй строки
                    int startRow = 1;
                    int startCol = columnNumber + 1; // Прибавляем 1, так как индексы начинаются с 0

                    for (int i = 0; i < array.length; i++) {
                        // Пропускаем запись в ячейку, если значение в массиве равно нулю
                        if (array[i] != 0) {
                            Row row = sheet.getRow(startRow + i);
                            if (row == null) {
                                row = sheet.createRow(startRow + i);
                            }

                            Cell cell = row.getCell(startCol);
                            if (cell == null) {
                                cell = row.createCell(startCol);
                            }

                            cell.setCellValue(array[i]);
                        }
                    }

                    // Записываем изменения в файл
                    FileOutputStream fileOut = new FileOutputStream(file);
                    workbook.write(fileOut);
                    fileOut.close();

                    // Закрываем Workbook
                    workbook.close();
                    addFormulasToExcel(fileName, array.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private int[] sumArray(int[][] arrN, int[] sumN) {
        for (int i = 0 ; i<arrN.length; i++){
            int sum = 0;
            for (int j = 0 ; j<arrN[i].length; j++) {
                 str = "N"+(i+1)+(j+1);
                resID = getResources().getIdentifier(str, "id", getPackageName());
                 selection = findViewById(resID);

                if (selection != null) { // Проверка на null
                    String text = selection.getText().toString();

                    try {
                        int number = Integer.parseInt(text);
                        arrN[i][j] = number;
                        sum += number;
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "TextView с идентификатором " + str + " не найден", Toast.LENGTH_LONG).show();
                }
            }
            sumN[i] = sum;
        }

        return sumN;
    }

    public void addFormulasToExcel(String fileName, int numberOfRows) {
        try {
            // Открываем существующий файл Excel
            File file = new File(fileName);
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

            // Добавляем формулы в ячейки от AH2 до AHn (где n - количество строк)
            for (int i = 1; i < numberOfRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }

                Cell cell = row.createCell(33); // 33 означает 34-ю ячейку, то есть AH
                cell.setCellFormula(String.format("SUM(C%d:AG%d)", i+1, i+1));
            }

            // Записываем изменения в файл
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();

            // Закрываем Workbook
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }






    //Метод который должен проверять столбцы на значение "Нет пары"
    public void ProverkaCtolbcov() {
        textViews = new TextView[]{
                (TextView) findViewById(R.id.TextViewPara1),
                (TextView) findViewById(R.id.TextViewPara2),
                (TextView) findViewById(R.id.TextViewPara3),
                (TextView) findViewById(R.id.TextViewPara4)
        };
        studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                strN = "N" + (i + 1) + (j + 1);
                resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }
        for (int i = 0; i < textViews.length; i++) {
            if (textViews[i].getText().toString().equals("Нет пары")) {
                for (int j = 0; j < studentCount; j++) {
                    arrTextViewWithN[j][i].setEnabled(false);
                }
            }
        }
    }



    public void updateTableRows() {
        TableRow[] rows = new TableRow[30];
        arrTextViewWithN = new TextView[30][4];

        for (int i = 0; i < rows.length; i++) {
             str = "TR" + (i + 1);
             resID = getResources().getIdentifier(str, "id", getPackageName());
            rows[i] = (TableRow) findViewById(resID);

            for (int j = 0; j < 4; j++) {
                 strN = "N" + (i + 1) + (j + 1);
                 resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }

        Spinner numberText = (Spinner) findViewById(R.id.NamberGrypp);
        numberText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                int selectedNumber = Integer.parseInt(selected);

                for (int i = 0; i < rows.length; i++) {
                    rows[i].setVisibility(View.GONE);
                    rows[i].invalidate();
                }

                int studentCount = SpisokGrypp(selectedNumber);
                for (int i = 0; i < studentCount; i++) {
                    rows[i].setVisibility(View.VISIBLE);
                    rows[i].invalidate();
                }

                // Вызываем метод addDataToFileAndTextView при изменении даты или номера группы
                 VseN = new int[studentCount][4];
                TextView[][] arrTextViewWithNSelected = Arrays.copyOfRange(arrTextViewWithN, 0, studentCount);
                addDataToTextView(VseN, arrTextViewWithNSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void KolichestvoChasovPropyskovKashdogoYchashegosia(){
        studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        arrN = new int[studentCount][4];
        sumN = new int[studentCount];
        VseN = new int[studentCount][4];
        arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                 strN = "N" + (i + 1) + (j + 1);
                resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }


        VseN = ArrayForFail(arrN);

        // добавление данных в файл и TextView
        addDataToTextView(VseN, arrTextViewWithN);
    }

    public int SpisokGrypp(int nomergrypp){
        String strr = "gr" + nomergrypp;
        int grID = getResources().getIdentifier(strr, "xml", getPackageName());
        studentCount = 0;

        try {
            XmlResourceParser xpp = getResources().getXml(grID);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("student")) {
                    String studentName = xpp.nextText();
                     str = "PN" + (studentCount + 1);
                     resID = getResources().getIdentifier(str, "id", getPackageName());
                     textView = (TextView) findViewById(resID);
                    textView.setText(studentName);
                    studentCount++;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentCount;
    }

    private int[][] ArrayForFail(int[][] arrN) {
        for (int i = 0 ; i<arrN.length; i++){
            for (int j = 0 ; j<arrN[i].length; j++) {
                 str = "N"+(i+1)+(j+1);
                resID = getResources().getIdentifier(str, "id", getPackageName());
                 selection = findViewById(resID);

                if (selection != null) { // Проверка на null
                    String text = selection.getText().toString();

                    try {
                        int number = Integer.parseInt(text);
                        arrN[i][j] = number;
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "TextView с идентификатором " + str + " не найден", Toast.LENGTH_LONG).show();
                }
            }
        }

        return arrN;
    }
    private String[] ArrayParForFail() {
        ArrPar = new String [4];
        textViews = new TextView[]{
                (TextView) findViewById(R.id.TextViewPara1),
                (TextView) findViewById(R.id.TextViewPara2),
                (TextView) findViewById(R.id.TextViewPara3),
                (TextView) findViewById(R.id.TextViewPara4)
        };
        for (int i = 0; i < ArrPar.length; i++) {
            ArrPar[i] = textViews[i].getText().toString();
        }
        return ArrPar;
    }
    private void addDataToTextView(int[][] VseN, TextView[][] arrTextViewWithN) {
        FileOutputStream fos = null;
        FileOutputStream Fos = null;
        try {
            // Получаем дату и номер группы
             Date = (TextView) findViewById(R.id.Data);
             namberGrypp = (Spinner) findViewById(R.id.NamberGrypp);
            String date = Date.getText().toString();
            String groupNumber = namberGrypp.getSelectedItem().toString();

            // Формируем имя файла
            String fileName = date + "_" + groupNumber + ".txt";
            String fileForParName = date + "_" + groupNumber + "_Пары.txt";

            // Проверяем, существует ли файл
            File file = new File(getFilesDir(), fileName);
            File filePars = new File(getFilesDir(), fileForParName);
            if (file.exists()) {
                // Если файл существует, читаем данные из файла
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                int i = 0, j = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    VseN[i][j] = Integer.parseInt(line);
                    j++;
                    if (j == VseN[i].length) {
                        i++;
                        j = 0;
                    }
                }
                fis.close();
            } else {
                // Если файла не существует, заполняем массив нулями
                for (int i = 0; i < VseN.length; i++) {
                    Arrays.fill(VseN[i], 0);
                }
                // Открываем файл для записи
                fos = openFileOutput(fileName, Context.MODE_PRIVATE);

                // Преобразуем каждое число в строку и записываем в файл
                for (int i = 0; i < VseN.length; i++) {
                    for (int j = 0; j < VseN[i].length; j++) {
                        String str1 = Integer.toString(VseN[i][j]) + "\n";
                        fos.write(str1.getBytes());
                    }
                }
            }
            textViews = new TextView[]{
                    (TextView) findViewById(R.id.TextViewPara1),
                    (TextView) findViewById(R.id.TextViewPara2),
                    (TextView) findViewById(R.id.TextViewPara3),
                    (TextView) findViewById(R.id.TextViewPara4)
            };
            ArrPar = new String[4];
            if (filePars.exists()) {
                // Если файл существует, читаем данные из файла
                FileInputStream fis = openFileInput(fileForParName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;

                int i = 0;
                while ((line = bufferedReader.readLine()) != null && i < textViews.length) {
                    textViews[i].setText(line);
                    ArrPar[i] = line;
                    i++;
                }
                fis.close();
            } else {


                for (int i = 0; i < textViews.length; i++) {
                    textViews[i].setText("Нет пары");
                    ArrPar[i] = "Нет пары";
                }
                // Открываем файл для записи
                Fos = openFileOutput(fileForParName, Context.MODE_PRIVATE);

                // Преобразуем каждое число в строку и записываем в файл
                for (int i = 0; i < textViews.length; i++) {
                    String str1 = textViews[i].getText().toString() + "\n";
                    Fos.write(str1.getBytes());
                }
            }

            String str1;
//Добавляем теже данные что и в файле, на экран
            for (int i = 0; i < VseN.length; i++) {
                for (int j = 0; j < VseN[i].length; j++) {
                    str1 = Integer.toString(VseN[i][j]);
                    arrTextViewWithN[i][j].setText(str1);///////////////////////////////ошибка at com.example.propyski4.MainActivity.addDataToTextView(MainActivity.java:573)
                }
                str1 = "\n";
            }

            for (int i = 0; i < 4; i++) {
                textViews[i].setText(ArrPar[i]);
            }
//Выводит теже нанные что и в файле на экран
            String STR = date + "\n" + groupNumber + "\n";
            for(int i = 0; i < VseN.length; i++) {
                for (int j = 0; j < VseN[i].length; j++) {
                     str = Integer.toString(VseN[i][j]) + " ";
                    STR += str;
                }
                STR += "\n";
            }
            for (int i = 0; i < 4; i++) {
                STR += (ArrPar[i] + "\n");
            }

            sel = (TextView) findViewById(R.id.KolPropyskov);
            sel.setText(STR);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (Fos != null) {
                    Fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SeivDataInFail() {
        studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        arrN = new int[studentCount][4];
        VseN = new int[studentCount][4];
        ArrPar = new String [4];
        ArrPar =  ArrayParForFail();
        VseN = ArrayForFail(arrN);
        try {
            // Получаем дату и номер группы
            Date = (TextView) findViewById(R.id.Data);
             namberGrypp = (Spinner) findViewById(R.id.NamberGrypp);
            String date = Date.getText().toString();
            String groupNumber = namberGrypp.getSelectedItem().toString();

            // Формируем имя файла
            String fileName = date + "_" + groupNumber + ".txt";
            String fileForParName = date + "_" + groupNumber + "_Пары.txt";
            // Открываем файл для записи
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            FileOutputStream fis = openFileOutput(fileForParName, Context.MODE_PRIVATE);

            // Преобразуем каждое число в строку и записываем в файл
            for (int i = 0; i < VseN.length; i++) {
                for (int j = 0 ; j<VseN[i].length; j++) {
                    String str1 = Integer.toString(VseN[i][j]) + "\n";
                    fos.write(str1.getBytes());
                }
            }

            // Записываем значения из TextViewPara1, TextViewPara2, TextViewPara3, TextViewPara4 в файл
            for (int i =0;i<4;i++)
            {
                fis.write((ArrPar[i]+"\n").getBytes());
            }

            //Добавляем теже данные что и в файле, на экран
            String STR = date + "\n" + groupNumber + "\n";
            for (int i = 0; i < VseN.length; i++) {
                for (int j = 0 ; j<VseN[i].length; j++) {
                    String str1 = Integer.toString(VseN[i][j]) + " ";
                    STR += str1;
                }
                STR += "\n";
            }
            for (int i =0;i<4;i++)
            {
                STR += (ArrPar[i]+"\n");
            }
            sel = (TextView) findViewById(R.id.KolPropyskov);
            sel.setText(STR);

            // Закрываем файл
            fos.close();
            fis.close();
            Toast.makeText(getApplicationContext(), "Данные записаны в файл " + fileName, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Данные записаны в файл " + fileForParName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choicedate_settings:
                onDateMenuItemSelected();
                return true;
            case R.id.save_settings:
                SeivDataInFail();
                WorkWithExcel();
                return true;
            case R.id.Propyski_za_mesiac_settings:
                Intent intent = new Intent(MainActivity.this, Excele_Vivod_Dannix.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    ////////////////
    public void setupDateToday() {
        // Получаем ссылку на TextView
         textView = findViewById(R.id.Data);

        // Получаем текущую дату

         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
         day = c.get(Calendar.DAY_OF_MONTH);

        // Форматируем текущую дату в виде строки
        String currentDate = day + "." + (month + 1) + "." + year;

        // Отображаем текущую дату в TextView
        textView.setText(currentDate);


        // Вызываем метод addDataToFileAndTextView при изменении даты
         studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        VseN = new int[studentCount][4];
        arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                strN = "N" + (i + 1) + (j + 1);
                resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }

        addDataToTextView(VseN, arrTextViewWithN);


    }


    public void onDateMenuItemSelected() {

         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
         day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Получаем ссылку на TextView
                 textView = findViewById(R.id.Data);
                // Форматируем выбранную дату в виде строки
                String selectedDate = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                // Отображаем выбранную дату в TextView
                textView.setText(selectedDate);

                // Вызываем метод addDataToFileAndTextView при изменении даты
                 studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
                 VseN = new int[studentCount][4];
                 arrTextViewWithN = new TextView[studentCount][4];

                for (int i = 0; i < studentCount; i++) {
                    for (int j = 0; j < 4; j++) {
                         strN = "N" + (i + 1) + (j + 1);
                         resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                        arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
                    }
                }

                addDataToTextView(VseN, arrTextViewWithN);


            }
        }, year, month, day);

        dpd.show();
    }


    public void NextDateMenuItemSelected() {
        // Получаем ссылку на TextView
         textView = findViewById(R.id.Data);
        // Получаем текущую дату из TextView
        String currentDate = textView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try {
            // Устанавливаем дату в календарь
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        // Увеличиваем дату на один день
        c.add(Calendar.DAY_OF_MONTH, 1);
        // Форматируем обновленную дату в виде строки
        String selectedDate = sdf.format(c.getTime());
        // Отображаем обновленную дату в TextView
        textView.setText(selectedDate);

        // Вызываем метод addDataToFileAndTextView при изменении даты
        studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        VseN = new int[studentCount][4];
         arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                 strN = "N" + (i + 1) + (j + 1);
                 resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }

        addDataToTextView(VseN, arrTextViewWithN);
    }

    public void BackDateMenuItemSelected() {
        // Получаем ссылку на TextView
        textView = findViewById(R.id.Data);
        // Получаем текущую дату из TextView
        String currentDate = textView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try {
            // Устанавливаем дату в календарь
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        // Уменьшаем дату на один день
        c.add(Calendar.DAY_OF_MONTH, -1);
        // Форматируем обновленную дату в виде строки
        String selectedDate = sdf.format(c.getTime());
        // Отображаем обновленную дату в TextView
        textView.setText(selectedDate);

        // Вызываем метод addDataToFileAndTextView при изменении даты
        studentCount = SpisokGrypp(Integer.parseInt(((Spinner) findViewById(R.id.NamberGrypp)).getSelectedItem().toString()));
        VseN = new int[studentCount][4];
        arrTextViewWithN = new TextView[studentCount][4];

        for (int i = 0; i < studentCount; i++) {
            for (int j = 0; j < 4; j++) {
                 strN = "N" + (i + 1) + (j + 1);
                resIDN = getResources().getIdentifier(strN, "id", getPackageName());
                arrTextViewWithN[i][j] = (TextView) findViewById(resIDN);
            }
        }

        addDataToTextView(VseN, arrTextViewWithN);
    }


    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupSpinnerWithTextView(Spinner spinner, TextView textView, int arrayResource) {
        setupSpinner(spinner, arrayResource);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                textView.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void CreationSpinnerWithTextView(){

        Spinner spinnerGryps = (Spinner)findViewById(R.id.NamberGrypp);
        setupSpinner(spinnerGryps, R.array.Gryps);

        spinners = new Spinner[]{
                (Spinner) findViewById(R.id.Para1),
                (Spinner) findViewById(R.id.Para2),
                (Spinner) findViewById(R.id.Para3),
                (Spinner) findViewById(R.id.Para4)
        };

        textViews = new TextView[]{
                (TextView) findViewById(R.id.TextViewPara1),
                (TextView) findViewById(R.id.TextViewPara2),
                (TextView) findViewById(R.id.TextViewPara3),
                (TextView) findViewById(R.id.TextViewPara4)
        };

        for (int i = 0; i < spinners.length; i++) {

            setupSpinnerWithTextView(spinners[i], textViews[i], R.array.para);



        }

    }

}
